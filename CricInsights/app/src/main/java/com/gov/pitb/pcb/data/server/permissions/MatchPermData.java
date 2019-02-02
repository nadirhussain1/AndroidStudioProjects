package com.gov.pitb.pcb.data.server.permissions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 22/08/2017.
 */

public class MatchPermData extends Model {
    public MatchPermData() {
        super();
    }

    public void allTrue(){
        this.isField=true;
        this.isPitch=true;
        this.isScore=true;
    }

    @SerializedName("field")
    @Expose
    @Column(name = "field")
    private boolean isField;
    @SerializedName("pitch")
    @Expose
    @Column(name = "pitch")
    private boolean isPitch;
    @SerializedName("score")
    @Expose
    @Column(name = "score")
    private boolean isScore;



    public boolean isField() {
        return isField;
    }

    public void setField(boolean field) {
        isField = field;
    }

    public boolean isPitch() {
        return isPitch;
    }

    public void setPitch(boolean pitch) {
        isPitch = pitch;
    }

    public boolean isScore() {
        return isScore;
    }

    public void setScore(boolean score) {
        isScore = score;
    }




}
