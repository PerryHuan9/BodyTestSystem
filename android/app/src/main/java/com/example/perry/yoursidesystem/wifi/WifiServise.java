package com.example.perry.yoursidesystem.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.perry.yoursidesystem.test.LogUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by perry on 2018/5/21.
 */

public class WifiServise extends Service {
    private WifiManager wifiManager;
    private int networkId;
    private Socket socket;
    private PrintStream output;
    private BufferedInputStream bufferedInputStream;
    private boolean isRead;
    private Handler handler;

    public class WifiBinder extends Binder {
        public void setHandler(Handler handle) {
            handler = handle;
        }


        public void disconnect() {
            socket=null;
            bufferedInputStream=null;
            isRead = false;
            LogUtil.w("wifi", "运行disconnect");
            wifiManager.disableNetwork(networkId);

        }


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WifiBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        socket = null;
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        isRead = true;
        LogUtil.w("wifi", "初始化完毕");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        networkId = configWifi("ATK-ESP8266");
        if (networkId == -1) {
            Toast.makeText(this, "您还没有连接过ATK-ESP8266", Toast.LENGTH_LONG).show();

        } else {
            if (wifiManager.enableNetwork(networkId, true)) {
                Toast.makeText(this, "成功连接ATK-ESP8266", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "连接ATK-ESP8266失败", Toast.LENGTH_LONG).show();
            }
        }
        isRead=true;
        new ReadThread().start();
        
//        new Thread(runnable).start();
        LogUtil.w("wifi", "运行onStartCommand" + isRead);
        return super.onStartCommand(intent, flags, startId);
    }


    public int configWifi(String ssid) {
        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration c : wifiConfigurationList) {
//            LogUtil.i("wifi", "已连接过的wifi：" + c.SSID);
            if (c.SSID.equals("\"" + ssid + "\"")) {
                LogUtil.i("wifi", "找到了" + ssid + ",它的ID为：" + c.networkId);
                return c.networkId;
            }
        }

        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRead = false;
        LogUtil.w("wifi", "运行oonDestroy");
        //closeSocket();
        wifiManager.disableNetwork(networkId);
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (initClientSocket()) {
                new ReadThread().start();

            } else {
                Toast.makeText(WifiServise.this, "创建client失败", Toast.LENGTH_LONG).show();
            }

        }
    };


    public boolean initClientSocket() {
        try {
            socket = new Socket("192.168.4.1", 8086);
            output = new PrintStream(socket.getOutputStream(), true, "utf-8");

        } catch (UnknownHostException e) {
            LogUtil.i("wifi", "请检查端口号是否为服务器IP");
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.i("wifi", "流异常");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] receiveData() {
        byte[] data = null;
        if (socket == null) {
            try {
                socket = new Socket("192.168.4.1", 8086);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (socket != null && socket.isConnected()) {
            try {
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                data = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return data;
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            LogUtil.i("wifi", "开始run");
            while (isRead) {
                byte[] data = receiveData();
                if (data != null && data.length > 1) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = new String(data);
                    LogUtil.i("wifi", "接收到的数据：" + new String(data));
                    if (handler != null) {
                        handler.sendMessage(message);
                    }else {
                        LogUtil.i("wifi","handler为null");
                    }
                }
            }
        }

    }


    private void sendMessage(String str) {
        output.println(str);
    }

    public void closeSocket() {
        try {
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LogUtil.i("wifi", "error" + e);
        }
    }

}
