package com.example.pinkmobility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


public class tableau_de_bord extends AppCompatActivity {

    public static ProgressBar progressBar;
    public static TextView vitesseDigitale;
    public static ImageButton raz;
    public static ImageButton off;

    int speed = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord);
        setVitesseBar();
        //setProgressValue(speed);
        setVitesseDigitale();
        raz();
        quitter();

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


        if (speed<100);
        progressBar.setMax(100);
        progressBar.setProgress(speed);

        if (speed>100)
            finish();
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
                deleteDatabase(String.valueOf(speed));
            }
        });
    }


    private void quitter() {
        off = (ImageButton) findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                }
        });
    }
}


