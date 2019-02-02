package com.brainpixel.deliveryapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.model.Specification;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 08/09/2018.
 */

public class SpecificationFragment extends Fragment {
    @BindView(R.id.colourTextView)
    TextView colourTextView;
    @BindView(R.id.materialTextView)
    TextView materialTextView;
    @BindView(R.id.warrantyTextView)
    TextView warrantyTextView;
    @BindView(R.id.skuTextView)
    TextView skuTextView;
    @BindView(R.id.brandTextView)
    TextView brandTextView;
    @BindView(R.id.weightTextView)
    TextView weightTextView;


    private MainItem selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = attachViews(inflater);
        selectedItem= GlobalDataManager.getInstance().selectedItem;
        return view;
    }

    private View attachViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.tab_specifications_layout, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (selectedItem != null) {
            fetchItemSpecificationFromFirebase(selectedItem.getItemId());
        }
    }

    private void bindValuesWithViews(Specification specification) {
        colourTextView.setText(specification.getColor());
        materialTextView.setText(specification.getMaterial());
        warrantyTextView.setText(specification.getWarranty());
        skuTextView.setText(specification.getSku());
        brandTextView.setText(specification.getBrand());
        weightTextView.setText(specification.getWeight());
    }

    private void fetchItemSpecificationFromFirebase(String itemId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("specifications").document(itemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Specification specification = document.toObject(Specification.class);
                    bindValuesWithViews(specification);
                }
            }
        });
    }

}
