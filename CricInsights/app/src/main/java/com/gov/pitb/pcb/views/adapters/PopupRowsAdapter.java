package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.navigators.AdapterItemClickListener;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 14/07/2017.
 */


public class PopupRowsAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> itemsList;
    private AdapterItemClickListener adapterItemClickListener;
    private String selectedItem = "";

    public PopupRowsAdapter(Activity activity, ArrayList<String> itemsList, AdapterItemClickListener adapterItemClickListener) {
        this.activity = activity;
        this.itemsList = itemsList;
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void setSelectedItem(String item) {
        selectedItem = item;
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
            convertView = inflater.inflate(R.layout.row_popup_widow, null);
            new ViewScaleHandler(activity).scaleRootView(convertView);
        }
        TextView spinnerItemTextView = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
        String item = itemsList.get(position);
        if (!TextUtils.isEmpty(selectedItem) && item.contentEquals(selectedItem)) {
            spinnerItemTextView.setBackgroundColor(Color.parseColor("#00A5E2"));
            spinnerItemTextView.setTextColor(Color.WHITE);
        } else {
            spinnerItemTextView.setBackgroundColor(Color.WHITE);
            spinnerItemTextView.setTextColor(Color.parseColor("#66000000"));
        }

        spinnerItemTextView.setText(item);
        convertView.setTag(position);
        convertView.setOnClickListener(itemClickListener);

        return convertView;
    }

    private OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            adapterItemClickListener.onAdapterItemClick(position);

        }
    };
}

