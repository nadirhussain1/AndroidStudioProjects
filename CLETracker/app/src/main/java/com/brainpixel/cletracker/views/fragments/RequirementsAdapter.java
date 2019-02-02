package com.brainpixel.cletracker.views.fragments;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.model.Category;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.github.lzyzsd.circleprogress.CircleProgress;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 08/04/2017.
 */

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.RequirementHolder> {

    private Activity activity;
    private ArrayList<Category> requiredCategoriesList;


    public RequirementsAdapter(Activity activity, ArrayList<Category> listItemData) {
        this.activity = activity;
        this.requiredCategoriesList = listItemData;
    }

    @Override
    public RequirementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_requirements_progress, parent, false);
        new ScalingUtility(activity).scaleRootView(itemView);
        return new RequirementsAdapter.RequirementHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequirementHolder holder, int position) {
        Category category = requiredCategoriesList.get(position);
        String hoursText = "" + category.getHoursDone() + "/" + category.getRequiredHours();
        int progress = 0;
        if (category.getRequiredHours() > 0) {
            progress = (int) ((category.getHoursDone() * 100.0f) / category.getRequiredHours());
            if (progress > 100) {
                progress = 100;
            }
        }
        holder.circleProgress.setProgress(progress);
        holder.requirementLabelView.setText(category.getName());
        holder.progressCreditsTextView.setText(hoursText);
    }

    @Override
    public int getItemCount() {
        return requiredCategoriesList.size();
    }

    public class RequirementHolder extends RecyclerView.ViewHolder {
        CircleProgress circleProgress;
        TextView requirementLabelView, progressCreditsTextView;

        public RequirementHolder(View itemView) {
            super(itemView);

            circleProgress = (CircleProgress) itemView.findViewById(R.id.circle_progress);
            requirementLabelView = (TextView) itemView.findViewById(R.id.requirementLabelView);
            progressCreditsTextView = (TextView) itemView.findViewById(R.id.progressCreditsTextView);
            ;
        }

    }
}
