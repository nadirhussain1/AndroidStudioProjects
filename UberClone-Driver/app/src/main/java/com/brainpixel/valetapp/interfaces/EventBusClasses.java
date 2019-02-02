package com.brainpixel.valetapp.interfaces;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by nadirhussain on 06/03/2017.
 */

public class EventBusClasses {
    public static class SearchLocationPressedEvent {
        public List<LatLng> destiRoutePoints;
    }

    public static class SearchEditorInputChanged {
        public String inputText;
    }

    public static class AddressDecodeEvent {
        public String address;
        public boolean isSource;
    }
    public static class PinLocationClickedEvent {

    }
    public static class NetworkStateChanged{

    }
}
