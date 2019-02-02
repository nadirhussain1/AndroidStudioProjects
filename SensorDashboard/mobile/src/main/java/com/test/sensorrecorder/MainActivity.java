package com.test.sensorrecorder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.test.sensorrecorder.data.DataManager;
import com.test.sensorrecorder.services.PhoneLocMonitoringService;
import com.test.sensorrecorder.services.TestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        CapabilityApi.CapabilityListener {

    private static final String TAG = "MainActivity";
    private static final String CAPABILITY_WEAR_APP = "sensor_recorder_remote__wear_app";
    private static final int REQUEST_GPS_PERMISSION = 1;


    private TextView guideTextView;


    private GoogleApiClient mGoogleApiClient;
    private List<Node> allConnectedWearDevices = new ArrayList<>();
    private Set<Node> mWearNodesWithApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        startTestService();
    }

    private void initViews() {
        guideTextView = (TextView) findViewById(R.id.guideTextView);
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PhoneLocMonitoringService.class));
        stopService(new Intent(this, TestService.class));
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        initiateLocationHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ((mGoogleApiClient != null) && mGoogleApiClient.isConnected()) {

            Wearable.CapabilityApi.removeCapabilityListener(
                    mGoogleApiClient,
                    this,
                    CAPABILITY_WEAR_APP);

            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Set up listeners for capability changes (install/uninstall of remote app).
        Wearable.CapabilityApi.addCapabilityListener(mGoogleApiClient, this, CAPABILITY_WEAR_APP);
        findWearDevicesWithApp();
        findAllWearDevices();
        startService(new Intent(this, PhoneLocMonitoringService.class));
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Log.d(TAG, "onConnectionSuspended(): connection to location client suspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //  Log.e(TAG, "onConnectionFailed(): " + connectionResult);
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        mWearNodesWithApp = capabilityInfo.getNodes();
        // Because we have an updated list of devices with/without our app, we need to also update
        // our list of active Wear devices.
        findAllWearDevices();
        verifyNodeAndUpdateUI();
    }

    // This function finds those watch devices which have application installed on them
    private void findWearDevicesWithApp() {
        // You can filter this by FILTER_REACHABLE if you only want to open Nodes (Wear Devices)
        // directly connect to your phone.
        PendingResult<GetCapabilityResult> pendingResult = Wearable.CapabilityApi.getCapability(mGoogleApiClient, CAPABILITY_WEAR_APP, CapabilityApi.FILTER_REACHABLE);
        pendingResult.setResultCallback(new ResultCallback<GetCapabilityResult>() {
            @Override
            public void onResult(@NonNull CapabilityApi.GetCapabilityResult getCapabilityResult) {
                if (getCapabilityResult.getStatus().isSuccess()) {
                    CapabilityInfo capabilityInfo = getCapabilityResult.getCapability();
                    mWearNodesWithApp = capabilityInfo.getNodes();
                    verifyNodeAndUpdateUI();
                } else {
                    Log.d(TAG, "Failed CapabilityApi: " + getCapabilityResult.getStatus());
                }
            }
        });
    }

    // This method finds all wear devices, whether they have app installed or not, connected with phone.
    private void findAllWearDevices() {
        PendingResult<NodeApi.GetConnectedNodesResult> pendingResult = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        pendingResult.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult getConnectedNodesResult) {

                if (getConnectedNodesResult.getStatus().isSuccess()) {
                    allConnectedWearDevices = getConnectedNodesResult.getNodes();
                    verifyNodeAndUpdateUI();
                } else {
                    Log.d(TAG, "Failed CapabilityApi: " + getConnectedNodesResult.getStatus());
                }
            }
        });
    }

    // This method updates message on the screen so that user can know the status
    private void verifyNodeAndUpdateUI() {
        if ((mWearNodesWithApp == null) || (allConnectedWearDevices == null)) {
            DataManager.getInstance().updatedConnectionStatus(false);
          //  Log.d(TAG, "Waiting on Results for both connected nodes and nodes with app");
        } else if (allConnectedWearDevices.isEmpty()) {
            DataManager.getInstance().updatedConnectionStatus(false);
            guideTextView.setText(getString(R.string.no_wear_device));
        } else if (mWearNodesWithApp.isEmpty()) {
            DataManager.getInstance().updatedConnectionStatus(false);
            guideTextView.setText(getString(R.string.install_wear_app));
        } else {
            DataManager.getInstance().updatedConnectionStatus(true);
            guideTextView.setText(getString(R.string.all_setup));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GPS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleGpsUseCases();
        }
    }

    private void initiateLocationHandler() {
       // Log.d(TAG, "Inside initiateLocationHandler  ");
        if (!hasPermission()) {
            requestLocationPermission();
        } else {
            handleGpsUseCases();
        }
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION);
    }

    private void handleGpsUseCases() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationEnableAlert();
        }

    }

    private void showLocationEnableAlert() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.enable_gps_label))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showLocationSettingsScreen();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    private void showLocationSettingsScreen() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void startTestService() {
        Intent intent = new Intent(MainActivity.this, TestService.class);
        startService(intent);
    }


}
