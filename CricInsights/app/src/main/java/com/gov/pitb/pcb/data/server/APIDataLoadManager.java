package com.gov.pitb.pcb.data.server;

import android.content.Context;

import com.gov.pitb.pcb.data.server.players.GetPlayersApiResponse;
import com.gov.pitb.pcb.data.server.players.PlayersData;
import com.gov.pitb.pcb.data.server.teams.GetTeamsApiResponse;
import com.gov.pitb.pcb.data.server.teams.TeamsData;
import com.gov.pitb.pcb.data.server.tournaments.TournamentsApiResponse;
import com.gov.pitb.pcb.data.server.tournaments.TournamentsData;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnServerDataLoadFailed;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnServerDataLoadSuccess;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.utils.ApiHelper;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class APIDataLoadManager {
    private TournamentsData tournamentsData;
    private PlayersData playersData;
    private TeamsData teamsData;


    public void loadDataFromServer(Context context) {
        invokeGetPlayersAPI(context);

    }

    private void onDataLoadFailed() {
        EventBus.getDefault().post(new OnServerDataLoadFailed());
    }

    private void onServerDataLoadSuccess() {
        OnServerDataLoadSuccess onServerDataLoadSuccess = new OnServerDataLoadSuccess();
        onServerDataLoadSuccess.tournamentsData = tournamentsData;
        onServerDataLoadSuccess.playersData = playersData;
        onServerDataLoadSuccess.teamsData = teamsData;

        EventBus.getDefault().post(onServerDataLoadSuccess);
    }


    private void invokeGetPlayersAPI(final Context context) {
        RetroInterface apiService = RetroApiClient.getClient(context).create(RetroInterface.class);
        Call<GetPlayersApiResponse> call = apiService.getPlayersDataAPI();
        ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<GetPlayersApiResponse>() {
            @Override
            public void onResponse(Call<GetPlayersApiResponse> call, Response<GetPlayersApiResponse> response) {
                GetPlayersApiResponse playersApiResponse = response.body();
                if (playersApiResponse.isSuccess()) {
                    playersData = playersApiResponse.getData();
                    invokeGetTeamsAPI(context);
                } else {
                    onDataLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<GetPlayersApiResponse> call, Throwable t) {
                onDataLoadFailed();
            }
        });
    }

    private void invokeGetTeamsAPI(final Context context) {
        RetroInterface apiService = RetroApiClient.getClient(context).create(RetroInterface.class);
        Call<GetTeamsApiResponse> call = apiService.getTeamsDataAPI();
        ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<GetTeamsApiResponse>() {
            @Override
            public void onResponse(Call<GetTeamsApiResponse> call, Response<GetTeamsApiResponse> response) {
                GetTeamsApiResponse teamsApiResponse = response.body();
                if (teamsApiResponse.isSuccess()) {
                    teamsData = teamsApiResponse.getData();
                    invokeGetTournamentsServerAPI(context);
                } else {
                    onDataLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<GetTeamsApiResponse> call, Throwable t) {
                onDataLoadFailed();
            }
        });
    }

    private void invokeGetTournamentsServerAPI(Context context) {
        RetroInterface apiService = RetroApiClient.getClient(context).create(RetroInterface.class);
        Call<TournamentsApiResponse> call = apiService.getTournamentsDataAPI();
        ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<TournamentsApiResponse>() {
            @Override
            public void onResponse(Call<TournamentsApiResponse> call, Response<TournamentsApiResponse> response) {
                TournamentsApiResponse tournamentsApiResponse = response.body();
                if (tournamentsApiResponse.isSuccess()) {
                    tournamentsData = tournamentsApiResponse.getData();
                    onServerDataLoadSuccess();
                } else {
                    onDataLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<TournamentsApiResponse> call, Throwable t) {
                onDataLoadFailed();
            }
        });
    }


}
