package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.navigators.EventBusEvents.CommentsAdded;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 18/07/2017.
 */

public class CommentsDialog {
    @BindView(R.id.commentsEditor)
    EditText commentsEditor;
    @BindView(R.id.HeadingTextView)
    TextView headingTextView;

    private Dialog dialog = null;

    public CommentsDialog(Activity activity, Delivery delivery) {
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.comments_dialog, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        dialog.setContentView(dialogView);
        ButterKnife.bind(this, dialogView);

        if (!TextUtils.isEmpty(delivery.getComments())) {
            commentsEditor.append(delivery.getComments());
        }
        String titleOversBalls = "" + delivery.getOverId() + "." + delivery.getBallNumberOfOver() + " overs";
        headingTextView.setText(titleOversBalls);
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
        CommentsAdded commentsAdded = new CommentsAdded();
        commentsAdded.commentsText = commentsEditor.getText().toString();
        EventBus.getDefault().post(commentsAdded);

        dismissDialog();
    }

    @OnClick(R.id.crossIconClickView)
    public void crossClicked() {
        dismissDialog();
    }
}
