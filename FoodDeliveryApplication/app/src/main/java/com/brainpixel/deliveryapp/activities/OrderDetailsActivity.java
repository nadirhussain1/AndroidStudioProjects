package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.adapters.OrderStatusAdapter;
import com.brainpixel.deliveryapp.adapters.ShippingItemsAdapter;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.handlers.OrderCancelled;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.model.ShippingAddress;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.brainpixel.deliveryapp.utils.VerticalSpaceItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 25/11/2018.
 */

public class OrderDetailsActivity extends Activity {
    public static final String KEY_SELECTED_ORDER = "KEY_SELECTED_ORDER";

    @BindView(R.id.orderNumberTextView)
    TextView orderNumberTextView;
    @BindView(R.id.totalBillTextView)
    TextView totalBillTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.orderStatusRecyclerView)
    RecyclerView orderStatusRecyclerView;
    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.cancelOrderTextView)
    TextView cancelOrderTextView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;


    private Order selectedOrder = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);

        if (getIntent() != null) {
            selectedOrder = (Order) getIntent().getSerializableExtra(KEY_SELECTED_ORDER);
        }
        bindViewsWithValues();

    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.order_detail_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void bindViewsWithValues() {
        orderNumberTextView.setText("ORDER NUMBER:" + selectedOrder.getOrderNumber());
        totalBillTextView.setText(selectedOrder.getTotalBill());

        ShippingAddress shippingAddress = selectedOrder.getShippingAddress();
        String description = shippingAddress.getDescription();

        String formattedAddress = shippingAddress.getReceiverName()
                + "\n" + description.split(",")[0] + "," + description.split(",")[1]
                + "\n" + description.split(",")[2] + "," + description.split(",")[3];

        addressTextView.setText(formattedAddress);

        ShippingItemsAdapter shippingItemsAdapter = new ShippingItemsAdapter(this, selectedOrder.getShippingItem());
        int space = (int) ScalingUtility.resizeDimension(10);
        itemsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(space));
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(shippingItemsAdapter);

        OrderStatusAdapter statusAdapter = new OrderStatusAdapter(this, selectedOrder.getDeliverySchedule());
        orderStatusRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderStatusRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderStatusRecyclerView.setAdapter(statusAdapter);

        if (selectedOrder.getOrderStatus().contentEquals(GlobalConstants.STATUS_ACTIVE)) {
            cancelOrderTextView.setVisibility(View.VISIBLE);
        } else {
            cancelOrderTextView.setVisibility(View.GONE);
        }
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.cancelOrderTextView)
    public void cancelOrderClicked() {
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                updateStatusToCancelInFirebase();
            }
        };

        GlobalUtil.showCustomizedAlert(this, "Cancel Order", "Are you sure that you want to cancel this order?",
                getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.no_label), null);
    }

    private void onOrderCancelled() {
        hideProgressBarLayout();
        cancelOrderTextView.setVisibility(View.GONE);

        OrderCancelled event = new OrderCancelled();
        event.order = selectedOrder;
        EventBus.getDefault().post(event);
    }

    private void updateStatusToCancelInFirebase() {
        showProgressBarLayout();
        FirebaseFirestore.getInstance().collection("orders").document(selectedOrder.getOrderNumber())
                .update("orderStatus", GlobalConstants.STATUS_CANCELLED)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onOrderCancelled();
                    }
                });
    }
}
