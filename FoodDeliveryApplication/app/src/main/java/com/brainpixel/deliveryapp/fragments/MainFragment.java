package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.HomeTabSwitchListener;
import com.brainpixel.deliveryapp.handlers.MainToolbarHandler;
import com.brainpixel.deliveryapp.handlers.OnCartCountUpdate;
import com.brainpixel.deliveryapp.handlers.OnDataLoaded;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.services.DataLoaderService;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class MainFragment extends Fragment {
    private static final int GRID_STATE = 0;
    private static final int LIST_STATE = 1;

    @BindView(R.id.homeLayout)
    LinearLayout homeLayout;
    @BindView(R.id.cartLayout)
    LinearLayout cartLayout;
    @BindView(R.id.cartCountTextView)
    TextView cartCountTextView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private MainToolbarHandler currentFragment;
    private int switchState = GRID_STATE;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void loadData() {
        showProgressBarLayout();

        Intent intent = new Intent(getActivity(), DataLoaderService.class);
        getActivity().startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnDataLoaded event) {
        hideProgressBarLayout();
        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());
        homeTabClicked();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnCartCountUpdate event) {
        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeTabSwitchListener event) {
        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());
        homeTabClicked();
    }

    @OnClick(R.id.homeLayout)
    public void homeTabClicked() {
        homeLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        cartLayout.setBackgroundColor(Color.WHITE);

        loadHomeFragment();
    }


    @OnClick(R.id.cartLayout)
    public void cartTabClicked() {
        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());
        cartLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        homeLayout.setBackgroundColor(Color.WHITE);

        loadCartFragment();
    }

    private void loadHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, fragment).commitAllowingStateLoss();

        currentFragment = fragment;
    }

    private void loadCartFragment() {
        CartFragment fragment = new CartFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, fragment).commitAllowingStateLoss();

        currentFragment = fragment;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);


        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(searchQueryListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gridlistswitch && currentFragment instanceof MainToolbarHandler) {
            if (switchState == GRID_STATE) {
                switchState = LIST_STATE;
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.list_icon));

                ((MainToolbarHandler) currentFragment).switchToList();
            } else {
                switchState = GRID_STATE;
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.grid_icon));

                ((MainToolbarHandler) currentFragment).switchToGrid();
            }
        }

        return false;
    }

    private OnQueryTextListener searchQueryListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (currentFragment instanceof MainToolbarHandler) {
                ((MainToolbarHandler) currentFragment).filterResults(newText);
            }
            return false;
        }
    };
}
