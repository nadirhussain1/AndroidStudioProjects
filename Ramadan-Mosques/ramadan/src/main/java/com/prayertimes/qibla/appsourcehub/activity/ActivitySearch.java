package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.*;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterCountry;
import com.prayertimes.qibla.appsourcehub.model.Country;
import com.prayertimes.qibla.appsourcehub.support.Geolocation;
import com.prayertimes.qibla.appsourcehub.support.ServerResponse;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.json.*;
import muslim.prayers.time.R;
public class ActivitySearch extends Utils
{
    private class GeocoderHandler extends Handler
    {
        public void handleMessage(Message message)
        {
            String s;
            switch(message.what)
            {
            default:
                return;

            case 1: // '\001'
                s = message.getData().getString("address");
                break;
            }
            try
            {
                String as[] = s.split("/");
                SavePref(USER_LAT, as[0].trim());
                SavePref(USER_LNG, as[1].trim());
                LogUtils.i((new StringBuilder(String.valueOf(as[0].trim()))).append(" lat").append(as[1].trim()).toString());
                return;
            }
            catch(NullPointerException nullpointerexception)
            {
                nullpointerexception.printStackTrace();
            }
        }

        private GeocoderHandler()
        {
            super();
        }

        GeocoderHandler(GeocoderHandler geocoderhandler)
        {
            this();
        }
    }


    AdapterCountry adapter;
    ArrayList arraylist;
    String city_name[];
    String country_name;
    EditText editsearch;
    Bundle extras;
    ListView list;
    Toolbar toolbar;
    String type;

    public ActivitySearch()
    {
        type = "country";
        arraylist = new ArrayList();
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
label0:
        {
            LogUtils.i((new StringBuilder(" on activity result")).append(j).append(" ").append(intent).toString());
            if(j == 0)
            {
                if(!extras.getString("cat").equals("main"))
                {
                    break label0;
                }
                startActivity(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class));
                finish();
            }
            return;
        }
        finish();
    }

    public void onBackPressed()
    {
        setResult(1, new Intent());
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        
        Intent intent;
        JSONArray jsonarray;
        String as[];
        int i;
        int j;
        
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml(getString(R.string.topbar_select_location)));
        Analytics(getString(R.string.topbar_select_location));
        typeface();
        list = (ListView)findViewById(R.id.listview);
        intent = getIntent();
        extras = intent.getExtras();
        if(extras == null || intent.getStringExtra("type") == null){
        	if(!loadString(USER_CITY).equals(""))
            {
                finish();
            }
            setTitle("Select Country");
            java.util.List list1 = Arrays.asList((Country[])(new GsonBuilder()).create().fromJson((new ServerResponse()).loadJSONReaderFromAsset(this, "country.json"), com.prayertimes.qibla.appsourcehub.model.Country[].class));
            arraylist.addAll(list1);
        }else{
	        type = intent.getStringExtra("type");
	        if(type != null){
		        if(type.equals("country")){
			        setTitle("Select Country");
			        java.util.List list2 = Arrays.asList((Country[])(new GsonBuilder()).create().fromJson((new ServerResponse()).loadJSONReaderFromAsset(this, "country.json"), com.prayertimes.qibla.appsourcehub.model.Country[].class));
			        arraylist.addAll(list2);
		        } else if(type.equals("cities")) {
			        setTitle("Select City");
			        country_name = intent.getStringExtra("country");
			        try
			        {
			            InputStream inputstream = getApplicationContext().getAssets().open("cities.json");
			            byte abyte0[] = new byte[inputstream.available()];
			            inputstream.read(abyte0);
			            inputstream.close();
			            jsonarray = (new JSONObject(new String(abyte0))).getJSONArray(country_name);
			            as = new String[jsonarray.length()];
			            i = 0;
			            while(i < jsonarray.length()){
			            	as[i] = jsonarray.getString(i);
			                i++;
			            }
			            Arrays.sort(as);
			            j = 0;
			            while(j < jsonarray.length())
			            {
			            	arraylist.add(new Country(as[j]));
			                j++;
			            }
			        }
			        catch(IOException ioexception)
			        {
			            ioexception.printStackTrace();
			        }
			        catch(JSONException jsonexception)
			        {
			            jsonexception.printStackTrace();
			        }
		        }
	        }
        }
        AdapterCountry adaptercountry = new AdapterCountry(this, arraylist);
        adapter = adaptercountry;
        list.setAdapter(adapter);
        editsearch = (EditText)findViewById(R.id.search);
        EditText edittext = editsearch;
        TextWatcher textwatcher = new TextWatcher() {
            public void afterTextChanged(Editable editable)
            {
                String s = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(s);
            }

            public void beforeTextChanged(CharSequence charsequence, int k, int l, int i1)
            {
            }

            public void onTextChanged(CharSequence charsequence, int k, int l, int i1)
            {
            }
        };
        edittext.addTextChangedListener(textwatcher);
        ListView listview = list;
        android.widget.AdapterView.OnItemClickListener onitemclicklistener = new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int k, long l)
            {
                if(type.equals("cities"))
                {
                    saveString(USER_LAT, "0.0");
                    saveString(USER_LNG, "0.0");
                    saveString(USER_MLAT, "");
                    saveString(USER_MLNG, "");
                    LogUtils.i((new StringBuilder(String.valueOf(country_name))).append(" city ").append(((Country)arraylist.get(k)).getName()).toString());
                    saveString(USER_CITY, ((Country)arraylist.get(k)).getName());
                    saveString(USER_COUNTRY, country_name);
                    saveString(USER_STATE, "");
                    saveString(USER_STREET, "");
                    (new Geolocation()).getAddressFromLocation((new StringBuilder(String.valueOf(((Country)arraylist.get(k)).getName()))).append(",").append(country_name).toString(), getApplicationContext(), new GeocoderHandler(null));
                    finish();
                    return;
                } else
                {
                    Intent intent1 = new Intent(ActivitySearch.this, com.prayertimes.qibla.appsourcehub.activity.ActivitySearch.class);
                    intent1.putExtra("country", ((Country)arraylist.get(k)).getName());
                    intent1.putExtra("type", "cities");
                    startActivityForResult(intent1, 1);
                    return;
                }
            }
        };
        listview.setOnItemClickListener(onitemclicklistener);
    }
}
