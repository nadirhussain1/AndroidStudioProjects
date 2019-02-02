package com.gov.pitb.pcb.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Match;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.config.Tournament;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchCurrentState;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.APIDataLoadManager;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.permissions.MatchPermissionsResponse;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.DataSavedToDb;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnMatchConfigured;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPlayersSelected;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnScreensChecked;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnServerDataLoadFailed;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnTournamentsDataLoaded;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.services.LoadDbDataService;
import com.gov.pitb.pcb.utils.ApiHelper;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.CustomSpinnerAdapter;
import com.gov.pitb.pcb.views.adapters.PlayersCustomAdapter;
import com.gov.pitb.pcb.views.adapters.TournamentsAdapter;
import com.gov.pitb.pcb.views.dialogs.PitchWeatherConditionsDialog;
import com.gov.pitb.pcb.views.dialogs.creatematch.TeamPlayersSelectionDialog;
import com.gov.pitb.pcb.views.dialogs.main.ScreensCheckDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Response;

import static com.gov.pitb.pcb.R.drawable.custom_red_button_bg;

/**
 * Created by nadirhussain on 11/06/2017.
 */

public class CreateMatchFragment extends Fragment {
    View rootView;

    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;
    @BindView(R.id.errorLayout)
    LinearLayout errorLayout;
    @BindView(R.id.tournamentLayout)
    RelativeLayout tournamentHeadingLayout;
    @BindView(R.id.mainContentLayout)
    RelativeLayout mainContentLayout;

    @BindView(R.id.tournamentSpinner)
    Spinner tournamentSpinner;
    @BindView(R.id.tossWinSpinner)
    Spinner tossWinSpinner;
    @BindView(R.id.strikerSpinner)
    Spinner strikerSpinner;
    @BindView(R.id.nonStrikerSpinner)
    Spinner nonStrikerSpinner;
    @BindView(R.id.decisionSpinner)
    Spinner decisionSpinner;
    @BindView(R.id.strikerLayout)
    LinearLayout strikerLayout;
    @BindView(R.id.nonStrikerLayout)
    LinearLayout nonStrikerLayout;

    @BindView(R.id.teamsInfoLayout)
    RelativeLayout teamsInfoLayout;
    @BindView(R.id.matchDateTextView)
    TextView matchDateTextView;
    @BindView(R.id.teamOneImageView)
    ImageView teamOneImageView;
    @BindView(R.id.teamOneNameView)
    TextView teamOneNameView;
    @BindView(R.id.teamTwoImageView)
    ImageView teamTwoImageView;
    @BindView(R.id.teamTwoNameView)
    TextView teamTwoNameView;
    @BindView(R.id.selectTeamOneButton)
    TextView selectTeamOneButton;
    @BindView(R.id.selectTeamTwoButton)
    TextView selectTeamTwoButton;
    @BindView(R.id.startScoringButton)
    TextView startScoringButton;
    @BindView(R.id.nextMatchButton)
    ImageView nextMatchButton;


    private Animation slideFromRightAnimation, slideFromLeftAnimation;
    private List<Tournament> tournaments;
    private int currentTournamentIndex, currentMatchIndex = 0;
    private Match selectedMatch;
    private boolean isTeamOneSelected, isTeamTwoSelected;
    private String battingTeamId;


