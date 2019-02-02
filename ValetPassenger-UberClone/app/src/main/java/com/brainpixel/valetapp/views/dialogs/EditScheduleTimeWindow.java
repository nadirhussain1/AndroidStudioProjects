package com.brainpixel.valetapp.views.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.EventBusClasses.UpcomingTripScheduleUpdated;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 06/06/2017.
 */

public class EditScheduleTimeWindow {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.scheduleTimeTextView)
    TextView scheduleTimeTextView;
    @BindView(R.id.scheduleDateTextView)
    TextView scheduleDateTextView;

    private Activity activity;
    private Dialog dialog = null;
    private View dialogView;
    private String rideId;
    private int scheduleDay, scheduledMonth, scheduledYear, scheduledStartHour, scheduledStartMinutes, scheduledEndHour, scheduledEndMinute = -1;

    public EditScheduleTimeWindow(Activity activity, String rideId, String scheduledDateTime) {
        this.activity = activity;
        this.rideId = rideId;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.schedule_time_layout, null);
        new ScalingUtility(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
        initScheduleVariables(scheduledDateTime);
    }

    private void initScheduleVariables(String scheduledDateTime) {
        String dateString = scheduledDateTime.split(" ")[0];
        String timeString = scheduledDateTime.split(" ")[1];

        scheduledYear = Integer.valueOf(dateString.split("-")[0]);
        scheduledMonth = Integer.valueOf(dateString.split("-")[1]);
        scheduleDay = Integer.valueOf(dateString.split("-")[2]);

        scheduledStartHour = Integer.valueOf(timeString.split(":")[0]);
        scheduledStartMinutes = Integer.valueOf(timeString.split(":")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(scheduledYear, scheduledMonth, scheduleDay, scheduledStartHour, scheduledStartMinutes);
        calendar.add(Calendar.MINUTE, 15);
        scheduledEndHour = calendar.get(Calendar.HOUR);
        scheduledEndMinute = calendar.get(Calendar.MINUTE);

        updateTimeViews();
        updateDateViews();
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

    @OnClick(R.id.scheduleTimeTextView)
    public void scheduleTimeTextViewClicked() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, scheduleTimeSetListener, scheduledStartHour, scheduledStartMinutes, true);
        timePickerDialog.show();

    }

    @OnClick(R.id.scheduleDateTextView)
    public void scheduleDateClicked() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, scheduleDateSetListener, scheduledYear, scheduledMonth, scheduleDay);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        datePickerDialog.show();
    }

    private OnDateSetListener scheduleDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            scheduledYear = year;
            scheduledMonth = month;
            scheduleDay = day;

            updateDateViews();
        }
    };
    private OnTimeSetListener scheduleTimeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(scheduledYear, scheduledMonth, scheduleDay, hour, minute);
            if (calendar.getTime().before(new Date())) {
                Toast.makeText(activity, "You can schedule ride only in future", Toast.LENGTH_SHORT).show();
                return;
            }

            scheduledStartHour = hour;
            scheduledStartMinutes = minute;
            calendar.add(Calendar.MINUTE, 15);
            scheduledEndHour = calendar.get(Calendar.HOUR);
            scheduledEndMinute = calendar.get(Calendar.MINUTE);
            updateTimeViews();
        }
    };

    private void updateTimeViews() {
        scheduleTimeTextView.setText(getScheduledTimeString());
    }

    private void updateDateViews() {
        scheduleDateTextView.setText(getDateLabelValues());
    }

    private String getScheduledTimeString() {
        String timeString = "" + scheduledStartHour + ":" + scheduledStartMinutes + " - " + scheduledEndHour + ":" + scheduledEndMinute;
        return timeString;
    }

    private String getDateLabelValues() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(scheduledYear, scheduledMonth, scheduleDay);

        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(calendar.getTime());

        String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());


        String dateTextView = dayName + ", " + monthName + " " + scheduleDay;
        return dateTextView;
    }

    private void onUpdateSuccess(String updatedTime) {
        UpcomingTripScheduleUpdated upcomingTripScheduleUpdated = new UpcomingTripScheduleUpdated();
        upcomingTripScheduleUpdated.updatedScheduleTime = updatedTime;
        EventBus.getDefault().post(upcomingTripScheduleUpdated);
        dismissDialog();
    }

    @OnClick(R.id.updateButton)
    public void updateScheduleTimeButton() {
        showProgressBar();
        final String scheduleDateTime = "" + scheduledYear + "-" + (scheduledMonth + 1) + "-" + scheduleDay + " " + scheduledStartHour + ":" + scheduledStartMinutes;
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.updateRideScheduledTime(rideId, scheduleDateTime);
        ApiHelper.enqueueWithRetry(activity, call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                hideProgressBar();
                if (response != null && response.body() != null) {
                    GeneralServerResponse generalServerResponse = response.body();
                    if (generalServerResponse.getSuccess()) {
                        onUpdateSuccess(scheduleDateTime);
                        return;
                    }
                }
                GlobalUtil.showMessageAlertWithOkButton(activity,"Error","Update time failed");
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                hideProgressBar();
                GlobalUtil.showMessageAlertWithOkButton(activity,"Error","Update time failed");
            }
        });

    }


}
