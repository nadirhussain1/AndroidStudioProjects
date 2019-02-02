package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.model.BowlScoreCardModel;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 03/08/2017.
 */

public class ScoreCardBowlAdapter extends RecyclerView.Adapter<ScoreCardBowlAdapter.BowlHolder> {
    private Activity activity = null;
    private ArrayList<BowlScoreCardModel> entries;

    public ScoreCardBowlAdapter(Activity activity, ArrayList<BowlScoreCardModel> bowlScoreCardModels) {
        this.activity = activity;
        this.entries = bowlScoreCardModels;
    }

    @Override
    public BowlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bowl_scorecard, parent, false);
        new ViewScaleHandler(activity).scaleRootView(itemView);
        return new BowlHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BowlHolder holder, int position) {
        BowlScoreCardModel model = entries.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#22000000"));
        }

        holder.playerNameView.setText(model.getPlayerName());
        if (model.isHasPlayed()) {
            holder.oversTextView.setText("" + model.getOvers());
            holder.mTextView.setText("" + model.getmOvers());
            holder.runsTextView.setText("" + model.getRuns());
            holder.wicketsTextView.setText("" + model.getWickets());
            holder.economyTextView.setText("" + model.getEconomy());
            holder.zerosTextView.setText("" + model.getZeros());
            holder.foursTextView.setText("" + model.getFours());
            holder.sixesTextView.setText("" + model.getSixes());
        } else {
            holder.oversTextView.setText("-");
            holder.mTextView.setText("-");
            holder.runsTextView.setText("-");
            holder.wicketsTextView.setText("-");
            holder.economyTextView.setText("-");
            holder.zerosTextView.setText("-");
            holder.foursTextView.setText("-");
            holder.sixesTextView.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class BowlHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView playerNameView, oversTextView, mTextView, runsTextView, wicketsTextView,
                economyTextView, zerosTextView, foursTextView, sixesTextView;

        public BowlHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            playerNameView = (TextView) itemView.findViewById(R.id.playerNameTextView);
            oversTextView = (TextView) itemView.findViewById(R.id.oversTextView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);
            runsTextView = (TextView) itemView.findViewById(R.id.runsTextView);
            wicketsTextView = (TextView) itemView.findViewById(R.id.wicketsTextView);

            economyTextView = (TextView) itemView.findViewById(R.id.economyTextView);
            zerosTextView = (TextView) itemView.findViewById(R.id.zerosTextView);
            foursTextView = (TextView) itemView.findViewById(R.id.foursTextView);
            sixesTextView = (TextView) itemView.findViewById(R.id.sixesTextView);

        }
    }
}
