package com.brainpixel.valetapp.views.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.core.LocationHandler;
import com.brainpixel.valetapp.core.NetworkChangeReceiver;
import com.brainpixel.valetapp.interfaces.EventBusClasses.NetworkStateChanged;
import com.brainpixel.valetapp.model.CustomLocation;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.ride.CompleteRideResponse;
import com.brainpixel.valetapp.model.ride.CompletedRideData;
import com.brainpixel.valetapp.model.ride.GetPassengersApiResponse;
import com.brainpixel.valetapp.model.ride.GetRideStatusResponse;
import com.brainpixel.valetapp.model.ride.GetStartedRideResponse;
import com.brainpixel.valetapp.model.ride.PassengerRideData;
import com.brainpixel.valetapp.model.route.RouteResultResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.storage.ValetPreferences;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, OnMarkerClickListener {
    public static final String ROUTE_API_URL = "http://maps.googleapis.com/maps/api/directions/json?";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int INIT_STATE = 0;
    private static final int ACCEPTED_STATE = 1;
    private static final int START_STATE = 2;

    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    @BindView(R.id.onlineStatusSwitchView)
    Switch onlineStatusSwitchView;
    @BindView(R.id.requestActionslayout)
    RelativeLayout requestActionsLayout;
    @BindView(R.id.completeRideInfoLayout)
    RelativeLayout completeRideInfoLayout;
    @BindView(R.id.progressBar)
    ProgressBar actionProgressBar;
    @BindView(R.id.centralProgressBar)
    ProgressBar centralProgressBar;
    @BindView(R.id.completeRideButton)
    Button completeRideButton;
    @BindView(R.id.basePriceValueTextView)
    TextView basePriceTextView;
    @BindView(R.id.chargePerKmValue)
    TextView chargePerKmTextView;
    @BindView(R.id.chargePerMintValue)
    TextView chargePerMinTextView;
    @BindView(R.id.totaKmValueTextView)
    TextView totalKmTextView;
    @BindView(R.id.totalMintValueTextView)
    TextView totalMinsTextView;
    @BindView(R.id.totalFareValueTextView)
    TextView totalFareTextView;
    @BindView(R.id.closeIconView)
    ImageView closeInfoIconView;

    private GoogleMap mMap;
    private LocationHandler locationHandler;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean isMapAlreadyLoaded = false;
    private Timer updateLocationTimer, fetchPassengersTimer, checkStatusTimer, onlineStatusTimer;
    private Snackbar messageSnackBar = null;
    private HashMap<String, PassengerRideData> passengersRequestsMap = new HashMap<>();
    private PassengerRideData selectedRideData;
    private int driverState = INIT_STATE;
    private long mLastClickTime = 0;
    private boolean isInProgress = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);


        registerConnectivityReceiver();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViewsAndClicks();
    }

    private void initViewsAndClicks() {
        rootLayout.setVisibility(View.INVISIBLE);
        onlineStatusSwitchView.setChecked(ValetPreferences.getOnlineStatus(getActivity()));
        locationHandler = new LocationHandler(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        handleLocationPermission();

    }

    @Override
    public void onPause() {
        super.onPause();
        pauseLocationHandler();
        cancelAllTimers();

    }

    @Override
    public void onDestroy() {
        GlobalUtil.printLog("EventBusDebug", "Inside onDestroy");
        super.onDestroy();
        unRegisterConnectivityReceiver();
    }

    private void registerConnectivityReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unRegisterConnectivityReceiver() {
        getActivity().unregisterReceiver(networkChangeReceiver);
    }


    private boolean hasPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void handleLocationPermission() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            loadViewsWithContents();
        }
    }

    private void loadViewsWithContents() {
        if (!isMapAlreadyLoaded) {
            isMapAlreadyLoaded = true;
            rootLayout.setVisibility(View.VISIBLE);
            loadMapFragment();
        }
        resumeLocationHandler();
        updateLocationApiCall();
        checkStartedRide(GlobalDataManager.getInstance().loggedInUserData.getUserId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            getActivity().finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkStateChanged event) {
        if (!GlobalUtil.isInternetConnected(getActivity()) && messageSnackBar == null) {
            messageSnackBar = GlobalUtil.showRedSnackBar(rootLayout, "Not connected with internet");
        } else if (messageSnackBar != null) {
            messageSnackBar.dismiss();
            messageSnackBar = null;
        }
    }

    private void loadMapFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
        zoomCameraToCurrentLocation();
    }

    private void resumeLocationHandler() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GlobalUtil.printLog("PermissionDebug", "InitializeProvider called");
            locationHandler.initializeProviders();
        }
    }

    private void pauseLocationHandler() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationHandler.onPause();
        }
    }

    private void zoomCameraToCurrentLocation() {
        if (!didZoomToCurrentLocation()) {
            initTimeOfLocationUpdate();
        }
    }

    private void initTimeOfLocationUpdate() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomCameraToCurrentLocation();
            }
        }, 3000);
    }

    private boolean didZoomToCurrentLocation() {
        CustomLocation currentLocation = LocationHandler.getCurrentBestLocation();
        if (currentLocation.getLatitude() != 0 && currentLocation.getLongitude() != 0) {
            LatLng mapCenterPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenterPoint, 13));
            return true;
        }
        return false;
    }

    private void cancelTimer(Timer timer) {
        timer.cancel();
    }

    private void cancelAllTimers() {
        GlobalUtil.printLog("HomeFragment", "cancel All Timers ");
        if (updateLocationTimer != null) {
            cancelTimer(updateLocationTimer);
        }
        cancelFetchPassengerTimer();
        cancelStatusTimer();
    }

    private void cancelFetchPassengerTimer() {
        GlobalUtil.printLog("HomeFragment", "cancelFetchPassengerTimer ");
        if (fetchPassengersTimer != null) {
            cancelTimer(fetchPassengersTimer);
        }
    }

    private void cancelStatusTimer() {
        if (checkStatusTimer != null) {
            cancelTimer(checkStatusTimer);
        }
    }

    private void runContinuousUpdateLocationTimer() {
        updateLocationTimer = new Timer();
        updateLocationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAdded() && getActivity() != null) {
                    updateLocationApiCall();
                }
            }
        }, 10000);
    }

    private void runFetchPassenegrTimer() {
        if (driverState == INIT_STATE) {
            GlobalUtil.printLog("HomeFragment", "Inside runFetchPassenegrTimer");
            fetchPassengersTimer = new Timer();
            fetchPassengersTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isAdded() && getActivity() != null) {
                        fetchPassengersRequests();
                    }
                }
            }, 20000);
        }
    }

    private void runCheckRideStatusTimer() {
        GlobalUtil.printLog("HomeFragment", "Inside runCheckRideStatusTimer");
        if (driverState >= ACCEPTED_STATE) {
            checkStatusTimer = new Timer();
            checkStatusTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isAdded() && getActivity() != null && selectedRideData != null) {
                        fetchRideLatestStatus("" + selectedRideData.getRId());
                    }
                }
            }, 10000);
        }
    }

    private void updateLocationApiCall() {
        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        if (!GlobalUtil.isInternetConnected(getActivity()) || customLocation == null || customLocation.getLatitude() == 0) {
            runContinuousUpdateLocationTimer();
            return;
        }
        String userId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
        double latitude = customLocation.getLatitude();
        double longitude = customLocation.getLongitude();
        int rideId = -1;
        if (selectedRideData != null) {
            rideId = selectedRideData.getRId();
        }


        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.updateLocation(userId, rideId, latitude, longitude);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    GeneralServerResponse generalServerResponse = response.body();
                    if (generalServerResponse.getSuccess()) {
                        // GlobalUtil.printLog("HomeFragment", "Location Successfully updated");
                    } else {
                        // GlobalUtil.printLog("HomeFragment", "Location update failed=" + generalServerResponse.getMessage());
                    }
                    runContinuousUpdateLocationTimer();
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                if (isAdded()) {
                    //  GlobalUtil.printLog("HomeFragment", "Location update failed=" + t.getMessage());
                    runContinuousUpdateLocationTimer();
                }
            }
        });
    }

    private void fetchPassengersRequests() {
        GlobalUtil.printLog("HomeFragment", "fetchPassengersRequests call made");
        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        if (!GlobalUtil.isInternetConnected(getActivity()) || customLocation == null || customLocation.getLatitude() == 0) {
            GlobalUtil.printLog("HomeFragment", "Location not available start runFetchPassenegrTimer ");
            runFetchPassenegrTimer();
            return;
        }
        String userId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
        String latLng = "" + customLocation.getLatitude() + "," + customLocation.getLongitude();

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetPassengersApiResponse> call = apiService.getPassengersList(userId, latLng);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetPassengersApiResponse>() {
            @Override
            public void onResponse(Call<GetPassengersApiResponse> call, Response<GetPassengersApiResponse> response) {
                if (isAdded() && driverState == INIT_STATE) {
                    if (response != null && response.body() != null) {
                        GlobalUtil.printLog("HomeFragment", "fetchPassengersRequests success");
                        GetPassengersApiResponse passengersApiResponse = response.body();
                        if (passengersApiResponse.getSuccess()) {
                            handlePassengersResponse(passengersApiResponse.getData());
                        }
                    }
                    runFetchPassenegrTimer();
                }
            }

            @Override
            public void onFailure(Call<GetPassengersApiResponse> call, Throwable t) {
                GlobalUtil.printLog("HomeFragment", "fetchPassengersRequests failed");
                if (isAdded()) {
                    runFetchPassenegrTimer();
                }
            }
        });
    }

    private void handlePassengersResponse(List<PassengerRideData> passengersRequestsList) {
        mMap.clear();
        if (passengersRequestsList != null && passengersRequestsList.size() > 0) {
            Marker marker;
            LatLng latLng;
            for (PassengerRideData passengerRideData : passengersRequestsList) {
                latLng = new LatLng(passengerRideData.getRFromLat(), passengerRideData.getRFromLng());
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker_icon)));

                passengersRequestsMap.put(marker.getId(), passengerRideData);
            }
        }

    }


    private void showProgressBar() {
        isInProgress = true;
        actionProgressBar.setVisibility(View.VISIBLE);
    }

    private void showAcceptProgressBar() {
        isInProgress = true;
        centralProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideAcceptProgressBar() {
        isInProgress = false;
        centralProgressBar.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar() {
        isInProgress = false;
        actionProgressBar.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.cancelButton)
    public void cancelRequest() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!isInProgress) {
            makeCancelRideCall();
        }
    }

    @OnClick(R.id.startButton)
    public void startRideButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!isInProgress) {
            makeStartRideCall();
        }
    }

    @OnClick(R.id.completeRideButton)
    public void completeRideButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!isInProgress) {
            makeCompleteRideApiCall();
        }
    }

    @OnClick(R.id.closeIconView)
    public void closeCompleteInfoClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        hideCompletedRideInfo();
    }

    @OnCheckedChanged(R.id.onlineStatusSwitchView)
    public void onlineStatusSwitchCheckChanged(CompoundButton compoundButton, boolean isOn) {
        if (!GlobalDataManager.getInstance().loggedInUserData.getLicenseAcceptStatus().equalsIgnoreCase("accepted") ||
                !GlobalDataManager.getInstance().loggedInUserData.getCnicAcceptStatus().equalsIgnoreCase("accepted")) {
            Toast.makeText(getActivity(), "You are not allowed to be online as your documents are not accepted", Toast.LENGTH_SHORT).show();
            return;
        }
        scheduleStatusChangeAPiCall();
        if (isOn) {
            onlineStatusSwitchView.setText("Online");
        } else {
            onlineStatusSwitchView.setText("Offline");
        }
    }


    private void makeAcceptRideCall() {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.displayToastInfoMessage(getActivity(), getString(R.string.connectivity_error));
            return;
        }
        showAcceptProgressBar();
        String rideId = "" + selectedRideData.getRId();
        String riderId = "" + selectedRideData.getRRiderId();
        String driverId = GlobalDataManager.getInstance().loggedInUserData.getUserId();

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.acceptRide(rideId, driverId, riderId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    hideAcceptProgressBar();
                    GeneralServerResponse generalServerResponse = response.body();
                    String message = "Great to accept ride request. Start the ride when you meet passenger";
                    if (generalServerResponse.getSuccess()) {
                        onRideAccepted();
                    } else {
                        message = generalServerResponse.getMessage();
                    }
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), message);
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                if (isAdded()) {
                    hideAcceptProgressBar();
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                }
            }
        });
    }

    private void makeCancelRideCall() {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.displayToastInfoMessage(getActivity(), getString(R.string.connectivity_error));
            return;
        }

        showProgressBar();
        int rideId = selectedRideData.getRId();
        int riderId = selectedRideData.getRRiderId();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.cancelRide(rideId, riderId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    hideProgressBar();
                    if (response != null && response.body() != null) {
                        onRideCancelled();
                        GeneralServerResponse generalServerResponse = response.body();
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), generalServerResponse.getMessage());
                    } else {
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                if (isAdded()) {
                    hideProgressBar();
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                }
            }
        });
    }

    private void makeStartRideCall() {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.displayToastInfoMessage(getActivity(), getString(R.string.connectivity_error));
            return;
        }
        showProgressBar();

        final CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        String rideId = "" + selectedRideData.getRId();
        String actualLatLng = "" + customLocation.getLatitude() + "," + customLocation.getLongitude();
        String fromLocName = selectedRideData.getRFromLocationName();
        selectedRideData.setRActualFromLat(customLocation.getLatitude());
        selectedRideData.setRActualFromLng(customLocation.getLongitude());


        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.startRide(rideId, actualLatLng, fromLocName);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    if (response == null || response.body() == null) {
                        hideProgressBar();
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                        return;
                    }
                    GeneralServerResponse generalServerResponse = response.body();
                    if (generalServerResponse.getSuccess()) {
                        onRideStarted();
                    } else {
                        hideProgressBar();
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), generalServerResponse.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                if (isAdded()) {
                    hideProgressBar();
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                }
            }
        });
    }

    private void uploadStartedRideMapScreenshot(final String rideScreenshotFilePath) {
        int rideId = selectedRideData.getRId();
        MultipartBody.Part mapScreenshotBody = null;
        if (rideScreenshotFilePath != null) {
            File file = new File(rideScreenshotFilePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            mapScreenshotBody = MultipartBody.Part.createFormData("map_screenshot", file.getName(), reqFile);
        }

        RequestBody rideIdBody = RequestBody.create(MediaType.parse("text/plain"), "" + rideId);
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.uploadRideImage(rideIdBody, mapScreenshotBody);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    if (response == null || response.body() == null) {
                        uploadStartedRideMapScreenshot(rideScreenshotFilePath);
                        return;
                    }
                    GeneralServerResponse generalServerResponse = response.body();
                    if (!generalServerResponse.getSuccess()) {
                        uploadStartedRideMapScreenshot(rideScreenshotFilePath);
                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                if (isAdded()) {
                    uploadStartedRideMapScreenshot(rideScreenshotFilePath);
                }
            }
        });
    }

    private void makeCompleteRideApiCall() {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.displayToastInfoMessage(getActivity(), getString(R.string.connectivity_error));
            return;
        }
        showAcceptProgressBar();

        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        String rideId = "" + selectedRideData.getRId();
        String actualLatLng = "" + customLocation.getLatitude() + "," + customLocation.getLongitude();
        String toLocName = getAddressFromLatLng(customLocation.getLatitude(), customLocation.getLongitude());

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<CompleteRideResponse> call = apiService.completeRide(rideId, actualLatLng, toLocName);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<CompleteRideResponse>() {
            @Override
            public void onResponse(Call<CompleteRideResponse> call, Response<CompleteRideResponse> response) {
                if (isAdded()) {
                    if (response == null || response.body() == null) {
                        hideAcceptProgressBar();
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                        return;
                    }
                    CompleteRideResponse completeRideResponse = response.body();
                    if (completeRideResponse.getSuccess()) {
                        onRideCompleted(completeRideResponse.getData());
                    } else {
                        hideAcceptProgressBar();
                        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), completeRideResponse.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<CompleteRideResponse> call, Throwable t) {
                if (isAdded()) {
                    hideAcceptProgressBar();
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.somehow_request_failed));
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String id = marker.getId();
        selectedRideData = passengersRequestsMap.get(id);
        if (selectedRideData != null) {
            showAcceptCancelDialog();
        } else {
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), "Ride doesn't exist anymore, choose another one");
        }


        return true;
    }

    private void showAcceptCancelDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Valet");
        alertDialogBuilder.setMessage(selectedRideData.getRFromLocationName())
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        initAcceptRideProcess();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.create().show();
    }

    private void initAcceptRideProcess() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        makeAcceptRideCall();
    }

    private void onRideAccepted() {
        GlobalUtil.printLog("HomeFragment", "Inside onRideAccepted");
        driverState = ACCEPTED_STATE;
        showStartActionLayout();
        cancelFetchPassengerTimer();
        runCheckRideStatusTimer();
    }

    private void showStartActionLayout() {
        centralProgressBar.setVisibility(View.INVISIBLE);
        requestActionsLayout.setVisibility(View.VISIBLE);
    }

    private void showCompleteButton() {
        completeRideButton.setVisibility(View.VISIBLE);
    }

    private void hideCompleteRideButton() {
        completeRideButton.setVisibility(View.GONE);
    }

    private void hideStartActionsLayout() {
        requestActionsLayout.setVisibility(View.INVISIBLE);
    }

    private void showCompletedRideInfo(CompletedRideData completedRideData) {
        basePriceTextView.setText("" + completedRideData.getRBasePrice());
        chargePerKmTextView.setText("" + completedRideData.getRPerKmCharge());
        chargePerMinTextView.setText("" + completedRideData.getRPerMinCharge());
        totalMinsTextView.setText("" + completedRideData.getRTotalMins());
        totalKmTextView.setText("" + completedRideData.getRTotalKms());
        totalFareTextView.setText("" + completedRideData.getRTotalCharged());

        completeRideInfoLayout.setVisibility(View.VISIBLE);
        closeInfoIconView.setVisibility(View.VISIBLE);
    }

    private void hideCompletedRideInfo() {
        completeRideInfoLayout.setVisibility(View.GONE);
        closeInfoIconView.setVisibility(View.GONE);
    }

    private void onRideCancelled() {
        cancelStatusTimer();
        mMap.clear();
        selectedRideData = null;
        driverState = INIT_STATE;
        hideProgressBar();
        hideStartActionsLayout();
        hideCompleteRideButton();
        hideAcceptProgressBar();
        runFetchPassenegrTimer();
    }

    private void onRideCompleted(CompletedRideData completedRideData) {
        onRideCancelled();
        showCompletedRideInfo(completedRideData);

    }

    private void onRideStarted() {
        driverState = START_STATE;
        hideProgressBar();
        hideStartActionsLayout();
        initDrawRoute();
        showCompleteButton();
    }

    private void initDrawRoute() {
        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        String sourceLoc = "" + customLocation.getLatitude() + "," + customLocation.getLongitude();
        String destLoc = "" + selectedRideData.getRToLat() + "," + selectedRideData.getRToLng();
        fetchRoutePoints(sourceLoc, destLoc);
    }

    private void fetchRoutePoints(final String originalLoc, final String destination) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            drawSourceDestinationTwoPointsArc(originalLoc, destination);
            return;
        }

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<RouteResultResponse> call = apiService.fetchRoutePoints(ROUTE_API_URL, originalLoc, destination, false, "metric", "driving");
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<RouteResultResponse>() {
            @Override
            public void onResponse(Call<RouteResultResponse> call, Response<RouteResultResponse> response) {
                RouteResultResponse routeResultResponse = response.body();
                if (routeResultResponse != null
                        && routeResultResponse.getRoutes() != null
                        && routeResultResponse.getRoutes().size() > 0) {

                    String points = routeResultResponse.getRoutes().get(0).getOverviewPolyline().getRoutePoints();
                    List<LatLng> decodedPoints = GlobalUtil.decodePoly(points);
                    drawRoute(decodedPoints);
                } else {
                    drawSourceDestinationTwoPointsArc(originalLoc, destination);
                }
                runCheckRideStatusTimer();
            }

            @Override
            public void onFailure(Call<RouteResultResponse> call, Throwable t) {
                drawSourceDestinationTwoPointsArc(originalLoc, destination);
                runCheckRideStatusTimer();
            }
        });
    }

    private void drawSourceDestinationTwoPointsArc(String originalLoc, String destination) {
        try {
            Double originalLatitude = Double.valueOf(originalLoc.split(",")[0]);
            Double originalLongitude = Double.valueOf(originalLoc.split(",")[1]);
            Double destLatitude = Double.valueOf(destination.split(",")[0]);
            Double destLongitude = Double.valueOf(destination.split(",")[1]);

            LatLng oriLatLng = new LatLng(originalLatitude, originalLongitude);
            LatLng destinLatLng = new LatLng(destLatitude, destLongitude);

            List<LatLng> decodedPoints = new ArrayList<>();
            decodedPoints.add(oriLatLng);
            decodedPoints.add(destinLatLng);

            drawRoute(decodedPoints);
        } catch (Exception exception) {
            GlobalUtil.printLog("Exception", "" + exception);
        }
    }

    private void drawRoute(List<LatLng> routePoints) {
        if (mMap != null && routePoints != null) {
            mMap.clear();

            PolylineOptions polylineOptions = new PolylineOptions();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng lastPoint = routePoints.get(routePoints.size() - 1);

            for (LatLng point : routePoints) {
                polylineOptions.add(point);
                builder.include(point);
            }

            mMap.addPolyline(polylineOptions);
            mMap.addMarker(new MarkerOptions()
                    .position(lastPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
            mMap.animateCamera(cu);

            onRouteDrawn();
        }

    }

    private void onRouteDrawn() {
        captureMapScreen();
    }


    private void fetchRideLatestStatus(final String rideId) {
        GlobalUtil.printLog("HomeFragment", "Inside fetchRideLatestStatus");
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetRideStatusResponse> call = apiService.getRideStatus(rideId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetRideStatusResponse>() {
            @Override
            public void onResponse(Call<GetRideStatusResponse> call, Response<GetRideStatusResponse> response) {
                if (isAdded() && response != null && response.body() != null) {
                    GetRideStatusResponse getRideStatusResponse = response.body();
                    if (getRideStatusResponse.getSuccess()) {
                        GlobalUtil.printLog("HomeFragment", "fetchRideLatestStatus=" + getRideStatusResponse.getRideStatus().getRideStatus());
                        if (getRideStatusResponse.getRideStatus().getRideStatus().equalsIgnoreCase("accepted")) {
                            onRideAccepted();
                        } else if (getRideStatusResponse.getRideStatus().getRideStatus().equalsIgnoreCase("started")) {
                            onRideStarted();
                        } else {
                            onRideCancelled();
                        }
                        return;
                    }
                }
                GlobalUtil.printLog("HomeFragment", "runCheckRideStatusTimer called from within fetchRideLatestStatus=");
                runCheckRideStatusTimer();

            }

            @Override
            public void onFailure(Call<GetRideStatusResponse> call, Throwable t) {
                GlobalUtil.printLog("HomeFragment", "fetchRideLatestStatus failed");
                if (isAdded()) {
                    runCheckRideStatusTimer();
                }
            }
        });
    }

    private void checkStartedRide(String userId) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetStartedRideResponse> call = apiService.getStartedRide(userId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetStartedRideResponse>() {
            @Override
            public void onResponse(Call<GetStartedRideResponse> call, Response<GetStartedRideResponse> response) {
                if (isAdded()) {
                    if (response != null && response.body() != null) {
                        GetStartedRideResponse getStartedRideResponse = response.body();
                        if (getStartedRideResponse.getSuccess() && getStartedRideResponse.getData() != null) {
                            GlobalUtil.printLog("HomeFragment", "checkStartedRide Success");
                            selectedRideData = getStartedRideResponse.getData();
                            fetchRideLatestStatus("" + selectedRideData.getRId());
                            return;
                        }
                    }
                    GlobalUtil.printLog("HomeFragment", "No Ride found");
                    fetchPassengersRequests();
                }
            }

            @Override
            public void onFailure(Call<GetStartedRideResponse> call, Throwable t) {
                if (isAdded()) {
                    GlobalUtil.printLog("HomeFragment", "checkStartedRide failed");
                    fetchPassengersRequests();
                }
            }
        });
    }

    private void updateOnlineStatusOfDriverAPI(String onlineStatus) {
        String driverId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.updateOnlineStatus(driverId, onlineStatus);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    if (response != null && response.body() != null) {
                        GeneralServerResponse getStartedRideResponse = response.body();
                        if (getStartedRideResponse.getSuccess()) {
                            GlobalUtil.printLog("HomeFragment", "Update online status API Success");
                        } else {
                            GlobalUtil.printLog("HomeFragment", "Update online status API failed");
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                GlobalUtil.printLog("HomeFragment", "Update online status API failed");
            }
        });
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        String place = "Unknown Place";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            place = address.getAddressLine(0) + "," + address.getAddressLine(1);
            GlobalUtil.printLog("GeoDebug", "" + address.toString());
        } catch (Exception exception) {
            GlobalUtil.printLog("GeoDebug", "" + exception);
        }
        return place;
    }

    private void scheduleStatusChangeAPiCall() {
        if (onlineStatusTimer != null) {
            cancelTimer(onlineStatusTimer);
            onlineStatusTimer = null;
        }
        onlineStatusTimer = new Timer();
        onlineStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAdded()) {
                    String status = "online";
                    if (!onlineStatusSwitchView.isChecked()) {
                        status = "offline";
                        ValetPreferences.saveOnlineStatus(getActivity(), false);
                    } else {
                        ValetPreferences.saveOnlineStatus(getActivity(), true);
                    }
                    updateOnlineStatusOfDriverAPI(status);
                }
            }
        }, 1000);
    }

    public void captureMapScreen() {
        SnapshotReadyCallback callback = new SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                try {
                    rootLayout.setDrawingCacheEnabled(true);
                    Bitmap backBitmap = rootLayout.getDrawingCache();
                    Bitmap bmOverlay = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), backBitmap.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(snapshot, new Matrix(), null);
                    canvas.drawBitmap(backBitmap, 0, 0, null);

                    String rideScreenshotFilePath = GlobalUtil.getFileStoragePath(getActivity(), "ride_screenshot.png");
                    GlobalUtil.storeBitmapToFilePath(bmOverlay, rideScreenshotFilePath);

                    uploadStartedRideMapScreenshot(rideScreenshotFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mMap.snapshot(callback);

    }


}
