package com.gov.pitb.pcb.views.fragments;

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
import com.gov.pitb.pcb.data.db.dynamic.FielderPosition;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.data.server.players.Fielder;
import com.gov.pitb.pcb.navigators.EventBusEvents.NextBallEvent;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.custom.wheels.FielderWagonWheelView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 17/07/2017.
 */

public class FieldersPlacementFragment extends Fragment {
    public static final String KEY_PREVIOUS_DELIVERY = "KEY_PREVIOUS_DELIVERY";
    @BindView(R.id.wagonWheelView)
    FielderWagonWheelView fieldWagonWheelView;
    @BindView(R.id.nextBallButton)
    TextView nextBallButtonView;
    @BindView(R.id.extraCheckBox)
    CheckBox extraCheckBox;

    private Delivery currentDelivery;
    private boolean isInEditMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fielders_fragment, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            currentDelivery = (Delivery) getArguments().getSerializable(KEY_PREVIOUS_DELIVERY);
            isInEditMode = true;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isInEditMode) {
            nextBallButtonView.setVisibility(View.GONE);
            List<Fielder> fieldersList = getDeliveryFielders();
            fieldWagonWheelView.showFieldersOnField(fieldersList);
        } else {
            currentDelivery = MatchStateManager.getInstance().getCurrentDelivery();
            fieldWagonWheelView.showFieldersOnField(MatchStateManager.getInstance().fieldersList);
        }

        MatchPermData matchPermData = InsightsDbManager.getMatchPermData();
        if (matchPermData.isScore() || matchPermData.isPitch()) {
            extraCheckBox.setVisibility(View.GONE);
            nextBallButtonView.setVisibility(View.GONE);
        }
    }

    private List<Fielder> getDeliveryFielders() {
        long fielderPosTableId = currentDelivery.getFielderPosId();
        FielderPosition fielderPosition = InsightsDbManager.getFielderPosition(fielderPosTableId);
        String positionsJson = fielderPosition.getPositions();
        return FielderPosition.decodeFielderJsonString(positionsJson);
    }

    @OnClick(R.id.nextBallButton)
    public void nextBallButtonPressed() {
        EventBus.getDefault().post(new NextBallEvent());
    }

    @OnClick(R.id.extraCheckBox)
    public void extraCheckClicked() {
        if (extraCheckBox.isChecked()) {
            currentDelivery.setWideType("EXTRA");
        } else {
            currentDelivery.setWideType("");
        }
    }
}
