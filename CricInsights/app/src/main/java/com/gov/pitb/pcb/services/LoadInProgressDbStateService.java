package com.gov.pitb.pcb.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchCurrentState;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.db.dynamic.OutPlayer;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnInProgressDbDataLoaded;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 13/07/2017.
 */

public class LoadInProgressDbStateService extends IntentService {
    public LoadInProgressDbStateService() {
        super("LoadInProgressDbStateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MatchStateController controller = InsightsDbManager.getController();
        MatchCurrentState currentState = InsightsDbManager.getMatchState();

        Inning inning = populateInning(controller);
        populateOutPlayers(controller);
        populateDeliveries(controller, inning.getBattingTeam().getTeamId());


        MatchStateManager.getInstance().setMatchCurrentState(currentState);
        MatchStateManager.getInstance().resumeInning(inning, controller);

        OnInProgressDbDataLoaded onInProgressDbDataLoaded = new OnInProgressDbDataLoaded();
        EventBus.getDefault().post(onInProgressDbDataLoaded);
    }

    private Inning populateInning(MatchStateController controller) {
        Inning inning = InsightsDbManager.getInning(controller.getCurrentMatchId(), controller.getCurrentInningId());
        Team battingTeam = InsightsDbManager.getTeam(inning.getBattingTeamId());
        Team bowlingTeam = InsightsDbManager.getTeam(inning.getBowlingTeamId());

        String battingPlayerIds = inning.getBattingTeamSelPlayersIds();
        ArrayList<Player> battingPlayers = getPlayersListFromIds(battingPlayerIds.split(","));
        battingTeam.setPlayersOfTeam(battingPlayers);

        String bowlingPlayerIds = inning.getBowlingTeamSelPlayersIds();
        ArrayList<Player> bowlingPlayers = getPlayersListFromIds(bowlingPlayerIds.split(","));
        bowlingTeam.setPlayersOfTeam(bowlingPlayers);

        inning.setTeams(battingTeam, bowlingTeam);

        return inning;
    }

    private void populateOutPlayers(MatchStateController controller) {
        List<OutPlayer> outTablePlayers = InsightsDbManager.getOutPlayersOfMatch(controller.getCurrentMatchId(), controller.getCurrentInningId());
        List<Player> outModelPlayers = new ArrayList<>();
        for (OutPlayer outPlayer : outTablePlayers) {
            Player player = InsightsDbManager.getPlayer(outPlayer.getOutPlayerId());
            outModelPlayers.add(player);
        }
        MatchStateManager.getInstance().setOutPlayers(outModelPlayers);
    }

    private void populateDeliveries(MatchStateController controller, String batTeamId) {
        List<Delivery> deliveries = InsightsDbManager.getOverDeliveries(controller.getCurrentMatchId(), controller.getCurrentInningId(), "" + controller.getCurrentOverId());
        if (deliveries == null) {
            deliveries = new ArrayList<>();
        }

        int currentDeliveryId = controller.getCurrentDeliveryId();
        String matchId = controller.getCurrentMatchId();
        String inningId = controller.getCurrentInningId();
        int overNumber = controller.getCurrentOverId();
        // This Block of Code is to check for extra Ball
        if (currentDeliveryId > 6) {
            // It means user was on extra delivery when last time values saved.
            Delivery latestDelivery = deliveries.get(deliveries.size() - 1);
            Delivery extraDelivery = new Delivery(latestDelivery);
            extraDelivery.setBallNumberOfOver(latestDelivery.getBallNumberOfOver());
            deliveries.add(extraDelivery);
        }

        // To add remaining balls of over, we have to found what is number of latest ball saved in db.
        int latestDeliverNumber = 0;
        if (deliveries.size() > 0) {
            Delivery latestDelivery = deliveries.get(deliveries.size() - 1);
            latestDeliverNumber = latestDelivery.getBallNumberOfOver();
        }
        for (int count = latestDeliverNumber + 1; count <= 6; count++) { // Add deliveries to make full over.
            Delivery delivery = new Delivery(matchId, inningId, overNumber, count, batTeamId);
            deliveries.add(delivery);
        }


        MatchStateManager.getInstance().setCurrentOverDeliveries(deliveries);

    }

    private ArrayList<Player> getPlayersListFromIds(String[] idsArr) {
        ArrayList<Player> playerArrayList = new ArrayList<>();
        for (int count = 0; count < idsArr.length; count++) {
            Player player = InsightsDbManager.getPlayer(idsArr[count]);
            player.setSelected(true);
            playerArrayList.add(player);
        }
        return playerArrayList;
    }
}
