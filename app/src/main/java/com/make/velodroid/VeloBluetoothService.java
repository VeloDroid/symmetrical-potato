package com.make.velodroid;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by matgrioni on 3/4/17.
 */

public class VeloBluetoothService {
    public static final byte TURN_RIGHT_OFF = 0x00;
    public static final byte TURN_RIGHT_ON = 0x01;
    public static final byte TURN_LEFT_OFF = 0x02;
    public static final byte TURN_LEFT_ON = 0x03;

    private BluetoothSocket mSocket;
    private OutputStream os;

    public VeloBluetoothService(BluetoothSocket socket) {
        mSocket = socket;

        try {
            os = socket.getOutputStream();
        } catch (IOException ex) {
            // TODO: Handle error gracefully here.
        }
    }

    public void write(byte msg) {
        try {
            os.write(new byte[]{msg});
        } catch (IOException ex) {
            // TODO Handle error gracefully here.
        }
    }

    public void close() {
        try {
            mSocket.close();
        } catch (IOException ex) {
            // TODO Handle error gracefully here.
        }
    }
}
