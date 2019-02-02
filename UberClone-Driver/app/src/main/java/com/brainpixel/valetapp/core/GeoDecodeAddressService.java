package com.brainpixel.valetapp.core;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import com.brainpixel.valetapp.interfaces.EventBusClasses.AddressDecodeEvent;
import com.brainpixel.valetapp.utils.GlobalUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

/**
 * Created by nadirhussain on 08/03/2017.
 */

public class GeoDecodeAddressService extends IntentService {
    public static String KEY_LATLNG = "KEY_LATLNG";
    public static String KEY_SOURCE_DEST = "KEY_SOURCE_DEST";

    public GeoDecodeAddressService() {
        super("GeoDecodeAddressService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String latLng = intent.getStringExtra(KEY_LATLNG);
        boolean isSource = intent.getBooleanExtra(KEY_SOURCE_DEST, true);

        double latitude = Double.valueOf(latLng.split(",")[0]);
        double longitude = Double.valueOf(latLng.split(",")[1]);

        String address = getAddressFromLatLng(latitude, longitude);
        AddressDecodeEvent event = new AddressDecodeEvent();
        event.address = address;
        event.isSource = isSource;

        EventBus.getDefault().post(event);
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        String place = "Unknown Place";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            place = address.getAddressLine(0) + "," + address.getAddressLine(1);
            GlobalUtil.printLog("GeoDebug", "" + address.toString());
        } catch (Exception exception) {
            GlobalUtil.printLog("GeoDebug", "" + exception);
        }
        return place;
    }
}
