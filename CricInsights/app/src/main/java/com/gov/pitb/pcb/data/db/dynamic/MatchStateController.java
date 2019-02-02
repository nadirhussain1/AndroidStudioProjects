package com.gov.pitb.pcb.data.db.dynamic;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;

/**
 * Created by nadirhussain on 07/06/2017.
 */

@Table(name = "table_controller")
public class MatchStateController extends Model {
    @Column(name = "matchId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String currentMatchId;
    @Column(name = "inningId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String currentInningId;
    @Column(name = "overId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int currentOverId;
    @Column(name = "deliveryId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int currentDeliveryId;
    @Column(name = "strikerId")
    private String strikerId;
    @Column(name = "nonStrikerId")
    private String nonStrikerId;
    @Column(name = "bowlerId")
    private String bowlerId;
    @Column(name = "currentfielderPositionId")
    private long currentfielderPositionId;

    public MatchStateController() {
        super();
    }

    public MatchStateController(String matchId, String inningId) {
        super();
        this.currentMatchId = matchId;
        this.currentInningId = inningId;
        currentOverId = 0;
        currentDeliveryId = 1;
        currentfielderPositionId = 0;
    }


    public String getCurrentMatchId() {
        return currentMatchId;
    }

    public void setCurrentMatchId(String currentMatchId) {
        this.currentMatchId = currentMatchId;
    }

    public String getCurrentInningId() {
        return currentInningId;
    }

    public void setCurrentInningId(String currentInningId) {
        this.currentInningId = currentInningId;
    }

    public int getCurrentOverId() {
        return currentOverId;
    }

    public void setCurrentOverId(int currentOverId) {
        this.currentOverId = currentOverId;
    }

    public int getCurrentDeliveryId() {
        return currentDeliveryId;
    }

    public void setCurrentDeliveryId(int currentDeliveryId) {
        this.currentDeliveryId = currentDeliveryId;
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

    public void switchBatsMans() {
        String tempId = strikerId;
        strikerId = nonStrikerId;
        nonStrikerId = tempId;
    }

    public void renewOver() {
        currentOverId++;
        currentDeliveryId = 1;
    }

    public void renewInning() {
        currentOverId = 0;
        currentDeliveryId = 1;
        currentInningId = "2";
    }

    public void resetControllerOnNewMatch(String matchId, String inningId) {
        this.currentMatchId = matchId;
        this.currentInningId = inningId;
        currentOverId = 0;
        currentDeliveryId = 1;
        currentfielderPositionId = 0;
    }

    public void replaceBatman(String oldBatsManId, String newBatsManId) {
        if (strikerId.equals(oldBatsManId)) {
            strikerId = newBatsManId;
        } else {
            nonStrikerId = newBatsManId;
        }
    }

    public long getCurrentfielderPositionId() {
        return currentfielderPositionId;
    }

    public void setCurrentfielderPositionId(long currentfielderPositionId) {
        this.currentfielderPositionId = currentfielderPositionId;
    }


}
