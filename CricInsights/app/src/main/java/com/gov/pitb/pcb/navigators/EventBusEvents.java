package com.gov.pitb.pcb.navigators;

import com.gov.pitb.pcb.data.db.config.Tournament;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.data.server.players.PlayersData;
import com.gov.pitb.pcb.data.server.teams.TeamsData;
import com.gov.pitb.pcb.data.server.tournaments.TournamentsData;

import java.util.List;

/**
 * Created by nadirhussain on 15/06/2017.
 */

public class EventBusEvents {

    public static class OnServerDataLoadFailed {
    }

    public static class OnServerDataLoadSuccess {
        public TournamentsData tournamentsData;
        public PlayersData playersData;
        public TeamsData teamsData;
    }

    public static class DataSavedToDb {
    }

    public static class OnTournamentsDataLoaded {
        public List<Tournament> tournaments;
    }

    public static class OnInProgressDbDataLoaded {

    }

    public static class OnMatchConfigured {
    }


    public static class OnPlayersSelected {
        public String teamId;
    }

    public static class OnBowlerSelected {
        public Player bowlerSelected;
    }

    public static class OnShotTypeSpecified {
        public String shotType;
    }

    public static class OnDotBallTypeSpecified {
        public String type;
    }

    public static class OnWideTypeSpecified {
        public String type;
    }

    public static class OnNoBallTypeSpecified {
        public String type;
    }


    public static class OnPlayerOut {
        public Player outPlayer;
        public Player fielder;
        public Player newPlayer;
        public boolean didBatmanCross;
        public String outType;
    }

    public static class NextBallEvent {

    }

    public static class InningsChanged {
        public Player striker;
        public Player nonStriker;
    }

    public static class CommentsAdded {
        public String commentsText;
    }

    public static class OnPreviousDeliverySelected {
        public Delivery selectedDelivery;
    }

    public static class OnNewMatchStarted {}

    public static class OnScreensChecked {
       public MatchPermData matchPermData;
    }


}
