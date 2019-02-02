package com.brainpixel.deliveryapp.views;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnQuantitySelected;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nadirhussain on 17/11/2018.
 */

public class QuantityDialog {
    private Dialog dialog;
    private View dialogContentView;
    private CartItem selectedItem;
    private Activity activity;

    public QuantityDialog(Activity activity, CartItem cartItem) {
        this.selectedItem = cartItem;
        this.activity = activity;

        dialogContentView = View.inflate(activity, R.layout.choose_quantity_layout, null);
        new ScalingUtility(activity).scaleRootView(dialogContentView);
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(dialogContentView);

        dialogContentView.findViewById(R.id.cancelButtonView).setOnClickListener(cancelClickListener);

        fetchAvailableQuantityOfItem(selectedItem);
    }

    public void showDialog() {
        dialog.show();
    }

    private void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    private void onAvailableQualityFetched(int availableQuantity) {
        ListView quantityListView = dialogContentView.findViewById(R.id.quantityListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.row_quantity_item, R.id.quantityValueTextView, getArrayOfPossibleValues(availableQuantity));
        quantityListView.setAdapter(arrayAdapter);

        quantityListView.setVisibility(View.VISIBLE);
        quantityListView.setOnItemClickListener(quantityItemClickListener);
        dialogContentView.findViewById(R.id.loadingBar).setVisibility(View.GONE);
    }

    private String[] getArrayOfPossibleValues(int availableQuantity) {
        String[] array = new String[availableQuantity];
        for (int count = 0; count < availableQuantity; count++) {
            array[count] = "" + (count + 1);
        }
        return array;
    }

    private OnItemClickListener quantityItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int newQuantity = position + 1;
            selectedItem.setCartQuantity("" + newQuantity);
            EventBus.getDefault().post(new OnQuantitySelected());
            cancelDialog();
        }
    };

    private void fetchAvailableQuantityOfItem(final CartItem cartItem) {
        FirebaseFirestore.getInstance().collection("quantities").document(cartItem.getItemId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        int availableQuantity = 100;
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            String quantityString = (String) documentSnapshot.get("quantity");
                            availableQuantity = GlobalUtil.getIntValue(quantityString);
                        }
                        onAvailableQualityFetched(availableQuantity);
                    }
                });

    }

    private OnClickListener cancelClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelDialog();
        }
    };


}
