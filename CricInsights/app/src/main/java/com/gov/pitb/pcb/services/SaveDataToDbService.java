package com.gov.pitb.pcb.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.server.players.PlayersData;
import com.gov.pitb.pcb.data.server.teams.TeamsData;
import com.gov.pitb.pcb.data.server.tournaments.TournamentsData;
import com.gov.pitb.pcb.navigators.EventBusEvents.DataSavedToDb;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class SaveDataToDbService extends IntentService {
    public static final String KEY_TOURNAMENTS_DATA = "KEY_TOURNAMENTS_DATA";
    public static final String KEY_PLAYERS_DATA = "KEY_PLAYERS_DATA";
    public static final String KEY_TEAMS_DATA = "KEY_TEAMS_DATA";

    public SaveDataToDbService() {
        super("SaveDataToDbService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TournamentsData tournamentsData = (TournamentsData) intent.getSerializableExtra(KEY_TOURNAMENTS_DATA);
        TeamsData teamsData = (TeamsData) intent.getSerializableExtra(KEY_TEAMS_DATA);
        PlayersData playersData = (PlayersData) intent.getSerializableExtra(KEY_PLAYERS_DATA);

        InsightsDbManager.savePlayers(playersData.getPlayers());
        InsightsDbManager.saveTeams(teamsData.getTeams());
        InsightsDbManager.saveTournaments(tournamentsData.getTournaments());


        EventBus.getDefault().post(new DataSavedToDb());
    }
}
