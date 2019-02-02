package com.prayertimes.qibla.appsourcehub.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterNames;
import com.prayertimes.qibla.appsourcehub.model.AllahNames;
import com.prayertimes.qibla.appsourcehub.utils.PListParser;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.util.*;
import muslim.prayers.time.R;
public class ActivityNamesList extends Utils
{
    public class Namelist extends AsyncTask
    {

        ProgressDialog pDialog;

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((String[])aobj);
        }

        protected String doInBackground(String as[])
        {
            ActivityNamesList.data = PListParser.getAllahNames(ActivityNamesList.this);
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((String)obj);
        }

        protected void onPostExecute(String s)
        {
            if(limit == 0){
            	ActivityNamesList.data.addAll(dblist);
                pDialog.dismiss();
                ActivityNamesList.adapter = new AdapterNames(ActivityNamesList.this, ActivityNamesList.data, ActivityNamesList.this.tf, ActivityNamesList.this.tf2);
                ActivityNamesList.this.list_names.setAdapter(ActivityNamesList.adapter);
                ActivityNamesList localActivityNamesList = ActivityNamesList.this;
                localActivityNamesList.limit = (10 + localActivityNamesList.limit);
                super.onPostExecute(s);
            }else{
            	Iterator localIterator = ActivityNamesList.this.dblist.iterator();
            	while(localIterator.hasNext()){
            		AllahNames localAllahNames = (AllahNames)localIterator.next();
                    ActivityNamesList.data.add(localAllahNames);
            	}
            	ActivityNamesList.adapter.notifyDataSetChanged();
            }
        }
        
        protected void onPreExecute()
        {
            pDialog = new ProgressDialog(ActivityNamesList.this);
            pDialog.setMessage("Loading Names List");
            pDialog.setCancelable(false);
            if(limit == 0)
            {
                pDialog.show();
            }
            super.onPreExecute();
        }

        public Namelist()
        {
            super();
        }
    }


    public static AdapterNames adapter;
    public static List data = new ArrayList();
    List dblist;
    RelativeLayout layout_allah;
    int limit;
    ListView list_names;
    Toolbar toolbar;

    public ActivityNamesList()
    {
        dblist = new ArrayList();
        limit = 0;
    }

    public List getAllahNames()
    {
        return dblist;
    }

    public void onCreate(Bundle bundle)
    {
        //fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_names_list);
        Actionbar(getString(R.string.title_lbl_names));
        Analytics(getString(R.string.title_lbl_names));
        banner_ad();
        list_names = (ListView)findViewById(R.id.list_names);
        (new Namelist()).execute(new String[0]);
    }

    protected void onResume()
    {
        if(isOnline())
        {
            findViewById(R.id.ad_layout).setVisibility(0);
        } else
        {
            findViewById(R.id.ad_layout).setVisibility(8);
        }
        super.onResume();
    }

    public void onStop()
    {
        super.onStop();
    }



}
