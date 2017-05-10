package com.example.pinkmobility;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static Button b_entrer;
    public static Button b_jumeler;
    BluetoothAdapter m_bluetoothAdapter;
    private static String TAG = "MainActivity";

    //de type BluetoothDevice pour lister les device B découvertes pour la premiere fois
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public Set<BluetoothDevice> devicesDejaJumele ;

    public DeviceListAdapter mDeviceListAdapter;
    private ListView LVNewDevice;

     BluetoothDevice mDevice;
     BluetoothDevice mBTDevice;

    String deviceName ;
    String deviceAddress;



    BluetoothConnectionService mBluetoothConnection;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");



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
                        boutJumelerDesactive();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "on");
                        jumeler();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1 state turning off");
                        boutJumelerDesactive();
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

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     *
     * bound = lié
     */



    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                    mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    go_tableauDB();
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        b_entrer = (Button) findViewById(R.id.Start);
        boutonDesactive();
        b_jumeler = (Button) findViewById(R.id.b_jumeler);


        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LVNewDevice = (ListView) findViewById(R.id.listNewDevice) ;
        mBTDevices = new ArrayList<>();

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);


        if (!m_bluetoothAdapter.isEnabled()) {
            boutJumelerDesactive();
            enableDisableBT();
        }
        if( m_bluetoothAdapter.isEnabled()){
            jumeler();
        }



        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        LVNewDevice.setOnItemClickListener(MainActivity.this);



    }


    public void startBTConnection(BluetoothDevice device, UUID uuid){

        Log.d(TAG, "juste pour voir.");

        mBluetoothConnection.startClient(device, uuid);

    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
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

    public void jumeler(){


        /*if (mBroadcastReceiver4.equals(BluetoothDevice.BOND_BONDED)){
            Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
            Toast.makeText(MainActivity.this, "device connecte : " + deviceName , Toast.LENGTH_LONG).show();
            boutJumelerDesactive();
            go_tableauDB();
        }

        else {*/

            Log.d(TAG, "je suis ici");

            b_jumeler.setBackgroundColor(Color.rgb(63, 81, 181));
            b_jumeler.setEnabled(true);


            b_jumeler.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discoverableBluetooth();

                          /*  byte[] bytes = b_jumeler.getText().toString().getBytes(Charset.defaultCharset());
                            mBluetoothConnection.write(bytes);

                            startConnection();*/


                        }

                    }
            );
        //}

    }



    public void boutJumelerDesactive(){

        b_jumeler.setBackgroundColor(808080);
        b_jumeler.setEnabled(false);

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

        }

    }

    public void discoverableBluetooth(){

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);

        startActivity(discoverableIntent);

        IntentFilter BTIntent = new IntentFilter(m_bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, BTIntent);

        bluetoothDiscover();
    }


    public void bluetoothDiscover() {


        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (m_bluetoothAdapter.isDiscovering()) {
            m_bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            m_bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!m_bluetoothAdapter.isDiscovering()) {

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



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        m_bluetoothAdapter.cancelDiscovery();


        Log.d(TAG, "onItemClick: You Clicked on a device.");
        deviceName = mBTDevices.get(i).getName();
        deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);


        devicesDejaJumele = m_bluetoothAdapter.getBondedDevices();

       if(devicesDejaJumele.size() > 0){


            for ( BluetoothDevice device : devicesDejaJumele){

                String deviceNameJumele = device.getName();
                String deviceAddressJumele = device.getAddress();


                Log.d(TAG, "deviceName = " + deviceNameJumele);
                Log.d(TAG, "deviceAddress = " + deviceAddressJumele);

                if (deviceNameJumele.equals(deviceName) && deviceAddressJumele.equals(deviceAddress)){

                    Log.d(TAG, " ca nous interesse" );
                    startBTConnection(device,UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));

                }


            }
        }


        Log.d(TAG, "Trying to pair with " + deviceName);
        mBTDevices.get(i).createBond();

        mBTDevice = mBTDevices.get(i);
        mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);

    }


}
















/*
//COTE SERVEUR

private class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    public AcceptThread() {
        BluetoothServerSocket tmp = null;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }

            if (socket != null) {
                manageConnectedSocket(socket);
                mmServerSocket.close();
                break;
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}



//COTE CLIENT

private class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        m_bluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        manageConnectedSocket(mmSocket);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}


*/