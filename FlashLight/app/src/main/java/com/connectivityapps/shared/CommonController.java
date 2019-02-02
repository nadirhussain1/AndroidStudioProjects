package com.connectivityapps.shared;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.interfaces.TimerFinishInterface;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;
import com.flurry.android.FlurryAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nadirhussain on 03/03/2015.
 */
public class CommonController {

    private TextView remainingTimeView = null;
    private ImageView soundImageView = null;
    private ImageView photoCaptureView = null;
    private TimerFinishInterface switchOffCallBack = null;
    private SwitchOffTimer timer = null;
    private Context mContext = null;
    private long timeToSwitchOff;
    private boolean isSafeToTakePicture = true;
    private Dialog thanksDialog = null;
    private Dialog lightOffAlertDialog = null;
    private String logFile="";

    public CommonController(View commonView, Context context, TimerFinishInterface callBack) {
        mContext = context;
        switchOffCallBack = callBack;
        initViews(commonView);
        applyClicks();

    }

    public void startSwitchOffTimer() {
        cancelSwitchOffTimer();
        timeToSwitchOff = getSwitchOffTime();
        if (timeToSwitchOff != -1) {
            timeToSwitchOff = timeToSwitchOff * 60 * 1000;
            timer = new SwitchOffTimer(timeToSwitchOff, 1000);
            timer.start();
        }
    }

    private int getSwitchOffTime() {
        switch (SettingsController.getInstance().switchOffLightTimeIndex) {
            case 0:
                return 3;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 20;
            case 4:
                return 30;
            case 5:
                remainingTimeView.setVisibility(View.INVISIBLE);
                return -1;

        }
        return -1;
    }

    public void cancelSwitchOffTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setTimerCallBack(TimerFinishInterface callBack) {
        switchOffCallBack = callBack;
    }


    private void initViews(View view) {
        remainingTimeView = (TextView) view.findViewById(R.id.lightOffTimeLeftTextView);
        MainActivity.batteryLevelIconView = (ImageView) view.findViewById(R.id.batteryLevelIconView);
        MainActivity.batteryLevelTextView = (TextView) view.findViewById(R.id.batteryLevelTextView);
        soundImageView = (ImageView) view.findViewById(R.id.soundIconView);
        photoCaptureView = (ImageView) view.findViewById(R.id.photoCaptureView);

        FontsUtil.applyFontToText(mContext, MainActivity.batteryLevelTextView, FontsUtil.ROBOTO_THIN);

        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            photoCaptureView.setVisibility(View.INVISIBLE);
        }

