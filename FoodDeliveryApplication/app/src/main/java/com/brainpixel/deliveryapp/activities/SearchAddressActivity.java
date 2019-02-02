package com.brainpixel.deliveryapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.adapters.SearchResultsAdapter;
import com.brainpixel.deliveryapp.handlers.OnAddressSelected;
import com.brainpixel.deliveryapp.handlers.OnLatLngDecodeEvent;
import com.brainpixel.deliveryapp.handlers.OnSearchedLocationClick;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.searchloc.PlacesSearchResponse;
import com.brainpixel.deliveryapp.model.searchloc.SearchDetailResponse;
import com.brainpixel.deliveryapp.model.searchloc.SearchedPlaceItem;
import com.brainpixel.deliveryapp.model.ShippingAddress;
import com.brainpixel.deliveryapp.network.RetroApiClient;
import com.brainpixel.deliveryapp.network.RetroInterface;
import com.brainpixel.deliveryapp.services.GeoDecodeAddressService;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 19/11/2018.
 */

public class SearchAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.mapLayout)
    RelativeLayout mapLayout;
    @BindView(R.id.locationNameView)
    TextView locationNameView;
    @BindView(R.id.searchInputEditor)
    EditText searchInputEditor;
    @BindView(R.id.searchInputLayout)
    RelativeLayout searchInputLayout;
    @BindView(R.id.pinLocationLayout)
    RelativeLayout pinLocationLayout;
    @BindView(R.id.searchedResultsHeadTextView)
    TextView searchResultHeadTextView;

    @BindView(R.id.searchRecyclerView)
    RecyclerView locationsRecyclerView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private Timer searchDelayTimer;
    private GoogleMap mMap;
    private ShippingAddress selectedAddressItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        EventBus.getDefault().register(this);

        searchInputEditor.addTextChangedListener(new SearchTextWatcher());
        loadMapView();
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.address_search_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this);

        searchResultHeadTextView.setVisibility(View.GONE);
        locationsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadMapView() {
        SupportMapFragment fragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mapView, fragment).commit();

        fragment.getMapAsync(this);
    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        goBack();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        goBack();
    }

    private void goBack() {
        finish();
    }

    @OnClick(R.id.clearImageView)
    public void onClearClick() {
        searchInputEditor.setText("");
    }

    @OnClick(R.id.pinLocationLayout)
    public void pinLocationClicked() {
        showMapView();
    }

    @OnClick(R.id.pinLocationDoneButton)
    public void pinLocationDoneClick() {
        goBackToAddressFragment();
    }

    private void goBackToAddressFragment() {
        OnAddressSelected onAddressSelected = new OnAddressSelected();
        onAddressSelected.selectedAddressItem = selectedAddressItem;
        EventBus.getDefault().post(onAddressSelected);

        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnLatLngDecodeEvent event) {
        this.selectedAddressItem = event.selectedAddressItem;
        if (selectedAddressItem != null) {
            locationNameView.setText(selectedAddressItem.getDescription());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnSearchedLocationClick event) {
        SearchedPlaceItem searchedPlaceItem = event.clickedPlaceItem;
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        selectedAddressItem = new ShippingAddress();
        selectedAddressItem.setDescription(searchedPlaceItem.getDescription());
        fetchPlaceDetails(searchedPlaceItem.getReference(), getString(R.string.google_places_key));
    }

    private void showRecyclerView() {
        pinLocationLayout.setVisibility(View.VISIBLE);
        searchResultHeadTextView.setVisibility(View.VISIBLE);
        locationsRecyclerView.setVisibility(View.VISIBLE);
        mapLayout.setVisibility(View.GONE);
    }

    private void showMapView() {
        pinLocationLayout.setVisibility(View.GONE);
        searchResultHeadTextView.setVisibility(View.GONE);
        locationsRecyclerView.setVisibility(View.GONE);
        mapLayout.setVisibility(View.VISIBLE);
    }

    private void onSearchEditorNewInput(String input) {
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        if (input.isEmpty()) {
            displayListOfSearchedLocations(new ArrayList<SearchedPlaceItem>());
            return;
        }

        String locationLatLong = GlobalDataManager.getInstance().configData.getCenterLatitude() + "," + GlobalDataManager.getInstance().configData.getCenterLongitude();
        String radius = "" + GlobalDataManager.getInstance().configData.getRadius();
        fetchGooglePlacesPrediction(input, locationLatLong, radius, getString(R.string.google_places_key));
    }

    private void displayListOfSearchedLocations(List<SearchedPlaceItem> list) {
        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(this, list);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationsRecyclerView.setAdapter(searchResultsAdapter);
    }


    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void cancelTimer() {
        if (searchDelayTimer != null) {
            searchDelayTimer.cancel();
            searchDelayTimer = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        zoomCameraToCenter();

        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                startAddressDecodeService(cameraPosition.target);
            }
        });
    }

    private void startAddressDecodeService(LatLng latLng) {
        String latLngString = "" + latLng.latitude + "," + latLng.longitude;
        Intent intent = new Intent(this, GeoDecodeAddressService.class);
        intent.putExtra(GeoDecodeAddressService.KEY_LATLNG, latLngString);
        startService(intent);
    }

    private void zoomCameraToCenter() {
        LatLng mapCenterPoint = new LatLng(GlobalDataManager.getInstance().configData.getCenterLatitude(), GlobalDataManager.getInstance().configData.getCenterLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenterPoint, 15));

    }

    private void fetchGooglePlacesPrediction(String text, String location, String radius, String apiKey) {
        showProgressBarLayout();

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<PlacesSearchResponse> call = apiService.getSearchPlacePredictionsList(text, location, radius, apiKey);
        call.enqueue(new Callback<PlacesSearchResponse>() {
            @Override
            public void onResponse(Call<PlacesSearchResponse> call, Response<PlacesSearchResponse> response) {
                hideProgressBarLayout();
                PlacesSearchResponse placesSearchResponse = response.body();
                if (placesSearchResponse != null
                        && placesSearchResponse.getSearchedPlaceItemList() != null && placesSearchResponse.getSearchedPlaceItemList().size() > 0) {

                    displayListOfSearchedLocations(placesSearchResponse.getSearchedPlaceItemList());
                } else {
                    displayListOfSearchedLocations(new ArrayList<SearchedPlaceItem>());
                }
            }

            @Override
            public void onFailure(Call<PlacesSearchResponse> call, Throwable t) {
                hideProgressBarLayout();
            }
        });

    }

    private void fetchPlaceDetails(String reference, String apiKey) {
        showProgressBarLayout();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<SearchDetailResponse> call = apiService.getPlaceDetails(false, reference, apiKey);
        call.enqueue(new Callback<SearchDetailResponse>() {
            @Override
            public void onResponse(Call<SearchDetailResponse> call, Response<SearchDetailResponse> response) {
                hideProgressBarLayout();
                SearchDetailResponse searchDetailResponse = response.body();

                if (searchDetailResponse != null && searchDetailResponse.getSearchDetailResult() != null
                        && searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry() != null) {

                    double latitude = searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry().getSearchLocationItem().getLatitude();
                    double longitude = searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry().getSearchLocationItem().getLongitude();

                    selectedAddressItem.setLatitude(latitude);
                    selectedAddressItem.setLongitude(longitude);

                }
                goBackToAddressFragment();

            }

            @Override
            public void onFailure(Call<SearchDetailResponse> call, Throwable t) {
                hideProgressBarLayout();
                GlobalUtil.showToastMessage(SearchAddressActivity.this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            }
        });


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
            showRecyclerView();
            final String inputText = editable.toString();
            searchDelayTimer = new Timer();
            searchDelayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onSearchEditorNewInput(inputText);
                        }
                    });

                }
            }, 1000);
        }
    }
}
