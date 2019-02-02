package com.brainpixel.deliveryapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.ItemSimpleDetailActivity;
import com.brainpixel.deliveryapp.adapters.HomeAdapter;
import com.brainpixel.deliveryapp.handlers.MainToolbarHandler;
import com.brainpixel.deliveryapp.handlers.OnItemClickListener;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.GridItemDecoration;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class HomeFragment extends Fragment implements OnItemClickListener, MainToolbarHandler {
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    public TextView emptyTextView;

    private HomeAdapter homeAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_display, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int space = (int) ScalingUtility.resizeDimension(10);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(space);
        recyclerView.addItemDecoration(gridItemDecoration);
        switchToGrid();
        bindRecyclerViewWithAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    private void bindRecyclerViewWithAdapter() {
        homeAdapter = new HomeAdapter(getActivity(), GlobalDataManager.getInstance().homeItems, this);
        recyclerView.setAdapter(homeAdapter);

        if (GlobalDataManager.getInstance().homeItems.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    public void switchToGrid() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (homeAdapter != null) {
            homeAdapter.displayGridView();
        }

    }

    public void switchToList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (homeAdapter != null) {
            homeAdapter.displayListView();
        }

    }

    @Override
    public void filterResults(String filter) {
        if (homeAdapter != null) {
            homeAdapter.filterResults(filter);
        }
    }


    @Override
    public void itemClicked(MainItem selectedItem) {
        GlobalDataManager.getInstance().selectedItem = selectedItem;
        Intent intent = new Intent(getActivity(), ItemSimpleDetailActivity.class);
        startActivity(intent);
    }


}
