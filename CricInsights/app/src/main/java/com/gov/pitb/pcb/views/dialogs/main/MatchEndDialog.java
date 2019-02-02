package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnNewMatchStarted;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 13/07/2017.
 */

public class MatchEndDialog {
    private Dialog dialog = null;
    private View dialogView;

    public MatchEndDialog(Activity activity) {
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.end_match_dialog, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.setContentView(dialogView);
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @OnClick(R.id.crossIconClickView)
    public void crossClicked() {
        dismissDialog();
    }

    @OnClick(R.id.newMatchButton)
    public void onReviewButtonClick() {
        EventBus.getDefault().post(new OnNewMatchStarted());
        dismissDialog();
    }
}
