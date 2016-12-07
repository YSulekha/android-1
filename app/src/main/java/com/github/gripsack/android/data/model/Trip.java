package com.github.gripsack.android.data.model;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by tuze on 11/19/16.
 */

@Parcel
public class Trip {
    private String tripId;
    private String tripName;
    private String beginDate;
    private String endDate;
    private Place searchDestination;
    private ArrayList<Integer> tripTypes;
   // private ArrayList<Place> destinations;
   // private ArrayList<User> collaborators;

    public Trip(){

    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setSearchDestination(Place searchDestination) {
        this.searchDestination = searchDestination;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setTripTypes(ArrayList<Integer> tripTypes) {
        this.tripTypes = tripTypes;
    }

   /* public void setDestinations(ArrayList<Place> destinations) {
        this.destinations = destinations;
    }

    public void setCollaborators(ArrayList<User> collaborators) {
        this.collaborators = collaborators;
    }*/

    public ArrayList<Integer> getTripTypes() {
        return tripTypes;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Place getSearchDestination() {
        return searchDestination;
    }

    public String getTripName() {
        return tripName;
    }

    /*public ArrayList<Place> getDestinations() {
        return destinations;
    }

    public ArrayList<User> getCollaborators() {
        return collaborators;
    }*/

    public String getTripId() {
        return tripId;
    }
}
