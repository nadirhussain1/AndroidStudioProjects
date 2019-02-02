package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 15/06/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> itemsList;

    public CustomSpinnerAdapter(Activity activity, ArrayList<String> itemsList) {
        this.activity = activity;
        this.itemsList = itemsList;
    }


    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item, null);
            new ViewScaleHandler(activity).scaleRootView(convertView);
        }
        TextView spinnerItemTextView = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
        spinnerItemTextView.setText(itemsList.get(position));

        return convertView;
    }


}
