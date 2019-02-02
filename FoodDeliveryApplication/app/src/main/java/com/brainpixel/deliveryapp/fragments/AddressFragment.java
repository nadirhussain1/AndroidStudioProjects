package com.brainpixel.deliveryapp.fragments;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.GoToPaymentTab;
import com.brainpixel.deliveryapp.handlers.LaunchPlacePickerEvent;
import com.brainpixel.deliveryapp.handlers.OnAddressSelected;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 18/11/2018.
 */

public class AddressFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @BindView(R.id.addressOneTextView)
    TextView addressOneTextView;
    @BindView(R.id.addressTwoTextView)
    TextView addressTwoTextView;
    @BindView(R.id.addressThreeTextView)
    TextView addressThreeTextView;
    @BindView(R.id.nextTextView)
    TextView nextTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleLocationPermission();
        prePopulatePreviousAddress();
    }

    private void prePopulatePreviousAddress() {
        Order currentOrder = GlobalDataManager.getInstance().currentOrder;
        if (currentOrder.getShippingAddress() != null) {
            String[] array = currentOrder.getShippingAddress().getDescription().split(",");
            addressOneTextView.setText(array[0] + "," + array[1]);
            addressTwoTextView.setText(array[2] + "," + array[3]);
            nextTextView.setBackgroundResource(R.drawable.green_rounded_bg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnAddressSelected event) {
        if (event.selectedAddressItem != null && event.selectedAddressItem.getDescription() != null) {
            String[] array = event.selectedAddressItem.getDescription().split(",");
            addressOneTextView.setText(array[0] + "," + array[1]);
            addressTwoTextView.setText(array[2] + "," + array[3]);

            event.selectedAddressItem.setReceiverName(PreferenceManager.getUserName(getActivity()));

            GlobalDataManager.getInstance().currentOrder.setShippingAddress(event.selectedAddressItem);
            nextTextView.setBackgroundResource(R.drawable.green_rounded_bg);
        }

    }

    @OnClick(R.id.locationIconView)
    public void onLocationIconClicked() {
        navigateToSearchAddressScreen();
    }

    @OnClick(R.id.addressOneTextView)
    public void onAddressFieldClicked() {
        navigateToSearchAddressScreen();
    }

    @OnClick(R.id.nextTextView)
    public void onNextClick() {
        if (GlobalDataManager.getInstance().currentOrder.getShippingAddress() == null) {
            GlobalUtil.showToastMessage(getActivity(), "Enter valid address", GlobalConstants.TOAST_RED);
            return;
        }
        EventBus.getDefault().post(new GoToPaymentTab());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleLocationSettings();
        }
    }

    private void navigateToSearchAddressScreen() {
        EventBus.getDefault().post(new LaunchPlacePickerEvent());
    }

    private boolean hasPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void handleLocationPermission() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            handleLocationSettings();
        }
    }

    private void handleLocationSettings() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isgpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isnetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isgpsEnabled && !isnetworkEnabled) {
            showLocationEnableSettingsDialog();
        }
    }

    private void showLocationEnableSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.settings_label));
        builder.setMessage(getActivity().getString(R.string.location_enable_dialog_message));
        builder.setPositiveButton(getActivity().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchLocationSettingsActivity();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void launchLocationSettingsActivity() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        getActivity().startActivity(intent);
    }


}
