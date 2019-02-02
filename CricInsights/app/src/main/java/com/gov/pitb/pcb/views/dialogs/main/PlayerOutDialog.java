package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPlayerOut;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.CustomSpinnerAdapter;
import com.gov.pitb.pcb.views.adapters.PlayersCustomAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 31/05/2017.
 */

public class PlayerOutDialog {
    @BindView(R.id.fielderLayout)
    RelativeLayout fielderLayout;
    @BindView(R.id.newBatsManLayout)
    RelativeLayout newBatsManLayout;
    @BindView(R.id.outTypeSpinner)
    Spinner outTypeSpinner;
    @BindView(R.id.fielderSpinner)
    Spinner fielderSpinner;
    @BindView(R.id.batsmanSpinner)
    Spinner batsManPlayerSpinner;
    @BindView(R.id.outBatsManSpinner)
    Spinner outBatsManSpinner;
    @BindView(R.id.crossCheckBox)
    CheckBox crossCheckBox;

    private Activity activity;
    private Dialog dialog = null;
    private View dialogView;


    public PlayerOutDialog(Activity activity, ArrayList<Player> currentBattingPlayers, ArrayList<Player> newBatsManOptionsList, ArrayList<Player> bowlingTeamPlayers) {
        this.activity = activity;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialogView = LayoutInflater.from(activity).inflate(R.layout.out_layout, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        bindSpinners(activity, currentBattingPlayers, newBatsManOptionsList, bowlingTeamPlayers);
        outTypeSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void bindSpinners(Activity activity, ArrayList<Player> currentBattingPlayers, ArrayList<Player> newBatsManOptionsList, ArrayList<Player> bowlingTeamPlayers) {
        String[] outTypes = activity.getResources().getStringArray(R.array.out_options);

        CustomSpinnerAdapter outTypesAdapter = new CustomSpinnerAdapter(activity, new ArrayList<>(Arrays.asList(outTypes)));
        outTypeSpinner.setAdapter(outTypesAdapter);

        Player selectDummyPlayer = new Player();
        selectDummyPlayer.setPlayerName("Select Player");

        currentBattingPlayers.add(0, selectDummyPlayer);
        PlayersCustomAdapter outPlayerAdapter = new PlayersCustomAdapter(activity, currentBattingPlayers);
        outBatsManSpinner.setAdapter(outPlayerAdapter);

        bowlingTeamPlayers.add(0, selectDummyPlayer);
        PlayersCustomAdapter fielderPlayerAdapter = new PlayersCustomAdapter(activity, bowlingTeamPlayers);
        fielderSpinner.setAdapter(fielderPlayerAdapter);

        if (newBatsManOptionsList != null && newBatsManOptionsList.size() > 0) {
            newBatsManOptionsList.add(0, selectDummyPlayer);
            PlayersCustomAdapter newBatsManPlayerAdapter = new PlayersCustomAdapter(activity, newBatsManOptionsList);
            batsManPlayerSpinner.setAdapter(newBatsManPlayerAdapter);
        } else {
            newBatsManLayout.setVisibility(View.GONE);
        }
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
    public void doneButtonClicked() {
        OnPlayerOut onPlayerOut = new OnPlayerOut();
        onPlayerOut.outType = (String) outTypeSpinner.getSelectedItem();

        int outBatManSelectedPosition = outBatsManSpinner.getSelectedItemPosition();
        int newBatsManPosition = batsManPlayerSpinner.getSelectedItemPosition();
        int fielderPosition = fielderSpinner.getSelectedItemPosition();

        if (outBatManSelectedPosition > 0
                && (newBatsManLayout.getVisibility() == View.GONE || newBatsManPosition > 0)
                && (fielderLayout.getVisibility() == View.GONE || fielderPosition > 0)) {

            onPlayerOut.outPlayer = (Player) outBatsManSpinner.getSelectedItem();
            onPlayerOut.newPlayer = (Player) batsManPlayerSpinner.getSelectedItem();
            onPlayerOut.didBatmanCross = crossCheckBox.isChecked();
            if (fielderLayout.getVisibility() == View.VISIBLE) {
                onPlayerOut.fielder = (Player) fielderSpinner.getSelectedItem();
            }

            EventBus.getDefault().post(onPlayerOut);
            dismissDialog();

        } else {
            Toast.makeText(activity, "Select all values", Toast.LENGTH_SHORT).show();
        }

    }

    private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (position < 2) {
                fielderLayout.setVisibility(View.VISIBLE);
            } else {
                fielderLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}
