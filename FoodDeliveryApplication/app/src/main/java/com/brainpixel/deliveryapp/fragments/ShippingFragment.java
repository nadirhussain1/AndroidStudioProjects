package com.brainpixel.deliveryapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.adapters.ShippingItemsAdapter;
import com.brainpixel.deliveryapp.handlers.GoToOrderCompletedEvent;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.model.DeliveryModel;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.brainpixel.deliveryapp.utils.VerticalSpaceItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 21/11/2018.
 */

public class ShippingFragment extends Fragment {
    @BindView(R.id.receiverNameTextView)
    TextView receiverNameView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.scheduleRecyclerView)
    ListView scheduleListView;
    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.totalAmountTextView)
    TextView totalAmountTextView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private Order currentOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentOrder = GlobalDataManager.getInstance().currentOrder;

        receiverNameView.setText(currentOrder.getShippingAddress().getReceiverName());
        addressTextView.setText(currentOrder.getShippingAddress().getDescription());
        totalAmountTextView.setText(currentOrder.getTotalBill());

        List<String> deliveryDates=new ArrayList<>();
        for(DeliveryModel model:currentOrder.getDeliverySchedule()){
            deliveryDates.add(model.getDate());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_order_date, R.id.quantityValueTextView, deliveryDates);
        scheduleListView.setAdapter(arrayAdapter);

        ShippingItemsAdapter ratingsAdapter = new ShippingItemsAdapter(getActivity(), currentOrder.getShippingItem());
        int space = (int) ScalingUtility.resizeDimension(10);
        itemsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(space));
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsRecyclerView.setAdapter(ratingsAdapter);
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.confirmOrderTextView)
    public void confirmOrder() {
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                addNewOrderToFirebase(currentOrder);
            }
        };

        GlobalUtil.showCustomizedAlert(getActivity(), getString(R.string.title_confirm_order), getString(R.string.message_confirm_order), getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.no_label), null);
    }

    private void addNewOrderToFirebase(Order order) {
        showProgressBarLayout();
        FirebaseFirestore.getInstance().collection("orders").add(order).addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    deleteCartItems();
                    initDelayTimer(task.getResult());
                } else {
                    hideProgressBarLayout();
                    GlobalUtil.showToastMessage(getActivity(), "Order could not be posted. Try again", GlobalConstants.TOAST_RED);
                }
            }
        });
    }

    private void deleteCartItems() {
        for (CartItem cartItem : GlobalDataManager.getInstance().cartItems) {
            FirebaseFirestore.getInstance().collection("cart").document(cartItem.getCartDocumentId()).delete();
        }
        GlobalDataManager.getInstance().cartItems.clear();
    }

    private void onOrderComplete(DocumentReference orderRef) {
        hideProgressBarLayout();
        GoToOrderCompletedEvent event=new GoToOrderCompletedEvent();
        event.orderReference =orderRef;
        EventBus.getDefault().post(event);

    }

    private void initDelayTimer(DocumentReference orderRef) {
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onOrderComplete(orderRef);
                    }
                });

            }
        }, 3000);
    }

}
