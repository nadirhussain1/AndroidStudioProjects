package com.gov.pitb.pcb.network;

import android.content.Context;

import com.gov.pitb.pcb.data.preferences.InsightsPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nadirhussain on 01/08/2017.
 */

public class CustomRetrofitInterceptor implements Interceptor {
    private Context context;

    public CustomRetrofitInterceptor(Context context) {
       this.context=context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String accessToken= InsightsPreferences.getAccessToken(context);
        if (accessToken != null) {
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("accessToken", accessToken)
                    .build();

            return chain.proceed(newRequest);
        }
        return chain.proceed(originalRequest);

    }


}

