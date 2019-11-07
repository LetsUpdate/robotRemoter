package com.red.robotremoter.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface Scanner {
    void onScanStopped();

    void addDevice(BluetoothDevice device, int new_rssi);
}
