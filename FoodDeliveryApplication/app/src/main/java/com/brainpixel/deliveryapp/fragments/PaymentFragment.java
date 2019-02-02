package com.brainpixel.deliveryapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.GoToShipmentTabEvent;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 21/11/2018.
 */

public class PaymentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.nextTextView)
    public void onNextClick() {
        EventBus.getDefault().post(new GoToShipmentTabEvent());
    }


}
