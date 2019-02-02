package com.brainpixel.valetapp.network;

/**
 * Created by nadirhussain on 08/10/2016.
 */

import android.util.Log;

import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pallavahooja on 16/05/16.
 */

public abstract class RetryableCallback<T> implements Callback<T> {

    private static final int TOTAL_RETRIES = 3;
    private static final String TAG = RetryableCallback.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;

    public RetryableCallback(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!ApiHelper.isCallSuccess(response))
            if (retryCount++ < TOTAL_RETRIES) {
                Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + TOTAL_RETRIES + ")");
                try {
                    GlobalUtil.printLog(TAG, "Response=" + response.body() + " = Message=" + response.message() + " =ErrorBody=" + response.errorBody().string());
                }catch (Exception e){

                }
                retry();
            } else
                onFinalResponse(call, response);
        else
            onFinalResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + TOTAL_RETRIES + ")");
            retry();
        } else
            onFinalFailure(call, t);
    }

    public void onFinalResponse(Call<T> call, Response<T> response) {

    }

    public void onFinalFailure(Call<T> call, Throwable t) {
    }

    private void retry() {
        call.clone().enqueue(this);
    }

}

