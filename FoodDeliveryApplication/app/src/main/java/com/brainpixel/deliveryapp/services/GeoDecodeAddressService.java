package com.brainpixel.deliveryapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import com.brainpixel.deliveryapp.handlers.OnLatLngDecodeEvent;
import com.brainpixel.deliveryapp.utils.GlobalUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

/**
 * Created by nadirhussain on 20/11/2018.
 */


public class GeoDecodeAddressService extends IntentService {
    public static String KEY_LATLNG = "KEY_LATLNG";


    public GeoDecodeAddressService() {
        super("GeoDecodeAddressService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String latLng = intent.getStringExtra(KEY_LATLNG);

        double latitude = Double.valueOf(latLng.split(",")[0]);
        double longitude = Double.valueOf(latLng.split(",")[1]);

        String address = getAddressFromLatLng(latitude, longitude);
        OnLatLngDecodeEvent event = new OnLatLngDecodeEvent();
        event.selectedAddressItem.setLatitude(latitude);
        event.selectedAddressItem.setLongitude(longitude);
        event.selectedAddressItem.setDescription(address);



        EventBus.getDefault().post(event);
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        String place = "Unknown Place";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            place = address.getAddressLine(0);
            GlobalUtil.printLog("GeoDebug", "" + address.toString());
        } catch (Exception exception) {
            GlobalUtil.printLog("GeoDebug", "" + exception);
        }
        return place;
    }
}

