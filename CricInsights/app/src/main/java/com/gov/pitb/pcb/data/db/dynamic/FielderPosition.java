package com.gov.pitb.pcb.data.db.dynamic;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.server.players.Fielder;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.custom.wheels.FielderWagonWheelView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 18/07/2017.
 */

@Table(name = "table_fielders")
public class FielderPosition extends Model {
    @Column(name = "positions")
    private String positions;

    public FielderPosition() {
        super();
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public static String encodeFielderPosToString(List<Fielder> fielders) {
        int resizeDimension = (int) ViewScaleHandler.resizeDimension(FielderWagonWheelView.WHEEL_SIZE);
        try {
            JSONArray jsonArray = new JSONArray();
            for (int count = 0; count < fielders.size(); count++) {

                Fielder fielder = fielders.get(count);

                JSONObject jsonObject = new JSONObject();
                float xPercent = (fielder.getPositionX() / resizeDimension) * 100;
                float YPercent = (fielder.getPositionY() / resizeDimension) * 100;

                jsonObject.put("id", fielder.getFielderId());
                jsonObject.put("x", xPercent);
                jsonObject.put("y", YPercent);

                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();

        } catch (Exception e) {
            GlobalUtil.printLog("Exception", "" + e.getMessage());
        }
        return "";

    }

    public static List<Fielder> decodeFielderJsonString(String fieldersJson) {
        int resizeDimension = (int) ViewScaleHandler.resizeDimension(FielderWagonWheelView.WHEEL_SIZE);
        List<Fielder> fieldersPositionsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(fieldersJson);
            for (int count = 0; count < jsonArray.length(); count++) {

                JSONObject jsonObject = jsonArray.getJSONObject(count);
                String playerId = jsonObject.getString("id");
                float xPercentage = (float) jsonObject.getDouble("x");
                float yPercentage = (float) jsonObject.getDouble("y");

                float xPoint = (xPercentage * resizeDimension) / 100;
                float yPoint = (yPercentage * resizeDimension) / 100;

                Player player = InsightsDbManager.getPlayer(playerId);
                Fielder fielder = new Fielder(player, xPoint, yPoint);
                fieldersPositionsList.add(fielder);

            }

        } catch (Exception e) {
            GlobalUtil.printLog("Exception", "" + e.getMessage());
        }
        return fieldersPositionsList;

    }


}
