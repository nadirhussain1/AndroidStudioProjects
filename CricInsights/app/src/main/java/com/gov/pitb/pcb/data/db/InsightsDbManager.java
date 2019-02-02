package com.gov.pitb.pcb.data.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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
import com.gov.pitb.pcb.utils.GlobalUtil;

import java.util.List;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class InsightsDbManager {
    private InsightsDbManager() {

    }

    public static void savePlayers(List<Player> players) {
        new Delete().from(Player.class).execute();
        for (Player player : players) {
            player.save();
        }
    }

    public static void saveTeams(List<Team> teams) {
        GlobalUtil.printLog("SaveDebug", "Teams Saved");
        new Delete().from(Team.class).execute();
        for (Team team : teams) {
            team.save();
        }
    }

    public static void saveTournaments(List<Tournament> tournaments) {
        new Delete().from(Tournament.class).execute();
        for (Tournament tournament : tournaments) {
            saveTournamentMatches(tournament.getMatches());
            tournament.save();
        }
    }

    public static void saveInning(Inning inning) {
        inning.save();
    }

    public static void saveStateController(MatchStateController controller) {
        long id = controller.save();
    }


    public static void saveState(MatchCurrentState matchCurrentState) {
        long id = matchCurrentState.save();
    }

    public static long saveFielderPosition(FielderPosition fielderPosition) {
        return fielderPosition.save();
    }


    private static void saveTournamentMatches(List<Match> matches) {
        if (matches != null) {
            for (Match match : matches) {
                match.save();
            }
        }
    }

    public static void saveOutPlayer(OutPlayer outPlayer) {
        outPlayer.save();
    }

    public static void saveDelivery(Delivery delivery) {
        delivery.save();
    }

    public static void saveWeatherConditions(ConditionsModel conditionsModel) {
        new Delete().from(ConditionsModel.class).execute();
        conditionsModel.save();
    }

    public static void saveMatchPermissions(MatchPermData matchPermData) {
        new Delete().from(MatchPermData.class).execute();
        matchPermData.save();
    }

    public static MatchPermData getMatchPermData() {
        return new Select().from(MatchPermData.class).executeSingle();
    }


    public static List<Tournament> getAllTournaments() {
        return new Select().from(Tournament.class).execute();
    }

    public static Team getTeam(String teamId) {
        Team team = new Select().
                from(Team.class)
                .where("teamId = ?", teamId)
                .executeSingle();
        return team;
    }

    public static List<Team> getTeams() {
        return new Select().
                from(Team.class)
                .execute();
    }

    public static Player getPlayer(String playerId) {
        return new Select().from(Player.class).where("playerId =?", playerId).executeSingle();
    }

    public static MatchStateController getController() {
        MatchStateController controller = new Select().
                from(MatchStateController.class)
                .executeSingle();
        return controller;
    }


    public static MatchCurrentState getMatchState() {
        MatchCurrentState matchCurrentState = new Select().
                from(MatchCurrentState.class)
                .executeSingle();
        return matchCurrentState;
    }

    public static Inning getInning(String matchId, String inningId) {
        Inning inning = new Select().
                from(Inning.class)
                .where("matchId = ? AND inningId = ? ", new String[]{matchId, inningId})
                .executeSingle();
        return inning;
    }

    public static Inning getFirstSavedInning() {
        Inning inning = new Select().
                from(Inning.class)
                .executeSingle();
        return inning;
    }


    public static List<OutPlayer> getOutPlayersOfMatch(String matchId, String inningId) {
        return new Select()
                .from(OutPlayer.class)
                .where("matchId = ? AND inningId = ? ", new String[]{matchId, inningId})
                .execute();
    }

    public static List<Delivery> getOverDeliveries(String matchId, String inningId, String overId) {
        return new Select()
                .from(Delivery.class)
                .where("matchId = ? AND inningId = ? AND overId =? ", new String[]{matchId, inningId, overId})
                .execute();
    }

    public static Delivery getDeliveryById(String matchId, String inningId, String overId, String deliveryId) {
        return new Select()
                .from(Delivery.class)
                .where("matchId = ? AND inningId = ? AND overId =? AND deliveryId=? ", new String[]{matchId, inningId, overId, deliveryId})
                .executeSingle();
    }

    public static FielderPosition getFielderPosition(long id) {
        return new Select()
                .from(FielderPosition.class)
                .where("Id = ? ", new String[]{"" + id})
                .executeSingle();
    }

    public static List<Delivery> getBatsManDeliveriesAgainstBowler(String strikerId, String bowlerId) {
        return new Select()
                .from(Delivery.class)
                .where("strikerId =? AND bowlerId =? AND wideType is null AND isBye =? AND isLegBye=? ", new String[]{strikerId, bowlerId, "0", "0"})
                .execute();
    }

    public static List<Delivery> getBatsmanAllValidScoreDeliveries(String strikerId) {
        return new Select()
                .from(Delivery.class)
                .where("strikerId =? AND wideType is null AND isBye =? AND isLegBye=?", new String[]{strikerId, "0", "0"})
                .execute();
    }

    public static List<Delivery> getBatsManValidDeliveriesInMatch(String matchId, String strikerId) {
        return new Select()
                .from(Delivery.class)
                .where("matchId =? AND strikerId =? AND wideType is null AND isBye =? AND isLegBye=?", new String[]{matchId, strikerId, "0", "0"})
                .execute();
    }

    public static List<Delivery> getBatsmanAllIncludingByeDeliveries(String strikerId) {
        return new Select()
                .from(Delivery.class)
                .where("strikerId =? AND wideType is null ", new String[]{strikerId})
                .execute();
    }

    public static List<Delivery> getBowlerDeliveries(String matchId, String bowlerId) {
        return new Select()
                .from(Delivery.class)
                .where("matchId =? AND bowlerId =? ", new String[]{matchId, bowlerId})
                .execute();
    }

    public static List<Delivery> getInningDeliveries(String matchId, String inningId) {
        return new Select()
                .from(Delivery.class)
                .where("matchId =? AND inningId =? ", new String[]{matchId, inningId})
                .execute();
    }


    public static List<Delivery> getAllUnSyncDeliveries() {
        return new Select()
                .from(Delivery.class)
                .where("isSyncWithServer =? ", new String[]{"0"})
                .orderBy("Id ASC")
                .execute();
    }

    public static ConditionsModel getWeatherConditions() {
        return new Select().from(ConditionsModel.class).executeSingle();
    }


}
