package com.example.perry.yoursidesystem.fragment.bodytestfragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.perry.yoursidesystem.MainActivity;
import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.bluetooth.BluetoothTool;
import com.example.perry.yoursidesystem.bluetooth.ConnectActivity;
import com.example.perry.yoursidesystem.database.UserBodyInfo;
import com.example.perry.yoursidesystem.test.LogUtil;
import com.example.perry.yoursidesystem.wifi.WifiServise;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by perry on 2017/12/15.
 */

public class BodytestFragment extends Fragment implements View.OnClickListener {
    private CirclePgBar temperatureBar;
    private CirclePgBar systolicPreBar;
    private CirclePgBar distolicPreBar;
    private CirclePgBar heartRateBar;
    private FloatingActionButton saveButton;
    private TextView suggestView;
    private ImageView bluetoothView;
    private ImageView byhandView;
    private ImageView wifiView;
    private TextView userNameView, userSexView;
    private TextView userWeightView, userHeightView;
    public static final float MAX_TEMPERATURE = 44;
    public static final float MAX_SYSTOLIC = 95;
    public static final float MAX_DISTOLIC = 160;
    public static final float MAX_HEART_RATE = 160;
    public static final String TEMPERATURE_UNIT = "℃";
    public static final String PRESSURE_UNIT = "mmHg";
    public static final String HEART_RATE_UNIT = "/min";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static boolean connected;
    private String deviceAddress;
    private final static int REQUEST_CONNECT_DEVICE = 1;


    private float temperature;
    private float systolicPre;
    private float distolicPre;
    private float heartRate;
    private int teperratureColor;
    private int systolicColor;
    private int distolicColor;
    private int heartRateColor;
    private WifiServise.WifiBinder wifiBinder;

    private boolean isWifiConnect = false;

    public static BluetoothTool bluetoothTool;

    private Handler wifiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = ((String) msg.obj).trim();
            LogUtil.w("wifi", "receive data:" + buffer.toString());
            if (str.lastIndexOf("a") > 0) {
                LogUtil.w("wifi", "我收到血压data:" + str);
                String[] str1s = str.split(",");
                mdistolicPre = Float.valueOf(str1s[0].split(":")[1]);
                msystolicPre = Float.valueOf(str1s[1]);
                mheartRate = Float.valueOf(str1s[2]);
                str = "";
                isRecePress = true;


            } else if (str.lastIndexOf("q") > 0) {
                LogUtil.w("wifi", "我收到体温data:" + str);
                String[] str1s = str.split(" ");
                mtemperature = Float.valueOf(str1s[1]);
                str = "";
                isReceTempe = true;

            }

            if (isReceTempe && isRecePress) {
                isRecePress = false;
                isReceTempe = false;
                temperature = mtemperature;
                systolicPre = msystolicPre;
                distolicPre = mdistolicPre;
                heartRate = mheartRate;
                saveInfo();
                LogUtil.w("wifi", "四个数据：" + mdistolicPre + ";" +
                        msystolicPre + ";" + mtemperature + ";" + mheartRate);
                handerData(msystolicPre, mdistolicPre, mtemperature, mheartRate);

            }

        }
    };


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wifiBinder = (WifiServise.WifiBinder) service;
            wifiBinder.setHandler(wifiHandler);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


//    private final ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
// .getService();
//            LogUtil.w("tag", "mBluetoothLeService服务:" + mBluetoothLeService);
//            if (!mBluetoothLeService.initialize()) {
//                LogUtil.w("tag", "Unable to initialize Bluetooth");
////                finish();
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.w("tag", "连接失败");
//            mBluetoothLeService = null;
//        }
//    };


