package com.red.robotremoter;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    ListAdapter adapter;
    private Handler handler;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        list = findViewById(R.id.list);
        adapter = new ListAdapter(this, R.layout.list_element, devices);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), devices.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        list.setAdapter(adapter);

        scanner = new Scanner_BTLE(this, this, -90);
        scanner.start();
        handler = new Handler();
        updateList();
        scanner.start();
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
        }, 1000);
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
        if (new_rssi < -80 && deviceHashMap.containsKey(deviceMac)) {
            deviceHashMap.remove(deviceMac);
        } else {
            if (deviceHashMap.containsKey(device.getAddress())) {
                deviceHashMap.get(deviceMac).setRSSI(new_rssi);
            } else {
                BTLE_Device dev = new BTLE_Device(device);
                dev.setRSSI(new_rssi);
                deviceHashMap.put(deviceMac, dev);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stop();
    }
}
