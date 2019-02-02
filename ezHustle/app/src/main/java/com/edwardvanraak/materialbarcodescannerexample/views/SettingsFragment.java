package com.edwardvanraak.materialbarcodescannerexample.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 08/04/2017.
 */

public class SettingsFragment extends Fragment {
    @BindView(R.id.soundCheckBox)
    CheckBox soundChekBox;
    @BindView(R.id.offlineDbCheckBox)
    CheckBox offlineDbCheckbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settings, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        setInitialValues();
        return rootView;
    }

    private void setInitialValues() {
        boolean isSoundEnabled = CustomPreferences.isSoundEnabled(getActivity());
        boolean isOfflineChecked = CustomPreferences.getOfflineDbSettingsFlag(getActivity());

        soundChekBox.setChecked(isSoundEnabled);
        offlineDbCheckbox.setChecked(isOfflineChecked);
        if (!CustomPreferences.isOfflineDbDownloaded(getActivity())) {
            offlineDbCheckbox.setEnabled(false);
        }

        soundChekBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CustomPreferences.saveSoundSettings(getActivity(), isChecked);
            }
        });
        offlineDbCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CustomPreferences.saveOfflineSettingsStatus(getActivity(), isChecked);
            }
        });
    }

    @OnClick(R.id.soundSettingsLayout)
    public void soundClicked() {
        boolean isEnabled = soundChekBox.isChecked();
        soundChekBox.setChecked(!isEnabled);
    }
}
