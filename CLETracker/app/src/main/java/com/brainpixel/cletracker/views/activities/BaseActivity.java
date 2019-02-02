package com.brainpixel.cletracker.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.utils.GlobalUtil;

/**
 * Created by nadirhussain on 30/03/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }

    public boolean performInternetCheck() {
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.no_connection_title), getString(R.string.no_connection_msg));
            return false;
        }
        return true;
    }

    public void displayMessageAlert(String message) {
        String title = getString(R.string.alert_message_title);
        GlobalUtil.showMessageAlertWithOkButton(this, title, message);
    }

}
