package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.model.BatScoreCardModel;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 03/08/2017.
 */

public class ScoreCardBatAdapter extends RecyclerView.Adapter<ScoreCardBatAdapter.ScoreCardHolder> {
    private Activity activity = null;
    private ArrayList<BatScoreCardModel> entries;

    public ScoreCardBatAdapter(Activity activity, ArrayList<BatScoreCardModel> batScoreCardsEntries) {
        this.activity = activity;
        this.entries = batScoreCardsEntries;
    }

    @Override
    public ScoreCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bat_scorecard, parent, false);
        new ViewScaleHandler(activity).scaleRootView(itemView);
        return new ScoreCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScoreCardHolder holder, int position) {
        BatScoreCardModel batScoreCardModel = entries.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#22000000"));
        }

        holder.playerNameView.setText(batScoreCardModel.getPlayerName());
        if (batScoreCardModel.isHasPlayed()) {
            holder.runsTextView.setText("" + batScoreCardModel.getRuns());
            holder.ballsTextView.setText("" + batScoreCardModel.getBalls());
            holder.foursTextView.setText("" + batScoreCardModel.getFours());
            holder.sixesTextView.setText("" + batScoreCardModel.getSixes());
            holder.strikeRateTextView.setText("" + batScoreCardModel.getStrikeRate());
        } else {
            holder.runsTextView.setText("-");
            holder.ballsTextView.setText("-");
            holder.foursTextView.setText("-");
            holder.sixesTextView.setText("-");
            holder.strikeRateTextView.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ScoreCardHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView playerNameView, runsTextView, ballsTextView, foursTextView, sixesTextView, strikeRateTextView;

        public ScoreCardHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            playerNameView = (TextView) itemView.findViewById(R.id.playerNameView);
            runsTextView = (TextView) itemView.findViewById(R.id.runsTextView);
            ballsTextView = (TextView) itemView.findViewById(R.id.ballsTextView);
            foursTextView = (TextView) itemView.findViewById(R.id.foursTextView);
            sixesTextView = (TextView) itemView.findViewById(R.id.sixesTextView);
            strikeRateTextView = (TextView) itemView.findViewById(R.id.strikeRateTextView);
        }
    }
}
