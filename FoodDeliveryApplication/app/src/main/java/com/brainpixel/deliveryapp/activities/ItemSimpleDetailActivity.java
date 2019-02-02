package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnCartCountUpdate;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 08/09/2018.
 */

public class ItemSimpleDetailActivity extends Activity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.cartCountTextView)
    TextView cartCountTextView;
    @BindView(R.id.itemNameView)
    TextView itemNameView;
    @BindView(R.id.itemIconView)
    ImageView itemIconView;
    @BindView(R.id.itemPriceTextView)
    TextView itemPriceTextView;
    @BindView(R.id.itemRatingBar)
    RatingBar itemRatingBar;
    @BindView(R.id.ratingCountTextView)
    TextView ratingCounterView;
    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private MainItem selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);

        selectedItem = GlobalDataManager.getInstance().selectedItem;
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_simple_detail_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViewsWithValues();
    }

    private void bindViewsWithValues() {
        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());

        titleView.setText(selectedItem.getName());
        itemNameView.setText(selectedItem.getName());
        itemPriceTextView.setText("Rs. " + selectedItem.getPrice());
        itemRatingBar.setRating(Float.valueOf(selectedItem.getAvgRating()));
        ratingCounterView.setText("" + selectedItem.getTotalRatings() + " ratings");
        descriptionTextView.setText(selectedItem.getShortDescription());

        if (!TextUtils.isEmpty(selectedItem.getImage())) {
            Picasso.get().load(selectedItem.getImage()).placeholder(R.drawable.item_placeholder).into(itemIconView);
        } else {
            Picasso.get().load(R.drawable.item_placeholder).into(itemIconView);
        }

    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        goBack();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        goBack();
    }

    private void goBack() {
        finish();
    }

    @OnClick(R.id.ratingCountTextView)
    public void openRatingTab() {
        openTabsActivity(TabsDetailsActivity.TAB_RATING);
    }

    @OnClick(R.id.readMoreTextView)
    public void openDescriptionTab() {
        openTabsActivity(TabsDetailsActivity.TAB_DESCRIPTION);
    }

    @OnClick(R.id.buyNowTextView)
    public void buyNowClicked() {
        showProgressBarLayout();
        checkExistenceOfItemInCart();
    }

    private void openTabsActivity(int currentTab) {
        Intent intent = new Intent(ItemSimpleDetailActivity.this, TabsDetailsActivity.class);
        intent.putExtra(TabsDetailsActivity.KEY_CURRENT_TAB, currentTab);
        startActivity(intent);
    }

    private void checkExistenceOfItemInCart() {
        FirebaseFirestore.getInstance().collection("cart")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("mainItemDocumentId", selectedItem.getMainItemDocumentId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots.size() > 0) {
                            hideProgressBarLayout();
                            String message = "" + selectedItem.getName() + " is already in your cart";
                            GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, message, GlobalConstants.TOAST_RED);
                        } else {
                            checkQuantityOfItem();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBarLayout();
                        GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, getString(R.string.add_item_cart_failure_msg), GlobalConstants.TOAST_RED);
                    }
                });
    }

    private void checkQuantityOfItem() {
        FirebaseFirestore.getInstance().collection("quantities").document(selectedItem.getItemId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String quantityString = (String) documentSnapshot.get("quantity");
                        int quantity = Integer.valueOf(quantityString);
                        if (quantity <= 0) {
                            hideProgressBarLayout();
                            String message = "" + selectedItem.getName() + " has been sold out. It will be available by next business day";
                            GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, message, GlobalConstants.TOAST_RED);
                        } else {
                            addItemToCart(quantity);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBarLayout();
                        GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, getString(R.string.add_item_cart_failure_msg), GlobalConstants.TOAST_RED);
                    }
                });
    }

    private void addItemToCart(final int quantity) {
        Map<String, String> newCartItemMap = new HashMap<>();

        newCartItemMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        newCartItemMap.put("cartQuantity", "1");
        newCartItemMap.put("mainItemDocumentId", selectedItem.getMainItemDocumentId());


        FirebaseFirestore.getInstance().collection("cart")
                .add(newCartItemMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        CartItem cartItem = new CartItem();

                        cartItem.setMainItemDocumentId(selectedItem.getMainItemDocumentId());
                        cartItem.setCartDocumentId(documentReference.getId());
                        cartItem.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        cartItem.setCartQuantity("1");
                        cartItem.fillMainItemData(selectedItem);

                        GlobalDataManager.getInstance().cartItems.add(cartItem);


                        String updatedQuantity = "" + (quantity - 1);
                        updateQuantity(updatedQuantity);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBarLayout();
                        GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, getString(R.string.add_item_cart_failure_msg), GlobalConstants.TOAST_RED);
                    }
                });

    }

    private void updateQuantity(String quantity) {
        FirebaseFirestore.getInstance().collection("quantities").document(selectedItem.getItemId())
                .update("quantity", quantity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressBarLayout();
                        String message = "" + selectedItem.getName() + " added to cart successfully";
                        GlobalUtil.showToastMessage(ItemSimpleDetailActivity.this, message, GlobalConstants.TOAST_DARK);

                        cartCountTextView.setText("" + GlobalDataManager.getInstance().cartItems.size());
                        EventBus.getDefault().post(new OnCartCountUpdate());
                    }
                });

    }

}
