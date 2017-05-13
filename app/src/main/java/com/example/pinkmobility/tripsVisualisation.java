package com.example.pinkmobility;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class tripsVisualisation extends AppCompatActivity {

    ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_visualisation);

        liste = (ListView) findViewById(R.id.listeTrip);

        displayListTrip();

    }

    private void displayListTrip(){

        List<Trip> arrayList = TripList.getInstance().getListTrip();

        ListView lv = (ListView) findViewById(R.id.listeTrip);

        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
    }
}
