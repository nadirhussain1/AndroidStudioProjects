package com.gov.pitb.pcb.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.MatchCurrentState;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnDotBallTypeSpecified;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnNoBallTypeSpecified;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnShotTypeSpecified;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnWideTypeSpecified;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.popups.DotBallTypePopUp;
import com.gov.pitb.pcb.views.popups.NoBallTypePopUp;
import com.gov.pitb.pcb.views.popups.ShotTypePopUp;
import com.gov.pitb.pcb.views.popups.WideBallTypePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 30/05/2017.
 */

public class ScoreSectionFragment extends Fragment {
    public static final String KEY_PREVIOUS_DELIVERY = "KEY_PREVIOUS_DELIVERY";

    @BindView(R.id.strikerNameView)
    TextView strikerNameView;
    @BindView(R.id.nonStrikerNameView)
    TextView nonStrikerNameView;
    @BindView(R.id.bowlerNameView)
    TextView bowlerNameView;
    @BindView(R.id.strikerStatsView)
    TextView strikerStatsView;
    @BindView(R.id.nonStrikerStatsView)
    TextView nonStrikerStatsView;
    @BindView(R.id.ballOverView)
    TextView bowlerStatsView;

    @BindView(R.id.legByeButton)
    TextView legByeButton;
    @BindView(R.id.ByeButton)
    TextView ByeButton;
    @BindView(R.id.wideButton)
    TextView wideButton;
    @BindView(R.id.NoBallButton)
    TextView noBallButton;

    @BindView(R.id.dotBallTextView)
    TextView dotBallTexView;
    @BindView(R.id.oneBallTextView)
    TextView oneBallTextView;
    @BindView(R.id.twoBallTextView)
    TextView twoBallTextView;
    @BindView(R.id.threeBallTextView)
    TextView threeBallTextView;
    @BindView(R.id.fourBallTextView)
    TextView fourBallTextView;
    @BindView(R.id.fiveBallTextView)
    TextView fiveBallTextView;
    @BindView(R.id.sixBallTextView)
    TextView sixBallTextView;

    @BindView(R.id.wicketButton)
    TextView wicketButton;

