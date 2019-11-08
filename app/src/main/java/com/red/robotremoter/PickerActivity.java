package com.red.robotremoter;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.red.robotremoter.bluetooth.BTLE_Device;
import com.red.robotremoter.bluetooth.Scanner;
import com.red.robotremoter.bluetooth.Scanner_BTLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PickerActivity extends AppCompatActivity implements Scanner {
    List<BTLE_Device> devices = new ArrayList<BTLE_Device>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    HashMap<String, BTLE_Device> deviceHashMap = new HashMap<>();
    Scanner_BTLE scanner;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        recyclerView = findViewById(R.id.list);
        adapter = new RecyclerViewAdapter(this, deviceHashMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        scanner = new Scanner_BTLE(this, this, -75);
        scanner.start();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onScanStopped() {

    }

    @Override
    public void addDevice(BluetoothDevice device, int new_rssi) {
        String deviceMac = device.getAddress();
        if (deviceHashMap.containsKey(device.getAddress())) {
            deviceHashMap.get(deviceMac).setRSSI(new_rssi);
        } else {
            BTLE_Device dev = new BTLE_Device(device);
            dev.setRSSI(new_rssi);
            deviceHashMap.put(deviceMac, dev);

        }
        Log.d("main", "Device name: " + device.getName() + " device rssi: " + new_rssi);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //scanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stop();
    }
}
