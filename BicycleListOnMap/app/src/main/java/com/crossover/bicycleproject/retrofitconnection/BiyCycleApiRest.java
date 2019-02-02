package com.crossover.bicycleproject.retrofitconnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class BiyCycleApiRest {

    public static final String BASE_URL = "http://192.168.8.100:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
