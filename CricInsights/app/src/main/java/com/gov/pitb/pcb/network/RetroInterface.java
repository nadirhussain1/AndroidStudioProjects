package com.gov.pitb.pcb.network;


import com.gov.pitb.pcb.data.model.ConditionsModel;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;
import com.gov.pitb.pcb.data.server.login.LoginRequestModel;
import com.gov.pitb.pcb.data.server.login.LoginResponse;
import com.gov.pitb.pcb.data.server.match.GetMatchDataApiResponse;
import com.gov.pitb.pcb.data.server.permissions.MatchPermissionsResponse;
import com.gov.pitb.pcb.data.server.players.GetPlayersApiResponse;
import com.gov.pitb.pcb.data.server.teams.GetTeamsApiResponse;
import com.gov.pitb.pcb.data.server.tournaments.TournamentsApiResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nadirhussain on 26/09/2016.
 */

public interface RetroInterface {
    @GET("get_tournament_players")
    Call<GetPlayersApiResponse> getPlayersDataAPI();

    @GET("get_tournament_teams")
    Call<GetTeamsApiResponse> getTeamsDataAPI();

    @GET("get_tournaments")
    Call<TournamentsApiResponse> getTournamentsDataAPI();

    @POST("dologin")
    Call<LoginResponse> doLogin(@Body LoginRequestModel loginRequestModel);

    @FormUrlEncoded
    @POST("send_delivery")
    Call<GeneralServerResponse> sendDelivery(@FieldMap HashMap<String, Object> deliveryDataMap);

    @POST("send_weather_report")
    Call<GeneralServerResponse> sendMatchConditions(@Body ConditionsModel model);

    @GET("getMatchData?")
    Call<GetMatchDataApiResponse> getMatchData(@Query("matchId") String matchId);

    @GET("getMatchPermissions?")
    Call<MatchPermissionsResponse> getMatchPermissions(@Query("matchId") String matchId);


}
