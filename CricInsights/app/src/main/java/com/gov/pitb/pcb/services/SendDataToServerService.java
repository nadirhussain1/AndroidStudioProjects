package com.gov.pitb.pcb.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.FielderPosition;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.utils.GlobalUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 01/08/2017.
 */

public class SendDataToServerService extends IntentService {
    public static final int SERVICE_ID = 1234;
    private List<Delivery> unsyncDeliveries;
    private int currentDeliveryIndex = 0;

    public SendDataToServerService() {
        super("SendDataToServerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (GlobalUtil.isInternetConnected(getApplicationContext())) {
            unsyncDeliveries = InsightsDbManager.getAllUnSyncDeliveries();
            GlobalUtil.printLog("ApiDebug", "SendDataToServerService Count=" + unsyncDeliveries.size());
            if (unsyncDeliveries != null && unsyncDeliveries.size() > 0) {
                currentDeliveryIndex = 0;
                sendNextDelivery();
            }
        }
    }

    private void sendNextDelivery() {
        GlobalUtil.printLog("ApiDebug", "Next Delivery Being sent");
        if ((currentDeliveryIndex < unsyncDeliveries.size()) && GlobalUtil.isInternetConnected(getApplicationContext())) {
            Delivery delivery = unsyncDeliveries.get(currentDeliveryIndex);
            if (delivery.getFielderPosId() > 0) {
                FielderPosition fielderPosition = InsightsDbManager.getFielderPosition(delivery.getFielderPosId());
                delivery.setFielderPositions(fielderPosition.getPositions());
            }
            sendDeliveryToServerCall(getApplicationContext(), delivery);
            currentDeliveryIndex++;
        }
    }

    private void sendDeliveryToServerCall(Context context, final Delivery delivery) {
        RetroInterface apiService = RetroApiClient.getClient(context).create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.sendDelivery(delivery.getDeliveryCustomMap());
        call.enqueue(new Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                GeneralServerResponse generalServerResponse = response.body();
                if (generalServerResponse != null && generalServerResponse.isSuccess()) {
                    delivery.setSyncWithServer(true);
                    InsightsDbManager.saveDelivery(delivery);
                    sendNextDelivery();
                    GlobalUtil.printLog("ApiDebug", "Sent success" + generalServerResponse.getMessage());
                } else {
                    GlobalUtil.printLog("ApiDebug", "Failed to send" + generalServerResponse);
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                GlobalUtil.printLog("ApiDebug", "" + t);
            }
        });
    }
}
