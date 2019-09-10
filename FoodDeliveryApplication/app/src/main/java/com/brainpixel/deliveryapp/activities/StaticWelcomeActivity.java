package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 15/07/2018.
 */

public class StaticWelcomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.number_input_static_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.enterNumberView)
    public void onEnterAreaClick() {
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        Intent intent = new Intent(this, NumberEditorActivity.class);
        startActivity(intent);
    }
}
