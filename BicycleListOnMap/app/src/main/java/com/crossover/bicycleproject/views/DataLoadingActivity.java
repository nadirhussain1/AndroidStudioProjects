package com.crossover.bicycleproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crossover.bicycleproject.MapsActivity;
import com.crossover.bicycleproject.R;
import com.crossover.bicycleproject.contracts.DataLoaderContract;
import com.crossover.bicycleproject.model.Places;
import com.crossover.bicycleproject.presenters.DataLoadingPresenter;
import com.crossover.bicycleproject.utils.CommonUtil;
import com.crossover.bicycleproject.utils.GlobalData;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class DataLoadingActivity extends AppCompatActivity implements DataLoaderContract.DataLoaderViewContract {

    // This is intermediate screen. We are loading data here so that we don't need to create
    // mess in map activity. So everything related with loading of nearby places is done
    // on this activity and wait bar indication is shown to user.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_loading_activity);
        if (!CommonUtil.isInternetConnected(this)) {
            CommonUtil.showAlertDialog(getString(R.string.no_connection_alert_message), this);
        }
        DataLoadingPresenter dataLoadingPresenter = new DataLoadingPresenter(this);
        dataLoadingPresenter.fetchPlacesData();
    }

    // Now results have been retrived from REST API.So go to map activity and display markers.
    @Override
    public void displayPlaces(Places places) {
        GlobalData.places = places;
        launchMapsActivity();
    }

    @Override
    public void displayErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void launchMapsActivity() {
        Intent intent = new Intent(DataLoadingActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
