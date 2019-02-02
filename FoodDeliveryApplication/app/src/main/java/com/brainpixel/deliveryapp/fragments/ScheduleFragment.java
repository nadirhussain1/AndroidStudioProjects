package com.brainpixel.deliveryapp.fragments;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.adapters.DatesAdapter;
import com.brainpixel.deliveryapp.handlers.ChangeTimeEvent;
import com.brainpixel.deliveryapp.handlers.GoToAddressTabEvent;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.handlers.ShowDatePickerEvent;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.DeliveryModel;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.GridItemDecoration;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 17/11/2018.
 */

public class ScheduleFragment extends Fragment {
    @BindView(R.id.deliveryModeGroup)
    RadioGroup modeRadioGroup;
    @BindView(R.id.selectDateTextView)
    TextView selectDateTextView;
    @BindView(R.id.selectedDatesRecyclerView)
    RecyclerView selectedDatesRecyclerView;
    @BindView(R.id.nextTextView)
    TextView nextTextView;

    private List<Calendar> selectedDatesCalendarList = new ArrayList<>();
    private int changeTimeEventPosition = 0;
    private DatesAdapter datesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modeRadioGroup.setOnCheckedChangeListener(deliveryModeChangeListener);
        prePopulatePreviousSchedule();
    }

    private void prePopulatePreviousSchedule() {
        Order currentOrder = GlobalDataManager.getInstance().currentOrder;
        if (currentOrder.getDeliverySchedule() != null) {
            for (DeliveryModel deliveryModel : currentOrder.getDeliverySchedule()) {
                String formattedDate = deliveryModel.getDate();
                Date parsedDate = GlobalUtil.parseToDate(formattedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                selectedDatesCalendarList.add(calendar);
            }
            if (selectedDatesCalendarList.size() > 1) {
                modeRadioGroup.check(R.id.multiTimeDeliveryButton);
            } else {
                modeRadioGroup.check(R.id.oneTimeDeliveryButton);
            }
            showSelectedDatesList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showSelectedDatesList() {
        if (selectedDatesCalendarList.size() > 0) {
            datesAdapter = new DatesAdapter(getActivity(), selectedDatesCalendarList);

            int space = (int) ScalingUtility.resizeDimension(10);
            selectedDatesRecyclerView.addItemDecoration(new GridItemDecoration(space));
            selectedDatesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
            selectedDatesRecyclerView.setAdapter(datesAdapter);

            nextTextView.setBackgroundResource(R.drawable.green_rounded_bg);
        } else {
            nextTextView.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
        }
    }


    @OnClick(R.id.selectDateTextView)
    public void openCalendarForDateSelection() {
        openDatePickerDialog();
    }

    @OnClick(R.id.nextTextView)
    public void onNextClick() {
        if (!canProceed()) {
            GlobalUtil.showToastMessage(getActivity(), "Select valid delivery date", GlobalConstants.TOAST_RED);
            return;
        }
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                EventBus.getDefault().post(new GoToAddressTabEvent());
            }
        };

        GlobalUtil.showCustomizedAlert(getActivity(), "Confirm Time", "Have you set correct time for all selected days?",
                getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.no_label), null);
    }

    private boolean canProceed() {
        return selectedDatesCalendarList.size() > 0;
    }

    private void populateScheduleOfOrder() {
        List<DeliveryModel> deliverySchedule = new ArrayList<>();
        for (Calendar calendar : selectedDatesCalendarList) {
            DeliveryModel model = new DeliveryModel();
            model.setDate(GlobalUtil.formatDate(calendar.getTime()));
            deliverySchedule.add(model);
        }
        GlobalDataManager.getInstance().currentOrder.setDeliverySchedule(deliverySchedule);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowDatePickerEvent event) {
        openDatePickerDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeTimeEvent event) {
        changeTimeEventPosition = event.position;
        showTimePickerDialog(selectedDatesCalendarList.get(changeTimeEventPosition));
    }

    private void openDatePickerDialog() {
        if (modeRadioGroup.getCheckedRadioButtonId() == R.id.oneTimeDeliveryButton) {
            openDatePickerDialog(CalendarView.ONE_DAY_PICKER);
        } else {
            openDatePickerDialog(CalendarView.MANY_DAYS_PICKER);
        }
    }

    private void openDatePickerDialog(int calendarType) {
        DatePickerBuilder builder = new DatePickerBuilder(getActivity(), selectDateListener).pickerType(calendarType);
        Calendar maxDateCalendar, minDateCalendar;

        if (calendarType == CalendarView.ONE_DAY_PICKER) {
            maxDateCalendar = Calendar.getInstance();
            maxDateCalendar.add(Calendar.WEEK_OF_YEAR, 1);

        } else {
            maxDateCalendar = Calendar.getInstance();
            maxDateCalendar.add(Calendar.MONTH, 3);

            builder.selectedDays(selectedDatesCalendarList);
        }

        minDateCalendar = Calendar.getInstance();
        minDateCalendar.add(Calendar.DAY_OF_YEAR, -1);

        builder.minimumDate(minDateCalendar)
                .maximumDate(maxDateCalendar)
                .headerColor(R.color.green_color_in_app)
                .selectionColor(R.color.green_color_in_app) // Color of the selection circle
                .dialogButtonsColor(R.color.green_color_in_app);

        DatePicker datePicker = builder.build();
        datePicker.show();
    }

    private void showTimePickerDialog(Calendar calendar) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeSetListener,
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    private OnSelectDateListener selectDateListener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendar) {
            selectedDatesCalendarList = calendar;
            populateScheduleOfOrder();
            showSelectedDatesList();
        }
    };
    private OnTimeSetListener timeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = selectedDatesCalendarList.get(changeTimeEventPosition);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            populateScheduleOfOrder();
            datesAdapter.notifyDataSetChanged();
        }
    };

    private OnCheckedChangeListener deliveryModeChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.oneTimeDeliveryButton) {
                selectDateTextView.setText("Select Date");
            } else {
                selectDateTextView.setText("Select Dates");
            }
            selectDateTextView.setVisibility(View.VISIBLE);
        }
    };
}
