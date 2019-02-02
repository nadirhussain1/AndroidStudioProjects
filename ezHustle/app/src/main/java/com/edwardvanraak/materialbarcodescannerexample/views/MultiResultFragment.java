package com.edwardvanraak.materialbarcodescannerexample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwardvanraak.materialbarcodescannerexample.MainActivity;
import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.interfaces.OnMultiResultItemClickListener;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ListItemData;
import com.edwardvanraak.materialbarcodescannerexample.utils.DividerItemDecoration;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 28/03/2017.
 */

public class MultiResultFragment extends Fragment implements OnMultiResultItemClickListener {
    public static final String KEY_ITEMS_LIST = "KEY_ITEMS_LIST";

    @BindView(R.id.multiResultRecyclerView)
    RecyclerView multiResultRecyclerView;

    private ArrayList<ListItemData> itemsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            itemsList = (ArrayList<ListItemData>) getArguments().getSerializable(KEY_ITEMS_LIST);
        }

        View rootView = inflater.inflate(R.layout.multi_result_list, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        multiResultRecyclerView.setLayoutManager(mLayoutManager);
        multiResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        multiResultRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        MultiResultAdapter multiResultAdapter = new MultiResultAdapter(getActivity(), itemsList, this);
        multiResultRecyclerView.setAdapter(multiResultAdapter);
    }

    @Override
    public void itemClicked(String identifier) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_ITEM_IDENTIFIER, identifier);
        intent.setAction(MainActivity.CLICK_RECEIVER_ACTION);
        getActivity().sendBroadcast(intent);
    }
}
