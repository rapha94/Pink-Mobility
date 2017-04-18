package com.example.pinkmobility;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class tableau_de_bord extends AppCompatActivity {

    private  ProgressBar progressBar;
    private  TextView vitesseDigitale;
    private  ImageButton raz;
    private  ImageButton off;
    private  TextView temps;

    public static DataBase myDB;

    public int counter;

    private long tps_total = 7000000;
    private CountDownTimer countDownTimer;

    int speed = 33;
    String i;
    String j;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord);
        setVitesseBar();
        //setProgressValue(speed);
        setVitesseDigitale();
        raz();
        quitter();
        decompte_temps();


    }

    public void onPause(){
        tableau_de_bord.super.onPause();

    }


    private void decompte_temps() {
        temps = (TextView) findViewById(R.id.Temps);

        countDownTimer = new CountDownTimer(tps_total, 1000) {  //mettre en argument le temps d'autonomie de la batterie (variable que l'on recoit)
            public void onTick(long millisUntilFinished) {
                counter++;
                if (counter<60){
                temps.setText(String.valueOf(counter) + " s");}
                else if (counter>60 && counter<3600){
                    temps.setText(String.valueOf(counter/60) + " min " + String.valueOf(counter%60) + " s" );
                }
                else {
                    temps.setText(String.valueOf(counter/3600) + " h " + String.valueOf(counter%3600) + " min" +
                            String.valueOf((counter%3600)%60) + " s" );
                }



            }
            public  void onFinish(){
                temps.setText("tps over");
            }
        }.start();
    }



    /*
    private void setProgressValue(int speed) {
        progressBar.setProgress(speed);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(speed + 5);
            }
        });
        thread.start();
    }*/



    private void setVitesseBar(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        if (speed<100)
        {
        progressBar.setMax(100);
        progressBar.setProgress(speed);
        }
        else
        {
            finish();}



    }


    private void setVitesseDigitale() {
        vitesseDigitale = (TextView) findViewById(R.id.vitesseDigitale);
        vitesseDigitale.setText(String.valueOf(speed));
    }


    private void raz() {
        raz = (ImageButton) findViewById(R.id.raz);
        raz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPause();

                AlertDialog.Builder a_builder = new AlertDialog.Builder(tableau_de_bord.this);
                a_builder.setMessage("do you want to start a new ride ")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder b_builder = new AlertDialog.Builder(tableau_de_bord.this);
                                b_builder.setMessage("do you want to save your trip ")
                                        .setCancelable(false)
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                counter = 0;
                                                myDB = new DataBase(getBaseContext());


                                                //pour le moment on a des int qu'on transforme donc en string pour les ajouter a la database
                                                i = String.valueOf(speed);
                                                j = String.valueOf(counter);

                                                // modifier le isInserted en fonction des variables que l'on recoit
                                                boolean isInserted = myDB.insertData(j.getClass().toString(),
                                                        i.getClass().toString());
                                                if (isInserted = true){
                                                    Toast.makeText(tableau_de_bord.this, "data Inserted", Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    Toast.makeText(tableau_de_bord.this, "data not Inserted", Toast.LENGTH_LONG).show();

                                                }

                                                Toast.makeText(tableau_de_bord.this, "New trip", Toast.LENGTH_LONG).show();


                                            }
                                        })
                                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                counter = 0;
                                                Toast.makeText(tableau_de_bord.this, "New trip", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                AlertDialog alert = b_builder.create();
                                alert.setTitle("Save trip ?");
                                alert.show();

                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("New Ride");
                alert.show();

            }
        });
    }


    private void quitter() {
        off = (ImageButton) findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder c_builder = new AlertDialog.Builder(tableau_de_bord.this);
                c_builder.setMessage("Quit Trip ? ")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                                //onDestroy();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = c_builder.create();
                alert.setTitle("Quit");
                alert.show();
                }
        });
    }
}





