package com.connectivityapps.flashlight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.connectivityapps.fragments.FullScreenFragment;
import com.connectivityapps.interfaces.TimerFinishInterface;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 05/03/2015.
 */
public class FullScreenActivity extends Activity {
    private RelativeLayout scalingLayout = null;
    private int colorProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = getLayoutInflater().inflate(R.layout.full_screen_activity, null);
        ScalingUtility.getInstance(this).scaleView(rootView);
        setContentView(rootView);

        initViewAndClicks();

        changeColor();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
        layoutParams.screenBrightness =  WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;; // Set Value
        getWindow().setAttributes(layoutParams);
    }

    private void initViewAndClicks() {
        scalingLayout = (RelativeLayout) findViewById(R.id.scalingLayout);
        ImageView handTouchImageView = (ImageView) findViewById(R.id.handTouchImageView);
        handTouchImageView.setOnClickListener(handTouchClickListener);

        colorProgress= FlashPreferences.getInstance(this).getScreenColorProgress();
        SeekBar colorSeekBar = (SeekBar) findViewById(R.id.colorChangeSeekBar);
        colorSeekBar.setOnSeekBarChangeListener(colorSeekBarChangeListener);
        colorSeekBar.setProgress(colorProgress);

        if( FullScreenFragment.commonController !=null) {
            FullScreenFragment.commonController.setTimerCallBack(switchOffTimerCallback);
            FullScreenFragment.commonController.startSwitchOffTimer();
        }
    }
    @Override
    protected void onDestroy() {
        MainActivity.playSound();
        if( FullScreenFragment.commonController !=null) {
            FullScreenFragment.commonController.cancelSwitchOffTimer();
        }
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
        layoutParams.screenBrightness =  WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF; // Set Value
        getWindow().setAttributes(layoutParams);

        super.onDestroy();
    }

    private View.OnClickListener handTouchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FullScreenActivity.this.finish();
        }
    };
    private SeekBar.OnSeekBarChangeListener colorSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d("ColorProgress",""+progress);
             colorProgress=progress;
            changeColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
              FlashPreferences.getInstance(FullScreenActivity.this).saveScreenLightColorProgress(colorProgress);
        }
    };
    private void changeColor(){
        if (colorProgress < 25) {
            int color = Color.rgb(255, 255, 255 - (colorProgress * 10));
            scalingLayout.setBackgroundColor(color);
        } else if (colorProgress < 65) {
            int value = (colorProgress - 25) * 3;
            int color = Color.rgb(255,150-value , 150-value);
            scalingLayout.setBackgroundColor(color);
        } else if (colorProgress < 83) {
            int value = (colorProgress - 50) * 5;
            int color = Color.rgb(0, 80 + value, 0);
            scalingLayout.setBackgroundColor(color);
        } else if(colorProgress<92){
            int value = (colorProgress - 50) * 5;
            int color = Color.rgb(0, 0, 50 + value);
            scalingLayout.setBackgroundColor(color);
        }else{
            int value = (colorProgress - 50) * 5;
            int color = Color.rgb(200 + value, 200 + value, 200 + value);
            scalingLayout.setBackgroundColor(color);
        }
    }
    private TimerFinishInterface switchOffTimerCallback=new TimerFinishInterface() {
        @Override
        public void timerFinished() {
           FullScreenActivity.this.finish();
        }
    };
}
