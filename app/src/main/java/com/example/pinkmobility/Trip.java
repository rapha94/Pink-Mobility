package com.example.pinkmobility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arny on 13/05/2017.
 */

public class Trip {

    private int time ;
    private int speed ;

    public Trip (){

        this.speed = 0 ;
        this.time = 0 ;
    }


    public Trip( int time , int speed){

        this.speed = speed ;
        this.time = time ;
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


}
