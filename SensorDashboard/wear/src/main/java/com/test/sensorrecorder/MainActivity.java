package com.test.sensorrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityApi.GetCapabilityResult;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.test.sensorrecorder.services.GlobalTouchService;
import com.test.sensorrecorder.services.LaunchDetectorService;
import com.test.sensorrecorder.services.SensorService;

import java.util.Set;

/**
 * Created by nadirhussain on 07/04/2018.
 */

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, CapabilityApi.CapabilityListener {
    private static final String TAG = "MainWearActivity";
    private static final String CAPABILITY_PHONE_APP = "sensor_recorder_remote__mobile_app";

    private static final int REQUEST_GPS_PERMISSION = 1;
    private TextView guideTextView;
    private Node mAndroidPhoneNodeWithApp;
    private GoogleApiClient mGoogleApiClient;


    // This is the first method that will be called when application is opened up on watch.
    //This method starts connecting to google play services which handles detection of connected
    //Phone.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        connectGoogleServices();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        initiateLocationHandler();
//    }


    private void initViews() {
        guideTextView = (TextView) findViewById(R.id.guideTextView);
    }

    private void connectGoogleServices() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    // Check if user has given permission of location. If not then request permission. If yes, then check
    // location related settings.
//    private void initiateLocationHandler() {
//        Log.d(TAG, "Inside initiateLocationHandler  ");
//        if (!hasPermission()) {
//            requestLocationPermission();
//        } else {
//            handleGpsUseCases();
//        }
//    }


    // When user has given permission , then check location settings.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_GPS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            handleGpsUseCases();
//        }
//    }

    // It checks whether watch has gps or not.
//    private boolean hasGps() {
//        boolean hasGps = getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
//        return hasGps;
//    }
//
//    private boolean hasPermission() {
//        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestLocationPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION);
//    }
//
//    //This method checks if user has location settings enabled on watch. If not,then it show alert to enable it.
//    private void handleGpsUseCases() {
//        Log.d(TAG, "Inside handleGpsUseCases  ");
//        if (hasGps()) {
//            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                showLocationEnableAlert();
//            }
//        }
//    }
//
//
//    private void showLocationEnableAlert() {
//        new AlertDialog.Builder(this)
//                .setMessage(getString(R.string.enable_gps_label))
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        showLocationSettingsScreen();
//                    }
//                })
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        dialog.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .create()
//                .show();
//
//    }
//
//    private void showLocationSettingsScreen() {
//        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.CapabilityApi.addCapabilityListener(
                mGoogleApiClient,
                MainActivity.this,
                CAPABILITY_PHONE_APP);

        checkIfPhoneHasApp();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed ");
    }

    /*
       This method is triggered, when phone connects/diconnects with watch.
     */
    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        mAndroidPhoneNodeWithApp = pickBestNodeId(capabilityInfo.getNodes());
        boolean hasNode = mAndroidPhoneNodeWithApp != null;
        takeActionOnNodeFinding(hasNode);
    }

    /*
       This method is called at start of the application to know if phone is already connected or not.
     */
    private void checkIfPhoneHasApp() {
        PendingResult<GetCapabilityResult> pendingResult = Wearable.CapabilityApi.getCapability(mGoogleApiClient, CAPABILITY_PHONE_APP, CapabilityApi.FILTER_REACHABLE);
        pendingResult.setResultCallback(new ResultCallback<GetCapabilityResult>() {
            @Override
            public void onResult(@NonNull CapabilityApi.GetCapabilityResult getCapabilityResult) {
                if (getCapabilityResult.getStatus().isSuccess()) {
                    CapabilityInfo capabilityInfo = getCapabilityResult.getCapability();
                    mAndroidPhoneNodeWithApp = pickBestNodeId(capabilityInfo.getNodes());
                    boolean hasNode = mAndroidPhoneNodeWithApp != null;
                    Log.d(TAG, "Initial Nodes " + hasNode);
                    takeActionOnNodeFinding(hasNode);
                } else {
                    Log.d(TAG, "Failed CapabilityApi: " + getCapabilityResult.getStatus());
                }
            }
        });
    }

    private Node pickBestNodeId(Set<Node> nodes) {
        Node bestNodeId = null;
        for (Node node : nodes) {
            bestNodeId = node;
        }
        return bestNodeId;
    }

    //Display appropriate message on screen
    private void takeActionOnNodeFinding(boolean hasNode) {
        if (hasNode) {
            guideTextView.setText(getString(R.string.measurement_in_progress));
            startMeasurement();
        } else {
            stopMeasurement();
            guideTextView.setText(getString(R.string.not_connected_with_phone));
        }

    }

    /*
      Starts all services which perform core tasks
     */
    private void startMeasurement() {
        startService(new Intent(this, SensorService.class));
        startService(new Intent(this, LaunchDetectorService.class));
        startService(new Intent(this, GlobalTouchService.class));

        //startService(new Intent(this, WatchLocMonitoringService.class));

    }

    private void stopMeasurement() {
        stopService(new Intent(this, SensorService.class));
        stopService(new Intent(this, LaunchDetectorService.class));
        stopService(new Intent(this, GlobalTouchService.class));
        // stopService(new Intent(this, WatchLocMonitoringService.class));

    }


    @Override
    protected void onDestroy() {
        stopMeasurement();
        super.onDestroy();
    }


}
