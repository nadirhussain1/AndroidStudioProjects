package com.connectivityapps.shared;

import android.content.Context;
import android.hardware.Camera;
import android.os.CountDownTimer;

import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.fragments.SosFragment;
import com.connectivityapps.utils.GlobalUtil;

/**
 * Created by nadirhussain on 05/03/2015.
 */
public class SosController {
    private int currentIndex = 0;
    private Context mContext = null;
    private boolean isInProgress = false;
    private int defaultOffBrightness = 0;
    private SosTimer sosTimer = null;
    private static SosController sosController = null;
    private boolean isOnTurn;

    public static SosController getInstance(Context context) {
        if (sosController == null) {
            sosController = new SosController(context);
        }
        return sosController;
    }

    public void changeContext(Context context) {
        mContext = context;
    }

    private SosController(Context context) {
        this.mContext = context;
        try {
            defaultOffBrightness = android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception exception) {

        }
    }

    public void startSos() {
        if (currentIndex >= 9 && SettingsController.getInstance().isSosRepeatEnabled) {
            try {
                Thread.sleep(1000);
            }catch(InterruptedException exception){

            }
            currentIndex = 0;
        }
        if (currentIndex < 9) {
            if (SettingsController.getInstance().sosLightToFlashIndex == 2) {
                turnOnLed();
                turnOffScreen();
            } else {

                if (SettingsController.getInstance().sosLightToFlashIndex == 0) {
                    turnOnLed();
                    turnOnScreen();
                } else {
                    turnOffLed();
                    turnOnScreen();
                }
            }
            long onSosTime = SosFragment.onTime;
            if (currentIndex >= 3 && currentIndex <= 5) {
                onSosTime = 3 * SosFragment.onTime;
            }
            isOnTurn=true;
            sosTimer = new SosTimer(onSosTime, 500);
            sosTimer.start();
        } else {
            stopSos();
        }
    }

    public void stopSos() {
        isInProgress = false;
        SosFragment.mainOnOffButton.setBackgroundResource(R.drawable.large_button_alert);
        SosFragment.sosStatusIconView.setBackgroundResource(R.drawable.braile_sos);

        turnOffLed();
        if (sosTimer != null) {
            sosTimer.cancel();
            sosTimer = null;
        }
        currentIndex = 0;
        playSound();
    }

    private void playSound() {
        MainActivity.playSound();
    }

    private void turnOnLed() {
        try {
            if (GlobalUtil.camera != null) {
                GlobalUtil.params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
            }
        }catch(Exception exception){

        }
    }

    private void turnOffLed() {
        try {
            if (GlobalUtil.camera != null) {
                GlobalUtil.params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                GlobalUtil.camera.setParameters(GlobalUtil.params);
            }
        }catch(Exception exception){

        }
    }

    private void turnOnScreen() {
//        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes(); // Get Params
//        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL; // Set Value
//        ((Activity) mContext).getWindow().setAttributes(layoutParams);
    }

    private void turnOffScreen() {
//        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes(); // Get Params
//        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF; // Set Value
//        ((Activity) mContext).getWindow().setAttributes(layoutParams);
    }

    private class SosTimer extends CountDownTimer {
        private SosTimer(long totalTime, long tickTime) {
            super(totalTime, tickTime);
            SosFragment.mainOnOffButton.setBackgroundResource(R.drawable.large_button_alert_active);
            int resId = GlobalUtil.getDrawableId(mContext, "braile_sos_active_" + (currentIndex + 1));
            SosFragment.sosStatusIconView.setBackgroundResource(resId);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if(isOnTurn){
                isOnTurn=false;
                turnOffLed();
                turnOffScreen();
                sosTimer=new SosTimer(SosFragment.onTime,500);
                sosTimer.start();
            }else {
                currentIndex++;
                startSos();
            }
        }
    }
}
