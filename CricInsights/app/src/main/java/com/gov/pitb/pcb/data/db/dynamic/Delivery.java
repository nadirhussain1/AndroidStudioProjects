package com.gov.pitb.pcb.data.db.dynamic;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by nadirhussain on 20/06/2017.
 */

@Table(name = "table_delivery")
public class Delivery extends Model implements Serializable {
    @Column(name = "matchId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String matchId;
    @Column(name = "inningId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String inningId;
    @Column(name = "overId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int overId;
    @Column(name = "deliveryId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int deliveryId;
    @Column(name = "ballNumberOfOver")
    private int ballNumberOfOver;
    @Column(name = "strikerId")
    private String strikerId;
    @Column(name = "nonStrikerId")
    private String nonStrikerId;
    @Column(name = "bowlerId")
    private String bowlerId;
    @Column(name = "wideType")
    private String wideType;
    @Column(name = "noType")
    private String noType;
    @Column(name = "shotType")
    private String shotType;
    @Column(name = "isBye")
    private boolean isBye;
    @Column(name = "isLegBye")
    private boolean isLegBye;
    @Column(name = "runs")
    private int runs;
    @Column(name = "wagonWheelPoint")
    private String wagonWheelPoint;
    @Column(name = "ballDropPoint")
    private String ballDropPoint;
    @Column(name = "ballHeightPint")
    private String ballHeightPoint;
    @Column(name = "underOverTheWicket")
    private String underOverTheWicket;
    @Column(name = "comments")
    private String comments;
    @Column(name = "fieldersPosId")
    private long fielderPosId;
    @Column(name = "isOutOnThisDelivery")
    private boolean isOutOnThisDelivery;
    @Column(name = "isSyncWithServer")
    private boolean isSyncWithServer;
    @Column(name = "batTeamId")
    private String batTeamId;
    @Column(name = "isScorer")
    private boolean isScorer;


    private boolean isFielderPositionChanged;
    private boolean isRemaining;
    private String fielderPositions;


    public Delivery() {
        super();
    }

    public Delivery(String matchId, String inningId, int overId, int deliveryId, String batTeamId) {
        super();
        this.matchId = matchId;
        this.inningId = inningId;
        this.overId = overId;
        this.deliveryId = deliveryId;
        this.ballNumberOfOver = deliveryId;
        this.isRemaining = true;
        this.batTeamId = batTeamId;

    }

    public Delivery(Delivery delivery) {
        super();
        this.matchId = delivery.getMatchId();
        this.inningId = delivery.getInningId();
        this.overId = delivery.getOverId();
        this.deliveryId = delivery.getDeliveryId() + 6;
        this.ballNumberOfOver = delivery.getBallNumberOfOver();
        this.batTeamId = delivery.getBatTeamId();
    }

    public String getInningId() {
        return inningId;
    }

    public void setInningId(String inningId) {
        this.inningId = inningId;
    }

    public int getOverId() {
        return overId;
    }

    public void setOverId(int overId) {
        this.overId = overId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getWideType() {
        return wideType;
    }


    public String getNoType() {
        return noType;
    }


    public void setBye(boolean bye) {
        isBye = bye;
    }

    public void setLegBye(boolean legBye) {
        isLegBye = legBye;
    }


    public String getWagonWheelPoint() {
        return wagonWheelPoint;
    }

    public void setWagonWheelPoint(String wagonWheelPoint) {
        this.wagonWheelPoint = wagonWheelPoint;
    }


    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }


    public void setWideBall(String wideType) {
        this.wideType = wideType;
        this.noType = "";
        isLegBye = false;
    }

    public void unSetWideBall() {
        wideType = "";
    }

    public void setNoBall(String noType) {
        this.noType = noType;
        this.wideType = "";

    }

    public void unSetNoBall() {
        this.noType = "";
    }

    public void selectBye() {
        shotType = "";
        isBye = true;
        isLegBye = false;
    }

    public void unSelectBye() {
        isBye = false;
    }

    public void selectLegBye() {
        shotType = "";
        isLegBye = true;
        isBye = false;
    }

    public void unSelectLegBye() {
        isLegBye = false;
    }

    public boolean isBye() {
        return isBye;
    }

    public boolean isLegBye() {
        return isLegBye;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public boolean isValidBall() {
        if (!TextUtils.isEmpty(wideType) || !TextUtils.isEmpty(noType)) {
            return false;
        }
        return true;
    }

    public boolean isExtraScore() {
        if (isBye || isLegBye || runs == 5) {
            return true;
        }
        return false;
    }

    public boolean isOutOnThisDelivery() {
        return isOutOnThisDelivery;
    }

    public void setOutOnThisDelivery(boolean outOnThisDelivery) {
        isOutOnThisDelivery = outOnThisDelivery;
    }

    public String getStrikerId() {
        return strikerId;
    }

    public void setStrikerId(String strikerId) {
        this.strikerId = strikerId;
    }

    public String getNonStrikerId() {
        return nonStrikerId;
    }

    public void setNonStrikerId(String nonStrikerId) {
        this.nonStrikerId = nonStrikerId;
    }

    public String getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(String bowlerId) {
        this.bowlerId = bowlerId;
    }

    public int getBallNumberOfOver() {
        return ballNumberOfOver;
    }

    public void setBallNumberOfOver(int ballNumberOfOver) {
        this.ballNumberOfOver = ballNumberOfOver;
    }


    public String isShotType() {
        return shotType;
    }

    public void setShotType(String shotType) {
        this.shotType = shotType;
    }

    public String getBallDropPint() {
        return ballDropPoint;
    }

    public void setBallDropPint(String ballDropPint) {
        this.ballDropPoint = ballDropPint;
    }

    public String getBallHeightPint() {
        return ballHeightPoint;
    }

    public void setBallHeightPint(String ballHeightPint) {
        this.ballHeightPoint = ballHeightPint;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getFielderPosId() {
        return fielderPosId;
    }

    public void setFielderPosId(long fielderPosId) {
        this.fielderPosId = fielderPosId;
    }

    public boolean isScorer() {
        return isScorer;
    }

    public void setScorer(boolean scorer) {
        isScorer = scorer;
    }

    public boolean isFielderPositionChanged() {
        return isFielderPositionChanged;
    }

    public void setFielderPositionChanged(boolean fielderPositionChanged) {
        isFielderPositionChanged = fielderPositionChanged;
    }

    public void setWideType(String wideType) {
        this.wideType = wideType;
    }

    public void setNoType(String noType) {
        this.noType = noType;
    }

    public String getShotType() {
        return shotType;
    }

    public String getUnderOverTheWicket() {
        return underOverTheWicket;
    }

    public void setUnderOverTheWicket(String underOverTheWicket) {
        this.underOverTheWicket = underOverTheWicket;
    }

    public boolean isRemaining() {
        return isRemaining;
    }

    public void setRemaining(boolean remaining) {
        isRemaining = remaining;
    }

    public boolean isSyncWithServer() {
        return isSyncWithServer;
    }

    public void setSyncWithServer(boolean syncWithServer) {
        isSyncWithServer = syncWithServer;
    }

    public String getBatTeamId() {
        return batTeamId;
    }

    public void setBatTeamId(String batTeamId) {
        this.batTeamId = batTeamId;
    }

    public String getFielderPositions() {
        return fielderPositions;
    }

    public void setFielderPositions(String fielderPositions) {
        this.fielderPositions = fielderPositions;
    }

    public HashMap<String, Object> getDeliveryCustomMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("matchId", matchId);
        hashMap.put("inningId", inningId);
        hashMap.put("overId", overId);
        hashMap.put("deliveryId", deliveryId);
        hashMap.put("ballNumberOfOver", ballNumberOfOver);

        if (isScorer) {
            hashMap.put("strikerId", strikerId);
            hashMap.put("nonStrikerId", nonStrikerId);
            hashMap.put("bowlerId", bowlerId);
            if (!TextUtils.isEmpty(wideType)) {
                hashMap.put("wideType", wideType);
            }
            if (!TextUtils.isEmpty(noType)) {
                hashMap.put("noType", noType);
            }
            if (!TextUtils.isEmpty(shotType)) {
                hashMap.put("shotType", shotType);
            }
            hashMap.put("isBye", isBye);
            hashMap.put("isLegBye", isLegBye);
            hashMap.put("runs", runs);
            hashMap.put("isOutOnThisDelivery", isOutOnThisDelivery);

            if (!TextUtils.isEmpty(wagonWheelPoint)) {
                hashMap.put("wagonWheelPoint", wagonWheelPoint);
            }

        }
        if (!TextUtils.isEmpty(ballDropPoint) || !TextUtils.isEmpty(ballHeightPoint)) {
            hashMap.put("ballDropPoint", ballDropPoint);
            hashMap.put("ballHeightPoint", ballHeightPoint);
            hashMap.put("underOverTheWicket", underOverTheWicket);
            hashMap.put("comments", comments);
        }
        if (!TextUtils.isEmpty(fielderPositions)) {
            hashMap.put("fielderPositions", fielderPositions);
        }
        hashMap.put("batTeamId", batTeamId);

        return hashMap;
    }

}
