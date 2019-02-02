package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.util.List;
import tascom.example.tasbeeh.DataBase;
import tascom.example.tasbeeh.DataBaseModel;
import muslim.prayers.time.R;
public class ListActivity extends Utils
{
    public class MyBaseAdapter extends BaseAdapter
    {

        String formatted_excerpt;

        public int getCount()
        {
            return databasefetch.size();
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return 0L;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            View view1 = ((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.listviewcontent, null);
            TextView textview = (TextView)view1.findViewById(R.id.tv_title);
            TextView textview1 = (TextView)view1.findViewById(R.id.tv_desc);
            TextView textview2 = (TextView)view1.findViewById(R.id.tv_count);
            textview.setText(((DataBaseModel)databasefetch.get(i)).getTitle());
            textview1.setText(((DataBaseModel)databasefetch.get(i)).getDescription());
            textview2.setText(String.valueOf(((DataBaseModel)databasefetch.get(i)).getCount()));
            Log.d("Fetching titles", (new StringBuilder()).append(textview).toString());
            return view1;
        }

        public MyBaseAdapter()
        {
            super();
        }
    }


    MyBaseAdapter adapter;
    int countvalue;
    List databasefetch;
    DataBase db;
    ListView lvpost;

    public ListActivity()
    {
        db = null;
    }

    public void backClick(int i, int j, String s)
    {
        Intent intent = new Intent();
        intent.putExtra("countvalue", i);
        Log.d("sending count from listactivity", (new StringBuilder(String.valueOf(i))).toString());
        intent.putExtra("idvalue", j);
        intent.putExtra("maintitle", s);
        setResult(-1, intent);
        finish();
    }

    public void onBackPressed()
    {
        backClick(-1, -1, "");
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tasbee_list);
        Actionbar("Tasbeeh List");
        Analytics("Tasbeeh List");
        typeface();
        banner_ad();
        lvpost = (ListView)findViewById(R.id.listcontent);
        adapter = new MyBaseAdapter();
        db = new DataBase(getApplication());
        db.openDataBase();
        databasefetch = db.fetchAllFeedList(0);
        int i = 0;
        do
        {
            if(i >= databasefetch.size())
            {
                lvpost.setAdapter(adapter);
                lvpost.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView adapterview, View view, int j, long l)
                    {
                        int k = ((DataBaseModel)databasefetch.get(j)).getCount();
                        int i1 = ((DataBaseModel)databasefetch.get(j)).getKey_id();
                        String s = ((DataBaseModel)databasefetch.get(j)).getTitle();
                        Log.d("ID", (new StringBuilder(String.valueOf(i1))).toString());
                        backClick(k, i1, s);
                    }
                });
                return;
            }
            Log.d("Size", ((DataBaseModel)databasefetch.get(i)).getTitle());
            Log.d("ID", (new StringBuilder(String.valueOf(((DataBaseModel)databasefetch.get(i)).getKey_id()))).toString());
            i++;
        } while(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId())
        {
        default:
            return super.onOptionsItemSelected(menuitem);

        case 16908332: 
            backClick(-1, -1, "");
            break;
        }
        return true;
    }
}
