package com.example.pinkmobility;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static Button b_entrer;
    BluetoothAdapter m_bluetoothAdapter;
    private static String TAG = "MainActivity";

    //de type BluetoothDevice pour lister les device B découvertes pour la premiere fois
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView LVNewDevice;

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
                        discoverableBluetooth();
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
                        bluetoothDiscover();
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


    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                LVNewDevice.setAdapter(mDeviceListAdapter);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        b_entrer = (Button) findViewById(R.id.Start);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LVNewDevice = (ListView) findViewById(R.id.listNewDevice) ;
        mBTDevices = new ArrayList<>();


        if (!m_bluetoothAdapter.isEnabled()) {
            boutonDesactive();
            enableDisableBT();


        }
        if( m_bluetoothAdapter.isEnabled()){
            go_tableauDB();
            discoverableBluetooth();
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        //mBluetoothAdapter.cancelDiscovery();
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

            //discoverableBluetooth();


        }


    }

    public void discoverableBluetooth(){

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);

        startActivity(discoverableIntent);

        IntentFilter BTIntent = new IntentFilter(m_bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, BTIntent);

    }


    public void bluetoothDiscover() {


                        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

                        if(m_bluetoothAdapter.isDiscovering()){
                            m_bluetoothAdapter.cancelDiscovery();
                            Log.d(TAG, "btnDiscover: Canceling discovery.");

                            //check BT permissions in manifest
                            checkBTPermissions();

                            m_bluetoothAdapter.startDiscovery();
                            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                        }
                        if(!m_bluetoothAdapter.isDiscovering()){

                            Log.d(TAG, "btnDiscover: il se passe qqch ? .");

                            //check BT permissions in manifest
                            checkBTPermissions();

                            m_bluetoothAdapter.startDiscovery();
                            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                        }


                    }










    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */

    // permet de checker la version d'android + les permissions associées
    // on en a besoin pour le startDiscovery
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
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




