package com.example.pinkmobility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arny on 13/05/2017.
 */

public class TripList {

    private static TripList INSTANCE = null ;

    private List<Trip> tripList ;

    private TripList(){

        this.tripList = new ArrayList<Trip>();

    }


    public static synchronized TripList getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new TripList ();

        }

        return INSTANCE;
    }

    public boolean addTrip(Trip tripToAdd){

        int sizeBeforeAdd = tripList.size() ;
        boolean returnedBool = false ;

        tripList.add(tripToAdd);

        if (sizeBeforeAdd != tripList.size()){
            returnedBool = true ;
        }

        return returnedBool ;

    }

    public Trip getTrip(int i){

        Trip tripToReturn = new Trip();

        if (!tripList.isEmpty()){
            tripToReturn = tripList.get(i);
        }

        return tripToReturn ;
    }

    public List<Trip> getListTrip (){

        return this.tripList;
    }

    public void removeTrip(int i){

        if (!tripList.isEmpty()){
            tripList.remove(i);
        }

    }

    public  void clearList(){

        if (!tripList.isEmpty()){
            tripList.clear();
        }
    }

}
