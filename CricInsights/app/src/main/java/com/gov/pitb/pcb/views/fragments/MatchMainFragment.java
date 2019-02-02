package com.gov.pitb.pcb.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.InningsChanged;
import com.gov.pitb.pcb.navigators.EventBusEvents.NextBallEvent;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnBowlerSelected;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPlayerOut;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPreviousDeliverySelected;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.activities.ScoreCardActivity;
import com.gov.pitb.pcb.views.activities.SuperMainActivity;
import com.gov.pitb.pcb.views.adapters.OverBallsHorizontalAdapter;
import com.gov.pitb.pcb.views.dialogs.PitchWeatherConditionsDialog;
import com.gov.pitb.pcb.views.dialogs.main.BowlerSelectionDialog;
import com.gov.pitb.pcb.views.dialogs.main.MatchEndDialog;
import com.gov.pitb.pcb.views.dialogs.main.PlayerOutDialog;
import com.gov.pitb.pcb.views.dialogs.main.SecondInningDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gov.pitb.pcb.R.id.teamOneImageView;

/**
 * Created by nadirhussain on 12/06/2017.
 */

public class MatchMainFragment extends Fragment {
    View rootView;
    @BindView(R.id.overBallsRecyclerView)
    RecyclerView ballsRecyclerView;
    @BindView(R.id.fieldSectionAreaView)
    TextView fieldSectionTextView;
    @BindView(R.id.pitchSectionAreaView)
    TextView pitchSectionTextView;
    @BindView(R.id.runsSectionAreaView)
    TextView scoreSectionTextView;
    @BindView(R.id.wheelSectionAreaView)
    TextView wheelSectionTextView;


