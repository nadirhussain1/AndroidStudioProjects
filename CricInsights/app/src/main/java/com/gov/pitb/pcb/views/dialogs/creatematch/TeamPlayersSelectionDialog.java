package com.gov.pitb.pcb.views.dialogs.creatematch;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPlayersSelected;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.TeamPlayerAdapter;
import com.gov.pitb.pcb.views.custom.misc.GridSpacingItemDecoration;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 22/05/2017.
 */

public class TeamPlayersSelectionDialog {
    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;
    @BindView(R.id.playersRecyclerView)
    RecyclerView teamPlayersRecyclerView;
    @BindView(R.id.teamImageView)
    ImageView teamImageView;
    @BindView(R.id.teamNameTextView)
    TextView teamNameTextView;
    @BindView(R.id.saveButton)
    TextView saveButton;


    private Activity activity;
    private Dialog dialog = null;
    private Team team;


    public TeamPlayersSelectionDialog(Activity activity, Team team) {
        this.team = team;
        this.activity = activity;

        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.select_team_layout, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
        dialog.setContentView(dialogView);
    }


    public void showDialog() {
        teamNameTextView.setText(team.getTeamName());
        if (!TextUtils.isEmpty(team.getTeamLogoUrl())) {
            Picasso.with(activity).load(team.getTeamLogoUrl()).into(teamImageView);
        }
        dialog.show();
        populatePlayersData();
        setUpRecyclerView(team.getPlayersOfTeam());


        loadingLayout.setVisibility(View.GONE);
        teamPlayersRecyclerView.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private void populatePlayersData() {
        if (team.getPlayersOfTeam().size() == 0) {
            ArrayList<Player> teamPlayersList = new ArrayList<>();
            String playerIds = team.getPlayerIds();
            String[] playersIdsArr = playerIds.split(",");
            for (int count = 0; count < playersIdsArr.length; count++) {
                String playerId = playersIdsArr[count];
                Player player = InsightsDbManager.getPlayer(playerId);
                teamPlayersList.add(player);
            }
            team.setPlayersOfTeam(teamPlayersList);
        }
    }

    private void setUpRecyclerView(ArrayList<Player> playersList) {
        TeamPlayerAdapter teamPlayerAdapter = new TeamPlayerAdapter(activity, playersList, saveButton);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        teamPlayersRecyclerView.setLayoutManager(mLayoutManager);
        teamPlayersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamPlayersRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, false));
        teamPlayersRecyclerView.setAdapter(teamPlayerAdapter);
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @OnClick(R.id.saveButton)
    public void onSavedButtonClick() {
        OnPlayersSelected onPlayersSelected = new OnPlayersSelected();
        onPlayersSelected.teamId = team.getTeamId();
        EventBus.getDefault().post(onPlayersSelected);
        dismissDialog();
    }

    @OnClick(R.id.crossIconClickView)
    public void onCrossClicked() {
        dismissDialog();
    }


}
