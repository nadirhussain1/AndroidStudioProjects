package com.gov.pitb.pcb.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.model.ConditionsModel;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.CustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 22/05/2017.
 */

public class PitchWeatherConditionsDialog {
    @BindView(R.id.humidityRadioGroupLayout)
    RadioGroup humidityRadioGroup;
    @BindView(R.id.grassSpinner)
    Spinner grassSpinner;
    @BindView(R.id.hardnessSpinner)
    Spinner hardnessSpinner;
    @BindView(R.id.overallRadioGroupLayout)
    RadioGroup overallRadioGroup;
    @BindView(R.id.cloudySpinner)
    Spinner cloudsConditionsSpinner;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Dialog dialog = null;
    private Activity activity;
    private View dialogView;
    private ConditionsModel conditionsModel;

    public PitchWeatherConditionsDialog(Activity activity, String matchId, String inningId, int overId) {
        this.activity = activity;
        conditionsModel = new ConditionsModel(matchId, inningId, overId);
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.pre_match_conditions_layout, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
        bindSpinners();

        setPreviousValues();

    }

    private void setPreviousValues() {
        ConditionsModel preModel = InsightsDbManager.getWeatherConditions();
        if (preModel != null) {

            if (preModel.getHumidity().equalsIgnoreCase("moist")) {
                humidityRadioGroup.check(R.id.moistRadioButton);
            }
            if (preModel.getOverallWeather().equalsIgnoreCase("moist")) {
                overallRadioGroup.check(R.id.moistWindyRadioButton);
            }

            switch (preModel.getGrass()) {
                case "Low":
                    grassSpinner.setSelection(1);
                    break;
                case "High":
                    grassSpinner.setSelection(2);
                    break;
                default:
                    grassSpinner.setSelection(0);
                    break;
            }

            switch (preModel.getHardness()) {
                case "Medium":
                    hardnessSpinner.setSelection(1);
                    break;
                case "High":
                    hardnessSpinner.setSelection(2);
                    break;
                default:
                    hardnessSpinner.setSelection(0);
                    break;
            }
            switch (preModel.getClouds()) {
                case "Sunny":
                    cloudsConditionsSpinner.setSelection(1);
                    break;
                case "Partial Cloudy":
                    cloudsConditionsSpinner.setSelection(2);
                    break;
                default:
                    cloudsConditionsSpinner.setSelection(0);
                    break;
            }
        }

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

    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        String humidity = "Dry";
        String overallWeather = "Windy";
        if (humidityRadioGroup.getCheckedRadioButtonId() == R.id.moistRadioButton) {
            humidity = "Moist";
        }
        if (overallRadioGroup.getCheckedRadioButtonId() == R.id.moistWindyRadioButton) {
            overallWeather = "Moist";
        }

        conditionsModel.setHumidity(humidity);
        conditionsModel.setOverallWeather(overallWeather);
        conditionsModel.setGrass((String) grassSpinner.getSelectedItem());
        conditionsModel.setHardness((String) hardnessSpinner.getSelectedItem());
        conditionsModel.setClouds((String) cloudsConditionsSpinner.getSelectedItem());

        InsightsDbManager.saveWeatherConditions(conditionsModel);
        sendConditionsToServerCall(conditionsModel);
        dismissDialog();
    }

    @OnClick(R.id.crossIconClickView)
    public void crossClicked() {
        dismissDialog();
    }

    private void bindSpinners() {
        String[] grassOptionsArray = activity.getResources().getStringArray(R.array.grass_options);
        String[] hardnessOptionsArray = activity.getResources().getStringArray(R.array.hardness_options);
        String[] weatherOptionsArray = activity.getResources().getStringArray(R.array.weather_options);

        CustomSpinnerAdapter grassSpinnerAdapter = new CustomSpinnerAdapter(activity, new ArrayList<>(Arrays.asList(grassOptionsArray)));
        CustomSpinnerAdapter hardnessSpinnerAdapter = new CustomSpinnerAdapter(activity, new ArrayList<>(Arrays.asList(hardnessOptionsArray)));
        CustomSpinnerAdapter weatherSpinnerAdapter = new CustomSpinnerAdapter(activity, new ArrayList<>(Arrays.asList(weatherOptionsArray)));

        grassSpinner.setAdapter(grassSpinnerAdapter);
        hardnessSpinner.setAdapter(hardnessSpinnerAdapter);
        cloudsConditionsSpinner.setAdapter(weatherSpinnerAdapter);
    }

    private void sendConditionsToServerCall(ConditionsModel conditionsModel) {
        RetroInterface apiService = RetroApiClient.getClient(activity).create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.sendMatchConditions(conditionsModel);
        call.enqueue(new Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                GeneralServerResponse generalServerResponse = response.body();
                if (generalServerResponse.isSuccess()) {
                    GlobalUtil.printLog("ApiDebug", "Match conditions success" + generalServerResponse.getMessage());
                } else {
                    GlobalUtil.printLog("ApiDebug", "Match conditions failed" + generalServerResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                GlobalUtil.printLog("ApiDebug", "" + t.getMessage());
            }
        });
    }

}
