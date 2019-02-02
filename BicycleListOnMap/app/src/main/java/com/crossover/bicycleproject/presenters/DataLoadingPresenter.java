package com.crossover.bicycleproject.presenters;

import com.crossover.bicycleproject.contracts.DataLoaderContract;
import com.crossover.bicycleproject.model.Places;
import com.crossover.bicycleproject.retrofitconnection.BiyCycleApiRest;
import com.crossover.bicycleproject.retrofitconnection.RentAppEndpointInterface;
import com.crossover.bicycleproject.utils.GlobalData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class DataLoadingPresenter implements DataLoaderContract.DataLoaderUserContract {
    DataLoaderContract.DataLoaderViewContract viewContract;

    public DataLoadingPresenter(DataLoaderContract.DataLoaderViewContract viewContract) {
        this.viewContract = viewContract;
    }

    // Retrieve all nearby places to Saitama Prefecture by using Retrofit library.
    // Then navigate results back to Views. This is MVP pattern. So presenter will be sitting
    // in between model and views.
    @Override
    public void fetchPlacesData() {
        RentAppEndpointInterface apiService = BiyCycleApiRest.getClient().create(RentAppEndpointInterface.class);
        Call<Places> getPlacesCall = apiService.getPlaces(GlobalData.accessToken);
        getPlacesCall.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                Places places = response.body();
                viewContract.displayPlaces(places);
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {
                viewContract.displayErrorToast(t.getMessage());
            }
        });
    }
}