//    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            switch (action) {
//
//                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
//                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                    if (info.getDetailedState().equals(NetworkInfo.DetailedState.DISCONNECTED)) {
//                        //wifi已断开  
//                        //mWifiStateChangeListener.onWifiDisconnect();
//                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState
//                            .CONNECTING)) {
//                        //正在连接...  
//                        //mWifiStateChangeListener.onWifiConnecting();
//                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState
//                            .CONNECTED)) {
//                        //连接到网络  
//                        // mWifiStateChangeListener.onWifiConnected();
//                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState
//                            .OBTAINING_IPADDR)) {
//                        //正在获取IP地址  
//                        // mWifiStateChangeListener.onWifiGettingIP();
//                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState.FAILED)) {
//                        //连接失败  
//                    }
//
//                    break;
//                case WifiManager.WIFI_STATE_CHANGED_ACTION:
//                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//                    switch (wifiState) {
//                        case WifiManager.WIFI_STATE_ENABLING:
//                            //wifi正在启用  
//                            //mWifiStateChangeListener.onWifiEnabling();
//                            break;
//                        case WifiManager.WIFI_STATE_ENABLED:
//                            //Wifi已启用  
//                            //mWifiStateChangeListener.onWifiEnable();
//                            break;
//                    }
//                    break;
//                case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
//                    int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -100);
//                    // LogUtil.log("密码认证错误："+error+"\n");
//                    if (error == WifiManager.ERROR_AUTHENTICATING) {
//                        //wifi密码认证错误！  
////                        mWifiStateChangeListener.onPasswordError();
//                    }
//                    break;
//                case WifiManager.NETWORK_IDS_CHANGED_ACTION:
//                    //已经配置的网络的ID可能发生变化时  
////                    mWifiStateChangeListener.onWifiIDChange();
//                    break;
//                case ConnectivityManager.CONNECTIVITY_ACTION:
//                    //连接状态发生变化，暂时没用到  
//                    int type = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };
//
//    private void registeWifiReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        getActivity().registerReceiver(wifiStateReceiver, filter);
//
//    }

    private final BroadcastReceiver bluetoothUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            LogUtil.w("tag", "已经进入接收函数mGattUpdateReceiver");
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                connected = true;
                Glide.with(getActivity()).load(R.drawable.connected).into(bluetoothView);
                LogUtil.w("tag", "已经在连接");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                connected = false;
                Glide.with(getActivity()).load(R.drawable.bluetooth).into(bluetoothView);
                LogUtil.w("tag", "连接不成功");
                bluetoothTool.disconnect();
            }
        }
    };

    private StringBuffer buffer = new StringBuffer();

    boolean isReceTempe = false;
    boolean isRecePress = false;
    float mtemperature = 0, msystolicPre = 0, mdistolicPre = 0, mheartRate = 0;

    private final Handler bluetoothHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothTool.CONNECT_FAILED:
                    Toast.makeText(BodytestFragment.this.getActivity(), "连接失败", Toast
                            .LENGTH_SHORT).show();
                    try {
                        bluetoothTool.connect();
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                    break;
                case BluetoothTool.CONNECT_SUCCESS:
                    Toast.makeText(BodytestFragment.this.getActivity(), "连接成功", Toast
                            .LENGTH_SHORT).show();
                    LogUtil.w("tag", "我已经成功连接");
                    Glide.with(BodytestFragment.this.getActivity()).load(R.drawable
                            .connected).into(bluetoothView);
                    break;
                case BluetoothTool.READ_FAILED:
                    Toast.makeText(BodytestFragment.this.getActivity(), "读取失败", Toast
                            .LENGTH_SHORT).show();
                    break;
                case BluetoothTool.WRITE_FAILED:
                    Toast.makeText(BodytestFragment.this.getActivity(), "写入失败", Toast
                            .LENGTH_SHORT).show();
                    break;
                case BluetoothTool.DATA:
                    buffer.append(msg.obj.toString());
                    String str = buffer.toString();
//                    LogUtil.w("success", "receive data:" + buffer.toString());
                    if (buffer.toString().lastIndexOf("a") > 0) {
                        LogUtil.w("success", "我收到血压data:" + str);
                        String[] str1s = str.split(",");
                        mdistolicPre = Float.valueOf(str1s[0].split(":")[1]);
                        msystolicPre = Float.valueOf(str1s[1]);
                        mheartRate = Float.valueOf(str1s[2]);
                        buffer.delete(0, buffer.length() - 1);
                        isRecePress = true;


                    } else if (buffer.toString().lastIndexOf("q") > 0) {
                        LogUtil.w("success", "我收到体温data:" + str);
                        String[] str1s = str.split(" ");
                        mtemperature = Float.valueOf(str1s[1]);
                        buffer.delete(0, buffer.length() - 1);
                        isReceTempe = true;

                    }

                    if (isReceTempe && isRecePress) {
                        isRecePress = false;
                        isReceTempe = false;
                        temperature = mtemperature;
                        systolicPre = msystolicPre;
                        distolicPre = mdistolicPre;
                        heartRate = mheartRate;
                        saveInfo();
                        LogUtil.w("success", "四个数据：" + mdistolicPre + ";" +
                                msystolicPre + ";" + mtemperature + ";" + mheartRate);
                        handerData(msystolicPre, mdistolicPre, mtemperature, mheartRate);

                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout
                .fragment_bodytest, container, false);
//        初始化控件
        saveButton = view.findViewById(R.id.floatButton);
        suggestView = view.findViewById(R.id.suggest);
        bluetoothView = view.findViewById(R.id.bluetoothImageView);
        byhandView = view.findViewById(R.id.byhandImageView);
        wifiView = view.findViewById(R.id.wifiImageView);
        userNameView = view.findViewById(R.id.user_name);
        userSexView = view.findViewById(R.id.user_sex);
        userWeightView = view.findViewById(R.id.user_weight);
        userHeightView = view.findViewById(R.id.user_height);
        temperatureBar = view.findViewById(R.id.temperatureBar);
        systolicPreBar = view.findViewById(R.id.systolicPreBar);
        distolicPreBar = view.findViewById(R.id.diastolicPreBar);
        heartRateBar = view.findViewById(R.id.heartRateBar);

        byhandView.setOnClickListener(this);
        bluetoothView.setOnClickListener(this);
        wifiView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        setUserCommonInfo();
        loadInfoFromDB();
        getActivity().bindService(new Intent(getActivity(), WifiServise.class), connection,
                Context.BIND_AUTO_CREATE);

        LogUtil.w("tag5", "调用的onCreatedView方法");

        return view;
    }

    private void setUserCommonInfo() {
        userNameView.setText("用户:" + (MainActivity.userName == null ? " " : MainActivity
                .userName));
        userSexView.setText("性别：" + (MainActivity.userSex == null ? " " : MainActivity
                .userSex));
        userHeightView.setText("身高：" + (MainActivity.userHeight == null ? " " :
                MainActivity.userHeight + "cm"));
        userWeightView.setText("体重：" + (MainActivity.userWeight == null ? " " :
                MainActivity.userWeight + "kg"));
    }

    private void loadInfoFromDB() {
        List<UserBodyInfo> infoList = DataSupport.select("systolicPre", "diastolicPre",
                "temperature", "heartRate").where("name=?", MainActivity.userName).find
                (UserBodyInfo.class);
        if (infoList.size() > 0) {
            UserBodyInfo info = infoList.get(infoList.size() - 1);
            temperature = info.getTemperature();
            systolicPre = info.getSystolicPre();
            distolicPre = info.getDiastolicPre();
            heartRate = info.getHeartRate();
            handerData(systolicPre, distolicPre, temperature, heartRate);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.byhandImageView:
                startActivityForResult(new Intent(getActivity(), ByhandActivity.class),
                        555);
                break;
            case R.id.bluetoothImageView:
                if (!connected) {
//                    Intent intent = new Intent(getActivity(), ConnectActivity.class);
//                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter()
                            .getRemoteDevice("20:16:11:21:11:22");
                    bluetoothTool = new BluetoothTool(device, bluetoothHandler);
                    bluetoothTool.connect();
                    connected = true;
                } else {
                    bluetoothTool.disconnect();
                }
                break;
            case R.id.wifiImageView:
                Intent intent = new Intent(getActivity(), WifiServise.class);
                if (!isWifiConnect) {
                    getActivity().startService(intent);
                    Glide.with(getActivity()).load(R.drawable.wifi3).into(wifiView);
                    isWifiConnect = true;
                } else {
                    wifiBinder.disconnect();
                    Glide.with(getActivity()).load(R.drawable.wifi2).into(wifiView);
                    isWifiConnect = false;
                }
                break;
            case R.id.floatButton:
                saveInfo();
                Toast.makeText(getActivity(), "数据已保存", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveInfo() {
        UserBodyInfo info = new UserBodyInfo(systolicPre, distolicPre,
                temperature, (int) heartRate);
        info.save();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555 && resultCode == ByhandActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            heartRate = Float.valueOf(bundle.getString(ByhandActivity
                    .HEART_RATE_TAG).trim());
            temperature = Float.valueOf(bundle.getString(ByhandActivity
                    .TEMPERATURE_TAG).trim());

            distolicPre = Float.valueOf(bundle.getString(ByhandActivity
                    .DIASTOLIC_TAG).trim());
            systolicPre = Float.valueOf(bundle.getString(ByhandActivity
                    .SYSTOLIC_TAG).trim());
            saveInfo();
            handerData(systolicPre, distolicPre, temperature, heartRate);
            LogUtil.w("tag3", "低压3：" + systolicPre + "，高压3：" + distolicPre +
                    "，体温3：" + temperature + "，心率3：" + heartRate);

        } else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == Activity
                .RESULT_OK) {
//            deviceName = data.getExtras().getString(EXTRAS_DEVICE_NAME);
            deviceAddress = data.getExtras().getString(EXTRAS_DEVICE_ADDRESS);
//            Log.w("tag", "mDeviceName:" + deviceName + ",mDeviceAddress:" + 
// deviceAddress);
//            Log.w("tag", "mBluetoothLeService:" + mBluetoothLeService);
//            if (mBluetoothLeService != null) {
//                mbluetoothleservice.disconnect();
//                final boolean result = mbluetoothleservice.connect(deviceaddress);
//                log.w("tag", "connect request result=" + result);
//            }
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter()
                    .getRemoteDevice("20:16:11:21:11:22");
            bluetoothTool = new BluetoothTool(device, bluetoothHandler);
            bluetoothTool.connect();


        }

    }

    private static IntentFilter bluetoothUpdateFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        return intentFilter;
    }


    /**
     * 处理所有数据
     *
     * @param systolic    低压
     * @param distolic    高压
     * @param temperature 体温
     * @param heartRate   心率
     */
    public void handerData(final float systolic, final float distolic, final float temperature,
                           final float heartRate) {
        String suggest = "您现在";
        if (systolic >= 90 || systolic <= 60) {
            systolicColor = Color.RED;
        } else {
            systolicColor = Color.GREEN;
        }
        if (distolic >= 140 || distolic <= 90) {
            distolicColor = Color.RED;
        } else {
            distolicColor = Color.GREEN;
        }

        if (systolic >= 90 || distolic >= 140) {
            suggest += "血压过高，";
        } else if (systolic <= 60 || distolic <= 90) {
            suggest += "血压过低，";
        }

        if (temperature > 37.5) {
            teperratureColor = Color.RED;
            suggest += "已发烧,";
        } else if (temperature < 34.0) {
            teperratureColor = Color.RED;
            suggest += "体温过低，";
        } else {
            teperratureColor = Color.GREEN;
        }

        if (heartRate > 160 || heartRate < 40) {
            heartRateColor = Color.RED;
        } else {
            heartRateColor = Color.GREEN;
        }

        if (heartRate > 160) {
            suggest += "心动过速。";
        } else if (heartRate < 40) {
            suggest += "心动过缓。";
        }
        if (suggest.equals("您现在")) {
            suggest += "一切健康。";
        }

        if (suggest.endsWith("，")) {
            String[] str = suggest.split("，", suggest.lastIndexOf(","));
            suggest = str[0] + "。";
        }
        LogUtil.i("tag3", "低压2：" + systolic + "，高压2：" + distolic + "，体温：" + temperature +
                "，心率：" + heartRate);
        suggestView.setText(suggest);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //        进行各种操作

                temperatureBar.setParameter(MAX_TEMPERATURE, temperature, teperratureColor,
                        TEMPERATURE_UNIT);
                systolicPreBar.setParameter(MAX_SYSTOLIC, systolic, systolicColor,
                        PRESSURE_UNIT);
                distolicPreBar.setParameter(MAX_DISTOLIC, distolic, distolicColor,
                        PRESSURE_UNIT);
                heartRateBar.setParameter(MAX_HEART_RATE, heartRate, heartRateColor,
                        HEART_RATE_UNIT);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        loadInfoFromDB();
        setUserCommonInfo();
        LogUtil.w("tag5", "调用的onResume方法");
        getActivity().registerReceiver(bluetoothUpdateReceiver, bluetoothUpdateFilter());

    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtil.w("tag5", "调用的onPause方法");
        getActivity().unregisterReceiver(bluetoothUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(connection);
        getActivity().stopService(new Intent(getActivity(), WifiServise.class));
        LogUtil.w("tag5", "调用的onDestroy方法");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.w("tag5", "调用的onStop方法");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.w("tag5", "调用的onStart方法");
    }
}






























