package com.gov.pitb.pcb.data.db.dynamic;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nadirhussain on 06/07/2017.
 */

@Table(name = "table_state")
public class MatchCurrentState extends Model {
    @Column(name = "totalScore")
    private int totalScore;
    @Column(name = "totalWickets")
    private int totalWickets;
    @Column(name = "totalExtras")
    private int totalExtras;
    @Column(name = "prevInningScore")
    private int prevInningScore;

    @Column(name = "strikerRuns")
    private int strikerRuns;
    @Column(name = "nonStrikerRuns")
    private int nonStrikerRuns;
    @Column(name = "strikerTotalBalls")
    private int strikerTotalBalls;
    @Column(name = "nonStrikerTotalBalls")
    private int nonStrikerTotalBalls;
    @Column(name = "bowlerOversDone")
    private int bowlerOversDone;
    @Column(name = "bowlerBallsDone")
    private int bowlerBallsDone = 0;
    @Column(name = "bowlerRunsGiven")
    private int bowlerRunsGiven;

    public MatchCurrentState() {
        super();
        bowlerBallsDone = 0;
        totalScore = 0;
        totalWickets = 0;
        totalExtras = 0;

    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public void setTotalWickets(int totalWickets) {
        this.totalWickets = totalWickets;
    }

    public int getTotalExtras() {
        return totalExtras;
    }

    public void setTotalExtras(int totalExtras) {
        this.totalExtras = totalExtras;
    }

    public int getStrikerRuns() {
        return strikerRuns;
    }

    public void setStrikerRuns(int strikerRuns) {
        this.strikerRuns = strikerRuns;
    }

    public int getNonStrikerRuns() {
        return nonStrikerRuns;
    }

    public void setNonStrikerRuns(int nonStrikerRuns) {
        this.nonStrikerRuns = nonStrikerRuns;
    }

    public int getStrikerTotalBalls() {
        return strikerTotalBalls;
    }

    public void setStrikerTotalBalls(int strikerTotalBalls) {
        this.strikerTotalBalls = strikerTotalBalls;
    }

    public int getNonStrikerTotalBalls() {
        return nonStrikerTotalBalls;
    }

    public void setNonStrikerTotalBalls(int nonStrikerTotalBalls) {
        this.nonStrikerTotalBalls = nonStrikerTotalBalls;
    }

    public int getBowlerOversDone() {
        return bowlerOversDone;
    }

    public void setBowlerOversDone(int bowlerOversDone) {
        this.bowlerOversDone = bowlerOversDone;
    }

    public int getBowlerBallsDone() {
        return bowlerBallsDone;
    }

    public void setBowlerBallsDone(int bowlerBallsDone) {
        this.bowlerBallsDone = bowlerBallsDone;
    }

    public int getBowlerRunsGiven() {
        return bowlerRunsGiven;
    }

    public void setBowlerRunsGiven(int bowlerRunsGiven) {
        this.bowlerRunsGiven = bowlerRunsGiven;
    }

    public void addWicket() {
        totalWickets++;
    }

    public void addExtrasRuns(int runs) {
        totalExtras += runs;
    }

    private void addTotalScore(int score) {
        totalScore += score;
        bowlerRunsGiven += score;
    }

    private void addBatsManScore(int score) {
        strikerRuns += score;
    }

    private void updateBallCount() {
        bowlerBallsDone++;
        strikerTotalBalls++;
    }


    public void updateCalculations(Delivery delivery) {
        if (isValidBatManDelivery(delivery)) {
            updateBallCount();
        }
        int runs = delivery.getRuns();
        int totalScore = runs;
        if (!delivery.isValidBall()) {
            totalScore++;
            addExtrasRuns(1);
        }
        if (delivery.isExtraScore()) {
            addExtrasRuns(runs);
        } else {
            addBatsManScore(runs);
        }
        addTotalScore(totalScore);
        if (delivery.isOutOnThisDelivery()) {
            addWicket();
        }

    }

    private boolean isValidBatManDelivery(Delivery delivery) {
        if (!TextUtils.isEmpty(delivery.getWideType())) {
            return false;
        }
        if (!TextUtils.isEmpty(delivery.getNoType())) {
            return delivery.getRuns() > 0;
        }
        return true;
    }

    public void switchStriker() {
        int tempScore = strikerRuns;
        strikerRuns = nonStrikerRuns;
        nonStrikerRuns = tempScore;

        int balls = strikerTotalBalls;
        strikerTotalBalls = nonStrikerTotalBalls;
        nonStrikerTotalBalls = balls;
    }

    public void renewInning() {
        prevInningScore = totalScore;
        resetValues();

    }

    public void resetStateOnNewMatch() {
        prevInningScore = 0;
        resetValues();
    }

    private void resetValues() {
        totalExtras = 0;
        totalScore = 0;
        totalWickets = 0;
        strikerRuns = 0;
        nonStrikerRuns = 0;
        strikerTotalBalls = 0;
        nonStrikerTotalBalls = 0;
        bowlerOversDone = 0;
        bowlerBallsDone = 0;
        bowlerRunsGiven = 0;
    }

    public void resetStrikerStats() {
        strikerTotalBalls = 0;
        strikerRuns = 0;
    }

    public void resetNonStrikerStats() {
        nonStrikerRuns = 0;
        nonStrikerTotalBalls = 0;
    }

    public void updateBowlerStats(int bowlerOversDone, int bowlerBallsDone, int bowlerRunsGiven) {
        this.bowlerBallsDone = bowlerBallsDone;
        this.bowlerOversDone = bowlerOversDone;
        this.bowlerRunsGiven = bowlerRunsGiven;
    }

    public int getPrevInningScore() {
        return prevInningScore;
    }

    public void setPrevInningScore(int prevInningScore) {
        this.prevInningScore = prevInningScore;
    }


}
