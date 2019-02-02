package com.edwardvanraak.materialbarcodescannerexample.network;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nadirhussain on 26/09/2016.
 */

public class RetroApiClient {
    private static final String BASE_URL = "https://api.ezhustle.com/v1/";
    public static final String FREE_SEARCH_URL=BASE_URL+"product/search/";
    public static final String ASIN_SEARCH_URL=FREE_SEARCH_URL+"ASIN/";
    public static final String ISBN_SEARCH_URL=FREE_SEARCH_URL+"ISBN/";
    public static final String UPC_SEARCH_URL=FREE_SEARCH_URL+"UPC/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            CustomRetrofitInterceptor customRetrofitInterceptor = new CustomRetrofitInterceptor();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .serializeNulls()
                            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                            .create()))
                    .build();
        }
        return retrofit;
    }


}
