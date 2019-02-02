package com.prayertimes.qibla.appsourcehub.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import muslim.prayers.time.R;
public class ActivityInstruction extends Utils
{

    Bundle extra;
    ImageView image;
    RelativeLayout layout;
    TextView tv_instruction;
    String val;

    public ActivityInstruction()
    {
        val = "";
    }

    public void instruct()
    {
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
        setContentView(R.layout.activity_instruction);
        typeface();
        Actionbar(getString(R.string.menu_instruction));
        Analytics(getString(R.string.menu_instruction));
        typeface();
        banner_ad();
        tv_instruction = (TextView)findViewById(R.id.tv_instruction);
        instruct();
    }
}
