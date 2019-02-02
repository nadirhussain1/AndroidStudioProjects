package com.prayertimes.qibla.appsourcehub.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.prayertimes.qibla.appsourcehub.adpater.AdapterMessages;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterNames;
import com.prayertimes.qibla.appsourcehub.model.Messages;
import com.prayertimes.qibla.appsourcehub.utils.PListParser;
import com.prayertimes.qibla.appsourcehub.utils.PMessageListParser;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.util.*;

import muslim.prayers.time.R;
public class ActivityMessageList extends Utils
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
            ActivityMessageList.data = PMessageListParser.getMessages(ActivityMessageList.this);
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((String)obj);
        }

        protected void onPostExecute(String s)
        {
            if(limit == 0){
            	ActivityMessageList.data.addAll(dblist);
                pDialog.dismiss();
                ActivityMessageList.adapter = new AdapterMessages(ActivityMessageList.this, ActivityMessageList.data, ActivityMessageList.this.tf, ActivityMessageList.this.tf2);
                ActivityMessageList.this.list_messages.setAdapter(ActivityMessageList.adapter);
                ActivityMessageList localActivityNamesList = ActivityMessageList.this;
                localActivityNamesList.limit = (10 + localActivityNamesList.limit);
                super.onPostExecute(s);
            }else{
            	Iterator localIterator = ActivityMessageList.this.dblist.iterator();
            	while(localIterator.hasNext()){
            		Messages localAllahNames = (Messages)localIterator.next();
                    ActivityMessageList.data.add(localAllahNames);
            	}
            	ActivityMessageList.adapter.notifyDataSetChanged();
            }
        }
        
        protected void onPreExecute()
        {
            pDialog = new ProgressDialog(ActivityMessageList.this);
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


    public static AdapterMessages adapter;
    public static List data = new ArrayList();
    List dblist;
    RelativeLayout layout_allah;
    int limit;
    ListView list_messages;
    Toolbar toolbar;

    public ActivityMessageList()
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
        setContentView(R.layout.activity_messages_list);
        Actionbar(getString(R.string.title_lbl_message));
        Analytics(getString(R.string.title_lbl_message));
        banner_ad();
        list_messages = (ListView)findViewById(R.id.list_messages);
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
