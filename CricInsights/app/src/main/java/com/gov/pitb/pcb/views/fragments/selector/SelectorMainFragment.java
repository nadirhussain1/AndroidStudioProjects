package com.gov.pitb.pcb.views.fragments.selector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.db.dynamic.Inning;
import com.gov.pitb.pcb.data.server.players.Player;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.adapters.CustomSpinnerAdapter;
import com.gov.pitb.pcb.views.adapters.PlayersCustomAdapter;
import com.gov.pitb.pcb.views.custom.wheels.InziWheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by nadirhussain on 19/07/2017.
 */

public class SelectorMainFragment extends Fragment {
    View rootView;
    @BindView(R.id.batsmanSpinner)
    Spinner batmanSpinner;
    @BindView(R.id.bowlerTypeSpinner)
    Spinner bowlerTypeSpinner;
    @BindView(R.id.bowlerNameSpinner)
    Spinner bowlerNameSpinner;
    @BindView(R.id.inziWheelView)
    InziWheelView inziWheelView;
    @BindView(R.id.dotsLayout)
    LinearLayout dotsLayout;
    @BindView(R.id.ballsTextView)
    TextView ballsTextView;
    @BindView(R.id.runsTextView)
    TextView runsTextView;


    private ArrayList<Player> allPlayersList = new ArrayList<>();
    private PlayersCustomAdapter batsManSpinnerAdapter, bowlerSpinnerAdapter;
    private CustomSpinnerAdapter bowlingTypeSpinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.selector_fragment, null);
        new ViewScaleHandler(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPlayersData();
        bindSpinners();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.selector_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mode:
                openUserModeDialog();
                break;
            default:
                break;
        }

        return true;
    }

    private void openUserModeDialog() {
      //  ((SuperMainActivity) getActivity()).openUserModeDialog();
    }

    private void bindSpinners() {
        batsManSpinnerAdapter = new PlayersCustomAdapter(getActivity(), allPlayersList);
        bowlerSpinnerAdapter = new PlayersCustomAdapter(getActivity(), new ArrayList<Player>());
        String[] bowlingTypesArray = getActivity().getResources().getStringArray(R.array.bowling_types);
        bowlingTypeSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), new ArrayList<String>(Arrays.asList(bowlingTypesArray)));

        bowlerNameSpinner.setAdapter(bowlerSpinnerAdapter);
        batmanSpinner.setAdapter(batsManSpinnerAdapter);
        bowlerTypeSpinner.setAdapter(bowlingTypeSpinnerAdapter);
    }


    private void loadPlayersData() {
        Inning inning = InsightsDbManager.getFirstSavedInning();
        String battingPlayerIds = inning.getBattingTeamSelPlayersIds();
        populatePlayersList(battingPlayerIds.split(","));

        String bowlingPlayerIds = inning.getBowlingTeamSelPlayersIds();
        populatePlayersList(bowlingPlayerIds.split(","));
    }

    private void populatePlayersList(String[] idsArr) {
        for (int count = 0; count < idsArr.length; count++) {
            allPlayersList.add(InsightsDbManager.getPlayer(idsArr[count]));
        }

    }

    private void updateBowlerSpinner(int selectedBatsmanPosition) {
        ArrayList<Player> bowlers = new ArrayList<>();
        Player option1 = new Player("Select bowler");
        Player option2 = new Player("Any Bowler");

        bowlers.add(option2);
        bowlers.add(0, option1);
        for (int count = 0; count < allPlayersList.size(); count++) {
            if (count != selectedBatsmanPosition) {
                bowlers.add(allPlayersList.get(count));
            }
        }
        bowlerSpinnerAdapter.refreshSpinnerItems(bowlers);
        bowlerNameSpinner.setSelection(0);
    }

    @OnItemSelected(R.id.batsmanSpinner)
    public void batsManSpinnerItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        inziWheelView.refreshWheel(new ArrayList<Delivery>());
        updateBowlerSpinner(position);
    }

    @OnItemSelected(R.id.bowlerNameSpinner)
    public void bowlerSpinnerItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position > 0) {
            bowlerTypeSpinner.setSelection(0);
            if (position == 1) {
                fetchResultsAgainstBowler(null);    //Against any bowler
            } else {
                Player bowler = (Player) bowlerNameSpinner.getSelectedItem();
                fetchResultsAgainstBowler(bowler);
            }
        }
    }

    @OnItemSelected(R.id.bowlerTypeSpinner)
    public void bowlingTypeItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position > 0) {
            bowlerNameSpinner.setSelection(0);
            if (position == 1) {
                fetchResultsAgainstBowlingType(null);
            } else {
                String bowlingType = (String) bowlerTypeSpinner.getSelectedItem();
                fetchResultsAgainstBowlingType(bowlingType);
            }
        }
    }


    private void fetchResultsAgainstBowler(Player bowler) {
        Player batsmanPlayer = (Player) batmanSpinner.getSelectedItem();
        List<Delivery> deliveries;
        if (bowler != null) {
            deliveries = InsightsDbManager.getBatsManDeliveriesAgainstBowler(batsmanPlayer.getPlayerId(), bowler.getPlayerId());
        } else {
            deliveries = InsightsDbManager.getBatsmanAllValidScoreDeliveries(batsmanPlayer.getPlayerId());

        }
        refreshResultsOnViews(deliveries);
    }

    private void fetchResultsAgainstBowlingType(String bowlingType) {
        Player batsmanPlayer = (Player) batmanSpinner.getSelectedItem();
        List<Delivery> deliveries = InsightsDbManager.getBatsmanAllValidScoreDeliveries(batsmanPlayer.getPlayerId());
        if (bowlingType == null) {
            // No need to filter by types
            refreshResultsOnViews(deliveries);
            return;
        }

        List<Delivery> filterDeliveries = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            String bowlerId = delivery.getBowlerId();
            Player player = getPlayer(bowlerId);
            if (player != null && player.getPlayerBowlingType() != null && player.getPlayerBowlingType().equals(bowlingType)) {
                filterDeliveries.add(delivery);
            }

        }
        refreshResultsOnViews(filterDeliveries);
    }

    private void refreshResultsOnViews(List<Delivery> deliveries) {
        ballsTextView.setText("" + deliveries.size());
        int score = 0;
        for (Delivery delivery : deliveries) {
            score += delivery.getRuns();
        }
        runsTextView.setText("" + score);
        inziWheelView.refreshWheel(deliveries);
    }

    private Player getPlayer(String playerId) {
        for (Player player : allPlayersList) {
            if (player.getPlayerId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

}
