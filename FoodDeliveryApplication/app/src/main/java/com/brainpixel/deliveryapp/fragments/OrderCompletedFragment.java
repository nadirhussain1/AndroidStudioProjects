package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.OrderDetailsActivity;
import com.brainpixel.deliveryapp.handlers.HomeTabSwitchListener;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 21/11/2018.
 */

public class OrderCompletedFragment extends Fragment {
    public static final String KEY_ORDER_REFERENCE = "KEY_ORDER_REFERENCE";

    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private String orderFirebaseId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_complete, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            orderFirebaseId = getArguments().getString(KEY_ORDER_REFERENCE);
        }
        return view;
    }


    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.continueShoppingButton)
    public void onContinueShoppingClick() {
        EventBus.getDefault().post(new HomeTabSwitchListener());
        getActivity().finish();
    }

    @OnClick(R.id.orderDetailsButton)
    public void onOrderDetailsButtonClick() {
        showProgressBarLayout();
        loadOrderFromFirebase();
    }

    private void showDetailsOfOrder(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.KEY_SELECTED_ORDER, order);
        getActivity().startActivity(intent);
    }

    private void loadOrderFromFirebase() {
        FirebaseFirestore.getInstance().collection("orders").document(orderFirebaseId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        hideProgressBarLayout();
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Order order = documentSnapshot.toObject(Order.class);
                            order.setOrderNumber(orderFirebaseId);
                            showDetailsOfOrder(order);
                        }
                    }
                });
    }
}
