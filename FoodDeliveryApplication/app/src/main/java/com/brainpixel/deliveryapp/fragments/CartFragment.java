package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.CheckoutActivity;
import com.brainpixel.deliveryapp.adapters.CartAdapter;
import com.brainpixel.deliveryapp.handlers.HomeTabSwitchListener;
import com.brainpixel.deliveryapp.handlers.MainToolbarHandler;
import com.brainpixel.deliveryapp.handlers.OnCartCountUpdate;
import com.brainpixel.deliveryapp.handlers.OnItemClickListener;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.handlers.OnQuantitySelected;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.GridItemDecoration;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class CartFragment extends Fragment implements MainToolbarHandler, OnItemClickListener {
    @BindView(R.id.itemsLayout)
    RelativeLayout itemsLayout;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;
    @BindView(R.id.cartRecyclerView)
    RecyclerView cartItemsRecyclerView;
    @BindView(R.id.totalItemsTextView)
    TextView totalItemsTextView;
    @BindView(R.id.totalBillTextView)
    TextView totalBillTextView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;


    private CartAdapter cartAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment_layout, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int space = (int) ScalingUtility.resizeDimension(10);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(space);
        cartItemsRecyclerView.addItemDecoration(gridItemDecoration);

        switchToGrid();
        showCartDataOnScreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void switchToGrid() {
        cartItemsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (cartAdapter != null) {
            cartAdapter.displayGridView();
        }

    }

    public void switchToList() {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (cartAdapter != null) {
            cartAdapter.displayListView();
        }

    }

    @Override
    public void filterResults(String filter) {
        if (cartAdapter != null) {
            cartAdapter.filterResults(filter);
        }
    }
    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.shoppingTextView)
    public void onContinueShoppingClicked() {
        EventBus.getDefault().post(new HomeTabSwitchListener());
    }

    @OnClick(R.id.checkoutTextView)
    public void onCheckoutClicked() {
        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnQuantitySelected event) {
        cartAdapter.notifyDataSetChanged();

        totalBillTextView.setText("Rs." + getTotalBill());
    }


    private void showEmptyList() {
        itemsLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    private void showCartDataOnScreen() {
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(getActivity(), GlobalDataManager.getInstance().cartItems, this);
            cartItemsRecyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.refreshItems(GlobalDataManager.getInstance().cartItems);
        }

        if (GlobalDataManager.getInstance().cartItems.isEmpty()) {
            showEmptyList();
        } else {
            itemsLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            totalItemsTextView.setText("" + GlobalDataManager.getInstance().cartItems.size() + " items");
            totalBillTextView.setText("Rs." + getTotalBill());
        }
    }

    private int getTotalBill() {
        int totalBill = 0;
        for (CartItem cartItem : GlobalDataManager.getInstance().cartItems) {
            if (cartItem.getPrice() != null) {
                int quantity = GlobalUtil.getIntValue(cartItem.getCartQuantity());
                int price = GlobalUtil.getIntValue(cartItem.getPrice());

                totalBill = totalBill + (quantity * price);
            }
        }
        return totalBill;
    }

    @Override
    public void itemClicked(final MainItem mainItem) {
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                deleteCartItemFromFirebase((CartItem) mainItem);
            }
        };

        GlobalUtil.showCustomizedAlert(getActivity(), getString(R.string.title_removing_item), getString(R.string.message_removing_item), getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.cancel_label), null);

    }

    private void deleteCartItemFromFirebase(final CartItem cartItem) {
        showProgressBarLayout();
        FirebaseFirestore.getInstance().collection("cart").document(cartItem.getCartDocumentId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GlobalDataManager.getInstance().cartItems.remove(cartItem);
                        redeemQuantityOfItem(cartItem);
                    }
                });
    }

    private void redeemQuantityOfItem(final CartItem cartItem) {
        FirebaseFirestore.getInstance().collection("quantities").document(cartItem.getItemId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            String quantityString = (String) documentSnapshot.get("quantity");
                            int oldQuantity = GlobalUtil.getIntValue(quantityString);
                            int newQuantity = oldQuantity + GlobalUtil.getIntValue(cartItem.getCartQuantity());
                            updateQuantity(cartItem, "" + newQuantity);
                        } else {
                            onRemoveOperationComplete();
                        }
                    }
                });

    }

    private void updateQuantity(CartItem cartItem, String newQuantity) {
        FirebaseFirestore.getInstance().collection("quantities").document(cartItem.getItemId())
                .update("quantity", newQuantity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onRemoveOperationComplete();
                    }
                });

    }

    private void onRemoveOperationComplete() {
        hideProgressBarLayout();
        showCartDataOnScreen();
        EventBus.getDefault().post(new OnCartCountUpdate());
    }
}
