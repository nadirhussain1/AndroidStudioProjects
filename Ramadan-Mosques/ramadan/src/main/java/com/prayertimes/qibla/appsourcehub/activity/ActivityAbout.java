package com.prayertimes.qibla.appsourcehub.activity;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdView;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class ActivityAbout extends Utils
{

    Bundle extra;
    ImageView image;
    RelativeLayout layout;
    TextView tv_instruction;
    String val;
    private AdView adview;

    public ActivityAbout()
    {
        val = "";
    }

    public void about()
    {
        tv_instruction.setGravity(17);
        tv_instruction.setText("Prayer Times & Qibla Pro\n Version : 1.5\n copyright 2016-2017\n www.appsourcehub.com\n All right reserved");
        image.setVisibility(0);
    }

    public void instruct()
    {
        image.setVisibility(8);
        try
        {
            InputStream inputstream = getAssets().open("instruction.txt");
            byte abyte0[] = new byte[inputstream.available()];
            inputstream.read(abyte0);
            inputstream.close();
            String s = new String(abyte0);
            tv_instruction.setText(s);
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        Actionbar(getString(R.string.menu_about));
        typeface();
        banner_ad();
        Analytics(getString(R.string.menu_about));
        tv_instruction = (TextView)findViewById(R.id.tv_instruction);
        image = (ImageView)findViewById(R.id.image);
    }
    
}
