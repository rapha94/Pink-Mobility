package com.example.pinkmobility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {


    BluetoothAdapter m_bluetoothAdapter;
    private static String TAG = "MainActivity";


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(m_bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, m_bluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "off");
                        boutonDesactive();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "on");
                        go_tableauDB();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1 state turning off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1 state turning on");
                        break;
                }

            }

        }
    };

    // Create a BroadcastReceiver for Scan.
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "discoverable");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "able to receive connection");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "unable to receive connection");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "connecting");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "connected");
                        break;
                }

            }

        }
    };




    public static Button b_entrer;

     /*private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 1;


    private Set<BluetoothDevice> devices;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

   BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Toast.makeText(MainActivity.this, "New Device = " + device.getName(), Toast.LENGTH_LONG).show();
            }
        }
    };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        b_entrer = (Button) findViewById(R.id.Start);


        if (!m_bluetoothAdapter.isEnabled()) {
            boutonDesactive();
        }
        if( m_bluetoothAdapter.isEnabled()){
            go_tableauDB();
        }

        enableDisableBT();



    }

    public void go_tableauDB() {

        b_entrer.setBackgroundColor(Color.rgb(63,81,181));
        b_entrer.setEnabled(true);
        b_entrer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent("com.example.pinkmobility.tableau_de_bord");
                        startActivity(intent);

                    }
                }
        );

    }

    public void boutonDesactive(){
        b_entrer.setBackgroundColor(808080);
        b_entrer.setEnabled(false);


    }


    public void  enableDisableBT(){
        if (m_bluetoothAdapter.isEnabled())
        {
            Log.d(TAG, "enableDisableBT : does not...");
            discoverableBluetooth();

        }
        if (!m_bluetoothAdapter.isEnabled())
        {
            // intent permettant d'activer le bluetooth
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            //intercepte les changements du bluetooth statut
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);

            discoverableBluetooth();


        }


    }

    public void discoverableBluetooth(){

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3);
        startActivity(discoverableIntent);

        IntentFilter BTIntent = new IntentFilter(m_bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, BTIntent);

    }


}





   /* public void testBluetooth() {

        if (!bluetoothAdapter.isEnabled()) {

            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            finish();

        if (resultCode == RESULT_OK)
            {
                deviceConnection();
            }


        else
            {
                b_entrer.setEnabled(false);
                b_entrer.setBackgroundColor(808080);;
            }
    }



public void deviceConnection() {

    // devise visible
    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
    startActivity(discoverableIntent);


   // liste des device déjà connues
    devices = bluetoothAdapter.getBondedDevices();
    for (BluetoothDevice blueDevice : devices)
    {
        Toast.makeText(MainActivity.this, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
    }


    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(bluetoothReceiver, filter);

    bluetoothAdapter.startDiscovery();


}*/




