package com.brainpixel.cletracker.views.activities;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.model.Category;
import com.brainpixel.cletracker.utils.ScalingUtility;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 17/04/2017.
 */

public class CategoriesSpinnerAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Category> requiredCategoriesList;


    public CategoriesSpinnerAdapter(Activity activity, ArrayList<Category> listItemData) {
        this.activity = activity;
        this.requiredCategoriesList = listItemData;
    }


    @Override
    public int getCount() {
        return requiredCategoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(activity, R.layout.row_spinner_category, null);
            new ScalingUtility(activity).scaleRootView(view);
        }
        TextView categoryNameView = (TextView) view.findViewById(R.id.categoryNameView);
        categoryNameView.setText(requiredCategoriesList.get(position).getName());
        return view;
    }


}
