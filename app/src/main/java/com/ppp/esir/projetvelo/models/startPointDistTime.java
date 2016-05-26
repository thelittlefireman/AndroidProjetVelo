package com.ppp.esir.projetvelo.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Guillaume on 26/05/2016.
 */
public class StartPointDistTime {

    private LatLng latLng;
    private double distance;
    private int time;

    public StartPointDistTime(LatLng latLng, int distance, int time) {
        this.latLng = latLng;
        this.distance = distance;
        this.time = time;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }
}
