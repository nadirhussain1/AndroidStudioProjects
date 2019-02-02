package com.gov.pitb.pcb.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.navigators.EventBusEvents.NextBallEvent;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.custom.wheels.ScorerWagonWheel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 07/06/2017.
 */

public class WagonWheelFragment extends Fragment {
    public static final String KEY_DELIVERY = "KEY_DELIVERY";

    @BindView(R.id.wagonWheelView)
    ScorerWagonWheel scorerWagonWheel;
    @BindView(R.id.nextBallButton)
    TextView nextBallButtonView;

    private Delivery previousDelivery;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wagon_wheel_layout, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            previousDelivery = (Delivery) getArguments().getSerializable(KEY_DELIVERY);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (previousDelivery != null) {
            nextBallButtonView.setVisibility(View.GONE);
            scorerWagonWheel.setDelivery(previousDelivery);
        } else {
            Delivery currentDelivery = MatchStateManager.getInstance().getCurrentDelivery();
            scorerWagonWheel.setDelivery(currentDelivery);
        }
    }

    @OnClick(R.id.nextBallButton)
    public void nextBallButtonPressed() {
        EventBus.getDefault().post(new NextBallEvent());
    }

}
