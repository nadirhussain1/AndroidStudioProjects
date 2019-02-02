package com.connectivityapps.fragments;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.interfaces.TimerFinishInterface;
import com.connectivityapps.shared.CommonController;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 24/02/2015.
 */
public class FlashFragment extends Fragment {
    private View rootView = null;
    private TextView timeLeftTextView = null;
    private ImageView mainOnOffButton = null;
    public static boolean isOn = false;
    private CommonController commonController = null;
    NotificationManager notificationManager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.flashlight_screen, null);
        ScalingUtility.getInstance(getActivity()).scaleView(rootView);
        initViewsAndApplyFont();
        commonController = new CommonController(rootView, getActivity(), timerFinishCallBack);
        notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        isOn = SettingsController.getInstance().isDefaultStateOn;
        if (GlobalUtil.camera != null) {
            GlobalUtil.camera.startPreview();
            controlLightState();
        }
        return rootView;
    }

    private void initViewsAndApplyFont() {
        timeLeftTextView = (TextView) rootView.findViewById(R.id.lightOffTimeLeftTextView);
        mainOnOffButton = (ImageView) rootView.findViewById(R.id.mainOnOffButton);
        FontsUtil.applyFontToText(getActivity(), timeLeftTextView, FontsUtil.ROBOTO_LIGHT);
        mainOnOffButton.setOnClickListener(flashSwitchClickListener);
    }

    @Override
    public void onDestroy() {
        if (GlobalUtil.camera != null) {
            GlobalUtil.camera.stopPreview();
        }
        switchOffLight();
        super.onDestroy();
    }


    private View.OnClickListener flashSwitchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOn) {
                switchOffLight();
            } else {
                switchOnFlash();
            }
        }
    };

    private void controlLightState() {
        if (isFlashAvailable()) {
            if (isOn) {
                switchOnFlash();
            } else {
                switchOffLight();
            }
        }
    }

    private void switchOnFlash() {
        if (GlobalUtil.camera != null) {
            mainOnOffButton.setBackgroundResource(R.drawable.large_button_flashlight_active);

            commonController.startSwitchOffTimer();
            timeLeftTextView.setVisibility(View.VISIBLE);

            playSound();
            try {
                GlobalUtil.params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
                isOn = true;
            } catch (Exception exception) {

            }
        }
    }

    private void switchOffLight() {
        mainOnOffButton.setBackgroundResource(R.drawable.large_button_flashlight);
        timeLeftTextView.setVisibility(View.INVISIBLE);
        commonController.cancelSwitchOffTimer();

        playSound();
        try {
            if (GlobalUtil.camera != null) {
                GlobalUtil.params.setFlashMode(Parameters.FLASH_MODE_OFF);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
                isOn = false;
            }
        } catch (Exception exception) {

        }
    }

    private boolean isFlashAvailable() {
        boolean hasFlash = getActivity().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setTitle("Error");
            alertBuilder.setMessage("Sorry, your device doesn't support flash light!");
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertBuilder.create().show();
        }
        return hasFlash;
    }


    private void playSound() {
        ((MainActivity) getActivity()).playSound();
    }

    private TimerFinishInterface timerFinishCallBack = new TimerFinishInterface() {
        @Override
        public void timerFinished() {
            switchOffLight();
        }
    };

}
