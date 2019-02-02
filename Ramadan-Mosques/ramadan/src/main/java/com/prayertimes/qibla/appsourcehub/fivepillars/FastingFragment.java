package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import muslim.prayers.time.R;
public class FastingFragment extends Fragment
{

    TextView txt_text;
    TextView txt_title;

    public FastingFragment()
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
        LogUtils.i((new StringBuilder("frag1 ")).append(s).toString());
        txt_title = (TextView)getView().findViewById(R.id.txt_title);
        txt_text = (TextView)getView().findViewById(R.id.txt);
        txt_text.setTextIsSelectable(true);
        if(s.equals("0")){
	        s2 = getString(R.string.fasting_title1);
	        s1 = getString(R.string.fasting_text1);
        }else if(s.equals("1"))
        {
            s2 = getString(R.string.fasting_title2);
            s1 = getString(R.string.fasting_text2);
        } else
        if(s.equals("2"))
        {
            s2 = getString(R.string.fasting_title3);
            s1 = getString(R.string.fasting_text3);
        } else
        if(s.equals("3"))
        {
            s2 = getString(R.string.fasting_title4);
            s1 = getString(R.string.fasting_text4);
        } else
        if(s.equals("4"))
        {
            s2 = getString(R.string.fasting_title5);
            s1 = getString(R.string.fasting_text5);
        } else
        if(s.equals("5"))
        {
            s2 = getString(R.string.fasting_title6);
            s1 = getString(R.string.fasting_text6);
        } else
        if(s.equals("6"))
        {
            s2 = getString(R.string.fasting_title7);
            s1 = getString(R.string.fasting_text7);
        } else
        if(s.equals("7"))
        {
            s2 = getString(R.string.fasting_title8);
            s1 = getString(R.string.fasting_text8);
        }
        txt_title.setText(s2);
        txt_text.setText(s1);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.activity_shahadah_fragment, viewgroup, false);
    }
}
