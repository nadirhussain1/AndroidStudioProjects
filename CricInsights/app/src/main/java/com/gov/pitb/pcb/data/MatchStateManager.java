package com.gov.pitb.pcb.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.FielderPosition;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchCurrentState;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.db.dynamic.OutPlayer;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Fielder;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPlayerOut;
import com.gov.pitb.pcb.services.SendDataToServerService;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nadirhussain on 06/07/2017.
 */

public class MatchStateManager {
    private static MatchStateManager matchStateManager;


    private MatchStateController matchStateController;
    private MatchCurrentState matchCurrentState;
    private List<Player> outPlayers;
    private List<Delivery> currentOverDeliveries = new ArrayList<>();
    private Inning currentInning;
    public List<Fielder> fieldersList = new ArrayList<>();


    public static MatchStateManager getInstance() {
        synchronized (MatchStateManager.class) {
            if (matchStateManager == null) {
                matchStateManager = new MatchStateManager();
            }
        }
        return matchStateManager;
    }

    private MatchStateManager() {
        matchCurrentState = new MatchCurrentState();
        outPlayers = new ArrayList<>();
    }


    public void initInning(Inning inning, MatchStateController matchStateController) {
        this.currentInning = inning;
        this.matchStateController = matchStateController;
        initNewOverDeliveries();
    }

    public void resumeInning(Inning inning, MatchStateController matchStateController) {
        this.currentInning = inning;
        this.matchStateController = matchStateController;
    }

    public void updateBowler(String bowlerId) {
        matchStateController.setBowlerId(bowlerId);
        matchCurrentState.updateBowlerStats(0, 0, 0);

        InsightsDbManager.saveState(matchCurrentState);
        InsightsDbManager.saveStateController(matchStateController);
    }

    public void initFielders() {
        fieldersList.clear();

        int dimension = (int) ViewScaleHandler.resizeDimension(1000);
        int scaleOffset = (int) ViewScaleHandler.resizeDimension(100);
        int max = dimension - scaleOffset;

        ArrayList<Player> players = currentInning.getBowlingTeam().getSelectedPlayersOfTeam();
        for (Player player : players) {
            if (!player.getPlayerId().equalsIgnoreCase(matchStateController.getBowlerId())) {
                int x = getRandom(max, scaleOffset);
                int y = getRandom(max, scaleOffset);
                Fielder fielder = new Fielder(player, x, y);
                fieldersList.add(fielder);
            }
        }
    }

    public void updateFielders(String newBowlerId) {
        for (Fielder fielder : fieldersList) {
            if (fielder.getFielderId().equalsIgnoreCase(newBowlerId)) {
                fieldersList.remove(fielder);
                break;
            }
        }

        int dimension = (int) ViewScaleHandler.resizeDimension(1000);
        int scaleOffset = (int) ViewScaleHandler.resizeDimension(100);
        int max = dimension - scaleOffset;

        int x = getRandom(max, scaleOffset);
        int y = getRandom(max, scaleOffset);
        Fielder fielder = new Fielder(getBowler(), x, y);
        fieldersList.add(fielder);
    }

    private int getRandom(int max, int min) {
        Random rand = new Random();
        return (rand.nextInt(max - min)) + min;
    }

    public List<Delivery> initNewOverDeliveries() {
        currentOverDeliveries = new ArrayList<>();
        for (int count = 1; count <= 6; count++) { // Add deliveries to make full over.
            String matchId = matchStateController.getCurrentMatchId();
            String inningId = matchStateController.getCurrentInningId();
            int overNumber = matchStateController.getCurrentOverId();
            String batTeamId = currentInning.getBattingTeamId();
            Delivery delivery = new Delivery(matchId, inningId, overNumber, count, batTeamId);
            currentOverDeliveries.add(delivery);
        }

        return currentOverDeliveries;
    }

    public List<Delivery> getDeliveries() {
        return currentOverDeliveries;
    }

    public Delivery getCurrentDelivery() {
        for (Delivery delivery : currentOverDeliveries) {
            if (delivery.getDeliveryId() == matchStateController.getCurrentDeliveryId()) {
                return delivery;
            }
        }
        return currentOverDeliveries.get(0);
    }

    public int getCurrentDeliveryId() {
        return matchStateController.getCurrentDeliveryId();
    }

    public String getCurrentInningId() {
        return currentInning.getInningId();
    }

    public int getTotalExtras() {
        return matchCurrentState.getTotalExtras();
    }

    public int getTotalWicket() {
        return matchCurrentState.getTotalWickets();
    }

    public int getTotalScore() {
        return matchCurrentState.getTotalScore();
    }

    public Team getBattingTeam() {
        return currentInning.getBattingTeam();
    }

    public Team getBowlingTeam() {
        return currentInning.getBowlingTeam();
    }

