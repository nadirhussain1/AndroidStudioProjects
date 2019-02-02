package com.edwardvanraak.materialbarcodescannerexample.network;


import com.edwardvanraak.materialbarcodescannerexample.model.login.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by nadirhussain on 26/09/2016.
 */

public interface RetroInterface {
    @GET
    Call<ResponseBody> performFreeTextSearch(@Header("Authorization") String token, @Url String url);

    @GET
    Call<ResponseBody> performScanSearch(@Header("Authorization") String token, @Url String url, @Query("costs") String cost, @Query("inbound_shipping") String inboundShipping);


    @GET("login")
    Call<LoginResponse> performLogin(@Header("Authorization") String authorization, @Header("Device-Serial") String deviceImei, @Header("Device-Name") String deviceName);

    @GET("offlinedb/amazon_product")
    Call<ResponseBody> getTotalCountOfAmazonProduct(@Header("Authorization") String token, @Query("timestamp") String timeStamp);

    @GET("offlinedb/amazon_product_prices")
    Call<ResponseBody> getTotalCountOfAmazonProductPrices(@Header("Authorization") String token, @Query("timestamp") String timeStamp);

    @GET("offlinedb/amazon_product")
    Call<ResponseBody> getAmazonProductPage(@Header("Authorization") String token, @Query("timestamp") String timeStamp, @Query("page") Integer page, @Query("page_size") Integer pageSize);

    @GET("offlinedb/amazon_product_prices")
    Call<ResponseBody> getAmazonProductPricesPage(@Header("Authorization") String token, @Query("timestamp") String timeStamp, @Query("page") Integer page, @Query("page_size") Integer pageSize);


}
