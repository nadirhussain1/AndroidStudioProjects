package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;
import muslim.prayers.time.R;
public class HajiFragment extends Fragment
{

    TextView text1;
    TextView txt_title;

    public HajiFragment()
    {
    }

    public void onActivityCreated(Bundle bundle)
    {
        String s;
        String s1;
        String s2;
        super.onActivityCreated(bundle);
        s = getArguments().getString("val");
        s1 = "";
        s2 = "";
        txt_title = (TextView)getView().findViewById(R.id.txt_title);
        text1 = (TextView)getView().findViewById(R.id.txt);
        text1.setTextIsSelectable(true);
        if(s.equals("0")){
	        s2 = getString(R.string.haji_title1);
	        s1 = getString(R.string.haji_text1);
        }else if(s.equals("1")) {
            s2 = getString(R.string.haji_title2);
            s1 = getString(R.string.haji_text2);
        } else if(s.equals("2")) {
            s2 = getString(R.string.haji_title3);
            s1 = getString(R.string.haji_text3);
        } else if(s.equals("3")) {
            s2 = getString(R.string.haji_title4);
            s1 = getString(R.string.haji_text4);
        } else if(s.equals("4")) {
            s2 = getString(R.string.haji_title5);
            s1 = getString(R.string.haji_text5);
        } else if(s.equals("5")) {
            s2 = getString(R.string.haji_title6);
            s1 = getString(R.string.haji_text6);
        } else if(s.equals("6")) {
            s2 = getString(R.string.haji_title7);
            s1 = getString(R.string.haji_text7);
        } else if(s.equals("7")) {
            s2 = getString(R.string.haji_title8);
            s1 = getString(R.string.haji_text8);
        } else if(s.equals("8")) {
            s2 = getString(R.string.haji_title9);
            s1 = getString(R.string.haji_text9);
        } else if(s.equals("9")) {
            s2 = getString(R.string.haji_title10);
            s1 = getString(R.string.haji_text10);
        }
        txt_title.setText(s2);
        text1.setText(s1);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.activity_shahadah_fragment, viewgroup, false);
    }
}
