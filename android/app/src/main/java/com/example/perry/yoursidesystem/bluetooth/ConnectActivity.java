package com.example.perry.yoursidesystem.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.fragment.bodytestfragment.BodytestFragment;
import com.example.perry.yoursidesystem.test.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; 

/**
 * Created by vip on 2017/3/13.
 */
//
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ConnectActivity extends ListActivity {
    private BluetoothAdapter bluetoothAdapter;
    private static final long SCAN_PERIOD = 10000;
    private static final int REQUEST_ENABLE_BT = 1;
    private MyBluetoothAdapter myBluetoothAdapter;
    private Handler handler;

    public final static UUID UUID_NOTIFY = UUID.fromString
            ("0000ffe1-0000-1000-8000-00805f9b34fb");

    private Button searchButton;
    private Button cancelButton;
    private boolean scanning;

//    private BluetoothTool bluetoothTool;


//    private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter
//            .LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, byte[] 
// scanRecord) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    myBluetoothAdapter.addDevice(device);
//                    myBluetoothAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    };

    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
//            Object[] lstName = b.keySet().toArray();
//
//            // 显示所有收到的消息及其细节
//            for (int i = 0; i < lstName.length; i++) {
//                String keyName = lstName[i].toString();
//                LogUtil.e("bluetooth", keyName + ">>>" + String.valueOf(b.get
// (keyName)));
//            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    myBluetoothAdapter.addDevice(device);
                    myBluetoothAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    LogUtil.w("blue", "开始搜索");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    LogUtil.w("blue", "结束搜索");
                    break;
            }


        }
    };

//    private ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//            final BluetoothDevice device = result.getDevice();
//            myBluetoothAdapter.addDevice(device);
//            myBluetoothAdapter.notifyDataSetChanged();
//            
//            
//
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            super.onBatchScanResults(results);
//            LogUtil.d("scan", "扫描onBatchScanResults==" + results.size());
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            super.onScanFailed(errorCode);
//            LogUtil.e("scan", "扫描失败onScanFailed， errorCode==" + errorCode);
//        }
//    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getPermission();
        searchButton = findViewById(R.id.search);
        cancelButton = findViewById(R.id.cancel);
        handler = new Handler();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!scanning) {
                    scanDevice(true);
                } else {
                    scanDevice(false);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDevice(false);
                finish();
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "蓝牙透传不支持", Toast.LENGTH_SHORT).show();
            finish();
        }
//
//        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService
//                (Context.BLUETOOTH_SERVICE);
//        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙透传不支持", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

//        开启被其它蓝牙设备发现的功能
//        if (bluetoothAdapter.getScanMode() != BluetoothAdapter
//                .SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
//            startActivity(intent);
//        }


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
// 注册广播接收器，接收并处理搜索结果        
        registerReceiver(searchDevices, intentFilter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        myBluetoothAdapter = new MyBluetoothAdapter();
        setListAdapter(myBluetoothAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        scanDevice(false);
        myBluetoothAdapter.clear();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchDevices);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final BluetoothDevice device = myBluetoothAdapter.getDevice(position);
        if (null == device) {
            return;
        }


//        switch (device.getBondState()) {
//
//            case BluetoothDevice.BOND_NONE:
//                try {
//                    Method createBondMethod = BluetoothDevice.class.getMethod
//                            ("createBond");
//                    createBondMethod.invoke(device);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case BluetoothDevice.BOND_BONDED:
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }

        if (scanning) {
//            BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
//            scanner.stopScan(scanCallback);
            bluetoothAdapter.cancelDiscovery();
            scanning = false;
        }


        final Intent intent = new Intent();
        intent.putExtra(BodytestFragment.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(BodytestFragment.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        LogUtil.i("blue",device.getAddress());
        LogUtil.i("blue",device.getName());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    public void scanDevice(boolean enable) {

        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
//                    BluetoothLeScanner scanner = bluetoothAdapter
// .getBluetoothLeScanner();
//                    scanner.stopScan(scanCallback);
                    bluetoothAdapter.startDiscovery();

                }
            }, SCAN_PERIOD);
            scanning = true;
//            BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
//            scanner.startScan(scanCallback);
            bluetoothAdapter.cancelDiscovery();
            searchButton.setText("搜索中");
        } else {
            scanning = false;
//            BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
//            scanner.stopScan(scanCallback);
            bluetoothAdapter.cancelDiscovery();
            searchButton.setText("搜索");
        }
    }

    private class MyBluetoothAdapter extends BaseAdapter {
        private List<BluetoothDevice> devices;
        private LayoutInflater inflator;

        public MyBluetoothAdapter() {
            devices = new ArrayList<BluetoothDevice>();
            inflator = ConnectActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!devices.contains(device)) {
                devices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return devices.get(position);
        }

        public void clear() {
            devices.clear();
        }


        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.adapter_list_item, null);
                holder = new ViewHolder();
                holder.deviceName = convertView.findViewById(R.id.device_name);
                holder.deviceAddress = convertView.findViewById(R.id.device_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BluetoothDevice device = devices.get(position);
            String dName = device.getName();

            if (null != dName && dName.length() > 0) {
                holder.deviceName.setText(dName);
            } else {
                holder.deviceName.setText("未知设备");
            }
            holder.deviceAddress.setText(device.getAddress());

            return convertView;
        }


    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


    //以下部分为开启蓝牙权限
    @SuppressLint("WrongConstant")
    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = 0;
            permissionCheck = this.checkSelfPermission(Manifest.permission
                    .ACCESS_FINE_LOCATION);
            permissionCheck += this.checkSelfPermission(Manifest.permission
                    .ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions( // 请求授权
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                // 自定义常量,任意整型
            } else {
                // 已经获得权限
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        switch (requestCode) {
            case 1:
                if (hasAllPermissionGranted(grantResults)) {
                    Log.d("tag", "onRequestPermissionsResult: OK");
                } else {
                    Log.d("tag", "onRequestPermissionsResult: NOT OK");
                }
                break;
        }
    }

    private boolean hasAllPermissionGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


}















