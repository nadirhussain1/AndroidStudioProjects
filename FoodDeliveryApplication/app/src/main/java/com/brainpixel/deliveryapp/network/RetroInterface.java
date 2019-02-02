package com.brainpixel.deliveryapp.network;


import com.brainpixel.deliveryapp.model.searchloc.PlacesSearchResponse;
import com.brainpixel.deliveryapp.model.searchloc.SearchDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nadirhussain on 26/09/2016.
 */

public interface RetroInterface {
    @GET("place/autocomplete/json?")
    Call<PlacesSearchResponse> getSearchPlacePredictionsList(@Query("input") String inputText, @Query("location") String location, @Query("radius") String radius, @Query("key") String apiKey);

    @GET("place/details/json?")
    Call<SearchDetailResponse> getPlaceDetails(@Query("sensor") boolean sensor, @Query("reference") String reference, @Query("key") String apiKey);


}
