package com.crossover.bicycleproject.retrofitconnection;

import com.crossover.bicycleproject.model.AccessToken;
import com.crossover.bicycleproject.model.Credentials;
import com.crossover.bicycleproject.model.Message;
import com.crossover.bicycleproject.model.Payment;
import com.crossover.bicycleproject.model.Places;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public interface RentAppEndpointInterface {
    @POST("api/v1/auth")
    Call<AccessToken> authUser(@Body Credentials credentials);
    @POST("api/v1/register")
    Call<AccessToken> registerUser(@Body Credentials credentials);
    @POST("api/v1/rent")
    Call<Message> rentBiCylce(@Header("Authorization") String authorization,@Body Payment payment);
    @GET("api/v1/places")
    Call<Places> getPlaces(@Header("Authorization") String authorization);
}