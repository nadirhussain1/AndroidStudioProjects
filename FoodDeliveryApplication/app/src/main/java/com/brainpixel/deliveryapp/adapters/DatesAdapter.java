package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.ChangeTimeEvent;
import com.brainpixel.deliveryapp.handlers.ShowDatePickerEvent;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

/**
 * Created by nadirhussain on 18/11/2018.
 */


public class DatesAdapter extends Adapter<DatesAdapter.DateViewHolder> {
    private Activity activity;
    private List<Calendar> calendars;
    private String[] months;
    private String[] days;

    public DatesAdapter(Activity activity, List<Calendar> calendars) {
        this.activity = activity;
        this.calendars = calendars;
        this.months = activity.getResources().getStringArray(R.array.months_names);
        this.days = activity.getResources().getStringArray(R.array.days_names);
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_selected_date, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        Calendar calendar = calendars.get(position);
        String date = months[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.DAY_OF_MONTH);
        String hour = "" + calendar.get(Calendar.HOUR);
        String minute = "" + calendar.get(Calendar.MINUTE);
        String amPm = "AM";
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            amPm = "PM";
        }

        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1] + ", " + getFormattedTime(hour) + ":" + getFormattedTime(minute) + " " + amPm;

        holder.dateTextView.setText(date);
        holder.dayTimeTextView.setText(day);
    }

    @Override
    public int getItemCount() {
        return calendars.size();
    }

    private String getFormattedTime(String time) {
        if (time.length() == 2) {
            return time;
        }
        return "0" + time;
    }

    public class DateViewHolder extends ViewHolder implements OnClickListener {
        public TextView dateTextView, dayTimeTextView, setTimeTextView;

        public DateViewHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            dayTimeTextView = itemView.findViewById(R.id.dayTimeTextView);
            setTimeTextView = itemView.findViewById(R.id.setTimeTextView);

            itemView.setOnClickListener(this);
            setTimeTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.setTimeTextView) {
                ChangeTimeEvent changeTimeEvent = new ChangeTimeEvent(getAdapterPosition());
                EventBus.getDefault().post(changeTimeEvent);
            } else {
                ShowDatePickerEvent showDatePickerEvent = new ShowDatePickerEvent(getAdapterPosition());
                EventBus.getDefault().post(showDatePickerEvent);
            }
        }
    }
}
