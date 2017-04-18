package com.example.pinkmobility;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import static com.example.pinkmobility.tableau_de_bord.myDB;

/**
 * Created by raphaelpresberg on 18/04/2017.
 */

public class DataBase extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "trip.db";
    public static final String TABLE_NAME = "trip_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TIME";
    public static final String COL_3 = "SPEED";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " +TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME INTEGER, SPEED INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }


    // modifier le insertData en fonction du type de variable que l'on recoit
    public boolean insertData (String time, String speed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, speed);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }


    }

}
