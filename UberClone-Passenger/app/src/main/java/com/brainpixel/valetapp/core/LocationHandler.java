package com.brainpixel.valetapp.core;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.model.CustomLocation;

/**
 * Created by nadirhussain on 18/10/2016.
 */

public final class LocationHandler implements android.location.LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 3 * 1000;

    private Context mContext;
    private LocationManager locationManager;
    private static CustomLocation prevlatestLocation = null;
    public static boolean isAllSetUpDone = false;

    public LocationHandler(Context context) {
        this.mContext = context;
        isAllSetUpDone = false;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        prevlatestLocation = new CustomLocation(System.currentTimeMillis(), CustomLocation.DEVICE_SOURCE);
    }

    public void initializeProviders() {
        GlobalUtil.printLog("ResumeDebug", "initializeProviders");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isgpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isnetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isgpsEnabled && !isnetworkEnabled) {
                showLocationEnableSettingsDialog();
                return;
            }
            if (!isAutoTimeEnabled()) {
                showAutoTimeEnableSettingsDialog();
                return;
            }

            isAllSetUpDone = true;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            findBestLocation(location);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }

    }


    private boolean isAutoTimeEnabled() {
        int timeSettings = 0;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                timeSettings = Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME);
            } else {
                timeSettings = Settings.System.getInt(mContext.getContentResolver(), Settings.System.AUTO_TIME);
            }
        } catch (Exception exception) {
            GlobalUtil.printLog("LocationDebug", "Exception=" + exception);
        }
        return timeSettings == 1;
    }

    public static CustomLocation getCurrentBestLocation() {
        long currentTime = System.currentTimeMillis();
        if (prevlatestLocation.getTime() < currentTime) {
            prevlatestLocation.updateTime(currentTime, CustomLocation.DEVICE_SOURCE);
        }
        return CustomLocation.copyInstance(prevlatestLocation);
    }


    public void onPause() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    private void showLocationEnableSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.settings_label));
        builder.setMessage(mContext.getString(R.string.location_enable_dialog_message));
        builder.setPositiveButton(mContext.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchLocationSettingsActivity();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void launchLocationSettingsActivity() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
    }

    private void showAutoTimeEnableSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.settings_label));
        builder.setMessage(mContext.getString(R.string.autotime_enable_dialog_message));
        builder.setPositiveButton(mContext.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchTimeSettingsActivity();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void launchTimeSettingsActivity() {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        mContext.startActivity(intent);
    }

    private void findBestLocation(Location newLocation) {
        GlobalUtil.printLog("ResumeDebug", "FindBestLoc="+newLocation);
        if (newLocation != null) {
            GlobalUtil.printLog("ResumeDebug", "FindBestLoc="+newLocation.getLatitude() +" ,"+newLocation.getLongitude());
            prevlatestLocation.updateLocation(newLocation.getLatitude(), newLocation.getLongitude(), newLocation.getProvider());
            if (newLocation.getTime() > prevlatestLocation.getTime()) {
                prevlatestLocation.updateTime(newLocation.getTime(), newLocation.getProvider());
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        findBestLocation(location);
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
