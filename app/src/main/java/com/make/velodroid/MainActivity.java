package com.make.velodroid;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BT_DEVICE = 1337;

    private class ConnectThread extends Thread {
        private final BluetoothSocket mSocket;

        private final UUID CLIENT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(CLIENT_UUID);
            } catch (IOException ex) {
                // TODO: Handle error gracefully here.
            }

            mSocket = tmp;
        }

        @Override
        public void run() {
            try {
                mSocket.connect();
            } catch (IOException connectEx) {
                try {
                    mSocket.close();
                } catch (IOException closeEx) {
                    // TODO: Handle error gracefully here.
                }
            }

            // The connection succeeded, so manage it here.
            setupBluetoothSocket(mSocket);
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException ex) {
                // TODO: Handle error gracefully here.
            }
        }
    }

    private TextView btStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewRideActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView ridesView = (RecyclerView) findViewById(R.id.rides);
        btStatus = (TextView) findViewById(R.id.bt_status);

        btStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothDeviceActivity.class);
                startActivityForResult(intent, REQUEST_BT_DEVICE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_BT_DEVICE:
                if (resultCode == RESULT_OK) {
                    BluetoothDevice device = data.getParcelableExtra(BluetoothDeviceActivity.DEVICE_EXTRA);
                    btStatus.setText(device.getAddress());

                    ConnectThread ct = new ConnectThread(device);
                    ct.start();
                }

                break;
        }
    }

    private void setupBluetoothSocket(BluetoothSocket socket) {
        VeloBluetoothService.initialize(socket);
    }
}