    public int getOverId() {
        return matchStateController.getCurrentOverId();
    }

    public Player getBowler() {
        String bowlerId = matchStateController.getBowlerId();
        ArrayList<Player> bowlersTeamPlayers = currentInning.getBowlingTeam().getSelectedPlayersOfTeam();
        for (Player player : bowlersTeamPlayers) {
            if (player.getPlayerId().equalsIgnoreCase(bowlerId)) {
                return player;
            }
        }
        return null;
    }

    public Player getStriker() {
        String strikerId = matchStateController.getStrikerId();
        ArrayList<Player> battingTeamPlayers = currentInning.getBattingTeam().getSelectedPlayersOfTeam();
        for (Player player : battingTeamPlayers) {
            if (player.getPlayerId().equalsIgnoreCase(strikerId)) {
                return player;
            }
        }
        return null;
    }

    public Player getNonStriker() {
        String nonStrikerId = matchStateController.getNonStrikerId();
        ArrayList<Player> battingTeamPlayers = currentInning.getBattingTeam().getSelectedPlayersOfTeam();
        for (Player player : battingTeamPlayers) {
            if (player.getPlayerId().equalsIgnoreCase(nonStrikerId)) {
                return player;
            }
        }
        return null;
    }


    public String getOverAllRunRate() {
        float totalBallsDone = (6 * matchStateController.getCurrentOverId()) + getCurrentDelivery().getBallNumberOfOver();
        int totalScore = matchCurrentState.getTotalScore();
        float runRate = (totalScore / totalBallsDone) * 6;
        return GlobalUtil.roundOfDecimalNumber(runRate);
    }


    public ArrayList<Player> getBowlersForNewOver() {
        ArrayList<Player> optionsList = new ArrayList<>();
        Team bowlingTeam = currentInning.getBowlingTeam();
        ArrayList<Player> bowlingPlayers = bowlingTeam.getSelectedPlayersOfTeam();
        for (Player player : bowlingPlayers) {
            if (player.getPlayerId() != matchStateController.getBowlerId()) {
                optionsList.add(player);
            }
        }
        return optionsList;
    }

    public ArrayList<Player> getNewBatsManOptions() {
        ArrayList<Player> optionsList = new ArrayList<>();
        Team battingTeam = currentInning.getBattingTeam();
        ArrayList<Player> battingTeamPlayers = battingTeam.getSelectedPlayersOfTeam();
        String nonStrikerId = matchStateController.getNonStrikerId();
        String strikerId = matchStateController.getStrikerId();

        for (Player player : battingTeamPlayers) {
            if (!outPlayers.contains(player) && !player.getPlayerId().equals(strikerId) && !player.getPlayerId().equals(nonStrikerId)) {
                optionsList.add(player);
            }
        }
        return optionsList;
    }

    public MatchCurrentState getMatchCurrentState() {
        return matchCurrentState;
    }

    public MatchStateController getMatchStateController() {
        return matchStateController;
    }

    public void setMatchCurrentState(MatchCurrentState matchCurrentState) {
        this.matchCurrentState = matchCurrentState;
    }

    public void setOutPlayers(List<Player> outPlayers) {
        this.outPlayers = outPlayers;
    }

    public void setCurrentOverDeliveries(List<Delivery> currentOverDeliveries) {
        this.currentOverDeliveries = currentOverDeliveries;
    }

    public boolean isBowlerSelected() {
        return !TextUtils.isEmpty(matchStateController.getBowlerId());
    }