        if (!SettingsController.getInstance().isShowSoundSwitchEnabled) {
            soundImageView.setVisibility(View.INVISIBLE);
        } else {
            soundImageView.setVisibility(View.VISIBLE);
            if (SettingsController.getInstance().isSoundEnabled) {
                soundImageView.setBackgroundResource(R.drawable.sound_on_selector);
            } else {
                soundImageView.setBackgroundResource(R.drawable.no_sound_selector);
            }
        }
        GlobalUtil.updateBatteryStatus(mContext);
    }

    private void applyClicks() {
        soundImageView.setOnClickListener(soundSettingsClickListener);
        if(GlobalUtil.isCameraFeatEnabled) {
            photoCaptureView.setOnClickListener(photoCaptureClickListener);
        }
    }

    private View.OnClickListener soundSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SettingsController.getInstance().isSoundEnabled = !SettingsController.getInstance().isSoundEnabled;
            if (SettingsController.getInstance().isSoundEnabled) {
                soundImageView.setBackgroundResource(R.drawable.sound_on_selector);
            } else {
                soundImageView.setBackgroundResource(R.drawable.no_sound_selector);
            }
            FlashPreferences.getInstance(mContext).changeSoundSettings();
        }
    };
    private View.OnClickListener photoCaptureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logFile=" Capture Button clicked=";
            logFile=logFile+" CameraNull="+(GlobalUtil.camera != null)+"  isSafe="+isSafeToTakePicture+" isReady="+GlobalUtil.isCameraReady;
            if (isSafeToTakePicture && GlobalUtil.camera != null && GlobalUtil.isCameraReady) {
                isSafeToTakePicture = false;

                FlurryAgent.logEvent("Photo action performed");
                GlobalUtil.camera.startPreview();
                MainActivity.playSound();
                System.gc();

                GlobalUtil.camera.takePicture(null, null, pictureCallBack);
            }
            GlobalUtil.saveLogsToSdCard("camera.txt",logFile);

        }
    };


    private Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFileDir = getDir();
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                Toast.makeText(mContext,mContext.getString(R.string.directory_create_failed), Toast.LENGTH_LONG).show();
                isSafeToTakePicture = true;
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile =mContext.getString(R.string.image_name_prefix)+ "_" + date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;
            File pictureFile = new File(filename);
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                Toast.makeText(mContext, mContext.getString(R.string.photo_saved_text), Toast.LENGTH_LONG).show();
            } catch (Exception error) {
                Toast.makeText(mContext,mContext.getString(R.string.saving_image_failed), Toast.LENGTH_LONG).show();
            }

            if (SettingsController.getInstance().shouldCameraDiscoverDialogShow) {
                showThanksAlert();
            }
            if(GlobalUtil.camera !=null) {
                GlobalUtil.camera.stopPreview();
            }
            isSafeToTakePicture = true;
        }
    };

    private void showThanksAlert() {
        thanksDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View calibrateView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.camera_function_alert, null);
        ScalingUtility.getInstance((Activity) mContext).scaleView(calibrateView);
        applyFontsAndClicksThanksAlert(calibrateView);

        thanksDialog.setContentView(calibrateView);
        thanksDialog.show();
    }

    private void applyFontsAndClicksThanksAlert(View view) {
        TextView descriptionTextView = (TextView) view.findViewById(R.id.cameraAlertTextView);
        TextView thanksTextView = (TextView) view.findViewById(R.id.thanksTextView);
        TextView dontShowTextView = (TextView) view.findViewById(R.id.dontShowTextView);
        CheckBox dontShowCheckBox = (CheckBox) view.findViewById(R.id.DontCheckBox);

        FontsUtil.applyFontToText(mContext, descriptionTextView, FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(mContext, thanksTextView, FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(mContext, dontShowTextView, FontsUtil.ROBOTO_REGULAR);

        thanksTextView.setOnClickListener(thanksClickListener);
        dontShowCheckBox.setOnCheckedChangeListener(dontShowCameraChangeListener);
    }

    private View.OnClickListener thanksClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (thanksDialog != null) {
                thanksDialog.cancel();
                thanksDialog = null;
            }
            if (lightOffAlertDialog != null) {
                lightOffAlertDialog.cancel();
                lightOffAlertDialog = null;
            }
        }
    };

    private File getDir() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/"+mContext.getString(R.string.app_name));
        dir.mkdirs();

        return dir;
    }

    private class SwitchOffTimer extends CountDownTimer {
        public SwitchOffTimer(long totalTime, long tickTime) {
            super(totalTime, tickTime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String formattedTime = formatTime(timeToSwitchOff - millisUntilFinished);
            remainingTimeView.setText(formattedTime);
        }

        @Override
        public void onFinish() {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
            notificationManager.cancel(MainActivity.myNotificationId);
            switchOffCallBack.timerFinished();

            if (SettingsController.getInstance().showLightOffAlert) {
                showLightOffAlert();
            }
        }
    }

    private void showLightOffAlert() {
        String message = mContext.getString(R.string.timer_shut_toast_text_1) + " " + getSwitchOffTime() + " " + mContext.getString(R.string.timer_shut_toast_text_2);

        lightOffAlertDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View lightOffAlertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.light_off_alert, null);
        ScalingUtility.getInstance((Activity) mContext).scaleView(lightOffAlertView);

        TextView descriptionTextView = (TextView) lightOffAlertView.findViewById(R.id.lightOffAlertTextView);
        TextView thanksTextView = (TextView) lightOffAlertView.findViewById(R.id.thanksTextView);
        TextView dontShowTextView = (TextView) lightOffAlertView.findViewById(R.id.dontShowTextView);
        CheckBox dontShowCheckBox = (CheckBox) lightOffAlertView.findViewById(R.id.DontCheckBox);

        FontsUtil.applyFontToText(mContext, descriptionTextView, FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(mContext, thanksTextView, FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(mContext, dontShowTextView, FontsUtil.ROBOTO_REGULAR);

        thanksTextView.setOnClickListener(thanksClickListener);
        dontShowCheckBox.setOnCheckedChangeListener(dontShowCheckChangeListener);

        descriptionTextView.setText(message);
        lightOffAlertDialog.setContentView(lightOffAlertView);
        lightOffAlertDialog.show();
    }

    private String formatTime(long remainingTime) {
        int hours = (int) remainingTime / (1000 * 60 * 60);
        remainingTime %= (1000 * 60 * 60);

        int minutes = (int) remainingTime / (1000 * 60);
        remainingTime %= (1000 * 60);

        int seconds = (int) remainingTime / (1000);

        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);

    }

    private String formatDigits(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }

    private CompoundButton.OnCheckedChangeListener dontShowCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsController.getInstance().showLightOffAlert = !isChecked;
            FlashPreferences.getInstance(mContext).showLightOffAlertSettings();
        }
    };
    private CompoundButton.OnCheckedChangeListener dontShowCameraChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsController.getInstance().shouldCameraDiscoverDialogShow = !isChecked;
            FlashPreferences.getInstance(mContext).saveCameraUseSettings();
        }
    };
}
