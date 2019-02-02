package com.gov.pitb.pcb;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.gov.pitb.pcb.data.db.ListTypeSerializer;
import com.gov.pitb.pcb.data.db.config.Match;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.config.Tournament;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.FielderPosition;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchCurrentState;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.db.dynamic.OutPlayer;
import com.gov.pitb.pcb.data.model.ConditionsModel;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Player;

/**
 * Created by nadirhussain on 06/07/2017.
 */

public class InsightsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeDB();
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(Tournament.class);
        configurationBuilder.addModelClasses(Team.class);
        configurationBuilder.addModelClasses(Match.class);
        configurationBuilder.addModelClasses(Player.class);

        configurationBuilder.addModelClasses(Delivery.class);
        configurationBuilder.addModelClasses(Inning.class);
        configurationBuilder.addModelClasses(OutPlayer.class);

        configurationBuilder.addModelClasses(MatchCurrentState.class);
        configurationBuilder.addModelClasses(MatchStateController.class);
        configurationBuilder.addModelClasses(FielderPosition.class);
        configurationBuilder.addModelClasses(ConditionsModel.class);
        configurationBuilder.addModelClasses(MatchPermData.class);

        configurationBuilder.addTypeSerializer(ListTypeSerializer.class);

        configurationBuilder.setCacheSize(1);
        ActiveAndroid.initialize(configurationBuilder.create());
    }
}
