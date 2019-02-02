package com.connectivityapps.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.flashlight.SosFullScreenActivity;
import com.connectivityapps.shared.CommonController;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.shared.SosController;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 04/03/2015.
 */
public class SosFragment extends Fragment {
    private View rootView = null;
    public static boolean isInProgress = false;
    private CommonController commonController = null;
    public static ImageView mainOnOffButton = null;
    public static ImageView ledLightIconView = null;
   // public static ImageView screenLightIconView = null;
    public static ImageView sosStatusIconView = null;
    public static int onTime = 250;
    private CompoundButton repeatSwitchBox = null;
    private static Context mContext = null;

    private RelativeLayout scalingLayout = null;
    private int[] textViewIds = new int[]{R.id.speedTextView, R.id.slowSpeedLabelView, R.id.fastSpeedLabelView, R.id.repeatLabelView};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.sos_screen, null);
        ScalingUtility.getInstance(getActivity()).scaleView(rootView);
        initViews();
        applyFonts();
        commonController = new CommonController(rootView, getActivity(), null);

        isInProgress = !SettingsController.getInstance().isDefaultSosStateOn;
        if(GlobalUtil.camera !=null) {
            GlobalUtil.camera.startPreview();
            initSosState();
        }
        return rootView;
    }

    private void initViews() {
        mainOnOffButton = (ImageView) rootView.findViewById(R.id.mainOnOffButton);
        ledLightIconView = (ImageView) rootView.findViewById(R.id.flashLightIconView);
      //  screenLightIconView = (ImageView) rootView.findViewById(R.id.screenLightIconView);
        sosStatusIconView = (ImageView) rootView.findViewById(R.id.sosSatusBarIconView);
        repeatSwitchBox = (CompoundButton) rootView.findViewById(R.id.repeatCheckBox);

        repeatSwitchBox.setChecked(SettingsController.getInstance().isSosRepeatEnabled);
        if (SettingsController.getInstance().sosLightToFlashIndex == 0) {
          //  screenLightIconView.setBackgroundResource(R.drawable.button_screen_active);
            ledLightIconView.setBackgroundResource(R.drawable.button_led_active);
        } else if (SettingsController.getInstance().sosLightToFlashIndex == 1) {
           // screenLightIconView.setBackgroundResource(R.drawable.button_screen_active);
            ledLightIconView.setBackgroundResource(R.drawable.button_led);
        } else {
           // screenLightIconView.setBackgroundResource(R.drawable.button_screen);
            ledLightIconView.setBackgroundResource(R.drawable.button_led_active);
        }
        mainOnOffButton.setOnClickListener(mainOnOffSwitchClickListener);
        ledLightIconView.setOnClickListener(ledSosSettingsClickListener);
        //screenLightIconView.setOnClickListener(screenSosSettingsClickListener);
        repeatSwitchBox.setOnCheckedChangeListener(repeatSettingsChangeListener);


        ((SeekBar) rootView.findViewById(R.id.speedSeekBar)).setOnSeekBarChangeListener(speedSeekBarListener);
         scalingLayout = (RelativeLayout) rootView.findViewById(R.id.rootLayout);
    }



    @Override
    public void onDestroy() {
        if( GlobalUtil.camera !=null) {
            GlobalUtil.camera.stopPreview();
        }
        SosController.getInstance(getActivity()).stopSos();
        super.onDestroy();
    }

    private void applyFonts() {
        for (int count = 0; count < textViewIds.length; count++) {
            TextView textView = (TextView) rootView.findViewById(textViewIds[count]);
            FontsUtil.applyFontToText(getActivity(), textView, FontsUtil.ROBOTO_REGULAR);
        }
    }

    private void initSosState() {
        if (!isInProgress) {
            isInProgress = true;
           // if (SettingsController.getInstance().sosLightToFlashIndex == 2) {
                playSound();
                SosController.getInstance(getActivity()).startSos();
//            } else {
//                openSosFullScreenActivity();
//            }
        } else {
            isInProgress=false;
            SosController.getInstance(getActivity()).stopSos();
        }
    }

    private void playSound() {
        MainActivity.playSound();
    }

    private static void openSosFullScreenActivity() {
        if (!GlobalUtil.isSosFullScreenShowing) {
            GlobalUtil.isSosFullScreenShowing = true;
            Intent intent = new Intent(mContext, SosFullScreenActivity.class);
            mContext.startActivity(intent);
        }
    }

    private View.OnClickListener mainOnOffSwitchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initSosState();
        }
    };
    private View.OnClickListener ledSosSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeLedSettings();
        }
    };

    public static void changeLedSettings() {
//        if (SettingsController.getInstance().sosLightToFlashIndex == 2) {
//            Toast.makeText(mContext, "You can't disable both screen and led light", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (SettingsController.getInstance().sosLightToFlashIndex == 1) {
//            ledLightIconView.setBackgroundResource(R.drawable.button_led_active);
//            SettingsController.getInstance().sosLightToFlashIndex = 0;
//        } else {
//            SettingsController.getInstance().sosLightToFlashIndex = 1;
//            ledLightIconView.setBackgroundResource(R.drawable.button_led);
//        }
//        FlashPreferences.getInstance(mContext).saveSosLightOptions();
    }

    public static void changeScreenSettings() {
//        if (SettingsController.getInstance().sosLightToFlashIndex == 1) {
//            screenLightIconView.setBackgroundResource(R.drawable.button_screen);
//            ledLightIconView.setBackgroundResource(R.drawable.button_led_active);
//            SettingsController.getInstance().sosLightToFlashIndex = 2;
//            return;
//        }
//        if (SettingsController.getInstance().sosLightToFlashIndex == 2) {
//            screenLightIconView.setBackgroundResource(R.drawable.button_screen_active);
//            SettingsController.getInstance().sosLightToFlashIndex = 0;
//            openSosFullScreenActivity();
//        } else {
//            SettingsController.getInstance().sosLightToFlashIndex = 2;
//            screenLightIconView.setBackgroundResource(R.drawable.button_screen);
//        }
//
//        FlashPreferences.getInstance(mContext).saveSosLightOptions();
    }

    private View.OnClickListener screenSosSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeScreenSettings();
        }

    };

    private CompoundButton.OnCheckedChangeListener repeatSettingsChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsController.getInstance().isSosRepeatEnabled = isChecked;
            FlashPreferences.getInstance(getActivity()).saveSosRepeatSignal();
        }
    };
    private SeekBar.OnSeekBarChangeListener speedSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress==0){
                progress=1;
            }
            onTime = 1000 / progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}
