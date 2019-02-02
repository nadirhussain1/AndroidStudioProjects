package com.brainpixel.cletracker.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.interfaces.OnCompletedCleCellClicked;
import com.brainpixel.cletracker.model.CLEDataModel;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.activities.ViewCLeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 05/04/2017.
 */

public class CompletedClesFragment extends Fragment {
    @BindView(R.id.summaryRecyclerView)
    RecyclerView completedClesRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.messageTextView)
    TextView messageTextView;

    private ArrayList<CLEDataModel> completedClesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_cles, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchCompletedClesData();
        initRecyclerView();
    }

    private void updateViews() {
        if (completedClesList.size() > 0) {
            initRecyclerView();
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            completedClesRecyclerView.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        completedClesRecyclerView.setLayoutManager(mLayoutManager);
        completedClesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        completedClesRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        CompletedCleAdapter completedCleAdapter = new CompletedCleAdapter(getActivity(), completedClesList);
        completedClesRecyclerView.setAdapter(completedCleAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnCompletedCleCellClicked onCompletedCleCellClicked) {
        String path = onCompletedCleCellClicked.certificateUrl;
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(getActivity(), "No certificate is attached with this CLE", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), ViewCLeActivity.class);
        intent.putExtra(ViewCLeActivity.KEY_CERTIFICATE_PATH, path);
        startActivity(intent);

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void fetchCompletedClesData() {
        showProgressBar();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CLEDataModel cleDataModel = postSnapshot.getValue(CLEDataModel.class);
                    completedClesList.add(cleDataModel);
                }
                hideProgressBar();
                updateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressBar();
                updateViews();
            }
        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("cles").addListenerForSingleValueEvent(valueEventListener);
    }
}
