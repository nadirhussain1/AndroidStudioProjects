package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.*;
import android.widget.TextView;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import muslim.prayers.time.R;
public class ShahadahFragment extends Fragment
{

    TextView text1;
    TextView txt_title;

    public ShahadahFragment()
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
        text1 = (TextView)getView().findViewById(R.id.txt);
        if(s.equals("0")){
        	s2 = getString(R.string.shahadah_title1);
        	s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text1)).append("</p></body>").toString();
        }else if(s.equals("1"))
        {
            s2 = getString(R.string.shahadah_title2);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text2)).append("</p></body>").toString();
        } else
        if(s.equals("2"))
        {
            s2 = getString(R.string.shahadah_title3);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text3)).append("</p></body>").toString();
        } else
        if(s.equals("3"))
        {
            s2 = getString(R.string.shahadah_title4);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text4)).append("</p></body>").toString();
        } else
        if(s.equals("4"))
        {
            s2 = getString(R.string.shahadah_title5);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text5)).append("</p></body>").toString();
        } else
        if(s.equals("5"))
        {
            s2 = getString(R.string.shahadah_title6);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text6)).append("</p></body>").toString();
        } else
        if(s.equals("6"))
        {
            s2 = getString(R.string.shahadah_title7);
            s1 = (new StringBuilder("<body><p>")).append(getString(R.string.shahadah_text7)).append("</p></body>").toString();
        }
        txt_title.setText(s2);
        text1.setText(Html.fromHtml(s1));
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.activity_shahadah_fragment, viewgroup, false);
    }
}
