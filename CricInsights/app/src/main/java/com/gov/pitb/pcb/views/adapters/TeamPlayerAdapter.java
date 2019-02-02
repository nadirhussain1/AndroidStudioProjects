package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 22/05/2017.
 */

public class TeamPlayerAdapter extends RecyclerView.Adapter<TeamPlayerAdapter.TeamHolder> {
    private Activity activity = null;
    private ArrayList<Player> playersList;
    private int checkedPlayersCount = 0;
    private TextView saveTextView;

    public TeamPlayerAdapter(Activity activity, ArrayList<Player> players, TextView saveTextView) {
        this.activity = activity;
        this.playersList = players;
        this.saveTextView = saveTextView;

        getCheckPlayersCount();
        setSaveButtonBackground();

    }

    private void getCheckPlayersCount() {
        for (Player player : playersList) {
            if (player.isSelected()) {
                checkedPlayersCount++;
            }
        }

    }

    private void setSaveButtonBackground() {
        if (checkedPlayersCount == 11) {
            saveTextView.setBackgroundResource(R.drawable.custom_primary_button_bg);
        } else {
            saveTextView.setBackgroundResource(R.drawable.custom_red_button_bg);
        }
    }

    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_row_player_selection, parent, false);
        new ViewScaleHandler(activity).scaleRootView(itemView);
        return new TeamHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamHolder holder, int position) {
        holder.playerNameView.setText(playersList.get(position).getPlayerName());
        if (!playersList.get(position).isSelected()) {
            excludedFromTeamView(holder.contentLayout, holder.whiteCrossIconView, holder.tickIconView, holder.playerNameView);
        } else {
            includedInTeamView(holder.contentLayout, holder.whiteCrossIconView, holder.tickIconView, holder.playerNameView);
        }
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

    private void includedInTeamView(RelativeLayout contentLayout, ImageView crossIconView, ImageView tickIconView, TextView playerNameView) {
        contentLayout.setBackgroundResource(R.drawable.custom_primary_button_bg);
        playerNameView.setTextColor(Color.WHITE);
//        crossIconView.setVisibility(View.INVISIBLE);
//        tickIconView.setVisibility(View.VISIBLE);
    }

    private void excludedFromTeamView(RelativeLayout contentLayout, ImageView crossIconView, ImageView tickIconView, TextView playerNameView) {
        contentLayout.setBackgroundColor(Color.WHITE);
        playerNameView.setTextColor(Color.BLACK);
//        crossIconView.setVisibility(View.VISIBLE);
//        tickIconView.setVisibility(View.INVISIBLE);
    }

    public class TeamHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public View itemView;
        public RelativeLayout contentLayout;
        public TextView playerNameView;
        public ImageView tickIconView, whiteCrossIconView;


        public TeamHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentLayout = (RelativeLayout) itemView.findViewById(R.id.contentLayout);
            playerNameView = (TextView) itemView.findViewById(R.id.playerNameTextView);
            tickIconView = (ImageView) itemView.findViewById(R.id.tickIcon);
            whiteCrossIconView = (ImageView) itemView.findViewById(R.id.whiteCrossIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Player checkRowPlayer = playersList.get(getAdapterPosition());
            if (checkRowPlayer.isSelected()) {
                checkedPlayersCount--;
                playersList.get(getAdapterPosition()).setSelected(false);
                excludedFromTeamView(contentLayout, whiteCrossIconView, tickIconView, playerNameView);
            } else if (checkedPlayersCount < 11) {
                checkedPlayersCount++;
                playersList.get(getAdapterPosition()).setSelected(true);
                includedInTeamView(contentLayout, whiteCrossIconView, tickIconView, playerNameView);
            }
            setSaveButtonBackground();
        }
    }
}
