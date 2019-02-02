package com.prayertimes.qibla.appsourcehub.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.map.GooglePlaces;
import com.prayertimes.qibla.appsourcehub.map.Place;
import com.prayertimes.qibla.appsourcehub.map.PlaceDetails;
import com.prayertimes.qibla.appsourcehub.map.PlacesList;
import com.prayertimes.qibla.appsourcehub.support.AppLocationService;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class MapActivity extends Utils
{
    class LoadPlaces extends AsyncTask
    {
        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((String[])aobj);
        }

        protected String doInBackground(String as[])
        {
            googlePlaces = new GooglePlaces();
            try
            {
                double d = nradius;
                Log.d("main radius", (new StringBuilder(String.valueOf(d))).toString());
                nearPlaces = googlePlaces.search(latitude, longitude, d, "mosque");
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((String)obj);
        }

        protected void onPostExecute(String s)
        {
            runOnUiThread(new Runnable() {
                public void run()
                {
                	if(nearPlaces == null)
                		return;
                    if(!nearPlaces.status.equals("OK")){
                    	Alert(getString(R.string.app_name), "Error occured, Try after some time");
                        return;
                    }
                    if(nearPlaces.results == null)
                    	return;
                    Iterator iterator = nearPlaces.results.iterator();
                    while(iterator.hasNext()){
                    	Place place = (Place)iterator.next();
                        HashMap hashmap = new HashMap();
                        hashmap.put(MapActivity.KEY_REFERENCE, place.reference);
                        hashmap.put(MapActivity.KEY_NAME, place.name);
                        hashmap.put(MapActivity.KEY_LAT, String.valueOf(place.geometry.location.lat));
                        hashmap.put(MapActivity.KEY_LNG, String.valueOf(place.geometry.location.lng));
                        MapActivity.title.add(place.name);
                        placesListItems.add(hashmap);
                        maplist.add(hashmap);
                    }
                    MapActivity.ListItems = placesListItems;
                    LogUtils.i((new StringBuilder(" map size ")).append(placesListItems.size()).toString());
                    if(placesListItems.size() > 0)
                    {
                        (new distance()).execute(new Void[0]);
                    }
                }
            });
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setCancelable(false);
            pDialog.show();
        }


        LoadPlaces()
        {
            super();
        }
    }

    class distance extends AsyncTask<Void, Void, Void>
    {
        ArrayList<HashMap<String, String>> placesListItems;

        protected Void doInBackground(Void avoid[])
        {
        	HashMap hashmap;
            String s;
            String as[];
            Document document;
            ArrayList arraylist;
            int k;String s1;
            String s3;
            NodeList nodelist;
            int j = 0;
            while(j < maplist.size())
            {
            	hashmap = new HashMap();
                s = (new StringBuilder("http://maps.google.com/maps/api/directions/xml?origin=")).append(latitude).append(",").append(longitude).append("&destination=").append((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT)).append(",").append((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG)).append("&sensor=false&units=metric").toString();
                as = (new String[] {
                    "text"
                });
				DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                BasicHttpContext basichttpcontext = new BasicHttpContext();
                try{
                	HttpPost temp1 = new HttpPost(s);
                	HttpResponse temp2 = defaulthttpclient.execute(temp1, basichttpcontext);
                	HttpEntity temp3 = temp2.getEntity();
	                java.io.InputStream inputstream = temp3.getContent();
	                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputstream);
	                if(document != null){
	    	            arraylist = new ArrayList();
	    	            k = as.length;
	    	            int l = 0;
	    	            while(l < k){
	    	            	nodelist = document.getElementsByTagName(as[l]);
	    	                if(nodelist.getLength() > 0)
	    	                    arraylist.add(nodelist.item(-1 + nodelist.getLength()).getTextContent());
	    	                else
	    	                	arraylist.add(" - ");
	    	                l++;
	    	            }
	    	            Object aobj[] = new Object[1];
	    	            aobj[0] = arraylist.get(0);
	    	            s1 = String.format("%s", aobj);
	    	            String s2 = (new StringBuilder()).append((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT)).toString();
	    	            StringBuilder stringbuilder = new StringBuilder(String.valueOf((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG)));
	    	            Log.i(s2, stringbuilder.append(" url ").append(s1).toString());
	    	            s3 = s1.replace(" km", "");
	    	            if(s3.contains("-"))
	    	            {
	    	                s3 = "0";
	    	            }
	    	            if(Float.parseFloat(s3) <= 5F)
		                {
		                    hashmap.put(MapActivity.KEY_LAT, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    hashmap.put(MapActivity.KEY_LNG, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    hashmap.put(MapActivity.KEY_DISTANCE, s1);
		                    hashmap.put(MapActivity.KEY_NAME, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_NAME));
		                    hashmap.put(MapActivity.KEY_REFERENCE, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_REFERENCE));
		                    MapActivity.lat_5.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    MapActivity.lng_5.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    maplist_5.add(hashmap);
		                }
		                if(Float.parseFloat(s3) <= 10F)
		                {
		                    hashmap.put(MapActivity.KEY_LAT, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    hashmap.put(MapActivity.KEY_LNG, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    hashmap.put(MapActivity.KEY_DISTANCE, s1);
		                    hashmap.put(MapActivity.KEY_NAME, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_NAME));
		                    hashmap.put(MapActivity.KEY_REFERENCE, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_REFERENCE));
		                    MapActivity.lat_10.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    MapActivity.lng_10.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    maplist_10.add(hashmap);
		                }
		                if(Float.parseFloat(s3) <= 15F)
		                {
		                    hashmap.put(MapActivity.KEY_LAT, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    hashmap.put(MapActivity.KEY_LNG, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    hashmap.put(MapActivity.KEY_DISTANCE, s1);
		                    hashmap.put(MapActivity.KEY_NAME, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_NAME));
		                    hashmap.put(MapActivity.KEY_REFERENCE, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_REFERENCE));
		                    MapActivity.lat_15.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                    MapActivity.lng_15.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                    maplist_15.add(hashmap);
		                }
		                hashmap.put(MapActivity.KEY_LAT, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                hashmap.put(MapActivity.KEY_LNG, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                hashmap.put(MapActivity.KEY_DISTANCE, s1);
		                hashmap.put(MapActivity.KEY_NAME, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_NAME));
		                hashmap.put(MapActivity.KEY_REFERENCE, (String)((HashMap)maplist.get(j)).get(MapActivity.KEY_REFERENCE));
		                MapActivity.lat_all.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LAT));
		                MapActivity.lng_all.add((String)((HashMap)maplist.get(j)).get(MapActivity.KEY_LNG));
		                MapActivity.lat = MapActivity.lat_all;
		                MapActivity.lng = MapActivity.lng_all;
		                maplist_all.add(hashmap);
		                placesListItems.add(hashmap);
	                }
                }catch(Exception e){}
                j++;
            }
            return null;
        }

        protected void onPostExecute(Void void1)
        {
            pDialog.dismiss();
            MapActivity mapactivity = MapActivity.this;
            MapActivity mapactivity1 = MapActivity.this;
            ArrayList arraylist = placesListItems;
            String as[] = new String[2];
            as[0] = MapActivity.KEY_DISTANCE;
            as[1] = MapActivity.KEY_NAME;
            mapactivity.adapter = new SimpleAdapter(mapactivity1, arraylist, R.layout.list_item, as, new int[] {
                R.id.reference, R.id.name
            });
            lv.setAdapter(adapter);
        }

        public void onPreExecute()
        {
            super.onPreExecute();
        }

        distance()
        {
            super();
            placesListItems = new ArrayList();
        }
    }


    public static String KEY_DISTANCE = "distance";
    public static String KEY_LAT = "lat";
    public static String KEY_LNG = "lng";
    public static String KEY_NAME = "name";
    public static String KEY_REFERENCE = "reference";
    public static String KEY_VICINITY = "vicinity";
    public static ArrayList ListItems = new ArrayList();
    public static ArrayList lat = new ArrayList();
    public static ArrayList lat_10 = new ArrayList();
    public static ArrayList lat_15 = new ArrayList();
    public static ArrayList lat_5 = new ArrayList();
    public static ArrayList lat_all = new ArrayList();
    public static ArrayList lng = new ArrayList();
    public static ArrayList lng_10 = new ArrayList();
    public static ArrayList lng_15 = new ArrayList();
    public static ArrayList lng_5 = new ArrayList();
    public static ArrayList lng_all = new ArrayList();
    public static ArrayList title = new ArrayList();
    ListAdapter adapter;
    RadioButton allkm;
    AppLocationService appLocationService;
    TextView btnShowOnMap;
    RadioButton fifteenkm;
    RadioButton fivekm;
    double fiveradius;
    GooglePlaces googlePlaces;
    int i;
    Boolean isInternetPresent;
    double latitude;
    double longitude;
    ListView lv;
    ArrayList maplist;
    ArrayList maplist_10;
    ArrayList maplist_15;
    ArrayList maplist_5;
    ArrayList maplist_all;
    int n;
    PlacesList nearPlaces;
    double nradius;
    ProgressDialog pDialog;
    PlaceDetails placeDetails;
    ArrayList placesListItems;
    RadioGroup radiobutton;
    RadioButton tenkm;

    public MapActivity()
    {
        isInternetPresent = Boolean.valueOf(false);
        n = 0;
        placesListItems = new ArrayList();
        maplist = new ArrayList();
        maplist_5 = new ArrayList();
        maplist_10 = new ArrayList();
        maplist_15 = new ArrayList();
        maplist_all = new ArrayList();
        nradius = 100000D;
        i = 0;
    }

    public void getLoc()
    {
        Location location = appLocationService.getLocation();
        LogUtils.i((new StringBuilder("location ")).append(location).toString());
        if(location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            saveString(USER_LAT, String.valueOf(latitude));
            saveString(USER_LNG, String.valueOf(longitude));
            return;
        } else
        {
            latitude = 0.0D;
            longitude = 0.0D;
            showSettingsAlert();
            return;
        }
    }

    public void listrefresh()
    {
        ArrayList arraylist = placesListItems;
        String as[] = new String[2];
        as[0] = KEY_LAT;
        as[1] = KEY_LNG;
        SimpleAdapter simpleadapter = new SimpleAdapter(this, arraylist, R.layout.list_item, as, new int[] {
            R.id.reference, R.id.name
        });
        lv.setAdapter(simpleadapter);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_map_list);
        Actionbar(getString(R.string.lbl_mosquee));
        typeface();
        banner_ad();
        Analytics(getString(R.string.lbl_mosquee));
        radiobutton = (RadioGroup)findViewById(R.id.radiogroupbox);
        fivekm = (RadioButton)findViewById(R.id.fivekm);
        tenkm = (RadioButton)findViewById(R.id.tenkm);
        fifteenkm = (RadioButton)findViewById(R.id.fifteenkm);
        allkm = (RadioButton)findViewById(R.id.allkm);
        allkm.setSelected(true);
        fivekm.setSelected(false);
        tenkm.setSelected(false);
        fifteenkm.setSelected(false);
        appLocationService = new AppLocationService(this);
        if(loadString(USER_LAT).equals(""));
        lv = (ListView)findViewById(R.id.list);
        btnShowOnMap = (TextView)findViewById(R.id.btn_show_map);
        btnShowOnMap.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(MapActivity.lat.size() > 0)
                {
                    Intent intent = new Intent(MapActivity.this, com.prayertimes.qibla.appsourcehub.activity.PlacesMapActivity.class);
                    intent.putExtra("user_latitude", Double.toString(latitude));
                    intent.putExtra("user_longitude", Double.toString(longitude));
                    intent.putExtra("near_places", nearPlaces);
                    startActivity(intent);
                    return;
                } else
                {
                    Toast.makeText(MapActivity.this, getString(R.string.toast_nomosquetolist), 0).show();
                    return;
                }
            }
        });
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int j, long l)
            {
                Locale locale = Locale.ENGLISH;
                Object aobj[] = new Object[2];
                aobj[0] = Double.valueOf(Double.parseDouble((String)((HashMap)placesListItems.get(j)).get(MapActivity.KEY_LAT)));
                aobj[1] = Double.valueOf(Double.parseDouble((String)((HashMap)placesListItems.get(j)).get(MapActivity.KEY_LNG)));
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format(locale, "http://maps.google.com/maps?daddr=%f,%f", aobj)));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
        allkm.setChecked(true);
        radiobutton.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radiogroup, int j)
            {
                try{
                	googlePlaces = new GooglePlaces();
	                if(j == R.id.fivekm){
		                nradius = 5000D;
		                placesListItems = maplist_5;
		                MapActivity.lat = MapActivity.lat_5;
		                MapActivity.lng = MapActivity.lng_5;
		                adapter = null;
		                MapActivity mapactivity7 = MapActivity.this;
		                MapActivity mapactivity8 = MapActivity.this;
		                ArrayList arraylist3 = maplist_5;
		                String as3[] = new String[2];
		                as3[0] = MapActivity.KEY_DISTANCE;
		                as3[1] = MapActivity.KEY_NAME;
		                mapactivity7.adapter = new SimpleAdapter(mapactivity8, arraylist3, R.layout.list_item, as3, new int[] {
		                    R.id.reference, R.id.name
		                });
		                lv.setAdapter(adapter);
	                }else if(j == R.id.tenkm)
	                {
	                	nradius = 10000D;
	                    MapActivity.lat = MapActivity.lat_10;
	                    MapActivity.lng = MapActivity.lng_10;
	                    adapter = null;
	                    MapActivity mapactivity = MapActivity.this;
	                    MapActivity mapactivity1 = MapActivity.this;
	                    ArrayList arraylist = maplist_10;
	                    String as[] = new String[2];
	                    as[0] = MapActivity.KEY_DISTANCE;
	                    as[1] = MapActivity.KEY_NAME;
	                    mapactivity.adapter = new SimpleAdapter(mapactivity1, arraylist, R.layout.list_item, as, new int[] {
	                        R.id.reference, R.id.name
	                    });
	                    lv.setAdapter(adapter);
	                } else if(j == R.id.fifteenkm){
		                nradius = 15000D;
		                MapActivity.lat = MapActivity.lat_15;
		                MapActivity.lng = MapActivity.lng_15;
		                adapter = null;
		                MapActivity mapactivity5 = MapActivity.this;
		                MapActivity mapactivity6 = MapActivity.this;
		                ArrayList arraylist2 = maplist_15;
		                String as2[] = new String[2];
		                as2[0] = MapActivity.KEY_DISTANCE;
		                as2[1] = MapActivity.KEY_NAME;
		                mapactivity5.adapter = new SimpleAdapter(mapactivity6, arraylist2, R.layout.list_item, as2, new int[] {
		                    R.id.reference, R.id.name
		                });
		                lv.setAdapter(adapter);
	                }else if(j == R.id.allkm){
		                nradius = 100000D;
		                MapActivity.lat = MapActivity.lat_all;
		                MapActivity.lng = MapActivity.lng_all;
		                adapter = null;
		                MapActivity mapactivity3 = MapActivity.this;
		                MapActivity mapactivity4 = MapActivity.this;
		                ArrayList arraylist1 = maplist_all;
		                String as1[] = new String[2];
		                as1[0] = MapActivity.KEY_DISTANCE;
		                as1[1] = MapActivity.KEY_NAME;
		                mapactivity3.adapter = new SimpleAdapter(mapactivity4, arraylist1, R.layout.list_item, as1, new int[] {
		                    R.id.reference, R.id.name
		                });
		                lv.setAdapter(adapter);
	                }
	                MapActivity mapactivity2 = MapActivity.this;
	                mapactivity2.i = 1 + mapactivity2.i;
                } catch(Exception exception) {
                    Log.d("Error message", exception.getMessage());
                }
            }
        });
        latitude = Double.parseDouble(loadString(USER_LAT));
        longitude = Double.parseDouble(loadString(USER_LNG));
        LogUtils.i((new StringBuilder(String.valueOf(latitude))).append(" lat lng ").append(longitude).toString());
        run();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuitem = menu.findItem(R.id.action_settings);
        menuitem.setIcon(R.drawable.ic_action_refresh_dark);
        menuitem.setVisible(false);
        return true;
    }

    protected void onDestroy()
    {
        if(appLocationService != null)
        {
            appLocationService.stopUsingGPS();
            stopService(new Intent(this, com.prayertimes.qibla.appsourcehub.support.AppLocationService.class));
        }
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        int j = menuitem.getItemId();
        if(j == 0x102002c)
        {
            finish();
        }
        if(j == R.id.action_settings)
        {
            if(isOnline())
            {
                getLocation();
                latitude = getLatitude();
                longitude = getLongitude();
                maplist.clear();
                maplist_10.clear();
                maplist_15.clear();
                maplist_all.clear();
                placesListItems.clear();
                if(latitude == 0.0D && longitude == 0.0D)
                {
                    showSettingsAlert();
                } else
                {
                    (new LoadPlaces()).execute(new String[0]);
                }
            } else
            {
                showalert();
            }
        }
        return super.onOptionsItemSelected(menuitem);
    }

    protected void onResume()
    {
        super.onResume();
    }

    public void run()
    {
        if(isOnline())
        {
            if(latitude == 0.0D && longitude == 0.0D)
                showSettingsAlert();
            else
                (new LoadPlaces()).execute(new String[0]);
        } else
            showalert();
    }

    public void showSettingsAlert()
    {
        (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title("").content("Can't get the latitude & longitude, Check Settings").cancelable(false).positiveText("Settings").positiveColor(Color.parseColor("#379e24")).negativeText(0x1040000).negativeColor(Color.parseColor("#379e24")).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onPositive(MaterialDialog materialdialog)
            {
                super.onPositive(materialdialog);
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                startActivity(intent);
            }
        }).build().show();
    }

}
