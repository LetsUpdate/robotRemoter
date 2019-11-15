package com.red.robotremoter.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * Created by Kelvin on 5/8/16.
 */
public class BTLE_Device {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BTLE_Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public int getRSSI() {
        return rssi;
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public String[] getUUIDs() {
        ParcelUuid[] uuids = bluetoothDevice.getUuids();
        if (uuids == null) {
            return new String[0];
        }
        String[] temp = new String[uuids.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = uuids[i].getUuid().toString();
        }
        return temp;
    }

    public boolean isConstainsUUID(String UUID) {
        for (String a :
                getUUIDs()) {
            Log.d("asd", a);
            if (a.equals(UUID))
                return true;
        }
        return false;
    }
    
}
