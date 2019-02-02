package com.connectivityapps.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.connectivityapps.flashlight.FullScreenActivity;
import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.shared.CommonController;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 26/02/2015.
 */

public class FullScreenFragment extends Fragment {
    private View rootView = null;
    public static CommonController commonController = null;
    public static boolean defaultStateOn=false;
    private  SeekBar colorChangeBar=null;
    private int lastProgress=0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.full_screen_fragment, null);
        ScalingUtility.getInstance(getActivity()).scaleView(rootView);
        initViewsAndApplyFont();
        commonController = new CommonController(rootView, getActivity(), null);

        defaultStateOn = SettingsController.getInstance().isDefaultStateOn;
        if(defaultStateOn){
            openFullScreenActivity();
        }
        return rootView;
    }


    private void initViewsAndApplyFont() {
        ImageView mainOnOffButton = (ImageView) rootView.findViewById(R.id.mainOnOffButton);
        colorChangeBar = (SeekBar) rootView.findViewById(R.id.colorChangeSeekBar);
        colorChangeBar.setOnSeekBarChangeListener(colorProgressChangeListener);
        mainOnOffButton.setBackgroundResource(R.drawable.large_button_fullscreen);
        mainOnOffButton.setOnClickListener(screenOnClickListener);

        rootView.findViewById(R.id.lightOffTimeLeftTextView).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        colorChangeBar.setProgress(FlashPreferences.getInstance(getActivity()).getScreenColorProgress());
    }
    private void openFullScreenActivity(){
        if(commonController==null){
            commonController = new CommonController(rootView, getActivity(), null);
        }
        playSound();
        Intent intent=new Intent(getActivity(), FullScreenActivity.class);
        getActivity().startActivity(intent);
    }

    private View.OnClickListener screenOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFullScreenActivity();
        }
    };
    private void playSound() {
        ((MainActivity) getActivity()).playSound();
    }
    private SeekBar.OnSeekBarChangeListener colorProgressChangeListener=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            lastProgress=progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            FlashPreferences.getInstance(getActivity()).saveScreenLightColorProgress(lastProgress);
        }
    };
}
