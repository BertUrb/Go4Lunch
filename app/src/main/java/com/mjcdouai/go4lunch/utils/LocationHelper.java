package com.mjcdouai.go4lunch.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationHelper implements LocationListener {

    private final MutableLiveData<Location> mLocationMutableLiveData = new MutableLiveData<>();
    private Location mLocation;

    public LocationHelper(Activity host, Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

            EasyPermissions.requestPermissions(host, "test", 55, perms);

        }
        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mLocation == null) {
            mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (mLocation == null) {
            mLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (mLocation == null) {
            mLocation = new Location("default Location");
            double lat = 48.856614;
            double lg = 2.3522219;
            mLocation.setLatitude(lat);
            mLocation.setLongitude(lg);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, (LocationListener) this);


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLocation = location;
        mLocationMutableLiveData.setValue(mLocation);

    }

    public Location getLocation() {
        return mLocation;
    }


    public MutableLiveData<Location> getLocationLiveData() {
        mLocationMutableLiveData.setValue(mLocation);
        return mLocationMutableLiveData;

    }
}
