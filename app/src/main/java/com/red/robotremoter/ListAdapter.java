package com.red.robotremoter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.red.robotremoter.bluetooth.BTLE_Device;

import java.util.List;

public class ListAdapter extends ArrayAdapter<BTLE_Device> {
    public ListAdapter(@NonNull Context context, int resource, @NonNull List<BTLE_Device> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // (3)
        View v = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false); // (4)

        TextView name = v.findViewById(R.id.name);
        TextView mac = v.findViewById(R.id.mac);
        TextView rssi = v.findViewById(R.id.rssi);

        BTLE_Device device = getItem(position);

        name.setText(device.getName()); // (6)

        mac.setText(device.getAddress());
        rssi.setText(String.valueOf(device.getRSSI()));

        return v; // (7)
    }
}
