package com.prayertimes.qibla.appsourcehub.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterNearByLocationList;
import com.prayertimes.qibla.appsourcehub.database.Locationdbhelper;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import muslim.prayers.time.R;

public class PlaceActivity extends Utils implements OnMapReadyCallback {


    public Locationdbhelper dbOpenHelper;
    public ArrayList<String> NearByPlaceNameList, NearByPlaceDistList,
            PlaceAddressList;
    public ArrayList<bean> listLocation;
    public static ArrayList<Double> DisplayLatitude, DisplayLongitude;
    public SQLiteDatabase database;

    public static String DB_NAME = "Geoname.sqlite";
    public static String TABLE_NAME = "locations";
    public static String ID = "id";
    public static String PLACE_NAME = "name";
    public static String COUNTRY_NAME = "country_name";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";

    ListView list;
    AdapterNearByLocationList adapter;

    // GPSTracker class
    public GPSTracker gps;
    public double latitude;
    public double longitude;
    public bean Bean;
    public static int i = 0;

    // google map
    public GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_activity);

        gps = new GPSTracker(PlaceActivity.this);
        getCountryName();

        DisplayLatitude = new ArrayList<Double>();
        DisplayLongitude = new ArrayList<Double>();
        NearByPlaceNameList = new ArrayList<String>();
        NearByPlaceDistList = new ArrayList<String>();
        PlaceAddressList = new ArrayList<String>();

        dbOpenHelper = new Locationdbhelper(PlaceActivity.this, DB_NAME);
        database = dbOpenHelper.openDataBase();

        Actionbar(getString(R.string.title_lbl_places));

        list = (ListView) findViewById(R.id.listView1);

        new Task().execute();

    }

    public String halalPlaceAddress(Double latitude, Double longitude) {
        String address = null;

        Geocoder geocoder = new Geocoder(PlaceActivity.this,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

            address = cityName + "," + stateName;
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return address;
    }

    private void loadDatabase() {
        String where = "country_name=?";
        String[] args = {"Iran"};

        Cursor locationCursor = database.query(TABLE_NAME, new String[]{ID,
                        PLACE_NAME, LATITUDE, LONGITUDE, COUNTRY_NAME}, null, null,
                null, null, null);
        locationCursor.moveToFirst();
        listLocation = new ArrayList<bean>();
        if (!locationCursor.isAfterLast()) {
            do {
                bean Bean = new bean();
                Bean.id = locationCursor.getString(0);
                Bean.placename = locationCursor.getString(1);
                Bean.latitude = locationCursor.getString(2);
                Bean.longitude = locationCursor.getString(3);
                Bean.countryName = locationCursor.getString(4);
                listLocation.add(Bean);
            } while (locationCursor.moveToNext());
        }

        locationCursor.close();

        getLatitudeLongitude();

    }

    public void getLatitudeLongitude() {

        for (int i = 0; i < listLocation.size(); i++) {
            double latitude1 = Double.parseDouble(listLocation.get(i)
                    .getLatitude());
            double longitude1 = Double.parseDouble(listLocation.get(i)
                    .getLongitude());
            Double distance = calculateDistance(latitude, longitude, latitude1,
                    longitude1);

            if (distance < 70) {
                DisplayLatitude.add(latitude1);
                DisplayLongitude.add(longitude1);
                NearByPlaceNameList.add(listLocation.get(i).getPlacename());
                NearByPlaceDistList.add(distance + "");
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_loation, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        menuitem.getItemId();
        return super.onOptionsItemSelected(menuitem);
    }

    public void getCountryName() {

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            // Loading map

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Showing / hiding your current location
            googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            for (int i = 0; i < DisplayLatitude.size(); i++) {
                // Adding a marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(
                        DisplayLatitude.get(i), DisplayLongitude.get(i)));

                // changing marker color
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                googleMap.addMarker(marker);

                // Move the camera to last position with a zoom level
                PlaceAddressList.add(halalPlaceAddress(DisplayLatitude.get(i),
                        DisplayLongitude.get(i)));
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    MarkerOptions marker2 = new MarkerOptions()
                            .position(new LatLng(latitude, longitude));
                    marker2.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    googleMap.addMarker(marker2);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitude, longitude)).zoom(7)
                            .build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GPSTracker extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10
        // meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1
        // minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener Calling this function will stop using GPS in
         * your app
         */
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog On pressing Settings button
         * will lauch Settings Options
         */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }

    public static double calculateDistance(double fromLatitude,
                                           double fromLongitude, double toLatitude, double toLongitude) {

        float results[] = new float[1];

        try {
            Location.distanceBetween(fromLatitude, fromLongitude, toLatitude,
                    toLongitude, results);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }

        int dist = (int) results[0];
        if (dist <= 0)
            return 0D;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        results[0] /= 1000D;
        String distance = decimalFormat.format(results[0]);
        double d = Double.parseDouble(distance);

        System.out.println("-----distance:===" + i);
        i++;
        return d;
    }

    /**
     * function to load map If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    private class Task extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(mactivity);
            pDialog.setMessage("Searching Nearby...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            loadDatabase();

            return null;
        }

        protected void onPostExecute(String params) {
            super.onPostExecute(params);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            adapter = new AdapterNearByLocationList(
                    getApplicationContext(), NearByPlaceNameList,
                    NearByPlaceDistList, PlaceAddressList, PlaceActivity.this.tf,
                    PlaceActivity.this.tf2);
            list.setAdapter(adapter);
            initilizeMap();

        }
    }
}
