package com.gov.pitb.pcb.data.server.players;

import android.graphics.RectF;

/**
 * Created by nadirhussain on 17/07/2017.
 */

public class Fielder {
    private float positionX;
    private float positionY;
    private Player player;

    public Fielder(Player player, float x, float y) {
        this.player = player;
        this.positionX = x;
        this.positionY = y;
    }

    private void updatePosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    public boolean checkDrag(float oldX, float oldY, float newX, float newY, int fielderWidth, int fielderHeight) {
        float left = positionX - fielderWidth / 2;
        float top = positionY - fielderHeight / 2;
        float right = positionX + fielderWidth / 2;
        float bottom = positionY + fielderHeight / 2;

        RectF areaRect = new RectF(left, top, right, bottom);
        if (areaRect.contains(oldX, oldY)) {
            updatePosition(newX, newY);
            return true;
        }
        return false;
    }

    public String getFielderId() {
        return player.getPlayerId();
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public String getFielderName() {
        return player.getPlayerName();
    }

}
