package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.OrderDetailsActivity;
import com.brainpixel.deliveryapp.adapters.MyOrdersAdapter;
import com.brainpixel.deliveryapp.handlers.OrderCancelled;
import com.brainpixel.deliveryapp.handlers.OrderItemClicked;
import com.brainpixel.deliveryapp.handlers.SwitchToMainFragmentEvent;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 24/11/2018.
 */

public class MyOrdersFragment extends Fragment {
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;
    @BindView(R.id.ordersRecyclerView)
    RecyclerView ordersRecyclerView;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;

    private List<Order> ordersList = new ArrayList<>();
    private MyOrdersAdapter ordersAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders_screen, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDataFromFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.continueShoppingButton)
    public void continueShoppingClicked() {
        EventBus.getDefault().post(new SwitchToMainFragmentEvent());
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void showOrdersDataOnScreen() {
        hideProgressBarLayout();
        if (ordersList.size() == 0) {
            showEmptyScreen();
        } else {
            showRecyclerView();
        }

    }

    private void showEmptyScreen() {
        emptyLayout.setVisibility(View.VISIBLE);
        ordersRecyclerView.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        ordersAdapter = new MyOrdersAdapter(getActivity(), ordersList);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ordersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ordersRecyclerView.setAdapter(ordersAdapter);

        emptyLayout.setVisibility(View.GONE);
        ordersRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadDataFromFirebase() {
        showProgressBarLayout();
        FirebaseFirestore.getInstance().collection("orders")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (task.isSuccessful() && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                Order item = documentSnapshot.toObject(Order.class);
                                item.setOrderNumber(documentSnapshot.getId());

                                ordersList.add(item);
                            }
                        }
                        showOrdersDataOnScreen();

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderItemClicked event) {
        Order order = ordersList.get(event.position);
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.KEY_SELECTED_ORDER, order);
        getActivity().startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderCancelled event) {
        Order cancelledOrder = event.order;
        for (Order item : ordersList) {
            if (item.getOrderNumber().contentEquals(cancelledOrder.getOrderNumber())) {
                item.setOrderStatus(GlobalConstants.STATUS_CANCELLED);
                break;
            }
        }
        ordersAdapter.notifyDataSetChanged();

    }

}
