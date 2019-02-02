package com.edwardvanraak.materialbarcodescannerexample.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 27/03/2017.
 */

public class MyCostFragment extends Fragment {
    @BindView(R.id.costEditor)
    EditText costEditor;
    @BindView(R.id.inboundShippingEditor)
    EditText inboundShippingEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mycost_screen, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        setInitialValues();
        return rootView;
    }

    private void setInitialValues() {
        costEditor.setText("" + CustomPreferences.getMyCostValue(getActivity()));
        inboundShippingEditor.setText("" + CustomPreferences.getInboundShipping(getActivity()));
    }

    @OnEditorAction(R.id.inboundShippingEditor)
    public boolean onNickNameEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            saveValues();
            return true;
        }
        return false;
    }

    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        saveValues();
    }

    private void saveValues() {
        float cost = 3.75f;
        float inboundShipping = 1;

        if (!TextUtils.isEmpty(costEditor.getText())) {
            cost = Float.valueOf(costEditor.getText().toString());
        }
        if (!TextUtils.isEmpty(inboundShippingEditor.getText())) {
            inboundShipping = Float.valueOf(inboundShippingEditor.getText().toString());
        }

        CustomPreferences.saveMyCostValue(getActivity(), cost);
        CustomPreferences.saveInboundShippingValue(getActivity(), inboundShipping);
        hideKeyboard();
        Toast.makeText(getActivity(), "values updated successfully", Toast.LENGTH_SHORT).show();
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inboundShippingEditor.getWindowToken(), 0);
    }
}
