package com.gov.pitb.pcb.data.db;

import com.activeandroid.serializer.TypeSerializer;
import com.gov.pitb.pcb.data.db.config.Match;
import com.gov.pitb.pcb.utils.GlobalUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 10/07/2017.
 */

public class ListTypeSerializer extends TypeSerializer {

    private static final String TAG = ListTypeSerializer.class.getSimpleName();


    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (null == data)
            return null;

        String str = "";
        for (Match match : (List<Match>) data) {
            String converted = convertObjectToJson(match);
            if (converted != null) {
                str = str + converted + "##";
            }
        }
        return str;
    }

    private String convertObjectToJson(Match match) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("matchId", match.getMatchId());
            jsonObject.put("teamOneId", match.getTeamOneId());
            jsonObject.put("teamTwoId", match.getTeamTwoId());
            jsonObject.put("matchDate", match.getMatchDate());
            jsonObject.put("matchOvers", match.getMatchOvers());

            return jsonObject.toString();

        } catch (Exception exception) {
            GlobalUtil.printLog("Serializer", "Exception =" + exception);
        }
        return null;
    }

    private Match getObjectFromJsonString(String s) {
        Match match = new Match();
        try {
            JSONObject jsonObject = new JSONObject(s);
            match.setMatchId(jsonObject.getString("matchId"));
            match.setTeamOneId(jsonObject.getString("teamOneId"));
            match.setTeamTwoId(jsonObject.getString("teamTwoId"));
            match.setMatchDate(jsonObject.getString("matchDate"));
            match.setMatchOvers(jsonObject.getString("matchOvers"));
        } catch (Exception exception) {
            GlobalUtil.printLog("Serializer", "DeSerialize Exception =" + exception);
        }
        return match;
    }


    @Override
    public List<Match> deserialize(Object data) {
        if (null == data)
            return null;

        List<Match> matches = new ArrayList<>();
        String str = (String) data;
        String[] array = str.split("##");

        for (int count = 0; count < array.length; count++) {
            try {
                matches.add(getObjectFromJsonString(array[count]));
            } catch (Exception exception) {
                GlobalUtil.printLog("Serializer", "Exception =" + exception);
            }
        }

        return matches;

    }
}
