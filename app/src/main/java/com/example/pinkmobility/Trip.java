package com.example.pinkmobility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arny on 13/05/2017.
 */

public class Trip {

    private int time ;
    private int speed ;
    private int id ;

    public Trip (){

        this.speed = 0 ;
        this.time = 0 ;
        this.id = 0 ;
    }


    public Trip( int time , int speed , int id){

        this.speed = speed ;
        this.time = time ;
        this.id = id;
    }

    public int getTime(){

        return this.time ;
    }

    public int getSpeed(){

        return this.speed ;
    }

    public void setTime( int time ){

        this.time = time ;
    }

    public void setSpeed( int speed ){

        this.speed = speed ;
    }

    @Override
    public String toString() {
        return "Trip " + this.id + ". " + this.speed + " Kms/h " + "\n" + this.time%3600 + " min" + this.time%3600%60 + "s";

    }

}
