package com.gov.pitb.pcb.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Tournament;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnTournamentsDataLoaded;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class LoadDbDataService extends IntentService {

    public LoadDbDataService() {
        super("LoadDbDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<Tournament> tournaments = InsightsDbManager.getAllTournaments();
        OnTournamentsDataLoaded onTournamentsDataLoaded = new OnTournamentsDataLoaded();
        onTournamentsDataLoaded.tournaments = tournaments;
        EventBus.getDefault().post(onTournamentsDataLoaded);
    }
}
