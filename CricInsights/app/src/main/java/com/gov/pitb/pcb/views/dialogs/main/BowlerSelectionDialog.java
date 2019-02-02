package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnBowlerSelected;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.PlayersCustomAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 23/05/2017.
 */

public class BowlerSelectionDialog {
    @BindView(R.id.bowlerSpinner)
    Spinner bowlerSpinner;
    @BindView(R.id.overHeadTextView)
    TextView overHeadTextView;

    private Dialog dialog = null;
    private View dialogView;

    public BowlerSelectionDialog(Activity activity, int overId, ArrayList<Player> bowlerOptionsPlayersList) {
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.new_bowler_layout, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);


        populateDataContents(activity, overId, bowlerOptionsPlayersList);
    }

    private void populateDataContents(Activity activity, int overId, ArrayList<Player> bowlerOptionsPlayersList) {
        overHeadTextView.setText("Over " + (overId+1));
        PlayersCustomAdapter spinnerAdapter = new PlayersCustomAdapter(activity, bowlerOptionsPlayersList);
        bowlerSpinner.setAdapter(spinnerAdapter);
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

    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        Player bowler = (Player) bowlerSpinner.getSelectedItem();
        OnBowlerSelected onBowlerSelected = new OnBowlerSelected();
        onBowlerSelected.bowlerSelected = bowler;
        EventBus.getDefault().post(onBowlerSelected);
        dismissDialog();
    }


}
