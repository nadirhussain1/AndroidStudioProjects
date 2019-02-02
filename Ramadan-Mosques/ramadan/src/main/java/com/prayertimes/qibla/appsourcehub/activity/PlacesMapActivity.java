package com.prayertimes.qibla.appsourcehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.prayertimes.qibla.appsourcehub.map.PlacesList;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import muslim.prayers.time.R;

public class PlacesMapActivity extends Utils implements
        com.google.android.gms.maps.GoogleMap.OnMapClickListener,
        com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener,
        LocationListener, com.google.android.gms.maps.OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            setUpMap();
        }
    }

    class location extends AsyncTask {

        String address;
        Bitmap bmImg;
        JSONArray jArrayCal;
        JSONObject jsonc;
        String mobile;
        String name;
        JSONObject poly;
        List polyz;
        JSONObject route;
        JSONArray routesArray;

        private List decodePoly(String s) {
            ArrayList arraylist;
            int i;
            int j;
            int k;
            int l;
            int i1;
            int j1;
            int k1;
            int k2;
            int l2;
            int i3;
            int k3;
            int i2;
            int j2;
            int j3;
            arraylist = new ArrayList();
            i = 0;
            j = s.length();
            k = 0;
            l = 0;
            while (i < j) {
                i1 = 0;
                j1 = 0;
                while (true) {
                    k1 = i + 1;
                    int l1 = -63 + s.charAt(i);
                    j1 |= (l1 & 0x1f) << i1;
                    i1 += 5;
                    if (l1 < 32) {
                        if ((j1 & 1) != 0) {
                            i2 = -1 ^ j1 >> 1;
                        } else {
                            i2 = j1 >> 1;
                        }
                        k += i2;
                        j2 = 0;
                        k2 = 0;
                        l2 = k1;
                        while (true) {
                            i3 = l2 + 1;
                            j3 = -63 + s.charAt(l2);
                            k2 |= (j3 & 0x1f) << j2;
                            j2 += 5;
                            if (j3 < 32) {
                                if ((k2 & 1) != 0) {
                                    k3 = -1 ^ k2 >> 1;
                                } else {
                                    k3 = k2 >> 1;
                                }
                                l += k3;
                                arraylist.add(new LatLng((double) k / 100000D,
                                        (double) l / 100000D));
                                i = i3;
                                break;
                            }
                            l2 = i3;
                        }
                        break;
                    }
                    i = k1;
                }
            }
            return arraylist;
        }

        protected Object doInBackground(Object aobj[]) {
            return doInBackground((String[]) aobj);
        }

        protected String doInBackground(String as[]) {
            StringBuilder stringbuilder;
            Log.i("", " coming ");
            stringbuilder = new StringBuilder();
            try {
                HttpURLConnection httpurlconnection = (HttpURLConnection) (new URL(
                        "http://maps.googleapis.com/maps/api/directions/json?origin=9.936,78.1471&destination=9.9252,78.1197&sensor=true"))
                        .openConnection();
                if (httpurlconnection.getResponseCode() == 200) {
                    BufferedReader bufferedreader = new BufferedReader(
                            new InputStreamReader(
                                    httpurlconnection.getInputStream()), 8192);
                    while (true) {
                        String s = bufferedreader.readLine();
                        if (s == null)
                            break;
                        stringbuilder.append(s);
                    }
                    bufferedreader.close();
                }
                routesArray = (new JSONObject(stringbuilder.toString()))
                        .getJSONArray("routes");
                if (routesArray.length() > 0) {
                    route = routesArray.getJSONObject(0);
                    String s1 = route.getJSONObject("overview_polyline")
                            .getString("points");
                    polyz = decodePoly(s1);
                    Log.i("",
                            (new StringBuilder(String.valueOf(s1)))
                                    .append(" polyz ").append(polyz).toString());
                }
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Object obj) {
            onPostExecute((String) obj);
        }

        protected void onPostExecute(String s) {
            LatLng latlng;
            LatLng latlng1;
            GoogleMap googlemap;
            PolylineOptions polylineoptions;
            LatLng alatlng[];
            if (routesArray.length() <= 0) {
                com.google.android.gms.maps.model.BitmapDescriptor bitmapdescriptor2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.mark_red);
                mMap.addMarker((new MarkerOptions())
                        .position(new LatLng(latitude, longitude)).title("You")
                        .icon(bitmapdescriptor2));
                return;
            }
            try {
                int i = 0;
                while (i < -1 + polyz.size()) {
                    latlng = (LatLng) polyz.get(i);
                    latlng1 = (LatLng) polyz.get(i + 1);
                    googlemap = mMap;
                    polylineoptions = new PolylineOptions();
                    alatlng = new LatLng[2];
                    alatlng[0] = new LatLng(latlng.latitude, latlng.longitude);
                    alatlng[1] = new LatLng(latlng1.latitude, latlng1.longitude);
                    googlemap.addPolyline(polylineoptions.add(alatlng)
                            .width(5F).color(Color.rgb(41, 171, 239))
                            .geodesic(true));
                    i++;
                }
                LatLng latlng2 = (LatLng) polyz.get(0);
                LatLng latlng3 = (LatLng) polyz.get(-1 + polyz.size());
                com.google.android.gms.maps.model.BitmapDescriptor bitmapdescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.mark_blue);
                com.google.android.gms.maps.model.BitmapDescriptor bitmapdescriptor1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.mark_red);
                mMap.addMarker((new MarkerOptions())
                        .position(
                                new LatLng(latlng2.latitude, latlng2.longitude))
                        .title("You").icon(bitmapdescriptor1));
                mMap.addMarker((new MarkerOptions())
                        .position(
                                new LatLng(latlng3.latitude, latlng3.longitude))
                        .title("Demo").icon(bitmapdescriptor));
            } catch (Exception exception) {
            }
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        location() {
            super();
            jsonc = null;
            jArrayCal = null;
        }
    }

    String address;
    Drawable drawable_user;
    String id;
    double latitude;
    double longitude;
    private GoogleMap mMap;
    private int mSelectedMapType;
    PlacesList nearPlaces;
    GoogleMapOptions options;
    String phone;
    PlacesList radius;
    String userLatitude, userLongitude;
    String title;

    public PlacesMapActivity() {
        mSelectedMapType = 0;
        options = new GoogleMapOptions();
    }

    private void setUpMap() {
        com.google.android.gms.maps.CameraUpdate cameraupdate = CameraUpdateFactory
                .newLatLng(new LatLng(Double.parseDouble(userLatitude), Double.parseDouble(userLongitude)));
        com.google.android.gms.maps.CameraUpdate cameraupdate1 = CameraUpdateFactory
                .zoomTo(15F);
        mMap.moveCamera(cameraupdate);
        mMap.animateCamera(cameraupdate1);
        mMap.addMarker((new MarkerOptions())
                .position(
                        new LatLng(Double.parseDouble(userLatitude), Double.parseDouble(userLongitude))).title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(0.0F)));
        int i = 0;
        while (i < MapActivity.lat.size()) {
            mMap.addMarker((new MarkerOptions())
                    .position(
                            new LatLng(Double
                                    .parseDouble((String) MapActivity.lat
                                            .get(i)), Double
                                    .parseDouble((String) MapActivity.lng
                                            .get(i))))
                    .title((String) MapActivity.title.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(210F)));
            i++;
        }

        setMapType(mSelectedMapType);
        options.compassEnabled(true);
        options.rotateGesturesEnabled(true);
        options.tiltGesturesEnabled(true);
        options.zoomControlsEnabled(true);
        SupportMapFragment.newInstance(options);
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);


    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.map_places);
        Actionbar("Nearby Mosque");
        banner_ad();
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0)
            return;

        setUpMapIfNeeded();
        Intent intent = getIntent();
        userLatitude = intent.getStringExtra("user_latitude");
        userLongitude = intent.getStringExtra("user_longitude");
        latitude = Double.parseDouble(loadString(USER_LAT));
        longitude = Double.parseDouble(loadString(USER_LNG));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        menu.findItem(R.id.action_map).setIcon(R.drawable.ic_settings);
        return true;
    }

    public void onInfoWindowClick(Marker marker) {
        Log.i("", (new StringBuilder("click")).append(marker.getSnippet())
                .toString());
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                this);
        builder.setTitle(marker.getTitle())
                .setMessage(marker.getSnippet())
                .setPositiveButton("Ok",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        });
        builder.create().show();
    }

    public void onLocationChanged(Location location1) {
    }

    public void onMapClick(LatLng latlng) {
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        int i = menuitem.getItemId();
        if (i == 0x102002c) {
            finish();
        }
        if (i == R.id.action_map) {
            (new com.afollestad.materialdialogs.MaterialDialog.Builder(this))
                    .title("Select Map Type")
                    .items(R.array.lbl_array_view_type)
                    .itemsCallbackSingleChoice(
                            LoadPrefInt("type"),
                            new com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice() {
                                public boolean onSelection(
                                        MaterialDialog dialog, View itemView,
                                        int which, CharSequence text) {
                                    SavePrefInt("type", which);
                                    mSelectedMapType = which;
                                    setUpMap();
                                    return true;
                                }
                            }).positiveText("OK").show();
        }
        return super.onOptionsItemSelected(menuitem);
    }

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    void setMapType(int i) {
        switch (i) {
            default:
                return;

            case 0: // '\0'
                mMap.setMapType(1);
                return;

            case 1: // '\001'
                mMap.setMapType(4);
                return;

            case 2: // '\002'
                mMap.setMapType(2);
                return;

            case 3: // '\003'
                mMap.setMapType(3);
                break;
        }
    }

}
