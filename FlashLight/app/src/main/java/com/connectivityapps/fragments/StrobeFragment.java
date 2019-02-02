package com.connectivityapps.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.interfaces.TimerFinishInterface;
import com.connectivityapps.shared.CommonController;
import com.connectivityapps.shared.FlashConstants;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.shared.WarningController;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 26/02/2015.
 */


public class StrobeFragment extends Fragment {
    private View rootView = null;
    private TextView timeLeftTextView = null;
    private ImageView mainOnOffButton = null;
    public static boolean isInProgress = false;


    private int freqProgress = 5;
    private long totalCycleTime = FlashConstants.STROBE_SPEED_CYLE_TIME / freqProgress;
    private long onTime = totalCycleTime / 2;
    private StrobeCounter strobeCounter = null;
    private CommonController commonController = null;

    private int[] textViewIds = new int[]{R.id.frequencyTextView, R.id.minFreqTextView, R.id.midFreqTextView, R.id.maxFreqTextView};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.strobe_light_screen, null);
        ScalingUtility.getInstance(getActivity()).scaleView(rootView);
        initViewsAndApplyFont();
        commonController = new CommonController(rootView, getActivity(), timerFinishCallBack);

        if (SettingsController.getInstance().showWarningDialog) {
            new WarningController(getActivity()).showDialog();
        }

        isInProgress = !SettingsController.getInstance().isDefaultStateOn;
        if (GlobalUtil.camera != null && isFlashAvailable()) {
            GlobalUtil.camera.startPreview();
            controlLightState();
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        if (GlobalUtil.camera != null) {
            GlobalUtil.camera.stopPreview();
        }
        stopStrobeLight();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        // GlobalUtil.camera.stopPreview();
        super.onPause();
    }


    private void initViewsAndApplyFont() {
        timeLeftTextView = (TextView) rootView.findViewById(R.id.lightOffTimeLeftTextView);
        mainOnOffButton = (ImageView) rootView.findViewById(R.id.mainOnOffButton);
        FontsUtil.applyFontToText(getActivity(), timeLeftTextView, FontsUtil.ROBOTO_LIGHT);
        mainOnOffButton.setOnClickListener(strobeSwitchCliclListener);

        SeekBar frequencyBar = (SeekBar) rootView.findViewById(R.id.frequencySeekBar);
        frequencyBar.setOnSeekBarChangeListener(frequencyBarChangeListener);
        mainOnOffButton.setBackgroundResource(R.drawable.large_button_strobe);

        for (int count = 0; count < textViewIds.length; count++) {
            TextView textView = (TextView) rootView.findViewById(textViewIds[count]);
            FontsUtil.applyFontToText(getActivity(), textView, FontsUtil.ROBOTO_REGULAR);
        }
    }

    private View.OnClickListener strobeSwitchCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            controlLightState();
        }
    };

    private void controlLightState() {
        if (isInProgress) {
            stopStrobeLight();
        } else {
            startStrobeLight();
        }
    }

    private void startStrobeLight() {
        playSound();
        if (GlobalUtil.camera != null) {
            mainOnOffButton.setBackgroundResource(R.drawable.large_button_strobe_active);

            strobeCounter = new StrobeCounter(totalCycleTime, onTime);
            strobeCounter.start();

            timeLeftTextView.setVisibility(View.VISIBLE);
            commonController.startSwitchOffTimer();

            isInProgress = true;
        }
    }

    private void stopStrobeLight() {
        mainOnOffButton.setBackgroundResource(R.drawable.large_button_strobe);
        playSound();
        turnLightOff();
        timeLeftTextView.setVisibility(View.INVISIBLE);
        commonController.cancelSwitchOffTimer();

        if (strobeCounter != null) {
            strobeCounter.cancel();
            strobeCounter = null;
        }
        isInProgress = false;

    }

    private void turnLightOn() {
        try {
            if (GlobalUtil.camera != null) {
                GlobalUtil.params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
            }
        } catch (Exception exception) {

        }
    }

    private void turnLightOff() {
        try {
            if (GlobalUtil.camera != null) {
                GlobalUtil.params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
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

    private SeekBar.OnSeekBarChangeListener frequencyBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress == 0) {
                totalCycleTime = FlashConstants.STROBE_SPEED_CYLE_TIME;

            } else {
                freqProgress = progress;
                totalCycleTime = FlashConstants.STROBE_SPEED_CYLE_TIME / freqProgress;
            }
            onTime = totalCycleTime / 2;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private class StrobeCounter extends CountDownTimer {

        public StrobeCounter(long totalTime, long tickTime) {
            super(totalTime, tickTime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            turnLightOff();
        }

        @Override
        public void onFinish() {
            turnLightOn();
            strobeCounter = new StrobeCounter(totalCycleTime, onTime);
            strobeCounter.start();
        }

    }

    private TimerFinishInterface timerFinishCallBack = new TimerFinishInterface() {
        @Override
        public void timerFinished() {
            stopStrobeLight();
        }
    };


}

