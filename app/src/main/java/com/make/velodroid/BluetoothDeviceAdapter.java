package com.make.velodroid;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by matgrioni on 3/4/17.
 */

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater inflater;

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, -1, devices);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice device = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bluetooth_device, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);

        name.setText(device.getName());
        address.setText(device.getAddress());

        return convertView;
    }
}
