package com.brainpixel.deliveryapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 08/09/2018.
 */

public class DescriptionFragment extends Fragment {
    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;

    private MainItem selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = attachViews(inflater);
        selectedItem = GlobalDataManager.getInstance().selectedItem;
        return view;
    }

    private View attachViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.tab_description_layout, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        descriptionTextView.setText("" + selectedItem.getLongDescription());
    }

}
