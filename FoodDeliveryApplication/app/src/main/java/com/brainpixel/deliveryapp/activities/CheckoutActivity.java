package com.brainpixel.deliveryapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.fragments.AddressFragment;
import com.brainpixel.deliveryapp.fragments.OrderCompletedFragment;
import com.brainpixel.deliveryapp.fragments.PaymentFragment;
import com.brainpixel.deliveryapp.fragments.ScheduleFragment;
import com.brainpixel.deliveryapp.fragments.ShippingFragment;
import com.brainpixel.deliveryapp.handlers.GoToAddressTabEvent;
import com.brainpixel.deliveryapp.handlers.GoToOrderCompletedEvent;
import com.brainpixel.deliveryapp.handlers.GoToPaymentTab;
import com.brainpixel.deliveryapp.handlers.GoToShipmentTabEvent;
import com.brainpixel.deliveryapp.handlers.HomeTabSwitchListener;
import com.brainpixel.deliveryapp.handlers.LaunchPlacePickerEvent;
import com.brainpixel.deliveryapp.handlers.OnAddressSelected;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.ConfigData;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.model.ShippingAddress;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.firestore.DocumentReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 17/11/2018.
 */

public class CheckoutActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1000;
    private static final int TAB_SCHEDULE = 1;
    private static final int TAB_ADDRESS = 2;
    private static final int TAB_PAYMENT = 3;
    private static final int TAB_SHIPMENT = 4;
    private static final int TAB_COMPLETED = 5;

    @BindView(R.id.scheduleLayout)
    LinearLayout scheduleLayout;
    @BindView(R.id.addressLayout)
    LinearLayout addressLayout;
    @BindView(R.id.shippingLayout)
    LinearLayout shippingLayout;
    @BindView(R.id.paymentLayout)
    LinearLayout paymentLayout;
    @BindView(R.id.tabsLayout)
    LinearLayout tabsLayout;
    @BindView(R.id.backIconView)
    ImageView backIconView;

    private GoogleApiClient mClient;
    private int currentTab = TAB_SCHEDULE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API)
                .build();

        GlobalDataManager.getInstance().createNewOrder();
        loadScheduleFragment();
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_checkout, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
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

    public void goBack() {
        if(currentTab==TAB_COMPLETED){
            EventBus.getDefault().post(new HomeTabSwitchListener());
            finish();
            return;
        }
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                finish();
            }
        };

        GlobalUtil.showCustomizedAlert(this, getString(R.string.title_going_back), getString(R.string.message_going_back),
                getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.no_label), null);
    }

    private void loadScheduleFragment() {
        currentTab = TAB_SCHEDULE;
        ScheduleFragment fragment = new ScheduleFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, fragment).commitAllowingStateLoss();
    }

    private void loadAddressFragment() {
        currentTab = TAB_ADDRESS;
        AddressFragment fragment = new AddressFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, fragment).commitAllowingStateLoss();
    }

    private void loadPaymentFragment() {
        currentTab = TAB_PAYMENT;
        PaymentFragment fragment = new PaymentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, fragment).commitAllowingStateLoss();
    }

    private void loadShipmentFragment() {
        currentTab = TAB_SHIPMENT;
        ShippingFragment fragment = new ShippingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, fragment).commitAllowingStateLoss();
    }

    private void loadOrderCompletedFragment(DocumentReference orderReference) {
        currentTab = TAB_COMPLETED;
        backIconView.setVisibility(View.INVISIBLE);
        tabsLayout.setVisibility(View.GONE);

        Bundle bundle=new Bundle();
        bundle.putString(OrderCompletedFragment.KEY_ORDER_REFERENCE,orderReference.getId());

        OrderCompletedFragment fragment = new OrderCompletedFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, fragment).commitAllowingStateLoss();
    }

    public void resetColorOfAllTabs() {
        scheduleLayout.setBackgroundColor(Color.WHITE);
        addressLayout.setBackgroundColor(Color.WHITE);
        paymentLayout.setBackgroundColor(Color.WHITE);
        shippingLayout.setBackgroundColor(Color.WHITE);
    }

    @OnClick(R.id.scheduleLayout)
    public void scheduleLayoutClicked() {
        resetColorOfAllTabs();
        scheduleLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        loadScheduleFragment();
    }

    @OnClick(R.id.addressLayout)
    public void addressLayoutClicked() {
        Order currentOrder = GlobalDataManager.getInstance().currentOrder;
        if (currentOrder.getDeliverySchedule() == null) {
            GlobalUtil.showToastMessage(this, "Please select delivery dates first", GlobalConstants.TOAST_RED);
            return;
        }
        resetColorOfAllTabs();
        addressLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        loadAddressFragment();
    }

    @OnClick(R.id.paymentLayout)
    public void paymentLayoutClicked() {
        Order currentOrder = GlobalDataManager.getInstance().currentOrder;
        if (currentOrder.getDeliverySchedule() == null) {
            GlobalUtil.showToastMessage(this, "Please select delivery dates first", GlobalConstants.TOAST_RED);
            return;
        }
        if (currentOrder.getShippingAddress() == null) {
            GlobalUtil.showToastMessage(this, "Please enter valid shipping address first.", GlobalConstants.TOAST_RED);
            return;
        }
        resetColorOfAllTabs();
        paymentLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        loadPaymentFragment();
    }

    @OnClick(R.id.shippingLayout)
    public void shippingLayoutClicked() {
        Order currentOrder = GlobalDataManager.getInstance().currentOrder;
        if (currentOrder.getDeliverySchedule() == null) {
            GlobalUtil.showToastMessage(this, "Please select delivery dates first", GlobalConstants.TOAST_RED);
            return;
        }
        if (currentOrder.getShippingAddress() == null) {
            GlobalUtil.showToastMessage(this, "Please enter valid shipping address first.", GlobalConstants.TOAST_RED);
            return;
        }
        resetColorOfAllTabs();
        shippingLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));
        loadShipmentFragment();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoToAddressTabEvent event) {
        scheduleLayout.setBackgroundColor(Color.WHITE);
        addressLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));

        loadAddressFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoToPaymentTab event) {
        addressLayout.setBackgroundColor(Color.WHITE);
        paymentLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));

        loadPaymentFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoToShipmentTabEvent event) {
        paymentLayout.setBackgroundColor(Color.WHITE);
        shippingLayout.setBackgroundColor(Color.parseColor("#F3F9F5"));

        loadShipmentFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoToOrderCompletedEvent event) {
        loadOrderCompletedFragment(event.orderReference);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LaunchPlacePickerEvent event) {
        launchPlacePicker();
    }

    private void launchPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                ConfigData configData = GlobalDataManager.getInstance().configData;

                Location selectedLocation = new Location("PointA");
                selectedLocation.setLatitude(place.getLatLng().latitude);
                selectedLocation.setLongitude(place.getLatLng().longitude);

                Location centralLocation = new Location("PointB");
                centralLocation.setLatitude(configData.getCenterLatitude());
                centralLocation.setLongitude(configData.getCenterLongitude());

                if (centralLocation.distanceTo(selectedLocation) > GlobalDataManager.getInstance().configData.getRadius()) {
                    GlobalUtil.showToastMessage(this, "We do not provide services for this area. Reselect your location", GlobalConstants.TOAST_RED);
                    launchPlacePicker();
                } else {
                    OnAddressSelected event = new OnAddressSelected();

                    ShippingAddress selectedAddressItem = new ShippingAddress();
                    selectedAddressItem.setLatitude(place.getLatLng().latitude);
                    selectedAddressItem.setLongitude(place.getLatLng().longitude);
                    selectedAddressItem.setDescription(place.getAddress().toString());

                    event.selectedAddressItem = selectedAddressItem;
                    EventBus.getDefault().post(event);
                }

            }
        }
    }
}
