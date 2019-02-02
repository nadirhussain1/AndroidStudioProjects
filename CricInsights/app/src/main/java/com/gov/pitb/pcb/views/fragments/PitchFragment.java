package com.gov.pitb.pcb.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.CommentsAdded;
import com.gov.pitb.pcb.navigators.EventBusEvents.NextBallEvent;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.custom.wheels.PitchSurface;
import com.gov.pitb.pcb.views.dialogs.main.CommentsDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 30/05/2017.
 */

public class PitchFragment extends Fragment {
    public static final String KEY_PREVIOUS_DELIVERY = "KEY_PREVIOUS_DELIVERY";
    private static final String AROUND_THE_WICKET = "AROUND";
    private static final String OVER_THE_WICKET = "OVER";

    View rootView;
    @BindView(R.id.pitchSurfaceView)
    PitchSurface pitchSurfaceView;
    @BindView(R.id.oTheWicketView)
    TextView oTheWicketTextView;
    @BindView(R.id.aTheWicketView)
    TextView aTheWicketTextView;
    @BindView(R.id.extraCheckBox)
    CheckBox extraCheckBox;
    @BindView(R.id.nextBallButton)
    TextView nextBallButtonView;


    private boolean isOverTheWicketSelected = true;
    private Delivery currentDelivery;
    private boolean isInEditMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.analyst_pitch_fragment, null);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInEditMode) {
            currentDelivery = MatchStateManager.getInstance().getCurrentDelivery();
            currentDelivery.setUnderOverTheWicket(OVER_THE_WICKET);
        } else {
            prePopulateUnderOverWicket();
            if (!currentDelivery.isValidBall()) {
                extraCheckBox.setChecked(true);
            }
        }
        Player striker = MatchStateManager.getInstance().getStriker();
        pitchSurfaceView.setDelivery(currentDelivery, striker.getPlayerBattingStyle());

        MatchPermData matchPermData = InsightsDbManager.getMatchPermData();
        if (matchPermData.isScore()) {
            extraCheckBox.setVisibility(View.GONE);
            nextBallButtonView.setVisibility(View.GONE);
        }
    }

    private void prePopulateUnderOverWicket() {
        if (currentDelivery.getUnderOverTheWicket().equals(OVER_THE_WICKET)) {
            selectOverTheWicket();
        } else {
            selectAroundTheWicket();
        }
    }

    @OnClick(R.id.oTheWicketView)
    public void oTheWicketClicked() {
        if (!isOverTheWicketSelected) {
            selectOverTheWicket();
        }

    }


    @OnClick(R.id.aTheWicketView)
    public void aTheWicketClicked() {
        if (isOverTheWicketSelected) {
            selectAroundTheWicket();
        }
    }

    private void selectOverTheWicket() {
        isOverTheWicketSelected = true;
        oTheWicketTextView.setBackgroundResource(R.drawable.selected_section_bg);
        oTheWicketTextView.setTextColor(Color.WHITE);

        aTheWicketTextView.setBackgroundResource(R.drawable.unselected_section_bg);
        aTheWicketTextView.setTextColor(Color.parseColor("#AA000000"));

        currentDelivery.setUnderOverTheWicket(OVER_THE_WICKET);
    }

    private void selectAroundTheWicket() {
        isOverTheWicketSelected = false;

        aTheWicketTextView.setBackgroundResource(R.drawable.selected_section_bg);
        aTheWicketTextView.setTextColor(Color.WHITE);

        oTheWicketTextView.setBackgroundResource(R.drawable.unselected_section_bg);
        oTheWicketTextView.setTextColor(Color.parseColor("#AA000000"));


        currentDelivery.setUnderOverTheWicket(AROUND_THE_WICKET);
    }


    @OnClick(R.id.commentActionButton)
    public void commentsActionButtonClicked() {
        CommentsDialog commentsDialog = new CommentsDialog(getActivity(), currentDelivery);
        commentsDialog.showDialog();
    }

    @OnClick(R.id.extraCheckBox)
    public void extraCheckBoxClicked() {
        if (extraCheckBox.isChecked()) {
            currentDelivery.setWideType("EXTRA");
        } else {
            currentDelivery.setWideType("");
        }
    }

    @OnClick(R.id.nextBallButton)
    public void nextBallButtonClicked() {
        EventBus.getDefault().post(new NextBallEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CommentsAdded event) {
        currentDelivery.setComments(event.commentsText);
    }


}