    public void doStatsCalculationsOnNextBall(Context context, OnPlayerOut onPlayerOutInfo, MatchPermData matchPermData) {
        Delivery currentDelivery = getCurrentDelivery();
        currentDelivery.setScorer(matchPermData.isScore());
        
        currentDelivery.setStrikerId(matchStateController.getStrikerId());
        currentDelivery.setNonStrikerId(matchStateController.getNonStrikerId());
        currentDelivery.setBowlerId(matchStateController.getBowlerId());

        matchCurrentState.updateCalculations(currentDelivery);
        if (!currentDelivery.isValidBall()) {
            addExtraDelivery(currentDelivery);
        }
        boolean switchStriker = false;
        if (currentDelivery.isOutOnThisDelivery() && onPlayerOutInfo != null) {
            OutPlayer outPlayer = new OutPlayer(matchStateController.getCurrentMatchId(), matchStateController.getCurrentInningId(), matchStateController.getCurrentOverId(), matchStateController.getCurrentDeliveryId());

            String fielderId = "";
            String newPlayerId = "";
            if (onPlayerOutInfo.fielder != null) {
                fielderId = onPlayerOutInfo.fielder.getPlayerId();
            }
            if (onPlayerOutInfo.newPlayer != null) {
                newPlayerId = onPlayerOutInfo.newPlayer.getPlayerId();
            }

            outPlayer.onOutInfo(onPlayerOutInfo.outPlayer.getPlayerId(), newPlayerId, fielderId, onPlayerOutInfo.didBatmanCross, onPlayerOutInfo.outType);
            InsightsDbManager.saveOutPlayer(outPlayer);
            outPlayers.add(InsightsDbManager.getPlayer(onPlayerOutInfo.outPlayer.getPlayerId()));

            if (matchStateController.getStrikerId().equalsIgnoreCase(outPlayer.getOutPlayerId())) {
                matchCurrentState.resetStrikerStats();
            } else {
                matchCurrentState.resetNonStrikerStats();
            }
            matchStateController.replaceBatman(outPlayer.getOutPlayerId(), outPlayer.getNextPlayerId());
            switchStriker = outPlayer.isCrossed();
        }
        if (!switchStriker) {
            boolean isOddScore = ((currentDelivery.getRuns() % 2) == 1);
            if (isOverEnd()) {
                switchStriker = !isOddScore;
            } else {
                switchStriker = isOddScore;
            }

        }
        if (switchStriker) {
            matchStateController.switchBatsMans();
            matchCurrentState.switchStriker();
        }
        if (matchPermData.isField()) {
            handleFieldersPositions(currentDelivery);
        }
        currentDelivery.setRemaining(false);
        InsightsDbManager.saveState(matchCurrentState);
        saveDelivery(context, currentDelivery);
    }

    public void doAnalystModeCalculations(Context context, MatchPermData matchPermData) {
        Delivery currentDelivery = getCurrentDelivery();
        currentDelivery.setScorer(matchPermData.isScore());
        if (!currentDelivery.isValidBall()) {
            addExtraDelivery(currentDelivery);
        }
        if (matchPermData.isField()) {
            handleFieldersPositions(currentDelivery);
        }

        currentDelivery.setRemaining(false);
        saveDelivery(context, currentDelivery);
    }

    private void handleFieldersPositions(Delivery currentDelivery) {
        if (currentDelivery.isFielderPositionChanged() || currentDelivery.getBallNumberOfOver() == 1) {
            String fielderJson = FielderPosition.encodeFielderPosToString(fieldersList);
            FielderPosition fielderPosition = new FielderPosition();
            fielderPosition.setPositions(fielderJson);
            long id = InsightsDbManager.saveFielderPosition(fielderPosition);

            matchStateController.setCurrentfielderPositionId(id);
            currentDelivery.setFielderPosId(id);
        } else {
            currentDelivery.setFielderPosId(matchStateController.getCurrentfielderPositionId());
        }
    }

    private void saveDelivery(Context context, Delivery delivery) {
        InsightsDbManager.saveDelivery(delivery);
        Intent intent = new Intent(context, SendDataToServerService.class);
        context.startService(intent);
    }


    private void addExtraDelivery(Delivery currentDelivery) {
        Delivery extraDelivery = new Delivery(currentDelivery);
        int index = currentOverDeliveries.indexOf(currentDelivery);
        currentOverDeliveries.add(index + 1, extraDelivery);
    }

    public boolean isOverEnd() {
        Delivery lastDelivery = currentOverDeliveries.get(currentOverDeliveries.size() - 1);
        return getCurrentDeliveryId() == lastDelivery.getDeliveryId();
    }

    public boolean isMatchEnd() {
        String inningId = currentInning.getInningId();
        if (inningId.contentEquals("1")) {
            return false;
        }
        if (isInningEnd() || matchCurrentState.getTotalScore() > matchCurrentState.getPrevInningScore()) {
            return true;
        }

        return false;
    }

    public boolean isInningEnd() {
        int overNumber = matchStateController.getCurrentOverId() + 1;
        if (outPlayers.size() >= 10 || (isOverEnd() && overNumber >= currentInning.getMatchOvers())) {
            return true;
        }
        return false;
    }

    public void jumpToNewBall() {
        Delivery currentDelivery = getCurrentDelivery();
        int index = currentOverDeliveries.indexOf(currentDelivery);
        matchStateController.setCurrentDeliveryId(currentOverDeliveries.get(index + 1).getDeliveryId());
        InsightsDbManager.saveStateController(matchStateController);
    }

    public void jumpToNewOver() {
        matchStateController.renewOver();
        initNewOverDeliveries();
        InsightsDbManager.saveStateController(matchStateController);
    }

    public void jumpToSecondInning() {
        outPlayers.clear();
        matchStateController.renewInning();
        matchCurrentState.renewInning();

        InsightsDbManager.saveStateController(matchStateController);
        InsightsDbManager.saveState(matchCurrentState);
    }

    public Inning getCurrentInning() {
        return currentInning;
    }


}
