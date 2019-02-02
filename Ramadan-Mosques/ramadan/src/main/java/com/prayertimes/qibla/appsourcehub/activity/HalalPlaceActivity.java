package com.prayertimes.qibla.appsourcehub.activity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterNearByHalalPlace;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import muslim.prayers.time.R;

public class HalalPlaceActivity extends Utils implements OnMapReadyCallback {

    // the foursquare client_id and the client_secret

    // ============== YOU SHOULD MAKE NEW KEYS ====================//
    final String CLIENT_ID = "IYGUT2N0YBBIF3MTNIZGPL1RV3FH0JZKI5H020L10WJUZFTD";
    final String CLIENT_SECRET = "PQAD4RRMXLJWV4G4LJJ3MBKWDCG1Z3P04AXEMDM2SWWZVPU5";

    // we will need to take the latitude and the logntitude from a certain point
    // this is the center of New York
    Double latitude;
    Double longtitude;

    static ArrayList<String> idList, nameList, addressList, distList;
    static ArrayList<Double> latList;
    static ArrayList<Double> longList;

    ArrayAdapter<String> myAdapter;
    static Context ctx;
    public GPSTracker gps;

    ListView list;
    AdapterNearByHalalPlace adapter;
    public AdRequest adRequest;

    // google map
    public GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_activity);

        gps = new GPSTracker(HalalPlaceActivity.this);

        idList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        addressList = new ArrayList<String>();
        latList = new ArrayList<Double>();
        longList = new ArrayList<Double>();
        distList = new ArrayList<String>();

        list = (ListView) findViewById(R.id.listView1);
        ctx = getApplicationContext();

        adRequest = (new AdRequest.Builder()).build();

        Actionbar(getString(R.string.title_lbl_places));

        getCurretnLocation();

        // start the AsyncTask that makes the call for the venus search.
        new LocationAsync().execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.halal_place, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        /*
		 * if (adapter.getCount()>1) { menuitem.getItemId(); return
		 * super.onOptionsItemSelected(menuitem); } return false;
		 */
        switch (menuitem.getItemId()) {
            case R.id.action_location_map:
                if (adapter.getCount() > 0) {
                    startActivity(new Intent(
                            this,
                            com.prayertimes.qibla.appsourcehub.activity.ActivityPlaceLocation.class));
                }
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(menuitem);
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

            for (int i = 0; i < latList.size(); i++) {
                // Adding a marker
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(latList.get(i), longList
                                .get(i)));

                // changing marker color
                BitmapDescriptor locationIcon = BitmapDescriptorFactory
                        .fromResource(R.drawable.location_icon);
                marker.icon(locationIcon);

                googleMap.addMarker(marker);

                // Move the camera to last position with a zoom level
						/*
						 * PlaceAddressList.add(halalPlaceAddress(DisplayLatitude
						 * .get(i), DisplayLongitude.get(i)));
						 */
            }

            if (gps.canGetLocation()) {

                MarkerOptions marker2 = new MarkerOptions()
                        .position(new LatLng(latitude, longtitude));
                marker2.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                googleMap.addMarker(marker2);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longtitude))
                        .zoom(7).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LocationAsync extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://api.foursquare.com/v2/venues/search?client_id="
                    + CLIENT_ID
                    + "&client_secret="
                    + CLIENT_SECRET
                    + "&v=20130815&ll="
                    + latitude
                    + ","
                    + longtitude
                    + "&categoryId=52e81612bcbc57f1066b79ff");
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right
                getData(temp);

                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < idList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    listTitle.add(nameList.get(i));
                }

                adapter = new AdapterNearByHalalPlace(getApplicationContext(),
                        nameList, distList, addressList, idList,
                        HalalPlaceActivity.this.tf, HalalPlaceActivity.this.tf2);

                if (!adapter.isEmpty()) {
                    list.setAdapter(adapter);
                } else {
                    String[] values = new String[]{"No halal restaurants were found around your current location"};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                            HalalPlaceActivity.this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1, values);

                    list.setAdapter(adapter1);
                }
                initilizeMap();


            }
        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        // trim the whitespaces
        return replyString.trim();
    }

    public void getCurretnLocation() {

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longtitude = gps.getLongitude();
            System.out.println("---location:" + latitude + longtitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private static void getData(String response) {

        try {
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response")
                            .getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        idList.add(jsonArray.getJSONObject(i).getString("id"));
                        nameList.add(jsonArray.getJSONObject(i).getString(
                                "name"));

                        if (jsonArray.getJSONObject(i).has("location")) {

                            Double lat = Double.parseDouble(jsonArray
                                    .getJSONObject(i).getJSONObject("location")
                                    .getString("lat"));
                            Double lng = Double.parseDouble(jsonArray
                                    .getJSONObject(i).getJSONObject("location")
                                    .getString("lng"));
                            latList.add(lat);
                            longList.add(lng);
                            distList.add(jsonArray.getJSONObject(i)
                                    .getJSONObject("location")
                                    .getString("distance"));

                            if (jsonArray.getJSONObject(i)
                                    .getJSONObject("location")
                                    .has("formattedAddress")) {

                                // addressList.add(jsonArray.getJSONObject(i).getJSONObject("location").getString("address"));
                                String address = "";
                                JSONArray formaterdAddress = jsonArray
                                        .getJSONObject(i)
                                        .getJSONObject("location")
                                        .getJSONArray("formattedAddress");
                                for (int j = 0; j < formaterdAddress.length(); j++) {

                                    address = address
                                            + formaterdAddress.getString(j);
                                }
                                addressList.add(address);
                            } else {
                                addressList.add(null);
                            }
                        }
                    }
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
}
