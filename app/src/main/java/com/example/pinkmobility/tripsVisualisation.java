package com.example.pinkmobility;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.List;

public class tripsVisualisation extends AppCompatActivity{

    private ImageButton b_clearAllTrips;
    private ImageButton b_clearTrips;
    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_visualisation);

        clearAllTrips();
        clearTrips();
        displayListTrip();

    }

    private void displayListTrip(){

        List<Trip> arrayList = TripList.getInstance().getListTrip();
        ListView listTrip = (ListView) findViewById(R.id.listTrip);

        listTrip.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
    }

    private void clearAllTrips(){

        b_clearAllTrips = (ImageButton) findViewById(R.id.clearAllTrip);

        b_clearAllTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder d_builder = new AlertDialog.Builder(tripsVisualisation.this);
                d_builder.setMessage("Do you really want to clear all Trips ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                TripList.getInstance().clearList();
                                displayListTrip();
                                tableau_de_bord.idTrip = 1;


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = d_builder.create();
                alert.setTitle("Clear All Trips");
                alert.show();
            }
        });
    }

    private void clearTrips(){



        b_clearTrips = (ImageButton) findViewById(R.id.clearTrip);

        b_clearTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(tripsVisualisation.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);


                AlertDialog.Builder d_builder = new AlertDialog.Builder(tripsVisualisation.this);
                d_builder.setView(input);
                d_builder.setMessage("Which Trip do you want to clear?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                m_Text = input.getText().toString();

                                int idTrip = Integer.parseInt(m_Text) - 1;

                                TripList.getInstance().removeTrip(idTrip);
                                displayListTrip();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = d_builder.create();
                alert.setTitle("Clear Trip");
                alert.show();
            }
        });
    }



}
