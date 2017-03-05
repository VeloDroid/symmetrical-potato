package com.make.velodroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceActivity extends AppCompatActivity {

    public static String DEVICE_EXTRA = "device";

    private static final int REQUEST_ENABLE_BT = 1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDevices.add(device);
                deviceAdapter.notifyDataSetChanged();
            }
        }
    };

    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> mDevices;
    private BluetoothDeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);

        mDevices = new ArrayList<>();

        ListView devicesView = (ListView) findViewById(R.id.bt_devices);
        deviceAdapter = new BluetoothDeviceAdapter(this, mDevices);
        devicesView.setAdapter(deviceAdapter);

        devicesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();

                BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.putExtra(DEVICE_EXTRA, device);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        setupBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != RESULT_OK) {
                    // TODO: Quit activity gracefully. The user denied bluetooth access.
                }
        }
    }

    private void setupBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // TODO: Quit activity gracefully
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, REQUEST_ENABLE_BT);
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        mBluetoothAdapter.startDiscovery();
    }
}
