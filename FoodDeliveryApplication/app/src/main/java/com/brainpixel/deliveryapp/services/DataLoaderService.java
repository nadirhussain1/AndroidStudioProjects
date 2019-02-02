package com.brainpixel.deliveryapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.brainpixel.deliveryapp.handlers.OnDataLoaded;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.model.ConfigData;
import com.brainpixel.deliveryapp.model.MainItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 17/11/2018.
 */

public class DataLoaderService extends IntentService {
    public DataLoaderService() {
        super("DataLoaderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        fetchHomeItemsFromFirebase();
    }

    private void fetchHomeItemsFromFirebase() {
        GlobalDataManager.getInstance().homeItems.clear();

        FirebaseFirestore.getInstance().collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                if (task.isSuccessful() && !querySnapshot.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        MainItem item = documentSnapshot.toObject(MainItem.class);
                        item.setMainItemDocumentId(documentSnapshot.getId());

                        GlobalDataManager.getInstance().homeItems.add(item);
                    }
                }
                fetchCartItemsFromFirebase();

            }
        });

    }

    private void fetchCartItemsFromFirebase() {
        final ArrayList<CartItem> cartItems = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("cart")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (task.isSuccessful() && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                CartItem item = documentSnapshot.toObject(CartItem.class);
                                item.setCartDocumentId(documentSnapshot.getId());

                                cartItems.add(item);
                            }
                        }
                        fillPropertiesOfMainItems(cartItems);
                    }
                });

    }

    private void fillPropertiesOfMainItems(ArrayList<CartItem> cartItems) {
        for (final CartItem cartItem : cartItems) {
            FirebaseFirestore.getInstance().collection("items").document(cartItem.getMainItemDocumentId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        MainItem mainItem = documentSnapshot.toObject(MainItem.class);
                        cartItem.fillMainItemData(mainItem);
                    }
                }
            });
        }
        GlobalDataManager.getInstance().cartItems = cartItems;
        fetchConfigurations();
    }

    private void fetchConfigurations() {
        FirebaseFirestore.getInstance().collection("configurations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                if (task.isSuccessful() && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    ConfigData configData = documentSnapshot.toObject(ConfigData.class);
                    GlobalDataManager.getInstance().configData = configData;
                }
                sendCompleteSignal();

            }
        });
    }

    private void sendCompleteSignal() {
        EventBus.getDefault().post(new OnDataLoaded());
    }


}
