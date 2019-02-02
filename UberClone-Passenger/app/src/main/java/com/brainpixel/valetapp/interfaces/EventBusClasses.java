package com.brainpixel.valetapp.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nadirhussain on 06/03/2017.
 */

public class EventBusClasses {
    public static class SearchLocationPressedEvent {
        public LatLng searchedLocLatLng;
        public String locationName;
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

    public static class NetworkStateChanged {

    }

    public static class OnUpcomingTripCancelled {

    }

    public static class UpcomingTripScheduleUpdated {
        public String updatedScheduleTime;
    }
}
