package com.example.perry.yoursidesystem.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.perry.yoursidesystem.test.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import java.util.logging.LogRecord;

/**
 * Created by Fsl on 2017/12/22.
 */

public class BluetoothTool {
    private BluetoothDevice device;
    private Handler mhandler;
    BluetoothSocket socket;
    public static final int CONNECT_FAILED = 1;
    public static final int CONNECT_SUCCESS = 5;
    public static final int READ_FAILED = 2;
    public static final int WRITE_FAILED = 3;
    public static final int DATA = 4;
    private boolean isConnect = false;

    private Thread thread;

    public BluetoothTool(BluetoothDevice device, Handler handler) {
        this.device = device;
        this.mhandler = handler;
    }


    /**
     * 开辟连接线程任务
     */
    public void connect() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothSocket tmp = null;
                Method method;
                try {
                    method = device.getClass().getMethod("createRfcommSocket", new
                            Class[]{int.class});
                    tmp = (BluetoothSocket) method.invoke(device, 1);
                } catch (Exception e) {
                    setState(CONNECT_FAILED);
                    Log.e("TAG", e.toString());
                }
                socket = tmp;
                try {
                    socket.connect();
                    isConnect = true;
                    setState(CONNECT_SUCCESS);
                    Readtask readtask = new Readtask();  //连接成功后开启读取数据的线程 
                    readtask.start();
                    LogUtil.w("tag", "我开始接收数据");
                } catch (Exception e) {
                    isConnect = false;
                    setState(CONNECT_FAILED);
                    Log.e("TAG", e.toString());
                }
            }
        });
        thread.start();
    }
    
    public void startWrite(String str){
        new WriteTask(str).run();
    }

    public void disconnect() {
        try {
            isConnect = false;
            socket.close();
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开辟线程读任务
     */
    public class Readtask extends Thread {
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream inputStream=null;   //建立输入流读取数据
            
            while (isConnect) {
                try {
                    inputStream = socket.getInputStream();
//                    LogUtil.w("tag", "开始读数据");

                    if ((bytes = inputStream.read(buffer)) > 0) {
                        byte[] buf_data= new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        LogUtil.w("tag","收到数据data:"+s);
                        Message msg = mhandler.obtainMessage();
                        msg.what = DATA;
                        msg.obj = s;
                        mhandler.sendMessage(msg);
                    }
                    
                    
//                    int len = -1;
//                    StringBuilder sb = new StringBuilder();
//                    byte[] buffer2 = new byte[1024];
//                    if ((len = inputStream.read(buffer2))>0) {
//                        sb.append(buffer2);
//                        LogUtil.w("tag","data:"+len);
//                        
//                    }
//                    LogUtil.w("tag","data:"+buffer2.toString());
//                    Message msg = mhandler.obtainMessage();
//                    msg.what = DATA;
//                    msg.obj = sb.toString();
//                    mhandler.sendMessage(msg);
                    

                } catch (IOException e) {
                    setState(READ_FAILED);
                    LogUtil.e("TAG", e.toString());
                    break;
                }finally {
//                    try {
//                        if(inputStream!=null){
//                            inputStream.close();
//                        }
//                        
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e("TAG", e.toString());
                }
            }
        }
    }

    /**
     * 开辟线程写任务
     */
    public class WriteTask extends Thread {
        private String srt;

        public WriteTask(String str) {
            this.srt = str;
        }

        @Override
        public void run() {
            OutputStream outputStream = null;
            byte[] st = srt.getBytes();
            try {
                outputStream = socket.getOutputStream();
                outputStream.write(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

 
    private void setState(int mes) {
        Message message = new Message();
        message.what = mes;
        mhandler.sendMessage(message);

    }

    /**
     * 下面这个方法目前还没有用到
     */
    private byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }


}  