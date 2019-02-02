package com.gov.pitb.pcb.views.fragments.analyst;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.config.Team;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.db.dynamic.MatchStateController;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.InningsChanged;
import com.gov.pitb.pcb.navigators.EventBusEvents.NextBallEvent;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnBowlerSelected;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPreviousDeliverySelected;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.activities.ScoreCardActivity;
import com.gov.pitb.pcb.views.activities.SuperMainActivity;
import com.gov.pitb.pcb.views.adapters.OverBallsHorizontalAdapter;
import com.gov.pitb.pcb.views.dialogs.PitchWeatherConditionsDialog;
import com.gov.pitb.pcb.views.dialogs.main.BowlerSelectionDialog;
import com.gov.pitb.pcb.views.dialogs.main.MatchEndDialog;
import com.gov.pitb.pcb.views.dialogs.main.SecondInningDialog;
import com.gov.pitb.pcb.views.fragments.FieldersPlacementFragment;
import com.gov.pitb.pcb.views.fragments.PitchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 17/07/2017.
 */

public class AnalystMainFragment extends Fragment {
    View rootView;

    @BindView(R.id.overBallsRecyclerView)
    RecyclerView ballsRecyclerView;
    @BindView(R.id.pitchSectionAreaView)
    TextView pitchSectionAreaView;
    @BindView(R.id.fieldSectionAreaView)
    TextView fieldSectionAreaView;

    private Fragment currentFragment = null;
    private OverBallsHorizontalAdapter adapter;
    private Delivery selectedPrevDelivery = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.analyst_main_fragment, null);
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
        bindBallsRecyclerView();
        if (MatchStateManager.getInstance().isBowlerSelected()) {
            MatchStateManager.getInstance().initFielders();
            loadPitchFragment();
        } else {
            showBowlerSelectionDialog();
        }
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
        inflater.inflate(R.menu.analyst_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scoreCard:
                openScoreCard();
                break;
            case R.id.end_inning:
                endInningAction();
                break;
            case R.id.end_match:
                endMatchAction();
                break;
            default:
                openConditionsDialog();
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

    private void selectPitchArea() {
        pitchSectionAreaView.setBackgroundResource(R.drawable.selected_section_bg);
        pitchSectionAreaView.setTextColor(Color.WHITE);

        fieldSectionAreaView.setBackgroundResource(R.drawable.unselected_section_bg);
        fieldSectionAreaView.setTextColor(Color.parseColor("#AA000000"));
    }

    private void selectFieldArea() {
        fieldSectionAreaView.setBackgroundResource(R.drawable.selected_section_bg);
        fieldSectionAreaView.setTextColor(Color.WHITE);

        pitchSectionAreaView.setBackgroundResource(R.drawable.unselected_section_bg);
        pitchSectionAreaView.setTextColor(Color.parseColor("#AA000000"));
    }


    private void loadPitchFragment() {
        currentFragment = new PitchFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(PitchFragment.KEY_PREVIOUS_DELIVERY, selectedPrevDelivery);
            currentFragment.setArguments(arguments);
        }

        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        selectPitchArea();
        updateMainTitle();
    }

    private void loadFieldersFragment() {
        currentFragment = new FieldersPlacementFragment();
        if (selectedPrevDelivery != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(FieldersPlacementFragment.KEY_PREVIOUS_DELIVERY, selectedPrevDelivery);
            currentFragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction().replace(R.id.sectionFragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        selectFieldArea();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnBowlerSelected event) {
        if (MatchStateManager.getInstance().isBowlerSelected()) {
            MatchStateManager.getInstance().updateFielders(event.bowlerSelected.getPlayerId());
            MatchStateManager.getInstance().updateBowler(event.bowlerSelected.getPlayerId());
        } else {
            MatchStateManager.getInstance().updateBowler(event.bowlerSelected.getPlayerId());
            MatchStateManager.getInstance().initFielders();
        }
        loadPitchFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NextBallEvent event) {
       // MatchStateManager.getInstance().doAnalystModeCalculations(getActivity());
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
            loadPitchFragment();
            adapter.notifyDataSetChanged();
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(InningsChanged inningsChanged) {
        Inning firstInning = MatchStateManager.getInstance().getCurrentInning();
        Inning secondInning = new Inning(firstInning.getMatchId(), "2", firstInning.getMatchOvers());
        secondInning.setTeams(firstInning.getBowlingTeam(), firstInning.getBattingTeam());

        MatchStateManager.getInstance().jumpToSecondInning();
        MatchStateController matchStateController = MatchStateManager.getInstance().getMatchStateController();


        InsightsDbManager.saveInning(secondInning);
        MatchStateManager.getInstance().initInning(secondInning, matchStateController);

        adapter.refreshDeliveries(MatchStateManager.getInstance().getDeliveries());
        MatchStateManager.getInstance().updateBowler("");
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
        loadPitchFragment();
    }


    @OnClick(R.id.pitchSectionAreaView)
    public void pitchPageClicked() {
        loadPitchFragment();
    }

    @OnClick(R.id.fieldSectionAreaView)
    public void fielderPageClicked() {
        loadFieldersFragment();
    }


    private void updateMainTitle() {
        int pastBall = MatchStateManager.getInstance().getCurrentDelivery().getBallNumberOfOver() - 1;
        String titleOversBalls = "" + (MatchStateManager.getInstance().getOverId()) + "." + pastBall + " overs";
        ((SuperMainActivity) getActivity()).updateTitle(titleOversBalls);
    }


    private void showMatchEndDialog() {
        MatchEndDialog matchEndDialog = new MatchEndDialog(getActivity());
        matchEndDialog.showDialog();
    }

    private void showInningChangeDialog() {
        Inning currentInning = MatchStateManager.getInstance().getCurrentInning();
        Team battingTeam = currentInning.getBattingTeam();
        SecondInningDialog secondInningDialog = new SecondInningDialog(getActivity(), battingTeam.getSelectedPlayersOfTeam());
        secondInningDialog.showDialog();
    }

}
