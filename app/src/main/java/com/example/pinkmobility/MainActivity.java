package com.example.pinkmobility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static Button b_entrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go_tableauDB();

    }



    public void go_tableauDB(){

        b_entrer = (Button) findViewById(R.id.Start);

        //b_entrer.setEnabled(false);
        //b_entrer.setBackgroundColor(808080);

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



}
