package com.red.robotremoter;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.red.robotremoter.bluetooth.BTLE_Device;
import com.red.robotremoter.bluetooth.Scanner;
import com.red.robotremoter.bluetooth.Scanner_BTLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PickerActivity extends AppCompatActivity implements Scanner {
    private List<BTLE_Device> devices = new ArrayList<BTLE_Device>();
    private HashMap<String, BTLE_Device> deviceHashMap = new HashMap<>();
    private Scanner_BTLE scanner;

    private RecyclerView recyclerView;
    private BLE_device_adapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        adapter = new BLE_device_adapter(devices);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        scanner = new Scanner_BTLE(this, this, -75);
        scanner.start();
        handler = new Handler();
        updateList();
    }

    @Override
    public void onScanStopped() {

    }

    private void updateList() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        }, 5000);
        devices.clear();
        devices.addAll(deviceHashMap.values());
        for (BTLE_Device a : devices
        ) {


            Log.d("asd", a.getName() + a.getAddress());
        }
        adapter.notifyDataSetChanged();
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