    private PlayersCustomAdapter strikerSpinnerAdapter, nonStrikerSpinnerAdapter;
    private int animationIndex = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_match_fragment, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);

        EventBus.getDefault().register(this);
        return rootView;
    }

    private void onDataLoadStartViews() {
        loadingLayout.setVisibility(View.VISIBLE);

        errorLayout.setVisibility(View.GONE);
        tournamentHeadingLayout.setVisibility(View.GONE);
        mainContentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadAndConfigAnimations();
        bindViews();
        initDataLoadingProcess();

    }

    private void loadAndConfigAnimations() {
        slideFromRightAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_animation);
        slideFromLeftAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.go_out_to_left);
        slideFromRightAnimation.setAnimationListener(animationListener);
        slideFromLeftAnimation.setAnimationListener(animationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void bindViews() {
        String[] decisions_options_array = getActivity().getResources().getStringArray(R.array.decision_option);
        CustomSpinnerAdapter decisionOptionsSpinner = new CustomSpinnerAdapter(getActivity(), new ArrayList<>(Arrays.asList(decisions_options_array)));
        decisionSpinner.setAdapter(decisionOptionsSpinner);
        tournamentSpinner.setOnItemSelectedListener(tournamentSelectListener);
    }

    private void initDataLoadingProcess() {
        onDataLoadStartViews();
        loadDataModel();
    }

    private void updateTournamentMatchesList() {
        currentMatchIndex = 0;
        updateMatchData();
    }

    private void updateMatchData() {
        if (tournaments.get(currentTournamentIndex).getMatches().size() == 0) {
            mainContentLayout.setVisibility(View.GONE);
            return;
        }
        if (currentMatchIndex < tournaments.get(currentTournamentIndex).getMatches().size()) {
            mainContentLayout.setVisibility(View.VISIBLE);
            isTeamOneSelected = false;
            isTeamTwoSelected = false;

            selectedMatch = tournaments.get(currentTournamentIndex).getMatches().get(currentMatchIndex);
            matchDateTextView.setText(""+selectedMatch.getMatchDate().split(" ")[0]);
            Team teamOne = InsightsDbManager.getTeam(selectedMatch.getTeamOneId());
            Team teamTwo = InsightsDbManager.getTeam(selectedMatch.getTeamTwoId());

            selectedMatch.setTeams(teamOne, teamTwo);
            battingTeamId = teamOne.getTeamId();

            String teamOneName = teamOne.getTeamName().replace(" ", "\n");
            String teamTwoName = teamTwo.getTeamName().replace(" ", "\n");

            teamOneNameView.setText(teamOneName);
            teamTwoNameView.setText(teamTwoName);
            if (!TextUtils.isEmpty(teamOne.getTeamLogoUrl())) {
                Picasso.with(getActivity()).load(teamOne.getTeamLogoUrl()).into(teamOneImageView);
            }
            if (!TextUtils.isEmpty(teamTwo.getTeamLogoUrl())) {
                Picasso.with(getActivity()).load(teamTwo.getTeamLogoUrl()).into(teamTwoImageView);
            }

            ArrayList<String> teamNames = new ArrayList<>();
            teamNames.add(teamOne.getTeamName());
            teamNames.add(teamTwo.getTeamName());

            CustomSpinnerAdapter tossAdapter = new CustomSpinnerAdapter(getActivity(), teamNames);
            tossWinSpinner.setAdapter(tossAdapter);

            strikerSpinnerAdapter = new PlayersCustomAdapter(getActivity(), teamOne.getSelectedPlayersOfTeam());
            nonStrikerSpinnerAdapter = new PlayersCustomAdapter(getActivity(), teamOne.getSelectedPlayersOfTeam());
            strikerSpinner.setAdapter(strikerSpinnerAdapter);
            nonStrikerSpinner.setAdapter(nonStrikerSpinnerAdapter);
            strikerLayout.setVisibility(View.GONE);
            nonStrikerLayout.setVisibility(View.GONE);


            selectTeamOneButton.setBackgroundResource(R.drawable.custom_red_button_bg);
            selectTeamTwoButton.setBackgroundResource(R.drawable.custom_red_button_bg);
            updateStartScoringButtonStatus();
        }
    }

    @OnClick(R.id.retryTextView)
    public void retryDataLoad() {
        initDataLoadingProcess();
    }

    @OnClick(R.id.matchConditionsButton)
    public void setMatchConditionsClicked() {
        PitchWeatherConditionsDialog conditionsDialog = new PitchWeatherConditionsDialog(getActivity(), selectedMatch.getMatchId(), "1", 0);
        conditionsDialog.showDialog();
    }

    @OnClick(R.id.selectTeamOneButton)
    public void selectTeamOneClicked() {
        TeamPlayersSelectionDialog teamPlayersSelectionDialog = new TeamPlayersSelectionDialog(getActivity(), selectedMatch.getTeamOne());
        teamPlayersSelectionDialog.showDialog();
    }

    @OnClick(R.id.selectTeamTwoButton)
    public void selectTeamTwoClicked() {
        TeamPlayersSelectionDialog teamPlayersSelectionDialog = new TeamPlayersSelectionDialog(getActivity(), selectedMatch.getTeamTwo());
        teamPlayersSelectionDialog.showDialog();
    }

    @OnClick(R.id.startScoringButton)
    public void onStartScoringClicked() {
        if (!isTeamOneSelected || !isTeamTwoSelected) {
            Toast.makeText(getActivity(), "Select 11 players of both teams", Toast.LENGTH_SHORT).show();
            return;
        }
        showMatchPermissionsDialog();

//        if (GlobalUtil.isInternetConnected(getActivity())) {
//            fetchMatchPermissions(selectedMatch.getMatchId());
//        } else {
//            onPermissionFetchFailed();
//        }
    }

    private void showMatchPermissionsDialog() {
        ScreensCheckDialog screensCheckDialog = new ScreensCheckDialog(getActivity());
        screensCheckDialog.showDialog();
    }


    private void updateTeamSelectionStatus(String teamId, boolean isTeamFinalized) {
        if (teamId.equalsIgnoreCase(selectedMatch.getTeamOne().getTeamId())) {
            isTeamOneSelected = isTeamFinalized;
        } else {
            isTeamTwoSelected = isTeamFinalized;
        }
        updateStartScoringButtonStatus();

    }

    private void updateStartScoringButtonStatus() {
        if (isTeamOneSelected && isTeamTwoSelected) {
            startScoringButton.setBackgroundResource(R.drawable.custom_primary_button_bg);
        } else {
            startScoringButton.setBackgroundResource(R.drawable.custom_red_button_bg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPlayersSelected event) {
        int selectedPlayersCount = getSelectedPlayersCount(event.teamId);
        if (selectedPlayersCount == 11) {
            setBackgroundToSelectTeamPlayerButton(R.drawable.custom_primary_button_bg, event.teamId);
            updateTeamSelectionStatus(event.teamId, true);
        } else {
            setBackgroundToSelectTeamPlayerButton(custom_red_button_bg, event.teamId);
            updateTeamSelectionStatus(event.teamId, false);
        }

        if (isTeamOneSelected && isTeamTwoSelected) {
            Team battingTeam = selectedMatch.getTeamById(battingTeamId);
            strikerSpinnerAdapter.refreshSpinnerItems(battingTeam.getSelectedPlayersOfTeam());
            updateNonStriker(strikerSpinner.getSelectedItemPosition());
            strikerLayout.setVisibility(View.VISIBLE);
            nonStrikerLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setBackgroundToSelectTeamPlayerButton(int bgDrawableRes, String teamId) {
        if (teamId.equalsIgnoreCase(selectedMatch.getTeamOne().getTeamId())) {
            selectTeamOneButton.setBackgroundResource(bgDrawableRes);
        } else {
            selectTeamTwoButton.setBackgroundResource(bgDrawableRes);
        }
    }

    private int getSelectedPlayersCount(String teamId) {
        Team team = selectedMatch.getTeamOne();
        if (teamId.equalsIgnoreCase(selectedMatch.getTeamTwoId())) {
            team = selectedMatch.getTeamTwo();
        }
        ArrayList<Player> players = team.getPlayersOfTeam();
        int count = 0;
        for (Player player : players) {
            if (player.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @OnClick(R.id.nextMatchButton)
    public void nextMatchArrowClicked() {
        goToNexMatch();
    }

    @OnItemSelected(R.id.tournamentSpinner)
    public void tournamentSpinnerItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0) {
            mainContentLayout.setVisibility(View.INVISIBLE);
        } else {
            mainContentLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnItemSelected(R.id.tossWinSpinner)
    public void tossWonSpinnerItemSelected(AdapterView<?> adapterView, View view, int selectedTeamIndex, long l) {
        updateStriker(decisionSpinner.getSelectedItemPosition(), selectedTeamIndex);
        updateNonStriker(strikerSpinner.getSelectedItemPosition());
    }

    @OnItemSelected(R.id.decisionSpinner)
    public void decisionItemSelected(AdapterView<?> adapterView, View view, int decisionPosition, long l) {
        updateStriker(decisionPosition, tossWinSpinner.getSelectedItemPosition());
        updateNonStriker(strikerSpinner.getSelectedItemPosition());
    }

    @OnItemSelected(R.id.strikerSpinner)
    public void strikerItemSelected(AdapterView<?> adapterView, View view, int selectedStrikerPosition, long l) {
        updateNonStriker(selectedStrikerPosition);
    }

    private void updateStriker(int decisionPosition, int selectedTeamIndex) {
        if (selectedMatch != null) {
            int battingTeamIndex;
            if (decisionPosition == 0) {
                battingTeamIndex = selectedTeamIndex;
            } else if (selectedTeamIndex == 0) {
                battingTeamIndex = 1;
            } else {
                battingTeamIndex = 0;
            }

            if (battingTeamIndex == 0) {
                battingTeamId = selectedMatch.getTeamOneId();
            } else {
                battingTeamId = selectedMatch.getTeamTwoId();
            }

            Team battingTeam = selectedMatch.getTeamById(battingTeamId);
            strikerSpinnerAdapter.refreshSpinnerItems(battingTeam.getSelectedPlayersOfTeam());
        }
    }

    private void updateNonStriker(int strikerPosition) {
        if (selectedMatch != null) {
            Team battingTeam = selectedMatch.getTeamById(battingTeamId);
            ArrayList<Player> battingTeamPlayersNames = battingTeam.getSelectedPlayersOfTeam();
            if (strikerPosition >= 0 && strikerPosition < battingTeamPlayersNames.size()) {
                battingTeamPlayersNames.remove(strikerPosition);
                nonStrikerSpinnerAdapter.refreshSpinnerItems(battingTeamPlayersNames);
            }
        }
    }


    private void goToNexMatch() {
        currentMatchIndex++;
        if (currentMatchIndex > tournaments.get(currentTournamentIndex).getMatches().size() - 1) {
            currentMatchIndex = 0;
        }
        startGoOutToLeftAnimation();
    }

    private void startGoOutToLeftAnimation() {
        teamsInfoLayout.startAnimation(slideFromLeftAnimation);
    }

    private void startComeFromRightAnimation() {
        teamsInfoLayout.startAnimation(slideFromRightAnimation);
    }

    private void onPrevious() {
        if (currentMatchIndex > 0) {
            currentMatchIndex--;
            teamsInfoLayout.startAnimation(slideFromLeftAnimation);
        }
    }


    private AnimationListener animationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animationIndex == 0) {
                animationIndex = 1;
                startComeFromRightAnimation();
            } else {
                animationIndex = 0;
                updateMatchData();
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };


    private void onMatchDataLoadError() {
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showTournamentsView() {
        loadingLayout.setVisibility(View.GONE);
        tournamentHeadingLayout.setVisibility(View.VISIBLE);

        TournamentsAdapter tournamentsAdapter = new TournamentsAdapter(getActivity(), tournaments);
        tournamentSpinner.setAdapter(tournamentsAdapter);
    }

    private void loadDataModel() {
        boolean isOfflineDataSaved = InsightsPreferences.isOfflineDataSaved(getActivity());
        if (isOfflineDataSaved) {
            startLoadDbDataService();
        }

        if (GlobalUtil.isInternetConnected(getActivity())) {
            new APIDataLoadManager().loadDataFromServer(getActivity());
        } else if (!isOfflineDataSaved) {
            GlobalUtil.showOkAlertDialog(getActivity(), getString(R.string.no_connection_title), getString(R.string.no_connection_msg));
            onMatchDataLoadError();
        }

    }

    private void startLoadDbDataService() {
        Intent intent = new Intent(getActivity(), LoadDbDataService.class);
        getActivity().startService(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnServerDataLoadFailed event) {
        boolean isOfflineDataSaved = InsightsPreferences.isOfflineDataSaved(getActivity());
        if (!isOfflineDataSaved) {
            onMatchDataLoadError();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataSavedToDb event) {
        boolean isOfflineDataSaved = InsightsPreferences.isOfflineDataSaved(getActivity());
        if (!isOfflineDataSaved) {
            InsightsPreferences.saveOfflineDataStatus(getActivity(), true);
            startLoadDbDataService();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnTournamentsDataLoaded event) {
        this.tournaments = event.tournaments;
        showTournamentsView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnScreensChecked event) {
        onPermissionFetchSuccess(event.matchPermData);
    }


    private OnItemSelectedListener tournamentSelectListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            currentTournamentIndex = position;
            updateTournamentMatchesList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void fetchMatchPermissions(String matchId) {
        loadingLayout.setVisibility(View.VISIBLE);

        RetroInterface apiService = RetroApiClient.getClient(getActivity()).create(RetroInterface.class);
        Call<MatchPermissionsResponse> call = apiService.getMatchPermissions(matchId);
        ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<MatchPermissionsResponse>() {
            @Override
            public void onResponse(Call<MatchPermissionsResponse> call, Response<MatchPermissionsResponse> response) {
                MatchPermissionsResponse matchPermissionsResponse = response.body();
                if (!matchPermissionsResponse.isSuccess()) {
                    onPermissionFetchFailed();
                    return;
                }
                onPermissionFetchSuccess(matchPermissionsResponse.getData());
            }

            @Override
            public void onFailure(Call<MatchPermissionsResponse> call, Throwable t) {
                onPermissionFetchFailed();
            }
        });
    }

    private void onPermissionFetchFailed() {
        loadingLayout.setVisibility(View.GONE);
        MatchPermData matchPermData = new MatchPermData();
        matchPermData.allTrue();

        configureSelectedMatchAndStartScoring(matchPermData);
    }

    private void onPermissionFetchSuccess(MatchPermData matchPermData) {
        loadingLayout.setVisibility(View.GONE);
        configureSelectedMatchAndStartScoring(matchPermData);
    }

    private void configureSelectedMatchAndStartScoring(MatchPermData matchPermData) {
        InsightsDbManager.saveMatchPermissions(matchPermData);

        int matchOvers = Double.valueOf(selectedMatch.getMatchOvers()).intValue();
        Inning inning = new Inning(selectedMatch.getMatchId(), "1", matchOvers);
        if (battingTeamId.equalsIgnoreCase(selectedMatch.getTeamOne().getTeamId())) {
            inning.setTeams(selectedMatch.getTeamOne(), selectedMatch.getTeamTwo());
        } else {
            inning.setTeams(selectedMatch.getTeamTwo(), selectedMatch.getTeamOne());
        }

        Player strikerPlayer = (Player) strikerSpinner.getSelectedItem();
        Player nonStrikerPlayer = (Player) nonStrikerSpinner.getSelectedItem();

        MatchStateController controller = InsightsDbManager.getController();
        MatchCurrentState currentState = InsightsDbManager.getMatchState();
        if (controller == null) {
            controller = new MatchStateController(selectedMatch.getMatchId(), "1");
        } else {
            controller.resetControllerOnNewMatch(selectedMatch.getMatchId(), "1");
        }

        if (currentState == null) {
            currentState = new MatchCurrentState();
        } else {
            currentState.resetStateOnNewMatch();
        }

        controller.setStrikerId(strikerPlayer.getPlayerId());
        controller.setNonStrikerId(nonStrikerPlayer.getPlayerId());


        InsightsDbManager.saveInning(inning);
        InsightsDbManager.saveStateController(controller);
        InsightsDbManager.saveState(currentState);

        MatchStateManager.getInstance().initInning(inning, controller);
        MatchStateManager.getInstance().setMatchCurrentState(currentState);

        EventBus.getDefault().post(new OnMatchConfigured());
    }

}
