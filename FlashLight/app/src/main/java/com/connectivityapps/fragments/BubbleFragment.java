package com.connectivityapps.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.connectivityapps.bubble.LevelView;
import com.connectivityapps.bubble.Orientation;
import com.connectivityapps.bubble.OrientationListener;
import com.connectivityapps.bubble.OrientationProvider;
import com.connectivityapps.flashlight.MainActivity;
import com.connectivityapps.flashlight.R;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 11/03/2015.
 */
public class BubbleFragment extends Fragment implements OrientationListener {
    private View rootView = null;
    public static OrientationProvider provider;
    public static Activity context;
    private LevelView view;
    private Dialog calibrateDialog=null;
    private int [] textViewIds=new int[]{R.id.caliberateHeadTextView,R.id.caliberationDescTextView,R.id.cancelButton,R.id.resetButton,R.id.caliberateButton};

    /** Gestion du son */

    private Button lockValuesButton,caliberateButton=null;
    private TextView displayTypeTextView=null;
    private boolean isLocked=false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bubble_screen, null);
        initViews();
        context=getActivity();
        return rootView;
    }
    private void initViews(){
        view = (LevelView)rootView.findViewById(R.id.level);
        lockValuesButton=(Button)rootView.findViewById(R.id.lockValuesButton);
        displayTypeTextView=(TextView)rootView.findViewById(R.id.displayTypeTextView);
        Button caliberateButton=(Button)rootView.findViewById(R.id.caliberateButton);

        FontsUtil.applyFontToText(getActivity(),lockValuesButton,FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(getActivity(),caliberateButton,FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(getActivity(),displayTypeTextView,FontsUtil.ROBOTO_REGULAR);
        caliberateButton.setOnClickListener(caliberateAlertClickListener);
        lockValuesButton.setOnClickListener(lockValuesButtonClickListener);

        if(SettingsController.getInstance().showAngleIndex==0){
            displayTypeTextView.setText("Angle");
        }else if(SettingsController.getInstance().showAngleIndex==1){
            displayTypeTextView.setText("Inclination");
        }else{
            displayTypeTextView.setText("Roof pitch");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        provider = OrientationProvider.getInstance();
        if (provider.isSupported()) {
            provider.startListening(this);
        } else {
            Toast.makeText(getActivity(), getText(R.string.not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (provider.isListening()) {
            provider.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onOrientationChanged(Orientation orientation, float pitch, float roll, float balance) {
        if (orientation.isLevel(pitch, roll, balance, provider.getSensibility())) {
            MainActivity.playSound();
        }
        view.onOrientationChanged(orientation, pitch, roll, balance);
    }

    @Override
    public void onCalibrationSaved(boolean success) {
        Toast.makeText(getActivity(), success ?
                        R.string.calibrate_saved : R.string.calibrate_failed,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalibrationReset(boolean success) {
        Toast.makeText(getActivity(), success ?
                        R.string.calibrate_restored : R.string.calibrate_failed,
                Toast.LENGTH_SHORT).show();
    }
    private View.OnClickListener caliberateAlertClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           showCaliberateAlertDialog();
        }
    };
    private View.OnClickListener lockValuesButtonClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isLocked=!isLocked;
            if(isLocked){
                lockValuesButton.setText(getString(R.string.values_locked_text));
                lockValuesButton.setTextColor(Color.parseColor("#FF0000"));
            }else{
                lockValuesButton.setText(getString(R.string.lock_value_text));
                lockValuesButton.setTextColor(Color.parseColor("#FFFFFF"));
            }
            provider.setLocked(isLocked);
        }
    };
    private void showCaliberateAlertDialog(){
        calibrateDialog=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View calibrateView = getActivity().getLayoutInflater().inflate(R.layout.caliberate_alert, null);
        ScalingUtility.getInstance(getActivity()).scaleView(calibrateView);
        applyFontsAndClicks(calibrateView);

        calibrateDialog.setContentView(calibrateView);
        calibrateDialog.show();
    }
    private void applyFontsAndClicks(View view){
         for(int count=0;count<textViewIds.length;count++){
             TextView textView=(TextView)view.findViewById(textViewIds[count]);
             FontsUtil.applyFontToText(getActivity(),textView,FontsUtil.ROBOTO_REGULAR);
         }
        view.findViewById(R.id.cancelButton).setOnClickListener(cancelClickListener);
        view.findViewById(R.id.resetButton).setOnClickListener(resetClickListener);
        view.findViewById(R.id.caliberateButton).setOnClickListener(calibrateClickListener);
    }
    private View.OnClickListener cancelClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           calibrateDialog.cancel();
        }
    };
    private View.OnClickListener resetClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calibrateDialog.cancel();
            provider.resetCalibration();
        }
    };
    private View.OnClickListener calibrateClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calibrateDialog.cancel();
           provider.saveCalibration();
        }
    };

}
