package com.gov.pitb.pcb.views.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.model.BatScoreCardModel;
import com.gov.pitb.pcb.data.model.BowlScoreCardModel;
import com.gov.pitb.pcb.data.server.match.GetMatchDataApiResponse;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.utils.ApiHelper;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.ScoreCardBatAdapter;
import com.gov.pitb.pcb.views.adapters.ScoreCardBowlAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 02/08/2017.
 */

public class ScoreCardActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolBarTitleView;
    @BindView(R.id.teamOneImageView)
    ImageView teamOneImageView;
    @BindView(R.id.teamTwoImageView)
    ImageView teamTwoImageView;
    @BindView(R.id.teamOneNameView)
    TextView teamOneNameView;
    @BindView(R.id.teamTwoNameView)
    TextView teamTwoNameView;
    @BindView(R.id.teamOneScoreView)
    TextView firstInningTotalScoreView;
    @BindView(R.id.teamTwoScoreView)
    TextView secondInningTotalScoreView;
    @BindView(R.id.firstBattingRecyclerView)
    RecyclerView firstBatRecyclerView;
    @BindView(R.id.firstInnBowlRecyclerView)
    RecyclerView firstBowlRecyclerView;
    @BindView(R.id.secondBattingRecyclerView)
    RecyclerView secondBatRecyclerView;
    @BindView(R.id.secondInnBowlRecyclerView)
    RecyclerView secondBowlRecyclerView;
    @BindView(R.id.firstInningTitleTextView)
    TextView firstInningTitleTextView;
    @BindView(R.id.secondInningTitleTextView)
    TextView secondInningTitleTextView;
    @BindView(R.id.mainContentLayout)
    RelativeLayout mainContentLayout;
    @BindView(R.id.indicationLayout)
    RelativeLayout indicationsLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noDataErrorView)
    TextView noDataErrorView;


    private List<Delivery> firstInningDeliveries;
    private List<Delivery> secondInningDeliveries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.scoreboard, null);
        new ViewScaleHandler(this).scaleRootView(rootView);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);

        setToolBar();
        fetchData();
    }

    private void setToolBar() {
        toolbar.setNavigationIcon(R.drawable.back_gray_arrow);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchData() {
        String matchId = MatchStateManager.getInstance().getMatchStateController().getCurrentMatchId();
        MatchPermData matchPermData = InsightsDbManager.getMatchPermData();
        if (matchPermData.isScore()) {
            firstInningDeliveries = InsightsDbManager.getInningDeliveries(matchId, "1");
            secondInningDeliveries = InsightsDbManager.getInningDeliveries(matchId, "2");
            showContentLayout();
            populateViewsWithData();
        } else {
            showIndicationLayout();
            fetchMatchDataFromServer(matchId);
        }
    }

    private void populateViewsWithData() {
        Inning currentInning = MatchStateManager.getInstance().getCurrentInning();
        Team battingTeam = MatchStateManager.getInstance().getBattingTeam();
        Team bowlingTeam = MatchStateManager.getInstance().getBowlingTeam();


        Team firstInningBatTeam;
        Team firstInningBowlTeam;

        if (currentInning.getInningId().equalsIgnoreCase("1")) {
            firstInningBatTeam = battingTeam;
            firstInningBowlTeam = bowlingTeam;
            secondInningTotalScoreView.setText("-");
        } else {
            firstInningBatTeam = bowlingTeam;
            firstInningBowlTeam = battingTeam;
            int secondInningTotalScore = calculateInningTotalScore(secondInningDeliveries);
            secondInningTotalScoreView.setText("" + secondInningTotalScore);
        }

        toolBarTitleView.setText(firstInningBatTeam.getTeamName() + " vs " + firstInningBowlTeam.getTeamName());
        if (!TextUtils.isEmpty(firstInningBatTeam.getTeamLogoUrl())) {
            Picasso.with(this).load(firstInningBatTeam.getTeamLogoUrl()).into(teamOneImageView);
        }
        if (!TextUtils.isEmpty(firstInningBowlTeam.getTeamLogoUrl())) {
            Picasso.with(this).load(firstInningBowlTeam.getTeamLogoUrl()).into(teamTwoImageView);
        }
        teamOneNameView.setText(firstInningBatTeam.getTeamName());
        teamTwoNameView.setText(firstInningBowlTeam.getTeamName());

        int firstInningTotalScore = calculateInningTotalScore(firstInningDeliveries);
        firstInningTotalScoreView.setText("" + firstInningTotalScore);

        firstInningTitleTextView.setText(firstInningBatTeam.getTeamName() + "'s innings");
        secondInningTitleTextView.setText(firstInningBowlTeam.getTeamName() + "'s innings");

        ArrayList<BatScoreCardModel> firstInningBatScoreList = getTeamBatScoreModelList(firstInningBatTeam, firstInningDeliveries);
        ArrayList<BatScoreCardModel> secondInningBatScoreList = getTeamBatScoreModelList(firstInningBowlTeam, secondInningDeliveries);

        ArrayList<BowlScoreCardModel> firstInningBowlList = getTeamBowlScoreModelList(firstInningBowlTeam, firstInningDeliveries);
        ArrayList<BowlScoreCardModel> secondInningBowlList = getTeamBowlScoreModelList(firstInningBatTeam, secondInningDeliveries);

        ScoreCardBatAdapter firstInnBatAdapter = new ScoreCardBatAdapter(this, firstInningBatScoreList);
        firstBatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firstBatRecyclerView.setAdapter(firstInnBatAdapter);
        firstBatRecyclerView.setNestedScrollingEnabled(false);

        ScoreCardBatAdapter secondInnBatAdapter = new ScoreCardBatAdapter(this, secondInningBatScoreList);
        secondBatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        secondBatRecyclerView.setAdapter(secondInnBatAdapter);
        secondBatRecyclerView.setNestedScrollingEnabled(false);

        ScoreCardBowlAdapter firstInnBowlAdapter = new ScoreCardBowlAdapter(this, firstInningBowlList);
        firstBowlRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firstBowlRecyclerView.setAdapter(firstInnBowlAdapter);
        firstBowlRecyclerView.setNestedScrollingEnabled(false);

        ScoreCardBowlAdapter secondInnBowlAdapter = new ScoreCardBowlAdapter(this, secondInningBowlList);
        secondBowlRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        secondBowlRecyclerView.setAdapter(secondInnBowlAdapter);
        secondBowlRecyclerView.setNestedScrollingEnabled(false);
    }

    private int calculateInningTotalScore(List<Delivery> deliveries) {
        int score = 0;
        for (Delivery delivery : deliveries) {
            score += delivery.getRuns();
            if (!delivery.isValidBall()) {
                score++;
            }
        }
        return score;
    }

    private ArrayList<BatScoreCardModel> getTeamBatScoreModelList(Team team, List<Delivery> inningDeliveries) {
        ArrayList<BatScoreCardModel> batScoreCardModelArrayList = new ArrayList<>();
        List<Player> players = team.getSelectedPlayersOfTeam();
        for (Player player : players) {
            List<Delivery> batsManDeliveries = filterBatsManDeliveries(inningDeliveries, player.getPlayerId());
            if (batsManDeliveries == null || batsManDeliveries.size() == 0) {
                BatScoreCardModel batScoreCardModel = new BatScoreCardModel(player.getPlayerName(), false);
                int lastIndex = batScoreCardModelArrayList.size();
                batScoreCardModelArrayList.add(lastIndex, batScoreCardModel);
            } else {
                BatScoreCardModel batScoreCardModel = new BatScoreCardModel(player.getPlayerName(), true);
                batScoreCardModel.setBalls(batsManDeliveries.size());
                int runs = 0;
                int fours = 0;
                int sixes = 0;

                for (Delivery delivery : batsManDeliveries) {
                    if (!delivery.isBye() && !delivery.isLegBye()) {
                        runs += delivery.getRuns();
                        if (delivery.getRuns() == 4) {
                            fours++;
                        } else if (delivery.getRuns() == 6) {
                            sixes++;
                        }
                    }
                }
                batScoreCardModel.setRuns(runs);
                batScoreCardModel.setFours(fours);
                batScoreCardModel.setSixes(sixes);

                batScoreCardModelArrayList.add(0, batScoreCardModel);
            }

        }

        return batScoreCardModelArrayList;
    }


    private ArrayList<BowlScoreCardModel> getTeamBowlScoreModelList(Team team, List<Delivery> inningDeliveries) {
        ArrayList<BowlScoreCardModel> bowlScoreCardModelArrayList = new ArrayList<>();
        List<Player> players = team.getSelectedPlayersOfTeam();
        for (Player player : players) {
            List<Delivery> deliveries = filterBowlerDeliveries(inningDeliveries, player.getPlayerId());
            if (deliveries == null || deliveries.size() == 0) {
                BowlScoreCardModel bowlScoreCardModel = new BowlScoreCardModel(player.getPlayerName(), false);
                int lastIndex = bowlScoreCardModelArrayList.size();
                bowlScoreCardModelArrayList.add(lastIndex, bowlScoreCardModel);
            } else {
                BowlScoreCardModel bowlScoreCardModel = new BowlScoreCardModel(player.getPlayerName(), true);
                bowlScoreCardModel.setBallsBowled(deliveries.size());

                int overs = 0;
                int mOvers = 0;
                int runs = 0;
                int wickets = 0;
                int zeros = 0;
                int fours = 0;
                int sixes = 0;

                int overRuns = 0;
                int prevOverId = -1;

                for (Delivery delivery : deliveries) {

                    if (prevOverId != delivery.getOverId()) {
                        if (prevOverId != -1 && overRuns == 0) {
                            mOvers++;
                        }
                        overRuns = 0;
                        overs++;
                        prevOverId = delivery.getOverId();
                    }

                    int score = delivery.getRuns();
                    if (!delivery.isValidBall()) {
                        score++;
                    }

                    runs += score;
                    overRuns += score;


                    if (delivery.getRuns() == 4) {
                        fours++;
                    } else if (delivery.getRuns() == 6) {
                        sixes++;
                    }
                    if (delivery.isOutOnThisDelivery()) {
                        wickets++;
                    } else if (delivery.getRuns() == 0 && isValidBatDelivery(delivery)) {
                        zeros++;
                    }
                }

                bowlScoreCardModel.setFours(fours);
                bowlScoreCardModel.setSixes(sixes);
                bowlScoreCardModel.setZeros(zeros);
                bowlScoreCardModel.setmOvers(mOvers);
                bowlScoreCardModel.setOvers(overs);
                bowlScoreCardModel.setRuns(runs);
                bowlScoreCardModel.setWickets(wickets);

                bowlScoreCardModelArrayList.add(0, bowlScoreCardModel);

            }

        }

        return bowlScoreCardModelArrayList;
    }

    private ArrayList<Delivery> filterBatsManDeliveries(List<Delivery> inningDeliveries, String strikerId) {
        ArrayList<Delivery> filteredDeliveries = new ArrayList<>();
        for (Delivery delivery : inningDeliveries) {
            if (isValidBatDelivery(delivery)) {
                if (delivery.getStrikerId().contentEquals(strikerId)) {
                    filteredDeliveries.add(delivery);
                }
            }
        }
        return filteredDeliveries;
    }

    private boolean isValidBatDelivery(Delivery delivery) {
        if (!TextUtils.isEmpty(delivery.getWideType())) {
            return false;
        }
        if (!TextUtils.isEmpty(delivery.getNoType())) {
            return delivery.getRuns() > 0;
        }
        return true;
    }

    private ArrayList<Delivery> filterBowlerDeliveries(List<Delivery> inningDeliveries, String bowlerId) {
        ArrayList<Delivery> filteredDeliveries = new ArrayList<>();
        for (Delivery delivery : inningDeliveries) {
            if (delivery.getBowlerId().contentEquals(bowlerId)) {
                filteredDeliveries.add(delivery);
            }
        }
        return filteredDeliveries;
    }

    private void showProgressBar() {
        noDataErrorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showErrorView() {
        noDataErrorView.setVisibility(View.VISIBLE);
        showIndicationLayout();
    }

    private void showContentLayout() {
        mainContentLayout.setVisibility(View.VISIBLE);
        indicationsLayout.setVisibility(View.GONE);
    }

    private void showIndicationLayout() {
        mainContentLayout.setVisibility(View.GONE);
        indicationsLayout.setVisibility(View.VISIBLE);
    }

    private void fetchMatchDataFromServer(String matchId) {
        showProgressBar();
        RetroInterface apiService = RetroApiClient.getClient(this).create(RetroInterface.class);
        Call<GetMatchDataApiResponse> call = apiService.getMatchData(matchId);
        ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<GetMatchDataApiResponse>() {
            @Override
            public void onResponse(Call<GetMatchDataApiResponse> call, Response<GetMatchDataApiResponse> response) {
                GetMatchDataApiResponse getMatchDataApiResponse = response.body();
                if (getMatchDataApiResponse.isSuccess()) {
                    filterInningsData(getMatchDataApiResponse.getMatchDeliveries());
                } else {
                    hideProgressBar();
                    showErrorView();
                }
            }

            @Override
            public void onFailure(Call<GetMatchDataApiResponse> call, Throwable t) {
                hideProgressBar();
                showErrorView();
            }
        });
    }

    private void filterInningsData(List<Delivery> matchDeliveries) {
        firstInningDeliveries = new ArrayList<>();
        secondInningDeliveries = new ArrayList<>();
        for (Delivery delivery : matchDeliveries) {
            if (delivery.getInningId().equalsIgnoreCase("1")) {
                firstInningDeliveries.add(delivery);
            } else {
                secondInningDeliveries.add(delivery);
            }
        }

        hideProgressBar();
        showContentLayout();
        populateViewsWithData();
    }

}
