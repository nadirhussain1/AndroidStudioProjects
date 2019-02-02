package com.brainpixel.deliveryapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.HelpEmailActivity;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 25/11/2018.
 */

public class HelpFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = attachViews(inflater);
        return view;
    }

    private View attachViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_help, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.emailUsTextView)
    public void onEmailOptionClick() {
        Intent intent = new Intent(getActivity(), HelpEmailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.callUsTextView)
    public void onCallOptionClicked() {
        showCallConfirmationAlert();
    }

    private void openPhoneCallScreen() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + GlobalDataManager.getInstance().configData.getAdminMobile()));
        startActivity(intent);
    }

    private void showCallConfirmationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder
                .setMessage("Call " + GlobalDataManager.getInstance().configData.getAdminMobile())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openPhoneCallScreen();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
