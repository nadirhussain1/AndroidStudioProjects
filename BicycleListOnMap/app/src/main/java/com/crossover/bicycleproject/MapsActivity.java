package com.crossover.bicycleproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.bicycleproject.contracts.RentABikeContract;
import com.crossover.bicycleproject.model.Payment;
import com.crossover.bicycleproject.model.Result;
import com.crossover.bicycleproject.presenters.RentABikePresenter;
import com.crossover.bicycleproject.utils.GlobalData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,OnMarkerClickListener,RentABikeContract.RentABikeViewContract{
    // They represent location of Saitama Prefecture. As we have to show places near to it
    // so we need to move camera to this location. These location latitude and longitude has been
    // found from Google.

    private static final double SAITAMA_PREFECTURE_LAT = 35.8570;
    private static final double SAITAMA_PREFECTURE_LANG = 139.6488;

    private GoogleMap mMap;
    @Bind(R.id.rentActionLayout)
    RelativeLayout rentActionLayout;
    @Bind(R.id.cardNumberEditText)
    EditText cardNumberEditor;
    @Bind(R.id.bikeOwnerName)
    TextView bikeOwnerNameView;
    private String selectedMarkerTitle="";

    private RentABikePresenter rentABikePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        rentABikePresenter=new RentABikePresenter(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     *
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(SAITAMA_PREFECTURE_LAT, SAITAMA_PREFECTURE_LANG);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.setOnMarkerClickListener(this);

        if (GlobalData.places != null && GlobalData.places.getResults() != null) {
            List<Result> resultList = GlobalData.places.getResults();
            for (int count = 0; count < resultList.size(); count++) {
                Result result = resultList.get(count);
                latLng = new LatLng(result.getLocation().getLat(), result.getLocation().getLng());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(result.getName());

                mMap.addMarker(markerOptions);
            }
        }

    }

    @OnClick(R.id.rentABikeButton)
    public void rentABikePressed() {
        Payment payment =new Payment(cardNumberEditor.getText().toString(),selectedMarkerTitle);
        rentABikePresenter.invokeRentABikeService(payment);
        hideRentABikeLayout();
    }

    // When bottom layout is visible and user clicks on any area other than credit card edit area and
    // rent a bike button, then we need to hide bottom layout
    @OnClick(R.id.rentActionLayout)
    public void transParentAreaClicked(){
        hideRentABikeLayout();
    }
     // Show the message received from REST API after rent a bike action
    @Override
    public void showRentServiceMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCreditCardErrorMessage() {
        Toast.makeText(this,getString(R.string.card_number_error),Toast.LENGTH_SHORT).show();
    }

    // When user clicks on any marker then show layout at bottom like UBER app
    // show Bike Owner name and Credit Card Edit Area and RentABike Button.
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker!=null){
            selectedMarkerTitle = marker.getTitle();
            showRentABikeLayout();
        }
        return true;
    }
    private void showRentABikeLayout(){
        bikeOwnerNameView.setText(selectedMarkerTitle);
        rentActionLayout.setVisibility(View.VISIBLE);
    }
    private void hideRentABikeLayout(){
        rentActionLayout.setVisibility(View.GONE);
    }
}
