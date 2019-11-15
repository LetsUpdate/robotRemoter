package com.red.robotremoter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.red.robotremoter.bluetooth.BTLE_Device;

import java.util.List;

public class BLE_device_adapter extends RecyclerView.Adapter<BLE_device_adapter.MyViewHolder> {

    private List<BTLE_Device> deviceList;

    public BLE_device_adapter(List<BTLE_Device> moviesList) {
        this.deviceList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BTLE_Device movie = deviceList.get(position);
        holder.name.setText(movie.getName());
        holder.rssi.setText(String.valueOf(movie.getRSSI()));
        holder.mac.setText(movie.getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mac, rssi;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            rssi = view.findViewById(R.id.rssi);
            mac = view.findViewById(R.id.mac);
        }
    }
}
