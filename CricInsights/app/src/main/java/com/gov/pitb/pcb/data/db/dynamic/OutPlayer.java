package com.gov.pitb.pcb.data.db.dynamic;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;

/**
 * Created by nadirhussain on 06/07/2017.
 */
@Table(name = "table_out")
public class OutPlayer extends Model {

    @Column(name = "matchId",uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String matchId;
    @Column(name = "inningId",uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String inningId;
    @Column(name = "overId",uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int overId;
    @Column(name = "deliveryId",uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private int deliveryId;
    @Column(name = "outPlayerId")
    private String outPlayerId;
    @Column(name = "fielderId")
    private String fielderId;
    @Column(name = "nextPlayerId")
    private String nextPlayerId;
    @Column(name = "outType")
    private String outType;
    @Column(name = "isCrossed")
    private boolean isCrossed;

    public OutPlayer() {
        super();
    }
    public OutPlayer(String matchId,String inningId,int overId,int deliveryId){
        super();
        this.matchId=matchId;
        this.inningId=inningId;
        this.overId=overId;
        this.deliveryId=deliveryId;
    }
    public void onOutInfo(String outPlayerId,String nextPlayerId,String fielderId,boolean isCrossed,String outType){
        this.outPlayerId=outPlayerId;
        this.nextPlayerId=nextPlayerId;
        this.fielderId=fielderId;
        this.isCrossed=isCrossed;
        this.outType=outType;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setCrossed(boolean crossed) {
        isCrossed = crossed;
    }

    public String getOutPlayerId() {
        return outPlayerId;
    }

    public void setOutPlayerId(String outPlayerId) {
        this.outPlayerId = outPlayerId;
    }

    public String getFielderId() {
        return fielderId;
    }

    public void setFielderId(String fielderId) {
        this.fielderId = fielderId;
    }

    public String getNextPlayerId() {
        return nextPlayerId;
    }

    public void setNextPlayerId(String nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }


}
