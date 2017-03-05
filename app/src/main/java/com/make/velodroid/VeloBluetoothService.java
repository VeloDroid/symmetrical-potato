package com.make.velodroid;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by matgrioni on 3/4/17.
 */

public class VeloBluetoothService {

    // TODO Remove singleton pattern.
    private static VeloBluetoothService instance;

    private BluetoothSocket mSocket;
    private OutputStream os;

    public static void initialize(BluetoothSocket socket) {
        instance = new VeloBluetoothService(socket);
    }

    public static VeloBluetoothService getInstance() {
        return instance;
    }

    private VeloBluetoothService(BluetoothSocket socket) {
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
