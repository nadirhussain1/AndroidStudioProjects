package com.brainpixel.valetapp.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class CustomRetrofitInterceptor implements Interceptor {
    private static final String RCT_API_KEY = "woo48g0so4c84gk4g0c0kgwsso8w8g4o";

    public CustomRetrofitInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = originalRequest.newBuilder()
                .addHeader("X-API-KEY", RCT_API_KEY)
                .build();

        Response response = chain.proceed(newRequest);


        return response;
    }


}
