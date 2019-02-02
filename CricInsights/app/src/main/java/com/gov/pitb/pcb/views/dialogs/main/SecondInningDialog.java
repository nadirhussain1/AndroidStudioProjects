package com.gov.pitb.pcb.views.dialogs.main;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.navigators.EventBusEvents.InningsChanged;
import com.gov.pitb.pcb.utils.GlobalConstants;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.PlayersCustomAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by nadirhussain on 04/07/2017.
 */

public class SecondInningDialog {
    @BindView(R.id.headTextView)
    TextView headTextView;
    @BindView(R.id.analystModeTextView)
    TextView analystModeTextView;
    @BindView(R.id.spinnersLayoutInfo)
    RelativeLayout spinnersLayoutInfo;
    @BindView(R.id.strikerSpinner)
    Spinner strikerSpinner;
    @BindView(R.id.nonStrikerSpinner)
    Spinner nonStrikerSpinner;

    private Dialog dialog = null;
    private ArrayList<Player> playersList;
    private PlayersCustomAdapter strikerAdapter;
    private PlayersCustomAdapter nonStrikerAdapter;


    public SecondInningDialog(Activity activity, ArrayList<Player> playersList) {
        this.playersList = playersList;

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.inning_change_layout, null);
        new ViewScaleHandler(activity).scaleRootView(dialogView);
        ButterKnife.bind(this, dialogView);
        hideShowViews(activity);

        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(dialogView);

    }

    private void hideShowViews(Activity activity) {
        int userType = InsightsPreferences.getUserType(activity);
        if (userType == GlobalConstants.SCORER_TYPE) {
            analystModeTextView.setVisibility(View.GONE);
            spinnersLayoutInfo.setVisibility(View.VISIBLE);

            bindSpinners(activity);
        } else {
            analystModeTextView.setVisibility(View.VISIBLE);
            spinnersLayoutInfo.setVisibility(View.GONE);
        }
    }

    private void bindSpinners(Activity activity) {
        String inningTextValue = "2 <sup>nd</sup> innings";
        headTextView.setText(Html.fromHtml(inningTextValue));

        strikerAdapter = new PlayersCustomAdapter(activity, playersList);
        nonStrikerAdapter = new PlayersCustomAdapter(activity, playersList);
        strikerSpinner.setAdapter(strikerAdapter);
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

    @OnClick(R.id.crossIconClickView)
    public void crossClicked() {
        dismissDialog();
    }

    @OnClick(R.id.doneButton)
    public void doneButton() {
        InningsChanged inningsChanged = new InningsChanged();
        if (spinnersLayoutInfo.getVisibility() == View.VISIBLE) {
            inningsChanged.striker = (Player) strikerSpinner.getSelectedItem();
            inningsChanged.nonStriker = (Player) nonStrikerSpinner.getSelectedItem();
        }

        EventBus.getDefault().post(inningsChanged);
        dismissDialog();

    }

    @OnItemSelected(R.id.strikerSpinner)
    public void strikerItemSelected(AdapterView<?> adapterView, View view, int selectedStrikerPosition, long l) {
        updateNonStriker(selectedStrikerPosition);
    }

    private void updateNonStriker(int strikerPosition) {
        ArrayList<Player> battingTeamPlayersNames = getPlayersListForNonStriker(playersList);
        battingTeamPlayersNames.remove(strikerPosition);

        nonStrikerSpinner.setAdapter(nonStrikerAdapter);
        nonStrikerAdapter.refreshSpinnerItems(battingTeamPlayersNames);
    }

    private ArrayList<Player> getPlayersListForNonStriker(ArrayList<Player> players) {
        ArrayList<Player> clone = new ArrayList<>();
        for (Player player : players) {
            clone.add(player);
        }
        return clone;
    }

}
