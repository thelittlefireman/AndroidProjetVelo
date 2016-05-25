package com.ppp.esir.projetvelo.listeners;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ppp.esir.projetvelo.activities.ControleVeloActivity;

/**
 * Created by Guillaume on 24/05/2016.
 */
public class MyLocationChangeListener implements GoogleMap.OnMyLocationChangeListener {
    private Location lastLocation;
    public float distanceParcourue;
    private TextView distance;
    private GoogleMap map;
    private boolean firstTime;

    public MyLocationChangeListener(TextView distanceParcourue, GoogleMap gMap) {
        this.distance = distanceParcourue;
        this.lastLocation = null;
        this.distanceParcourue = 0;
        this.distance.setText("GPS?");
        this.map = gMap;
        this.firstTime = true;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (this.firstTime) {
            animateCameraTo(location, 15);
        } else if (lastLocation != null && lastLocation.getAccuracy() < 10 && location.getAccuracy() < 10) {
            distanceParcourue += lastLocation.distanceTo(location);
            if (distanceParcourue < 1000)
                this.distance.setText(String.valueOf(Math.round(distanceParcourue * 100) / 100) + " m");
            else
                this.distance.setText(String.valueOf(Math.round((distanceParcourue / 1000) * 100) / 100) + " Km");
        }
        if(!firstTime && ControleVeloActivity.mapLock)
            animateCameraTo(location, this.map.getCameraPosition().zoom);
        else if(firstTime)
            this.firstTime = false;

        lastLocation = location;
    }

    public void animateCameraTo(final Location location, final float minZoom) {
        Log.i(getClass().getName().toString(), "lat : " + location.getLatitude() + "    lng : " + location.getLongitude());
        map.getUiSettings().setScrollGesturesEnabled(false);
        LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());
        if(location.hasBearing())
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(coord)             // Sets the center of the map to current location
                    .zoom(minZoom)                   // Sets the zoom
                    .bearing(location.getBearing()) // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {

                public void onFinish() {
                    map.getUiSettings().setScrollGesturesEnabled(true);
                }

                public void onCancel() {
                    map.getUiSettings().setAllGesturesEnabled(true);

                }
            });
        }
        else
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, minZoom), new GoogleMap.CancelableCallback() {

                public void onFinish() {
                    map.getUiSettings().setScrollGesturesEnabled(true);
                }

                public void onCancel() {
                    map.getUiSettings().setAllGesturesEnabled(true);

                }
            });
        }
    }
}
