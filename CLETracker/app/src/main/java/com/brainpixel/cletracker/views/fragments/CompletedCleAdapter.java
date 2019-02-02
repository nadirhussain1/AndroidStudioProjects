package com.brainpixel.cletracker.views.fragments;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.interfaces.OnCompletedCleCellClicked;
import com.brainpixel.cletracker.model.CLEDataModel;
import com.brainpixel.cletracker.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 05/04/2017.
 */

public class CompletedCleAdapter extends RecyclerView.Adapter<CompletedCleAdapter.CleHolder> {
    private Activity activity;
    private ArrayList<CLEDataModel> completedClesList;


    public CompletedCleAdapter(Activity activity, ArrayList<CLEDataModel> listItemData) {
        this.activity = activity;
        this.completedClesList = listItemData;
    }

    @Override
    public CleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_completed_cle, parent, false);
        new ScalingUtility(activity).scaleRootView(itemView);
        return new CompletedCleAdapter.CleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CleHolder holder, int position) {
        CLEDataModel cleDataModel = completedClesList.get(position);
        holder.dateTextView.setText(cleDataModel.getDate());
        holder.creditsTextView.setText("" + cleDataModel.getHours());
        holder.titleTextView.setText(cleDataModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return completedClesList.size();
    }

    public class CleHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView dateTextView, titleTextView, creditsTextView;

        public CleHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateValueTextView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleValueTextView);
            creditsTextView = (TextView) itemView.findViewById(R.id.creditsValueTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position < completedClesList.size()) {
                OnCompletedCleCellClicked onCompletedCleCellClicked = new OnCompletedCleCellClicked();
                onCompletedCleCellClicked.certificateUrl = completedClesList.get(position).getCertificatePath();
                EventBus.getDefault().post(onCompletedCleCellClicked);
            }
        }
    }
}
