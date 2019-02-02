package com.gov.pitb.pcb.views.popups;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.navigators.AdapterItemClickListener;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnWideTypeSpecified;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.PopupRowsAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nadirhussain on 12/07/2017.
 */

public class WideBallTypePopup implements AdapterItemClickListener {
    private Activity activity;
    private PopupWindow popupWindow;
    private ListView listView;
    private ArrayList<String> optionsList;
    private String selectedType = "";


    public WideBallTypePopup(Activity activity,String prevWideType) {
        this.activity = activity;

        View windowView = View.inflate(activity, R.layout.wide_ball_options_layout, null);
        new ViewScaleHandler(activity).scaleRootView(windowView);
        popupWindow = new PopupWindow(windowView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOnDismissListener(dismissListener);

        listView = (ListView) windowView.findViewById(R.id.wideBallOptionsListView);
        configureListView(prevWideType);
    }

    private void configureListView(String wideType) {
        String[] shotTypeArray = activity.getResources().getStringArray(R.array.wide_ball_options);
        optionsList = new ArrayList<>(Arrays.asList(shotTypeArray));
        PopupRowsAdapter customSpinnerAdapter = new PopupRowsAdapter(activity, optionsList, this);
        customSpinnerAdapter.setSelectedItem(wideType);
        listView.setAdapter(customSpinnerAdapter);
    }

    public void show(View anchorView) {
        int anchorTop = (int) (-1 * ViewScaleHandler.resizeDimension(600));
        popupWindow.showAsDropDown(anchorView, 0, anchorTop);
    }

    private void dismiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onAdapterItemClick(int position) {
        selectedType = optionsList.get(position);
        dismiss();
    }

    private OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            OnWideTypeSpecified onShotTypeSpecified = new OnWideTypeSpecified();
            onShotTypeSpecified.type = selectedType;
            EventBus.getDefault().post(onShotTypeSpecified);
        }
    };
}
