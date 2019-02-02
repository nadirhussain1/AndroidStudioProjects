package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnScreensChecked;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 12/07/2017.
 */

public class ScreensCheckDialog {
    @BindView(R.id.fieldCheckBox)
    CheckBox fieldCheckBox;
    @BindView(R.id.pitchCheckBox)
    CheckBox pitchCheckBox;
    @BindView(R.id.scorerCheckBox)
    CheckBox scorerCheckBox;


    private Dialog dialog = null;
    private Activity activity;


    public ScreensCheckDialog(Activity activity) {
        this.activity = activity;
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.select_mode_dialog, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);

        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @OnClick(R.id.saveButton)
    public void saveModeClicked() {
        boolean isFieldChecked = fieldCheckBox.isChecked();
        boolean isPitchChecked = pitchCheckBox.isChecked();
        boolean isScoreChecked = scorerCheckBox.isChecked();

        if (!isFieldChecked && !isPitchChecked && !isScoreChecked) {
            Toast.makeText(activity, "Please check at least one of the given options", Toast.LENGTH_SHORT).show();
            return;
        }
        MatchPermData matchPermData = new MatchPermData();
        matchPermData.setField(isFieldChecked);
        matchPermData.setPitch(isPitchChecked);
        matchPermData.setScore(isScoreChecked);


        OnScreensChecked onScreensChecked = new OnScreensChecked();
        onScreensChecked.matchPermData = matchPermData;

        EventBus.getDefault().post(onScreensChecked);
        dismissDialog();
    }

    @OnClick(R.id.crossIconClickView)
    public void crossClicked() {
        dismissDialog();
    }

}
