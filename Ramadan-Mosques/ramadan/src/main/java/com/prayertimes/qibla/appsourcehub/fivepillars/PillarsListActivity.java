package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ExpandableListView;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.util.*;
import muslim.prayers.time.R;
public class PillarsListActivity extends Utils
    implements android.view.View.OnClickListener
{

    ExpandableListView expListView;
    Bundle extra;
    HashMap listDataChild;
    List listDataHeader;
    ExpandableListAdapter listadapter;
    String name;
    String type;

    public PillarsListActivity()
    {
        name = "";
        type = "";
    }

    private void FastingList()
    {
        listDataHeader = new ArrayList();
        listDataChild = new HashMap();
        listDataHeader.add(getString(R.string.fasting_lheader1));
        listDataHeader.add(getString(R.string.fasting_lheader2));
        listDataHeader.add(getString(R.string.fasting_lheader3));
        listDataHeader.add(getString(R.string.fasting_lheader4));
        listDataHeader.add(getString(R.string.fasting_lheader5));
        listDataHeader.add(getString(R.string.fasting_lheader6));
        listDataHeader.add(getString(R.string.fasting_lheader7));
        listDataHeader.add(getString(R.string.fasting_lheader8));
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        ArrayList arraylist7 = new ArrayList();
        listDataChild.put((String)listDataHeader.get(0), arraylist);
        listDataChild.put((String)listDataHeader.get(1), arraylist1);
        listDataChild.put((String)listDataHeader.get(2), arraylist2);
        listDataChild.put((String)listDataHeader.get(3), arraylist3);
        listDataChild.put((String)listDataHeader.get(4), arraylist4);
        listDataChild.put((String)listDataHeader.get(5), arraylist5);
        listDataChild.put((String)listDataHeader.get(6), arraylist6);
        listDataChild.put((String)listDataHeader.get(7), arraylist7);
    }

    private void HajiList()
    {
        listDataHeader = new ArrayList();
        listDataChild = new HashMap();
        listDataHeader.add(getString(R.string.haji_lheader1));
        listDataHeader.add(getString(R.string.haji_lheader2));
        listDataHeader.add(getString(R.string.haji_lheader3));
        listDataHeader.add(getString(R.string.haji_lheader4));
        listDataHeader.add(getString(R.string.haji_lheader5));
        listDataHeader.add(getString(R.string.haji_lheader6));
        listDataHeader.add(getString(R.string.haji_lheader7));
        listDataHeader.add(getString(R.string.haji_lheader8));
        listDataHeader.add(getString(R.string.haji_lheader9));
        listDataHeader.add(getString(R.string.haji_lheader10));
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        ArrayList arraylist7 = new ArrayList();
        ArrayList arraylist8 = new ArrayList();
        ArrayList arraylist9 = new ArrayList();
        listDataChild.put((String)listDataHeader.get(0), arraylist);
        listDataChild.put((String)listDataHeader.get(1), arraylist1);
        listDataChild.put((String)listDataHeader.get(2), arraylist2);
        listDataChild.put((String)listDataHeader.get(3), arraylist3);
        listDataChild.put((String)listDataHeader.get(4), arraylist4);
        listDataChild.put((String)listDataHeader.get(5), arraylist5);
        listDataChild.put((String)listDataHeader.get(6), arraylist6);
        listDataChild.put((String)listDataHeader.get(7), arraylist7);
        listDataChild.put((String)listDataHeader.get(8), arraylist8);
        listDataChild.put((String)listDataHeader.get(9), arraylist9);
    }

    private void SalahList()
    {
        listDataHeader = new ArrayList();
        listDataChild = new HashMap();
        listDataHeader.add(getString(R.string.salah_lheader1));
        listDataHeader.add(getString(R.string.salah_lheader2));
        listDataHeader.add(getString(R.string.salah_lheader3));
        listDataHeader.add(getString(R.string.salah_lheader4));
        listDataHeader.add(getString(R.string.salah_lheader5));
        listDataHeader.add(getString(R.string.salah_lheader6));
        listDataHeader.add(getString(R.string.salah_lheader7));
        listDataHeader.add(getString(R.string.salah_lheader8));
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        ArrayList arraylist7 = new ArrayList();
        listDataChild.put((String)listDataHeader.get(0), arraylist);
        listDataChild.put((String)listDataHeader.get(1), arraylist1);
        listDataChild.put((String)listDataHeader.get(2), arraylist2);
        listDataChild.put((String)listDataHeader.get(3), arraylist3);
        listDataChild.put((String)listDataHeader.get(4), arraylist4);
        listDataChild.put((String)listDataHeader.get(5), arraylist5);
        listDataChild.put((String)listDataHeader.get(6), arraylist6);
        listDataChild.put((String)listDataHeader.get(7), arraylist7);
    }

    private void ShahadahList()
    {
        listDataHeader = new ArrayList();
        listDataChild = new HashMap();
        listDataHeader.add(getString(R.string.shahadah_lheader1));
        listDataHeader.add(getString(R.string.shahadah_lheader2));
        listDataHeader.add(getString(R.string.shahadah_lheader3));
        listDataHeader.add(getString(R.string.shahadah_lheader4));
        listDataHeader.add(getString(R.string.shahadah_lheader5));
        listDataHeader.add(getString(R.string.shahadah_lheader6));
        listDataHeader.add(getString(R.string.shahadah_lheader7));
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        listDataChild.put((String)listDataHeader.get(0), arraylist);
        listDataChild.put((String)listDataHeader.get(1), arraylist1);
        listDataChild.put((String)listDataHeader.get(2), arraylist2);
        listDataChild.put((String)listDataHeader.get(3), arraylist3);
        listDataChild.put((String)listDataHeader.get(4), arraylist4);
        listDataChild.put((String)listDataHeader.get(5), arraylist5);
        listDataChild.put((String)listDataHeader.get(6), arraylist6);
    }

    private void ZakatList()
    {
        listDataHeader = new ArrayList();
        listDataChild = new HashMap();
        listDataHeader.add(getString(R.string.zakat_lheader1));
        listDataHeader.add(getString(R.string.zakat_lheader2));
        listDataHeader.add(getString(R.string.zakat_lheader3));
        listDataHeader.add(getString(R.string.zakat_lheader4));
        listDataHeader.add(getString(R.string.zakat_lheader5));
        listDataHeader.add(getString(R.string.zakat_lheader6));
        listDataHeader.add(getString(R.string.zakat_lheader7));
        listDataHeader.add(getString(R.string.zakat_lheader8));
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        ArrayList arraylist7 = new ArrayList();
        listDataChild.put((String)listDataHeader.get(0), arraylist);
        listDataChild.put((String)listDataHeader.get(1), arraylist1);
        listDataChild.put((String)listDataHeader.get(2), arraylist2);
        listDataChild.put((String)listDataHeader.get(3), arraylist3);
        listDataChild.put((String)listDataHeader.get(4), arraylist4);
        listDataChild.put((String)listDataHeader.get(5), arraylist5);
        listDataChild.put((String)listDataHeader.get(6), arraylist6);
        listDataChild.put((String)listDataHeader.get(7), arraylist7);
    }

    public void onClick(View view)
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_expandlist);
        typeface();
        banner_ad();
        expListView = (ExpandableListView)findViewById(R.id.lvExp);
        extra = getIntent().getExtras();
        name = extra.getString("name");
        if(name.equals("shahadah")){
        Actionbar(getString(R.string.lbl_shahdah));
        type = "Shahadah";
        ShahadahList();
        } else if(name.equals("salah")) {
            Actionbar(getString(R.string.lbl_salat));
            type = "Salah";
            SalahList();
        } else if(name.equals("zakat")) {
            Actionbar(getString(R.string.lbl_zakat));
            type = "Zakat";
            ZakatList();
        } else if(name.equals("fasting")) {
            Actionbar(getString(R.string.lbl_sawn));
            type = "Fasting";
            FastingList();
        } else if(name.equals("haji")) {
            Actionbar(getString(R.string.lbl_hajj));
            type = "Haji";
            HajiList();
        }
        listadapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listadapter);
        expListView.setOnGroupExpandListener(new android.widget.ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int i)
            {
                Intent intent = new Intent(PillarsListActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PagerActivity.class);
                intent.putExtra("ga", i);
                intent.putExtra("type", type);
                Log.d("Sending", (new StringBuilder(String.valueOf(i))).toString());
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }
}
