package com.example.alzheimertracker;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSTracker implements LocationListener {

    private final Context mContext;
    private Location location;

    public GPSTracker(Context context) {
        this.mContext = context;
        //getLocation();
    }

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
