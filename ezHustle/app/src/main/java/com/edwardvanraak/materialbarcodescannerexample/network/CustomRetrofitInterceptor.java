package com.edwardvanraak.materialbarcodescannerexample.network;

import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nadirhussain on 27/09/2016.
 */

public class CustomRetrofitInterceptor implements Interceptor {
    private static final String RCT_API_KEY = "woo48g0so4c84gk4g0c0kgwsso8w8g4o";

    public CustomRetrofitInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;

        Request originalRequest = chain.request();
        Request newRequest;
        if (GlobalUtil.auth_token_value != null) {
            newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", GlobalUtil.auth_token_value)
                    .build();

            response = chain.proceed(newRequest);
        } else {
            response = chain.proceed(originalRequest);
        }


        return response;

    }


}
