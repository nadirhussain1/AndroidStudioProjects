package com.brainpixel.valetapp.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.EventBusClasses.OnUpcomingTripCancelled;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 06/06/2017.
 */


public class CancelScheduleRideWindow {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Activity activity;
    private Dialog dialog = null;
    private View dialogView;
    private String rideId;

    public CancelScheduleRideWindow(Activity activity, String rideId) {
        this.activity = activity;
        this.rideId = rideId;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.cancel_upcoming_trip_window, null);
        new ScalingUtility(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.setContentView(dialogView);
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.cancelRideButton)
    public void onCancelActionConfirm() {
        doCancelRide(rideId, GlobalDataManager.getInstance().loggedInUserData.getUserId());
    }

    @OnClick(R.id.neverMindButton)
    public void neverMindClicked() {
        dismissDialog();
    }

    private void onCancelledRide() {
        dismissDialog();
        EventBus.getDefault().post(new OnUpcomingTripCancelled());
    }

    private void doCancelRide(String rideId, String userId) {
        if (!GlobalUtil.isInternetConnected(activity)) {
            GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_message_title), activity.getString(R.string.connectivity_error));
            return;
        }
        showProgressBar();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.cancelRide(rideId, userId);
        ApiHelper.enqueueWithRetry(activity, call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                hideProgressBar();
                GeneralServerResponse generalServerResponse = response.body();
                if (generalServerResponse.getSuccess()) {
                    onCancelledRide();
                    return;
                }
                GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_message_title), generalServerResponse.getMessage());
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                hideProgressBar();
                GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_message_title), t.getMessage());
            }
        });
    }
}
