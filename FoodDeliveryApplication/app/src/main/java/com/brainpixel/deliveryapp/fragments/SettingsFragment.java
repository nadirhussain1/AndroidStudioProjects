package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.NumberEditorActivity;
import com.brainpixel.deliveryapp.activities.UpdateProfileActivity;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 26/11/2018.
 */

public class SettingsFragment extends Fragment {
    @BindView(R.id.nameValueTextView)
    TextView nameValueTextView;
    @BindView(R.id.mobileValueTextView)
    TextView mobileValueTextView;
    @BindView(R.id.emailValueTextView)
    TextView emailValueTextView;
    @BindView(R.id.appVersionTextView)
    TextView appVersionTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = attachViews(inflater);
        return view;
    }

    private View attachViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.settings_screen, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nameValueTextView.setText(PreferenceManager.getUserName(getActivity()));
        mobileValueTextView.setText(PreferenceManager.getUserMobile(getActivity()));
        emailValueTextView.setText(PreferenceManager.getUserEmail(getActivity()));

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            appVersionTextView.setText("App Version:" + pInfo.versionName + " (" + pInfo.versionCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.nameLayout)
    public void nameLayoutClicked() {
        openProfileUpdateScreen(UpdateProfileActivity.UPDATE_NAME_INDEX);
    }

    @OnClick(R.id.mobileNumberLayout)
    public void mobilLayoutClicked() {
        Intent intent = new Intent(getActivity(), NumberEditorActivity.class);
        intent.putExtra(NumberEditorActivity.KEY_UPDATE_FLAG, true);
        startActivity(intent);
    }

    @OnClick(R.id.emailLayout)
    public void emailLayoutClicked() {
        openProfileUpdateScreen(UpdateProfileActivity.UPDATE_EMAIL_INDEX);
    }

    @OnClick(R.id.changePasswordLayout)
    public void changePasswordLayoutClicked() {
        openProfileUpdateScreen(UpdateProfileActivity.UPDATE_PASSWORD_INDEX);
    }

    @OnClick(R.id.termsAndConditionsTextView)
    public void termsTabClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalDataManager.getInstance().configData.getTermsAndConditionsUrl()));
        startActivity(browserIntent);
    }

    private void openProfileUpdateScreen(int index) {
        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
        intent.putExtra(UpdateProfileActivity.KEY_UPDATE_INDEX, index);
        startActivity(intent);
    }


}