    private Delivery currentDelivery;
    private boolean isInEditMode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.runs_fragment, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            currentDelivery = (Delivery) getArguments().getSerializable(KEY_PREVIOUS_DELIVERY);
            isInEditMode = true;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInEditMode) {
            currentDelivery = MatchStateManager.getInstance().getCurrentDelivery();
        }
        bindValuesToViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void bindValuesToViews() {
        Player strikerPlayer;
        Player nonStrikerPlayer;
        Player bowlerPlayer;
        String strikerStatsValue;
        String nonStrikerStats;
        String bowlerStats;

        if (!isInEditMode) {
            strikerPlayer = MatchStateManager.getInstance().getStriker();
            nonStrikerPlayer = MatchStateManager.getInstance().getNonStriker();
            bowlerPlayer = MatchStateManager.getInstance().getBowler();

            MatchCurrentState matchCurrentState = MatchStateManager.getInstance().getMatchCurrentState();

            strikerStatsValue = "" + matchCurrentState.getStrikerRuns() + " (" + matchCurrentState.getStrikerTotalBalls() + ")";
            nonStrikerStats = "" + matchCurrentState.getNonStrikerRuns() + " (" + matchCurrentState.getNonStrikerTotalBalls() + ")";
            bowlerStats = "" + matchCurrentState.getBowlerOversDone() + "." + matchCurrentState.getBowlerBallsDone();
        } else {
            strikerPlayer = InsightsDbManager.getPlayer(currentDelivery.getStrikerId());
            nonStrikerPlayer = InsightsDbManager.getPlayer(currentDelivery.getNonStrikerId());
            bowlerPlayer = InsightsDbManager.getPlayer(currentDelivery.getBowlerId());

            List<Delivery> strikerDeliveries = InsightsDbManager.getBatsmanAllIncludingByeDeliveries(strikerPlayer.getPlayerId());
            strikerStatsValue = getBatsManValidStats(strikerDeliveries, currentDelivery);

            List<Delivery> nonStrikerDeliveries = InsightsDbManager.getBatsmanAllIncludingByeDeliveries(nonStrikerPlayer.getPlayerId());
            nonStrikerStats = getBatsManValidStats(nonStrikerDeliveries, currentDelivery);

            MatchCurrentState matchCurrentState = MatchStateManager.getInstance().getMatchCurrentState();
            int pastBallNumber = currentDelivery.getBallNumberOfOver() - 1;
            bowlerStats = "" + matchCurrentState.getBowlerOversDone() + "." + pastBallNumber;

            populateExtrasViews(currentDelivery);
            populateScoreImage(currentDelivery);
        }

        strikerNameView.setText(strikerPlayer.getPlayerName());
        nonStrikerNameView.setText(nonStrikerPlayer.getPlayerName());
        bowlerNameView.setText(bowlerPlayer.getPlayerName());

        strikerStatsView.setText(strikerStatsValue);
        nonStrikerStatsView.setText(nonStrikerStats);
        bowlerStatsView.setText(bowlerStats);


    }

    private void populateExtrasViews(Delivery delivery) {
        if (delivery.isBye()) {
            selectByeButton();
        } else if (delivery.isLegBye()) {
            selectLegByeButton();
        }

        if (!TextUtils.isEmpty(delivery.getWideType())) {
            selectWide();
        } else if (!TextUtils.isEmpty(delivery.getNoType())) {
            markNoBall();
        }
    }

    private void populateScoreImage(Delivery delivery) {
        switch (delivery.getRuns()) {
            case 1:
                selectSingleScoreImage();
                break;
            case 2:
                selectDoubleScoreImage();
                break;
            case 3:
                selectTripleScoreImage();
                break;
            case 4:
                selectFourScoreImage();
                break;
            case 5:
                selectFiveScoreImage();
                break;
            case 6:
                selectSixScoreImage();
                break;
            default:
                selectDotBallScoreImage();

        }
    }


    @OnClick(R.id.legByeButton)
    public void legByeClicked() {
        if (!legByeButton.isSelected() && !wideButton.isSelected()) {
            selectLegByeButton();
        } else if (legByeButton.isSelected()) {
            unSelectLegBye();
        }
    }

    private void selectLegByeButton() {
        legByeButton.setSelected(true);
        ByeButton.setSelected(false);
        currentDelivery.selectLegBye();
        changeBackgroundOfScoreDigits();
    }

    private void unSelectLegBye() {
        legByeButton.setSelected(false);
        currentDelivery.unSelectLegBye();
        changeBackgroundOfScoreDigits();
    }

    @OnClick(R.id.ByeButton)
    public void byeButtonClicked() {
        if (ByeButton.isSelected()) {
            unSelectBye();
        } else {
            selectByeButton();
        }
    }

    private void selectByeButton() {
        ByeButton.setSelected(true);
        legByeButton.setSelected(false);
        currentDelivery.selectBye();
        changeBackgroundOfScoreDigits();
    }

    private void unSelectBye() {
        ByeButton.setSelected(false);
        currentDelivery.unSelectBye();
        changeBackgroundOfScoreDigits();
        if (wideButton.isSelected()) {
            resetColorOfAllRunsButtons();
        }
    }

    @OnClick(R.id.wideButton)
    public void wideClicked() {
        if (!wideButton.isSelected()) {
            if (!ByeButton.isSelected()) {
                resetColorOfAllRunsButtons();
            }
            showWideOptionsPopUp(wideButton);
            selectWide();
        } else {
            unSelectWide();
        }

    }

    private void selectWide() {
        String text = "WIDE  <sup><font color='red'>+1</font></sup>";
        wideButton.setText(Html.fromHtml(text));
        wideButton.setSelected(true);
        unSelectLegBye();
        undoNoBall();
        unDoWicket();
    }

    private void unSelectWide() {
        wideButton.setText("WIDE");
        wideButton.setSelected(false);
        currentDelivery.unSetWideBall();
    }

    private void unDoWicket() {
        wicketButton.setSelected(false);
        currentDelivery.setOutOnThisDelivery(false);
    }

    @OnClick(R.id.NoBallButton)
    public void noBallClicked() {
        if (!noBallButton.isSelected()) {
            showNoBallOptionsPopUp(noBallButton);
            markNoBall();
        } else {
            undoNoBall();
        }
    }

    private void markNoBall() {
        String text = "NO BALL  <sup><font color='red'>+1</font></sup>";
        noBallButton.setText(Html.fromHtml(text));
        noBallButton.setSelected(true);

        unSelectWide();
        wicketButton.setSelected(false);
        currentDelivery.setOutOnThisDelivery(false);
    }

    private void undoNoBall() {
        noBallButton.setText("NO BALL");
        noBallButton.setSelected(false);
        currentDelivery.unSetNoBall();
    }

    private boolean canClickDigit() {
        if (wideButton.isSelected() && !ByeButton.isSelected()) {
            return false;
        }
        return true;
    }

    @OnClick(R.id.dotBallTextView)
    public void dotBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectDotBallScoreImage();
            if (!isByeOrLegBye()) {
                showDotBallOptions(dotBallTexView);
            }
        }
    }

    private void selectDotBallScoreImage() {
        currentDelivery.setRuns(0);
        dotBallTexView.setSelected(true);
    }

    private boolean isByeOrLegBye() {
        return ByeButton.isSelected() || legByeButton.isSelected();
    }

    @OnClick(R.id.oneBallTextView)
    public void oneBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectSingleScoreImage();
            if (!isByeOrLegBye()) {
                showShotTypeOptions(oneBallTextView, false);
            }
        }
    }

    private void selectSingleScoreImage() {
        currentDelivery.setRuns(1);
        oneBallTextView.setSelected(true);
    }

    @OnClick(R.id.twoBallTextView)
    public void twoBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectDoubleScoreImage();
            if (!isByeOrLegBye()) {
                showShotTypeOptions(twoBallTextView, false);
            }
        }
    }

    private void selectDoubleScoreImage() {
        currentDelivery.setRuns(2);
        twoBallTextView.setSelected(true);
    }

    @OnClick(R.id.threeBallTextView)
    public void threeBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectTripleScoreImage();
            if (!isByeOrLegBye()) {
                showShotTypeOptions(threeBallTextView, false);
            }
        }
    }

    private void selectTripleScoreImage() {
        currentDelivery.setRuns(3);
        threeBallTextView.setSelected(true);
    }

    @OnClick(R.id.fourBallTextView)
    public void fourBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectFourScoreImage();
            if (!isByeOrLegBye()) {
                showShotTypeOptions(fourBallTextView, false);
            }
        }
    }

    private void selectFourScoreImage() {
        currentDelivery.setRuns(4);
        fourBallTextView.setSelected(true);
    }

    @OnClick(R.id.fiveBallTextView)
    public void fiveBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectFiveScoreImage();
        }
    }

    private void selectFiveScoreImage() {
        currentDelivery.setRuns(5);
        fiveBallTextView.setSelected(true);
    }

    @OnClick(R.id.sixBallTextView)
    public void sixBallClicked() {
        if (canClickDigit()) {
            resetColorOfAllRunsButtons();
            selectSixScoreImage();
            if (!isByeOrLegBye()) {
                showShotTypeOptions(sixBallTextView, true);
            }

        }
    }

    private void selectSixScoreImage() {
        currentDelivery.setRuns(6);
        sixBallTextView.setSelected(true);
    }

    @OnClick(R.id.wicketButton)
    public void wicketLayoutClicked() {
        if (!wideButton.isSelected() && !noBallButton.isSelected() && !isInEditMode) {
            wicketButton.setSelected(!wicketButton.isSelected());
            currentDelivery.setOutOnThisDelivery(wicketButton.isSelected());
        }
    }



    private void changeBackgroundOfScoreDigits() {
        if (legByeButton.isSelected() || ByeButton.isSelected()) {
            dotBallTexView.setBackgroundResource(R.drawable.extra_score_digit_selector);
            oneBallTextView.setBackgroundResource(R.drawable.extra_score_digit_selector);
            twoBallTextView.setBackgroundResource(R.drawable.extra_score_digit_selector);
            threeBallTextView.setBackgroundResource(R.drawable.extra_score_digit_selector);
            fourBallTextView.setBackgroundResource(R.drawable.extra_score_digit_selector);
            sixBallTextView.setBackgroundResource(R.drawable.extra_score_digit_selector);

            dotBallTexView.setTextColor(Color.BLACK);
            oneBallTextView.setTextColor(Color.BLACK);
            twoBallTextView.setTextColor(Color.BLACK);
            threeBallTextView.setTextColor(Color.BLACK);
            fourBallTextView.setTextColor(Color.BLACK);
            sixBallTextView.setTextColor(Color.BLACK);

        } else {
            dotBallTexView.setBackgroundResource(R.drawable.bat_score_digit_selector);
            oneBallTextView.setBackgroundResource(R.drawable.bat_score_digit_selector);
            twoBallTextView.setBackgroundResource(R.drawable.bat_score_digit_selector);
            threeBallTextView.setBackgroundResource(R.drawable.bat_score_digit_selector);
            fourBallTextView.setBackgroundResource(R.drawable.bat_score_digit_selector);
            sixBallTextView.setBackgroundResource(R.drawable.bat_score_digit_selector);

            dotBallTexView.setTextColor(Color.WHITE);
            oneBallTextView.setTextColor(Color.WHITE);
            twoBallTextView.setTextColor(Color.WHITE);
            threeBallTextView.setTextColor(Color.WHITE);
            fourBallTextView.setTextColor(Color.WHITE);
            sixBallTextView.setTextColor(Color.WHITE);
        }
    }

    private void resetColorOfAllRunsButtons() {
        currentDelivery.setRuns(0);
        dotBallTexView.setSelected(false);
        oneBallTextView.setSelected(false);
        twoBallTextView.setSelected(false);
        threeBallTextView.setSelected(false);
        fourBallTextView.setSelected(false);
        fiveBallTextView.setSelected(false);
        sixBallTextView.setSelected(false);
    }

    private void showShotTypeOptions(View anchorView, boolean isSix) {
        ShotTypePopUp shotTypePopUp = new ShotTypePopUp(getActivity(), isSix, currentDelivery.getShotType());
        shotTypePopUp.show(anchorView);
    }

    private void showDotBallOptions(View anchorView) {
        DotBallTypePopUp dotBallTypePopUp = new DotBallTypePopUp(getActivity(), currentDelivery.getShotType());
        dotBallTypePopUp.show(anchorView);
    }

    private void showWideOptionsPopUp(View anchorView) {
        WideBallTypePopup wideBallTypePopup = new WideBallTypePopup(getActivity(), currentDelivery.getWideType());
        wideBallTypePopup.show(anchorView);
    }

    private void showNoBallOptionsPopUp(View anchorView) {
        NoBallTypePopUp noBallTypePopUp = new NoBallTypePopUp(getActivity(), currentDelivery.getNoType());
        noBallTypePopUp.show(anchorView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnShotTypeSpecified event) {
        currentDelivery.setShotType(event.shotType);
        if (TextUtils.isEmpty(event.shotType)) {
            resetColorOfAllRunsButtons();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnNoBallTypeSpecified event) {
        if (TextUtils.isEmpty(event.type)) {
            undoNoBall();
        }
        currentDelivery.setNoBall(event.type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnWideTypeSpecified event) {
        if (TextUtils.isEmpty(event.type)) {
            unSelectWide();
        } else {
            currentDelivery.setWideType(event.type);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnDotBallTypeSpecified event) {
        currentDelivery.setShotType(event.type);
        if (TextUtils.isEmpty(event.type)) {
            resetColorOfAllRunsButtons();
        }
    }


    private String getBatsManValidStats(List<Delivery> deliveries, Delivery currentDelivery) {
        int score = 0;
        int count = 0;
        for (Delivery delivery : deliveries) {
            if (!delivery.isBye() && !delivery.isLegBye() && (delivery.getBallNumberOfOver() <= currentDelivery.getBallNumberOfOver())) {
                score += delivery.getRuns();
                count++;
            }
        }
        String statsText = "" + score + " (" + count + ")";
        return statsText;
    }


}
