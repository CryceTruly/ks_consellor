package com.crycetruly.keepsafeconsellor.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Elia on 1/17/2018.
 */

public class GpsTracker extends Service implements LocationListener {
    Context context;
    boolean isGPSenabled,isNetworkEnable=false;
    boolean canGetLocation=false;
    Location location;
    private static final String TAG = "GpsTracker";

    public GpsTracker() {
    }

    String[]permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    public GpsTracker(Context context) {
        this.context = context;
    }
public Location getLocation(Activity activity){
    Log.d(TAG, "getLocation: ");
        try {
            LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSenabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
               if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                       ){

if (isGPSenabled){
    if (location==null){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,this);

        if (locationManager!=null){
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
    }
}
                   if (location==null){
    if (isNetworkEnable){

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10,this);

        if (locationManager!=null){
            location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }
    }
                   }
               }
            }else {
ActivityCompat.requestPermissions(activity,permissions,2);
            }
        }catch (Exception e){

        }

        return location;
}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
