package com.red.robotremoter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //region BT VARS
    private static final int REQUEST_ENABLE_BT = 1;
    //rib~rob
    public static String UARTSERVICE_SERVICE_UUID = "6E400001B5A3F393E0A9E50E24DCCA9E";
    public static String UART_RX_CHARACTERISTIC_UUID = "6E400002B5A3F393E0A9E50E24DCCA9E";
    public static String UART_TX_CHARACTERISTIC_UUID = "6E400003B5A3F393E0A9E50E24DCCA9E";
    public final float MIN_ACTIVATE = 1.2f;
    public BluetoothAdapter bluetoothAdapter;
    SensorManager sensorManager;
    TextView disp1, disp2, sensyDisp;
    SeekBar sensy;
    private int max = 5;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region init others
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        disp1 = findViewById(R.id.textView);
        disp2 = findViewById(R.id.textView2);
        sensy = findViewById(R.id.seekBar);
        sensyDisp = findViewById(R.id.textView3);
        sensy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                max = progress;
                sensyDisp.setText("Érzékenyég: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //endregion
        //region initBluetooth
        //bleutooth BLE tesztelése
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "A BLE nem támogatott", Toast.LENGTH_SHORT).show();
            finish();
        }
        //bluetooth adapter definiálása
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        //bluetooth bekapcsolása
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //endRegion
    }

    private void onVauleUpdated(String command) {
        //itt kell lekezelni a küldést
    }

    //region unimportant
    @Override
    public void onSensorChanged(SensorEvent event) {
        motorCalculator(Limiter(event.values[0]), Limiter(-event.values[1]));//Y
    }

    private float Limiter(float a) {
        if (a < MIN_ACTIVATE && a > -MIN_ACTIVATE) a = 0;
        if (a > max) a = max;
        if (a < -max) a = -max;
        a = a / max;
        return a;
    }

    private void motorCalculator(float turn, float forward) {

        float left = forward;
        float right = forward;
        if (turn < 0)
            if (forward >= 0)
                right = right + turn;
            else
                right = right - turn;
        else if (turn > 0)
            if (forward >= 0)
                left = left - turn;
            else
                left = left + turn;

        //Innen már csak kiírás
        String L = String.valueOf(Math.round(left * 100)), R = String.valueOf(Math.round(right * 100));
        disp1.setText(L + "%");
        disp2.setText(R + "%");
        //string előkészítése
        String Kessz = PrepareString(L) + PrepareString(R);
        onVauleUpdated(Kessz);
        Log.d("eredmeny", "Késsz: " + Kessz);

    }

    //A beérkező stringet 4 hosszúvá lakakítja
    private String PrepareString(String a) {
        if (a.length() < 4) {
            byte lenght = (byte) a.length();
            if (a.charAt(0) == '-') {
                a = a.substring(1);
                for (int i = 0; i < 4 - lenght; i++) {
                    a = "0" + a;
                }
                a = "-" + a;
            } else {
                for (int i = 0; i < 4 - lenght; i++) {
                    a = "0" + a;
                }
            }
        }
        return a;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //endregion
    //region resume/pause
    @Override
    protected void onResume() {
        super.onResume();
        Sensor gyorsulas = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, gyorsulas, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    //endregion
}