    private OverBallsHorizontalAdapter adapter;
    private Delivery selectedPrevDelivery = null;
    MatchPermData matchPermData;
    private int firstFragmentIndex = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_main_fragment, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        matchPermData = InsightsDbManager.getMatchPermData();
        bindBallsRecyclerView();
        if (!MatchStateManager.getInstance().isBowlerSelected()) {
            showBowlerSelectionDialog();
        } else {
            init();
        }
    }

    private void init() {
        if (matchPermData.isField()) {
            MatchStateManager.getInstance().initFielders();
        }
        loadInitialFragment();
    }


    private void bindBallsRecyclerView() {
        List<Delivery> currentDeliveries = MatchStateManager.getInstance().getDeliveries();
        adapter = new OverBallsHorizontalAdapter(getActivity(), currentDeliveries);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ballsRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        ballsRecyclerView.setAdapter(adapter);
    }

    private void showBowlerSelectionDialog() {
        int overId = MatchStateManager.getInstance().getOverId();
        ArrayList<Player> bowlers = MatchStateManager.getInstance().getBowlersForNewOver();

        BowlerSelectionDialog bowlerSelectionDialog = new BowlerSelectionDialog(getActivity(), overId, bowlers);
        bowlerSelectionDialog.showDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scorer_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.end_inning:
                endInningAction();
                break;
            case R.id.end_match:
                endMatchAction();
                break;
            case R.id.menu_scoreCard:
                openScoreCard();
                break;
            case R.id.menu_conditions:
                openConditionsDialog();
                break;
            default:
                openMenuScoreWindow();
                break;
        }

        return true;
    }

    private void endInningAction() {
        Inning currentInning = MatchStateManager.getInstance().getCurrentInning();
        if (currentInning.getInningId().equalsIgnoreCase("1")) {
            showInningChangeDialog();
        } else {
            InsightsPreferences.saveMatchInProgress(getActivity(), false);
            showMatchEndDialog();
        }
    }

    private void endMatchAction() {
        InsightsPreferences.saveMatchInProgress(getActivity(), false);
        showMatchEndDialog();
    }

    private void openScoreCard() {
        Intent intent = new Intent(getActivity(), ScoreCardActivity.class);
        startActivity(intent);
    }

    private void openConditionsDialog() {
        MatchStateController controller = MatchStateManager.getInstance().getMatchStateController();
        PitchWeatherConditionsDialog conditionsDialog = new PitchWeatherConditionsDialog(getActivity(), controller.getCurrentMatchId(), controller.getCurrentInningId(), controller.getCurrentOverId());
        conditionsDialog.showDialog();
    }

    private void openMenuScoreWindow() {
        View windowView = View.inflate(getActivity(), R.layout.score_detail_window, null);
        new ViewScaleHandler(getActivity()).scaleRootView(windowView);

        TextView totalExtraValueView = (TextView) windowView.findViewById(R.id.totalExtraValueView);
        TextView headTextView = (TextView) windowView.findViewById(R.id.headTextView);
        TextView runRateValueView = (TextView) windowView.findViewById(R.id.runRateValueView);

        ImageView battingTeamImageView = (ImageView) windowView.findViewById(teamOneImageView);
        ImageView bowlingTeamImageView = (ImageView) windowView.findViewById(R.id.teamTwoImageView);
        TextView battingTeamNameView = (TextView) windowView.findViewById(R.id.teamOneNameView);
        TextView bowlingTeamNameView = (TextView) windowView.findViewById(R.id.teamTwoNameView);
        TextView scoreTextView = (TextView) windowView.findViewById(R.id.scoreTextView);
        TextView wicketsTextView = (TextView) windowView.findViewById(R.id.wicketsTextView);
        TextView overTextView = (TextView) windowView.findViewById(R.id.overTextView);
        TextView ballTextView = (TextView) windowView.findViewById(R.id.ballTextView);

        String inningTextValue = "1 <sup>st</sup> innings";
        if (MatchStateManager.getInstance().getCurrentInningId().equalsIgnoreCase("2")) {
            inningTextValue = "2 <sup>nd</sup> innings";
        }

        headTextView.setText(Html.fromHtml(inningTextValue));
        totalExtraValueView.setText("" + MatchStateManager.getInstance().getTotalExtras());
        runRateValueView.setText(MatchStateManager.getInstance().getOverAllRunRate());

        Team battingTeam = MatchStateManager.getInstance().getBattingTeam();
        Team bowlingTeam = MatchStateManager.getInstance().getBowlingTeam();

        if (!TextUtils.isEmpty(battingTeam.getTeamLogoUrl())) {
            Picasso.with(getActivity()).load(battingTeam.getTeamLogoUrl()).into(battingTeamImageView);
        }
        if (!TextUtils.isEmpty(bowlingTeam.getTeamLogoUrl())) {
            Picasso.with(getActivity()).load(bowlingTeam.getTeamLogoUrl()).into(bowlingTeamImageView);
        }

        battingTeamNameView.setText(battingTeam.getTeamName());
        bowlingTeamNameView.setText(bowlingTeam.getTeamName());

        scoreTextView.setText("" + MatchStateManager.getInstance().getTotalScore());
        wicketsTextView.setText("" + MatchStateManager.getInstance().getTotalWicket());
        overTextView.setText("" + (MatchStateManager.getInstance().getOverId()));

        int pastBallNumber = MatchStateManager.getInstance().getCurrentDelivery().getBallNumberOfOver() - 1;
        ballTextView.setText("" + pastBallNumber);


        PopupWindow scoreCardWindow = new PopupWindow(windowView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        scoreCardWindow.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 125);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnBowlerSelected event) {
        if (matchPermData.isField()) {
            if (MatchStateManager.getInstance().isBowlerSelected()) {
                MatchStateManager.getInstance().updateFielders(event.bowlerSelected.getPlayerId());
                MatchStateManager.getInstance().updateBowler(event.bowlerSelected.getPlayerId());
            } else {
                MatchStateManager.getInstance().updateBowler(event.bowlerSelected.getPlayerId());
                MatchStateManager.getInstance().initFielders();
            }
        } else {
            MatchStateManager.getInstance().updateBowler(event.bowlerSelected.getPlayerId());
        }
        loadInitialFragment();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPlayerOut onPlayerOutInfo) {
        nextBallOperation(onPlayerOutInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NextBallEvent event) {
        Delivery currentDelivery = MatchStateManager.getInstance().getCurrentDelivery();
        if (currentDelivery.isOutOnThisDelivery()) {
            onPlayerOutEvent();
        } else {
            nextBallOperation(null);
        }
    }

    private void nextBallOperation(OnPlayerOut onPlayerOut) {
        if (matchPermData.isScore()) {
            MatchStateManager.getInstance().doStatsCalculationsOnNextBall(getActivity(), onPlayerOut, matchPermData);
        } else {
            MatchStateManager.getInstance().doAnalystModeCalculations(getActivity(), matchPermData);
        }

        if (MatchStateManager.getInstance().isMatchEnd()) {
            InsightsPreferences.saveMatchInProgress(getActivity(), false);
            showMatchEndDialog();
            return;
        } else if (MatchStateManager.getInstance().isInningEnd()) {
            showInningChangeDialog();
            return;
        } else if (MatchStateManager.getInstance().isOverEnd()) {
            MatchStateManager.getInstance().jumpToNewOver();
            adapter.refreshDeliveries(MatchStateManager.getInstance().getDeliveries());
            showBowlerSelectionDialog();
        } else {
            MatchStateManager.getInstance().jumpToNewBall();
            loadFirstTabFragment();
            adapter.notifyDataSetChanged();
        }

    }

    private void onPlayerOutEvent() {
        ArrayList<Player> currentBattingPairPlayers = new ArrayList<>();
        currentBattingPairPlayers.add(MatchStateManager.getInstance().getStriker());
        currentBattingPairPlayers.add(MatchStateManager.getInstance().getNonStriker());

        ArrayList<Player> newBatsManOptionsList = MatchStateManager.getInstance().getNewBatsManOptions();
        ArrayList<Player> fieldersList = MatchStateManager.getInstance().getBowlersForNewOver();

        PlayerOutDialog playerOutDialog = new PlayerOutDialog(getActivity(), currentBattingPairPlayers, newBatsManOptionsList, fieldersList);
        playerOutDialog.showDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(InningsChanged inningsChanged) {
        Inning firstInning = MatchStateManager.getInstance().getCurrentInning();
        Inning secondInning = new Inning(firstInning.getMatchId(), "2", firstInning.getMatchOvers());
        secondInning.setTeams(firstInning.getBowlingTeam(), firstInning.getBattingTeam());

        MatchStateManager.getInstance().jumpToSecondInning();
        MatchStateController matchStateController = MatchStateManager.getInstance().getMatchStateController();
        matchStateController.setStrikerId(inningsChanged.striker.getPlayerId());
        matchStateController.setNonStrikerId(inningsChanged.nonStriker.getPlayerId());

        InsightsDbManager.saveInning(secondInning);
        MatchStateManager.getInstance().initInning(secondInning, matchStateController);
        adapter.refreshDeliveries(MatchStateManager.getInstance().getDeliveries());
        showBowlerSelectionDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPreviousDeliverySelected onPreviousDeliverySelected) {
        Delivery delivery = onPreviousDeliverySelected.selectedDelivery;
        if (delivery.getDeliveryId() == MatchStateManager.getInstance().getCurrentDeliveryId()) {
            selectedPrevDelivery = null;
        } else {
            selectedPrevDelivery = delivery;
        }
        loadFirstTabFragment();
    }

    private void loadFieldFragment() {
        FieldersPlacementFragment fragment = new FieldersPlacementFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(FieldersPlacementFragment.KEY_PREVIOUS_DELIVERY, selectedPrevDelivery);
            fragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, fragment).commit();
    }

    private void loadPitchFragment() {
        PitchFragment fragment = new PitchFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(PitchFragment.KEY_PREVIOUS_DELIVERY, selectedPrevDelivery);
            fragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, fragment).commit();
    }

    private void loadScorerFragment() {
        ScoreSectionFragment fragment = new ScoreSectionFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(ScoreSectionFragment.KEY_PREVIOUS_DELIVERY, selectedPrevDelivery);
            fragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, fragment).commit();
    }

    private void loadWheelFragment() {
        WagonWheelFragment fragment = new WagonWheelFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(WagonWheelFragment.KEY_DELIVERY, selectedPrevDelivery);
            fragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, fragment).commit();
    }

    private void loadInitialFragment() {

        if (!matchPermData.isField()) {
            fieldSectionTextView.setVisibility(View.GONE);
        } else {
            firstFragmentIndex = 1;
        }

        if (!matchPermData.isPitch()) {
            pitchSectionTextView.setVisibility(View.GONE);
        } else if (firstFragmentIndex == -1) {
            firstFragmentIndex = 2;
        }

        if (!matchPermData.isScore()) {
            scoreSectionTextView.setVisibility(View.GONE);
            wheelSectionTextView.setVisibility(View.GONE);
        } else if (firstFragmentIndex == -1) {
            firstFragmentIndex = 3;
        }


        loadFirstTabFragment();
    }

    private void loadFirstTabFragment() {
        loadFragment(firstFragmentIndex);
    }

    private void loadFragment(int selectedFragmentIndex) {
        unSelectAllSectionsHeaders();
        switch (selectedFragmentIndex) {
            case 1:
                selectFieldSection();
                loadFieldFragment();
                break;
            case 2:
                selectPitchSection();
                loadPitchFragment();
                break;
            case 3:
                selectScoreSection();
                loadScorerFragment();
                break;
            default:
                selectWagonWheelSection();
                loadWheelFragment();
                break;

        }

        String title = getTitleForCurrentStats();
        updateMainTitle(title);
    }


    @OnClick(R.id.fieldSectionAreaView)
    public void fieldSectionClicked() {
        loadFragment(1);
    }

    @OnClick(R.id.pitchSectionAreaView)
    public void pitchSectionClicked() {
        loadFragment(2);
    }

    @OnClick(R.id.runsSectionAreaView)
    public void runsSectionClicked() {
        loadFragment(3);
    }

    @OnClick(R.id.wheelSectionAreaView)
    public void wheelSectionClicked() {
        loadFragment(4);
    }

    private void selectFieldSection() {
        fieldSectionTextView.setBackgroundResource(R.drawable.selected_section_bg);
        fieldSectionTextView.setTextColor(Color.WHITE);
    }

    private void selectPitchSection() {
        pitchSectionTextView.setBackgroundResource(R.drawable.selected_section_bg);
        pitchSectionTextView.setTextColor(Color.WHITE);
    }

    private void selectScoreSection() {
        scoreSectionTextView.setBackgroundResource(R.drawable.selected_section_bg);
        scoreSectionTextView.setTextColor(Color.WHITE);
    }

    private void selectWagonWheelSection() {
        wheelSectionTextView.setBackgroundResource(R.drawable.selected_section_bg);
        wheelSectionTextView.setTextColor(Color.WHITE);
    }

    private void unSelectAllSectionsHeaders() {
        fieldSectionTextView.setBackgroundResource(R.drawable.unselected_section_bg);
        pitchSectionTextView.setBackgroundResource(R.drawable.unselected_section_bg);
        scoreSectionTextView.setBackgroundResource(R.drawable.unselected_section_bg);
        wheelSectionTextView.setBackgroundResource(R.drawable.unselected_section_bg);

        fieldSectionTextView.setTextColor(Color.parseColor("#AA000000"));
        pitchSectionTextView.setTextColor(Color.parseColor("#AA000000"));
        scoreSectionTextView.setTextColor(Color.parseColor("#AA000000"));
        wheelSectionTextView.setTextColor(Color.parseColor("#AA000000"));
    }


    private String getTitleForCurrentStats() {
        String titleRunsWickets = "" + MatchStateManager.getInstance().getTotalScore() + " / " + MatchStateManager.getInstance().getTotalWicket();
        int pastBall = MatchStateManager.getInstance().getCurrentDelivery().getBallNumberOfOver() - 1;
        String titleOversBalls = "" + (MatchStateManager.getInstance().getOverId()) + "." + (pastBall) + " overs";
        String title = titleRunsWickets + " - " + titleOversBalls;

        return title;
    }

    private void updateMainTitle(String title) {
        ((SuperMainActivity) getActivity()).updateTitle(title);
    }

    private void showMatchEndDialog() {
        MatchEndDialog matchEndDialog = new MatchEndDialog(getActivity());
        matchEndDialog.showDialog();
    }

    private void showInningChangeDialog() {
        Inning currentInning = MatchStateManager.getInstance().getCurrentInning();
        Team newBowlingTeam = currentInning.getBowlingTeam();
        SecondInningDialog secondInningDialog = new SecondInningDialog(getActivity(), newBowlingTeam.getSelectedPlayersOfTeam());
        secondInningDialog.showDialog();
    }


}
