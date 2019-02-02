package com.connectivityapps.flashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.ScalingUtility;
import com.flurry.android.FlurryAgent;

/**
 * Created by nadirhussain on 25/02/2015.
 */


public class Settings extends Activity {
    private int [] fontsViewsId=new int []{R.id.settingsTextView,R.id.generalTextView,R.id.soundTextView,
            R.id.switchOffTextView,R.id.soundSwitchTextView,
            R.id.startUpTextView,R.id.modeToOpenTextView,R.id.defaultStateLightTextView,
            R.id.sosHeadTextView,R.id.defaultSosStateTextView,
            R.id.sosRepeatTextView,R.id.bubbleLevelHeadTextView,R.id.showAngleTextView

    };

    private CompoundButton soundEnabledSwitch,showSoundSwitch,defaultStateSwitch,defaultSosStateSwitch,sosRepeatSwitch=null;
    private Spinner switchOffLightTimeSpinner,modeSpinner,bubbleDisplaySpinner=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView=getLayoutInflater().inflate(R.layout.settings,null);
        ScalingUtility.getInstance(this).scaleView(rootView);
        setContentView(rootView);

        applyCustomFonts();
        initViews();
        applyClicks();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
    private void applyCustomFonts(){
        for(int count=0;count<fontsViewsId.length;count++){
            TextView textView=(TextView)findViewById(fontsViewsId[count]);
            FontsUtil.applyFontToText(this,textView,FontsUtil.ROBOTO_REGULAR);
        }
    }
    private void initViews(){
        inflateViews();

        soundEnabledSwitch.setChecked(SettingsController.getInstance().isSoundEnabled);
        showSoundSwitch.setChecked(SettingsController.getInstance().isShowSoundSwitchEnabled);
        defaultStateSwitch.setChecked(SettingsController.getInstance().isDefaultStateOn);
        defaultSosStateSwitch.setChecked(SettingsController.getInstance().isDefaultSosStateOn);
        sosRepeatSwitch.setChecked(SettingsController.getInstance().isSosRepeatEnabled);

        switchOffLightTimeSpinner.setSelection(SettingsController.getInstance().switchOffLightTimeIndex);
        modeSpinner.setSelection(SettingsController.getInstance().modeToOpenAtLaunchIndex);
        bubbleDisplaySpinner.setSelection(SettingsController.getInstance().showAngleIndex);

    }
    private void inflateViews(){
        soundEnabledSwitch=(CompoundButton)findViewById(R.id.soundSwitch);
        showSoundSwitch=(CompoundButton)findViewById(R.id.showSoundSwitch);
        defaultStateSwitch=(CompoundButton)findViewById(R.id.defaultStateLightSwitch);
        defaultSosStateSwitch=(CompoundButton)findViewById(R.id.defaultSosStateSwitch);
        sosRepeatSwitch=(CompoundButton)findViewById(R.id.sosRepeatSwitch);

        switchOffLightTimeSpinner=(Spinner)findViewById(R.id.switchOffTimeSpinner);
        modeSpinner=(Spinner)findViewById(R.id.modeOpenSpinner);
        bubbleDisplaySpinner=(Spinner)findViewById(R.id.bubbleDisplayOptionsSpinner);
    }
    private void applyClicks(){
        findViewById(R.id.backIconClickedView).setOnClickListener(backClicklListener);
    }
    private View.OnClickListener backClicklListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSettingsAndCloseScreen();
        }
    };

    @Override
    public void onBackPressed() {
        saveSettingsAndCloseScreen();
    }
    private void saveSettingsAndCloseScreen(){
        SettingsController.getInstance().isSoundEnabled=soundEnabledSwitch.isChecked();
        SettingsController.getInstance().isShowSoundSwitchEnabled=showSoundSwitch.isChecked();
        SettingsController.getInstance().isDefaultStateOn=defaultStateSwitch.isChecked();
        SettingsController.getInstance().isDefaultSosStateOn=defaultSosStateSwitch.isChecked();
        SettingsController.getInstance().isSosRepeatEnabled=sosRepeatSwitch.isChecked();

        SettingsController.getInstance().switchOffLightTimeIndex=switchOffLightTimeSpinner.getSelectedItemPosition();
        SettingsController.getInstance().modeToOpenAtLaunchIndex=modeSpinner.getSelectedItemPosition();
        SettingsController.getInstance().showAngleIndex=bubbleDisplaySpinner.getSelectedItemPosition();

        FlashPreferences.getInstance(this).saveSettings();
        String displayOption="ANGLE";
        if(bubbleDisplaySpinner.getSelectedItemPosition()==1){
            displayOption="INCLINATION";
        }else if(bubbleDisplaySpinner.getSelectedItemPosition()==2){
            displayOption="ROOF_PITCH";
        }
        FlashPreferences.getInstance(this).saveBubbleDisplayOption(displayOption);


        Settings.this.finish();
    }
}
