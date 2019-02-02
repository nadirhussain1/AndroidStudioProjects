package com.brainpixel.valetapp.views.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brainpixel.valetapp.MainActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.core.GeoDecodeAddressService;
import com.brainpixel.valetapp.core.LocationHandler;
import com.brainpixel.valetapp.core.NetworkChangeReceiver;
import com.brainpixel.valetapp.interfaces.EventBusClasses.AddressDecodeEvent;
import com.brainpixel.valetapp.interfaces.EventBusClasses.NetworkStateChanged;
import com.brainpixel.valetapp.interfaces.EventBusClasses.PinLocationClickedEvent;
import com.brainpixel.valetapp.interfaces.EventBusClasses.SearchEditorInputChanged;
import com.brainpixel.valetapp.interfaces.EventBusClasses.SearchLocationPressedEvent;
import com.brainpixel.valetapp.model.CustomLocation;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.ride.DriverInfoObject;
import com.brainpixel.valetapp.model.ride.GetDriverLocationResponse;
import com.brainpixel.valetapp.model.ride.GetDriversResponse;
import com.brainpixel.valetapp.model.ride.GetRideStatusResponse;
import com.brainpixel.valetapp.model.ride.GetStartedRideResponse;
import com.brainpixel.valetapp.model.ride.RequestRideResponse;
import com.brainpixel.valetapp.model.ride.RideDriverInfoObject;
import com.brainpixel.valetapp.model.route.RouteResultResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.PiccasoCircleTransformation;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SOURCE_EDITOR_FOCUSED = 1;
    private static final int DESTINATION_EDITOR_FOCUSED = 2;
    private static final int RIDE_IN_PROGRESS = 2;
    private static final int RIDE_RESET_STATE = 1;

    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    @BindView(R.id.whereToLayaout)
    RelativeLayout whereToLayout;
    @BindView(R.id.backButtonLayout)
    RelativeLayout backButtonLayout;
    @BindView(R.id.searchInputsLayout)
    RelativeLayout searchInputsLayout;
    @BindView(R.id.sourceInputLayout)
    RelativeLayout sourceInputLayout;
    @BindView(R.id.destinationInputLayout)
    RelativeLayout destinationInputLayout;
    @BindView(R.id.sourceLocEditor)
    EditText sourceLocEditor;
    @BindView(R.id.destinationLocEditor)
    EditText destinationLocEditor;
    @BindView(R.id.clearImageView)
    ImageView sourceClearImageView;
    @BindView(R.id.clearDestImageView)
    ImageView destClearImageView;
    @BindView(R.id.pinLocLayout)
    RelativeLayout pinLocationLayout;
    @BindView(R.id.requestDriverLayout)
    RelativeLayout requestUberLayout;
    @BindView(R.id.requestingLayout)
    RelativeLayout requestingLayout;
    @BindView(R.id.requestingStaticTextView)
    TextView requestingSchedulingStaticLabelTextView;
    @BindView(R.id.driverInfoLayout)
    RelativeLayout driverInfoLayout;
    @BindView(R.id.requestUberButton)
    Button requestUberButton;
    @BindView(R.id.scheduleRideLayout)
    RelativeLayout scheduleRideLayout;
    @BindView(R.id.closeIconView)
    ImageView closeScheduleView;
    @BindView(R.id.scheduleTimeTextView)
    TextView scheduleTimeTextView;
    @BindView(R.id.scheduleDateTextView)
    TextView scheduleDateTextView;
    @BindView(R.id.estimatedFareStaticTextView)
    TextView estimatedFareStaticLabelView;
    @BindView(R.id.estimatedFareValueTextView)
    TextView estimatedFareTextView;
    @BindView(R.id.driverIconView)
    ImageView driverIconView;
    @BindView(R.id.driverNameTextView)
    TextView driverNameTextView;

    private GoogleMap mMap;
    private LocationHandler locationHandler;
    private Timer searchDelayTimer, fetchDriversTimer, fetchRideStatusTimer;
    private NetworkChangeReceiver networkChangeReceiver;
    private Snackbar messageSnackBar = null;


    private boolean isMapAlreadyLoaded = false;
    private LatLng pickUpLocationLatLng, destinationLocation, currentPinLocLatLng = null;
    private RideDriverInfoObject inProgressRideInfo;
    private String destinationLocationName, sourceLocatioName;
    private SearchTextWatcher sourceTextWatcher, destinationTextWatcher;
    private boolean isSearchFragmentVisible = false;
    private int currentFocusedEditor = SOURCE_EDITOR_FOCUSED;
    private int rideState;
    private long mLastClickTime = 0;
    private boolean isInProgress = false;

    private int scheduleDay, scheduledMonth, scheduledYear, scheduledStartHour, scheduledStartMinutes, scheduledEndHour, scheduledEndMinute = -1;
    private boolean isScheduledRide = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);
        registerConnectivityReceiver();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViewsAndClicks();
        sourceTextWatcher = new SearchTextWatcher();
        destinationTextWatcher = new SearchTextWatcher();
    }

    private void initViewsAndClicks() {
        rootLayout.setVisibility(View.INVISIBLE);
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
        cancelTimer();
        cancelFetchDriversTimer();

    }

    @Override
    public void onDestroy() {
        GlobalUtil.printLog("EventBusDebug", "Inside onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            whereToLayout.setVisibility(View.VISIBLE);
            loadMapFragment();
        }
        resumeLocationHandler();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            getActivity().finish();
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
        } else {
            checkStartedRide(GlobalDataManager.getInstance().loggedInUserData.getUserId());
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

    private void initDriversFetchInfoHandler() {
        fetchDriversTimer = new Timer();
        fetchDriversTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAdded() && getActivity() != null) {
                    fetchNearByDriversInfo();
                }
            }
        }, 30000);
    }

    private void initFetchRideStatus() {
        fetchRideStatusTimer = new Timer();
        fetchRideStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAdded() && getActivity() != null) {
                    fetchRideLatestStatus("" + inProgressRideInfo.getRId());
                }
            }
        }, 10000);


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

    @OnClick(R.id.backClickArea)
    public void backArrowClicked() {
        resetViewsToInitialState();
        mMap.clear();
        removePinLocation();
        removeSearchFragment();
        zoomCameraToCurrentLocation();
    }

    @OnClick(R.id.pinDoneButton)
    public void locationPinDoneClicked() {
        if (currentFocusedEditor != DESTINATION_EDITOR_FOCUSED) {
            onSourceLocationDone(currentPinLocLatLng);
        } else {
            onDestinationLocationDone(currentPinLocLatLng);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SearchLocationPressedEvent event) {
        if (currentFocusedEditor == SOURCE_EDITOR_FOCUSED) {
            onSourceLocationDone(event.searchedLocLatLng);
            sourceLocatioName = event.locationName;
            sourceLocEditor.setText(sourceLocatioName);
        } else {
            destinationLocationName = event.locationName;
            onDestinationLocationDone(event.searchedLocLatLng);
        }
    }

    private void onSourceLocationDone(LatLng latLng) {
        pickUpLocationLatLng = latLng;
        destinationLocEditor.requestFocus();
    }

    private void onDestinationLocationDone(LatLng latLng) {
        destinationLocation = latLng;
        if (pickUpLocationLatLng != null) {
            String orginalLocPoints = "" + pickUpLocationLatLng.latitude + "," + pickUpLocationLatLng.longitude;
            String destinationLocPoints = "" + destinationLocation.latitude + "," + destinationLocation.longitude;
            fetchRoutePoints(orginalLocPoints, destinationLocPoints, false);
        }
    }

    @OnFocusChange(R.id.destinationLocEditor)
    public void onDestiFocusChanged(View v, boolean hasFocus) {
        if (hasFocus) {
            destinationFocused();
        }

    }

    @OnFocusChange(R.id.sourceLocEditor)
    public void onSourceFocusChanged(View v, boolean hasFocus) {
        if (hasFocus) {
            onSourceFocused();
        }

    }

    @OnClick(R.id.sourceLocEditor)
    public void sourceEditorClicked() {
        sourceClicked();
    }

    @OnClick(R.id.sourceClearClickArea)
    public void sourceClearClicked() {
        if (sourceClearImageView.getVisibility() == View.VISIBLE) {
            sourceLocEditor.setText("");
        } else {
            sourceClicked();
        }
    }

    @OnClick(R.id.destClearClickArea)
    public void destinationClearClicked() {
        destinationLocEditor.setText("");
    }

    private void onSourceFocused() {
        sourceInputLayout.setBackgroundColor(Color.parseColor("#33000000"));
        destinationInputLayout.setBackgroundColor(Color.parseColor("#11000000"));
        destinationLocEditor.setText("");
        destClearImageView.setVisibility(View.INVISIBLE);

        if (sourceLocEditor.getText().length() > 0) {
            sourceClearImageView.setVisibility(View.VISIBLE);
        }
        sourceClicked();
    }


    private void sourceClicked() {
        if (currentFocusedEditor == DESTINATION_EDITOR_FOCUSED) { // First time click so show pin view
            removeSearchFragment();
            initPinLocation();
        } else if (!isSearchFragmentVisible) {
            // Show Search Fragment when clicked twice on SourceEditor
            showSearchFragment();
            removePinLocation();
            destinationLocEditor.removeTextChangedListener(destinationTextWatcher);
            sourceLocEditor.addTextChangedListener(sourceTextWatcher);
        }
        currentFocusedEditor = SOURCE_EDITOR_FOCUSED;

    }

    private void destinationFocused() {
        currentFocusedEditor = DESTINATION_EDITOR_FOCUSED;

        sourceInputLayout.setBackgroundColor(Color.parseColor("#11000000"));
        destinationInputLayout.setBackgroundColor(Color.parseColor("#33000000"));
        sourceClearImageView.setVisibility(View.INVISIBLE);

        destinationLocEditor.addTextChangedListener(destinationTextWatcher);
        sourceLocEditor.removeTextChangedListener(sourceTextWatcher);

        showSearchFragment();
        removePinLocation();
    }

    private void showSearchFragment() {
        isSearchFragmentVisible = true;
        ((MainActivity) getActivity()).addSearchFragment(R.id.searchLayoutContainer);
    }

    private void removeSearchFragment() {
        isSearchFragmentVisible = false;
        ((MainActivity) getActivity()).removeSearchFragment();
    }


    @OnClick(R.id.whereToTextView)
    public void whereToBoxClicked() {
        isScheduledRide = false;
        showSearchEditorWindow();
    }

    private void showSearchEditorWindow() {
        hideWhereToLayout();
        hideScheduleRideLayout();
        geoCodeInitialCurrentLoc();
        showSearchEditorsInputLayout();
        ((MainActivity) getActivity()).hideDrawerIcon();
    }

    @OnClick(R.id.scheduleImageView)
    public void scheduleRideIconClicked() {
        isScheduledRide = true;
        showScheduleRideLayout();
    }

    @OnClick(R.id.closeIconView)
    public void closeIconClicked() {
        hideScheduleRideLayout();
    }

    @OnClick(R.id.scheduleTimeTextView)
    public void scheduleTimeTextViewClicked() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), scheduleTimeSetListener, scheduledStartHour, scheduledStartMinutes, true);
        timePickerDialog.show();

    }

    @OnClick(R.id.scheduleDateTextView)
    public void scheduleDateClicked() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), scheduleDateSetListener, scheduledYear, scheduledMonth, scheduleDay);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        datePickerDialog.show();
    }

    @OnClick(R.id.setPickUpWindowButton)
    public void setPickUpWindowClicked() {
        hideScheduleRideLayout();
        showSearchEditorWindow();
    }


    @OnClick(R.id.requestUberButton)
    public void requestUberClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (!isInProgress) {
            isInProgress = true;

            String fromLatLng = "" + pickUpLocationLatLng.latitude + "," + pickUpLocationLatLng.longitude;
            String toLatLng = "" + destinationLocation.latitude + "," + destinationLocation.longitude;
            String fromLocName = sourceLocatioName;
            if (TextUtils.isEmpty(fromLocName)) {
                fromLocName = sourceLocEditor.getText().toString();
            }
            String toLocName = destinationLocationName;
            if (TextUtils.isEmpty(toLocName)) {
                toLocName = destinationLocEditor.getText().toString();
            }
            String riderId = GlobalDataManager.getInstance().loggedInUserData.getUserId();

            if (!isScheduledRide) {
                doRequestUberApiCall(riderId, fromLatLng, toLatLng, fromLocName, toLocName);
            } else {
                String scheduleDateTime = "" + scheduledYear + "-" + (scheduledMonth + 1) + "-" + scheduleDay + " " + scheduledStartHour + ":" + scheduledStartMinutes;
                doScheduleRideApiCall(riderId, fromLatLng, toLatLng, fromLocName, toLocName, scheduleDateTime);
            }
        }
    }

    @OnClick(R.id.cancelRideButton)
    public void cancelRideButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!isInProgress) {
            isInProgress = true;
            doCancelRide("" + inProgressRideInfo.getRId(), GlobalDataManager.getInstance().loggedInUserData.getUserId());
        }
    }

    private void resetViewsToInitialState() {
        backButtonLayout.setVisibility(View.INVISIBLE);
        searchInputsLayout.setVisibility(View.INVISIBLE);
        requestUberLayout.setVisibility(View.INVISIBLE);
        requestingLayout.setVisibility(View.INVISIBLE);
        driverInfoLayout.setVisibility(View.INVISIBLE);
        hideScheduleRideLayout();


        whereToLayout.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showDrawerIcon();
    }

    private void showSearchEditorsInputLayout() {
        searchInputsLayout.setVisibility(View.VISIBLE);
        destinationLocEditor.requestFocus();
    }

    private void hideWhereToLayout() {
        whereToLayout.setVisibility(View.INVISIBLE);
        backButtonLayout.setVisibility(View.VISIBLE);
    }

    private void showScheduleRideLayout() {
        if (scheduledEndMinute == -1) {
            Calendar calendar = Calendar.getInstance();
            scheduledYear = calendar.get(Calendar.YEAR);
            scheduledMonth = calendar.get(Calendar.MONTH);
            scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            scheduledStartHour = calendar.get(Calendar.HOUR);
            scheduledStartMinutes = calendar.get(Calendar.MINUTE);

            calendar.add(Calendar.MINUTE, 15);
            scheduledEndHour = calendar.get(Calendar.HOUR);
            scheduledEndMinute = calendar.get(Calendar.MINUTE);

            updateDateViews();
            updateTimeViews();
        }
        scheduleRideLayout.setVisibility(View.VISIBLE);
        closeScheduleView.setVisibility(View.VISIBLE);
    }

    private void hideScheduleRideLayout() {
        scheduleRideLayout.setVisibility(View.GONE);
        closeScheduleView.setVisibility(View.GONE);
    }

    private void showRequestingLayout() {
        backButtonLayout.setVisibility(View.INVISIBLE);
        requestUberLayout.setVisibility(View.INVISIBLE);
        if (isScheduledRide) {
            requestingSchedulingStaticLabelTextView.setText("Scheduling Ride");
        } else {
            requestingSchedulingStaticLabelTextView.setText("Finding Ride");
        }
        requestingLayout.setVisibility(View.VISIBLE);
    }

    private void onCancelRide() {
        driverInfoLayout.setVisibility(View.INVISIBLE);
        cancelRideStatusTimer();
        fetchNearByDriversInfo();
        backArrowClicked();
    }

    private void onRideAccepted(RideDriverInfoObject rideDriverInfo) {
        GlobalUtil.printLog("HomeFragment", "Ride accepted");
        cancelFetchDriversTimer();
        this.inProgressRideInfo = rideDriverInfo;
        fetchRideLatestStatus("" + inProgressRideInfo.getRId());
        onRideAcceptedViewChanges();
    }

    private void onRideScheduled() {
        Toast.makeText(getActivity(), "Your ride has been scheduled successfully", Toast.LENGTH_LONG).show();
        backArrowClicked();
    }

    private void onRideAcceptedViewChanges() {
        rideState = RIDE_IN_PROGRESS;
        requestingLayout.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(inProgressRideInfo.getUProfileImage())) {
            Picasso.with(getActivity()).load(inProgressRideInfo.getUProfileImage()).transform(new PiccasoCircleTransformation()).into(driverIconView);
        }
        String name = inProgressRideInfo.getUFirstName() + " " + inProgressRideInfo.getULastName();
        driverNameTextView.setText("Driver is on its way to you");
        driverInfoLayout.setVisibility(View.VISIBLE);
    }

    private void onRideStartedViewChanges() {
        rideState = RIDE_IN_PROGRESS;
        requestingLayout.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(inProgressRideInfo.getUProfileImage())) {
            Picasso.with(getActivity()).load(inProgressRideInfo.getUProfileImage()).transform(new PiccasoCircleTransformation()).into(driverIconView);
        }
        String name = inProgressRideInfo.getUFirstName() + " " + inProgressRideInfo.getULastName();
        driverNameTextView.setText("Drive is in progress ");
        driverInfoLayout.setVisibility(View.VISIBLE);
    }

    private void onRequestRideBusy(String message) {
        requestingLayout.setVisibility(View.INVISIBLE);
        requestUberLayout.setVisibility(View.VISIBLE);
        backButtonLayout.setVisibility(View.VISIBLE);
        requestUberButton.setText(message);
    }

    private void geoCodeInitialCurrentLoc() {
        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        pickUpLocationLatLng = new LatLng(customLocation.getLatitude(), customLocation.getLongitude());
        startAddressDecodeService(pickUpLocationLatLng, true);
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

    private void onRouteSpecified(List<LatLng> routePoints) {
        removePinLocation();
        hideKeyboard();

        searchInputsLayout.setVisibility(View.INVISIBLE);
        requestUberLayout.setVisibility(View.VISIBLE);

        if (!isScheduledRide) {
            requestUberButton.setText("Request Ride");
            String estimatedFare = GlobalUtil.calculateEstimatedFare(routePoints.get(0), routePoints.get(routePoints.size() - 1));
            estimatedFareTextView.setText(estimatedFare);
            estimatedFareStaticLabelView.setVisibility(View.VISIBLE);
        } else {
            requestUberButton.setText("Schedule Ride");
            estimatedFareStaticLabelView.setVisibility(View.GONE);

            String scheduledDateTime = getDateLabelValues() + " at " + getScheduledTimeString();
            estimatedFareTextView.setText(scheduledDateTime);
        }


    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sourceLocEditor.getWindowToken(), 0);
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
            addMarker(lastPoint);

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
            mMap.animateCamera(cu);
        }
        ((MainActivity) getActivity()).removeSearchFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddressDecodeEvent event) {
        if (!event.isSource) {
            destinationLocationName = event.address;
            destinationLocEditor.setText(event.address);
        } else {
            sourceLocatioName = event.address;
            sourceLocEditor.setText(event.address);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PinLocationClickedEvent event) {
        ((MainActivity) getActivity()).removeSearchFragment();
        initPinLocation();
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(options);
    }

    private void initPinLocation() {
        pinLocationLayout.setVisibility(View.VISIBLE);
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                currentPinLocLatLng = cameraPosition.target;
                startAddressDecodeService(currentPinLocLatLng, currentFocusedEditor == SOURCE_EDITOR_FOCUSED);
            }
        });

    }

    private void startAddressDecodeService(LatLng latLng, boolean isSource) {
        String latLngString = "" + latLng.latitude + "," + latLng.longitude;
        Intent intent = new Intent(getActivity(), GeoDecodeAddressService.class);
        intent.putExtra(GeoDecodeAddressService.KEY_LATLNG, latLngString);
        intent.putExtra(GeoDecodeAddressService.KEY_SOURCE_DEST, isSource);
        getActivity().startService(intent);
    }


    private void removePinLocation() {
        pinLocationLayout.setVisibility(View.GONE);
        mMap.setOnCameraChangeListener(null);
    }

    private void showClearCrossImage() {
        if (currentFocusedEditor == DESTINATION_EDITOR_FOCUSED) {
            destClearImageView.setVisibility(View.VISIBLE);
        } else {
            sourceClearImageView.setVisibility(View.VISIBLE);
        }
    }

    private void hideClearCrossImage() {
        sourceClearImageView.setVisibility(View.INVISIBLE);
        destClearImageView.setVisibility(View.INVISIBLE);

    }

    private void cancelTimer() {
        if (searchDelayTimer != null) {
            searchDelayTimer.cancel();
            searchDelayTimer = null;
        }
    }

    private void cancelFetchDriversTimer() {
        if (fetchDriversTimer != null) {
            fetchDriversTimer.cancel();
            fetchDriversTimer = null;
        }
    }

    private void cancelRideStatusTimer() {
        if (fetchRideStatusTimer != null) {
            fetchRideStatusTimer.cancel();
            fetchRideStatusTimer = null;
        }
    }


    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            cancelTimer();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final String inputText = editable.toString();
            if (inputText.length() > 0) {
                showClearCrossImage();
            } else {
                hideClearCrossImage();
            }

            searchDelayTimer = new Timer();
            searchDelayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isAdded()) {
                                    SearchEditorInputChanged event = new SearchEditorInputChanged();
                                    event.inputText = inputText;
                                    EventBus.getDefault().post(event);
                                }
                            }
                        });
                    }
                }
            }, 500);
        }
    }

    private void fetchRoutePoints(final String originalLoc, final String destination, final boolean isRideInProgress) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            drawSourceDestinationTwoPointsArc(originalLoc, destination, isRideInProgress);
            return;
        }

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<RouteResultResponse> call = apiService.fetchRoutePoints(SearchFragment.ROUTE_API_URL, originalLoc, destination, false, "metric", "driving");
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<RouteResultResponse>() {
            @Override
            public void onResponse(Call<RouteResultResponse> call, Response<RouteResultResponse> response) {
                if (isAdded()) {
                    RouteResultResponse routeResultResponse = response.body();
                    if (routeResultResponse != null
                            && routeResultResponse.getRoutes() != null
                            && routeResultResponse.getRoutes().size() > 0) {

                        String points = routeResultResponse.getRoutes().get(0).getOverviewPolyline().getRoutePoints();
                        List<LatLng> decodedPoints = GlobalUtil.decodePoly(points);
                        drawRoute(decodedPoints);
                        if (!isRideInProgress) {
                            onRouteSpecified(decodedPoints);
                        }
                    } else {
                        drawSourceDestinationTwoPointsArc(originalLoc, destination, isRideInProgress);
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteResultResponse> call, Throwable t) {
                if (isAdded()) {
                    drawSourceDestinationTwoPointsArc(originalLoc, destination, isRideInProgress);
                }
            }
        });
    }

    private void drawSourceDestinationTwoPointsArc(String originalLoc, String destination, boolean isRideInProgress) {
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

            if (!isRideInProgress) {
                onRouteSpecified(decodedPoints);
            }
        } catch (Exception exception) {
            GlobalUtil.printLog("Exception", "" + exception);
        }
    }

    private void fetchNearByDriversInfo() {
        CustomLocation customLocation = LocationHandler.getCurrentBestLocation();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetDriversResponse> call = apiService.getDriversListInRadius(customLocation.getLatitude(), customLocation.getLongitude(), GlobalDataManager.getInstance().loggedInUserData.getUserId());
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetDriversResponse>() {
            @Override
            public void onResponse(Call<GetDriversResponse> call, Response<GetDriversResponse> response) {
                if (isAdded()) {
                    if (response != null && response.body() != null) {
                        GetDriversResponse getDriversResponse = response.body();
                        if (getDriversResponse.getSuccess()) {
                            drawDriverPersonsMarkers(getDriversResponse.getData());
                        }
                    }
                    initDriversFetchInfoHandler();
                }
            }

            @Override
            public void onFailure(Call<GetDriversResponse> call, Throwable t) {
                if (isAdded()) {
                    initDriversFetchInfoHandler();
                }
            }
        });
    }

    private void drawDriverPersonsMarkers(List<DriverInfoObject> driversList) {
        if (isAdded()) {
            for (int count = 0; count < driversList.size(); count++) {
                DriverInfoObject driverInfoObject = driversList.get(count);
                if (driverInfoObject != null && driverInfoObject.getULatestLat() != null && driverInfoObject.getULatestLng() != null) {
                    LatLng latLng = new LatLng(driverInfoObject.getULatestLat(), driverInfoObject.getULatestLng());
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_marker_icon)));
                }
            }

        }
    }

    private void doRequestUberApiCall(String riderId, String fromLatLng, String toLatLng, String fromPlaceName, String toPlaceName) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            isInProgress = false;
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.connectivity_error));
            return;
        }
        GlobalUtil.printLog("HomeFragment", "doRequestUberApiCall made =");
        showRequestingLayout();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<RequestRideResponse> call = apiService.requestRide(riderId, fromLatLng, toLatLng, fromPlaceName, toPlaceName);
        call.enqueue(new Callback<RequestRideResponse>() {
            @Override
            public void onResponse(Call<RequestRideResponse> call, Response<RequestRideResponse> response) {
                if (isAdded()) {
                    isInProgress = false;
                    if (response == null || response.body() == null) {
                        onRequestRideBusy("Server not responded");
                        return;
                    }
                    RequestRideResponse requestRideResponse = response.body();
                    if (!requestRideResponse.getSuccess() || requestRideResponse.getData() == null) {
                        onRequestRideBusy("No driver available");
                        return;
                    }

                    onRideAccepted(requestRideResponse.getData().get(0));

                }
            }

            @Override
            public void onFailure(Call<RequestRideResponse> call, Throwable t) {
                isInProgress = false;
                if (isAdded()) {
                    onRequestRideBusy("Request Failed");
                }
            }
        });

    }

    private void doScheduleRideApiCall(String riderId, String fromLatLng, String toLatLng, String fromPlaceName, String toPlaceName, String scheduleTime) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            isInProgress = false;
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.connectivity_error));
            return;
        }
        showRequestingLayout();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.scheduleRide(riderId, fromLatLng, toLatLng, fromPlaceName, toPlaceName, scheduleTime);
        call.enqueue(new Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                if (isAdded()) {
                    isInProgress = false;
                    if (response == null || response.body() == null) {
                        onRequestRideBusy("Request to schedule failed");
                        return;
                    }
                    GeneralServerResponse generalServerResponse = response.body();
                    if (!generalServerResponse.getSuccess()) {
                        onRequestRideBusy(generalServerResponse.getMessage());
                        return;
                    }

                    onRideScheduled();
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                isInProgress = false;
                if (isAdded()) {
                    onRequestRideBusy("Request to schedule failed");
                }
            }
        });

    }


    private void doCancelRide(String rideId, String userId) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            isInProgress = false;
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.connectivity_error));
            return;
        }

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.cancelRide(rideId, userId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                isInProgress = false;
                if (isAdded()) {
                    GeneralServerResponse generalServerResponse = response.body();
                    if (generalServerResponse.getSuccess()) {
                        onCancelRide();
                    }
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), generalServerResponse.getMessage());
                }

            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                isInProgress = false;
                if (isAdded()) {
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), t.getMessage());
                }
            }
        });
    }

    private void fetchRideLatestStatus(String rideId) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetRideStatusResponse> call = apiService.getRideStatus(rideId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetRideStatusResponse>() {
            @Override
            public void onResponse(Call<GetRideStatusResponse> call, Response<GetRideStatusResponse> response) {
                if (isAdded() && response != null && response.body() != null) {
                    GetRideStatusResponse getRideStatusResponse = response.body();
                    if (getRideStatusResponse.getSuccess()) {

                        if (getRideStatusResponse.getRideStatus().getRideStatus().equalsIgnoreCase("accepted")) {
                            rideState = RIDE_IN_PROGRESS;
                            onRideAcceptedViewChanges();
                            String targetLocation = "" + LocationHandler.getCurrentBestLocation().getLatitude() + "," + LocationHandler.getCurrentBestLocation().getLongitude();
                            fetchDriverLatestLoc("" + inProgressRideInfo.getRDriverId(), targetLocation);
                        } else if (getRideStatusResponse.getRideStatus().getRideStatus().equalsIgnoreCase("started")) {
                            rideState = RIDE_IN_PROGRESS;
                            onRideStartedViewChanges();
                            String targetLocation = "" + inProgressRideInfo.getRToLat() + "," + inProgressRideInfo.getRToLng();
                            fetchDriverLatestLoc("" + inProgressRideInfo.getRDriverId(), targetLocation);
                        } else {
                            rideState = RIDE_RESET_STATE;
                            backArrowClicked();
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<GetRideStatusResponse> call, Throwable t) {
                if (isAdded()) {
                    initFetchRideStatus();
                }
            }
        });
    }

    private void fetchDriverLatestLoc(String driverId, final String targetRouteLocation) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GetDriverLocationResponse> call = apiService.getDriverLatestLocation(driverId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<GetDriverLocationResponse>() {
            @Override
            public void onResponse(Call<GetDriverLocationResponse> call, Response<GetDriverLocationResponse> response) {
                if (isAdded() && rideState == RIDE_IN_PROGRESS) {
                    if (response != null && response.body() != null) {
                        GetDriverLocationResponse getDriverLocationResponse = response.body();
                        if (getDriverLocationResponse.getSuccess()) {
                            String sourceLoc = getDriverLocationResponse.getDriverLocation().getuLatestLat() + "," + getDriverLocationResponse.getDriverLocation().getuLatestLng();
                            fetchRoutePoints(sourceLoc, targetRouteLocation, true);
                        }
                    }
                    initFetchRideStatus();
                }
            }

            @Override
            public void onFailure(Call<GetDriverLocationResponse> call, Throwable t) {
                if (isAdded() && rideState == RIDE_IN_PROGRESS) {
                    initFetchRideStatus();
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
                            hideWhereToLayout();
                            inProgressRideInfo = getStartedRideResponse.getData();
                            fetchRideLatestStatus("" + inProgressRideInfo.getRId());
                            return;
                        }
                    }
                    fetchNearByDriversInfo();
                }
            }

            @Override
            public void onFailure(Call<GetStartedRideResponse> call, Throwable t) {
                if (isAdded()) {
                    fetchNearByDriversInfo();
                }
            }
        });
    }

    private OnDateSetListener scheduleDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            scheduledYear = year;
            scheduledMonth = month;
            scheduleDay = day;

            updateDateViews();
        }
    };
    private OnTimeSetListener scheduleTimeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(scheduledYear, scheduledMonth, scheduleDay, hour, minute);
            if (calendar.getTime().before(new Date())) {
                Toast.makeText(getActivity(), "You can schedule ride only in future", Toast.LENGTH_SHORT).show();
                return;
            }

            scheduledStartHour = hour;
            scheduledStartMinutes = minute;
            calendar.add(Calendar.MINUTE, 15);
            scheduledEndHour = calendar.get(Calendar.HOUR);
            scheduledEndMinute = calendar.get(Calendar.MINUTE);
            updateTimeViews();
        }
    };

    private void updateTimeViews() {
        scheduleTimeTextView.setText(getScheduledTimeString());
    }

    private void updateDateViews() {
        scheduleDateTextView.setText(getDateLabelValues());
    }

    private String getScheduledTimeString() {
        String timeString = "" + scheduledStartHour + ":" + scheduledStartMinutes + " - " + scheduledEndHour + ":" + scheduledEndMinute;
        return timeString;
    }

    private String getDateLabelValues() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(scheduledYear, scheduledMonth, scheduleDay);

        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(calendar.getTime());

        String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());


        String dateTextView = dayName + ", " + monthName + " " + scheduleDay;
        return dateTextView;
    }


}
