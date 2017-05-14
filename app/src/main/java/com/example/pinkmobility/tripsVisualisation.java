package com.example.pinkmobility;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class tripsVisualisation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_visualisation);

        displayListTrip();

    }

    private void displayListTrip(){

        List<Trip> arrayList = TripList.getInstance().getListTrip();
        ListView listTrip = (ListView) findViewById(R.id.listTrip);

        listTrip.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
    }
}
