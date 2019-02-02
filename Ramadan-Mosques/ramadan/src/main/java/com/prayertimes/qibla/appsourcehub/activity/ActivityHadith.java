package com.prayertimes.qibla.appsourcehub.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import muslim.prayers.time.R;
public class ActivityHadith extends Utils
    implements android.widget.RadioGroup.OnCheckedChangeListener
{

    AdRequest adRequest;
    int id;
    ImageView img_slider_next;
    ImageView img_slider_previous;
    int interflag;
    String lang;
    LinearLayout lyt_content;
    RadioGroup radioGroup;
    String text;
    TextView txt_arabic;
    TextView txt_counter;
    TextView txt_english;
    TextView txt_title;

    public ActivityHadith()
    {
        id = 1;
        lang = "arabic";
        interflag = 1;
    }

    public void changeSliderView()
    {
        if(id < 0 || id == 1)
        {
            img_slider_previous.setVisibility(4);
        } else
        {
            img_slider_previous.setVisibility(0);
        }
        if(id == 42)
        {
            img_slider_next.setVisibility(4);
            return;
        } else
        {
            img_slider_next.setVisibility(0);
            return;
        }
    }

    public void getContent(int i)
    {
        String as[];
        txt_title.setText((new StringBuilder("Hadith ")).append(i).toString());
        txt_counter.setText((new StringBuilder(String.valueOf(i))).append("/42").toString());
        txt_title.setTextAppearance(this, styleheader[efont]);
        txt_counter.setTextAppearance(this, style[efont]);
        try
        {
        	InputStream inputstream = getAssets().open((new StringBuilder("hadith/Hadith ")).append(i).append(".txt").toString());
            byte abyte0[] = new byte[inputstream.available()];
            inputstream.read(abyte0);
            inputstream.close();
            as = (new String(abyte0)).split("English:");
            if(lang.equals("arabic"))
            {
                txt_arabic.setText(as[0].trim().replace("Arabic:", ""));
                txt_arabic.setTypeface(tf_arabic);
                return;
            }
            if(lang.equals("english"))
            {
                txt_arabic.setTypeface(tf2);
                txt_arabic.setText(as[1].trim().replace("Arabic:", ""));
            }
        }
        catch(IOException ioexception) { }
    }

    public void getRadioGroupId()
    {
        setRadioGroupId(radioGroup.getCheckedRadioButtonId());
    }

    public void onCheckedChanged(RadioGroup radiogroup, int i)
    {
        setRadioGroupId(i);
    }

    public void onCreate(Bundle bundle)
    {
        //fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_hadith);
        adRequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();
        
        Actionbar(getString(R.string.lbl_hadith).replace("40", ""));
        Analytics(getString(R.string.lbl_hadith));
        typeface();
        banner_ad();
        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_arabic = (TextView)findViewById(R.id.txt_arabic);
        txt_english = (TextView)findViewById(R.id.txt_english);
        txt_counter = (TextView)findViewById(R.id.txt_counter);
        img_slider_next = (ImageView)findViewById(R.id.img_slider_next);
        img_slider_previous = (ImageView)findViewById(R.id.img_slider_previous);
        lyt_content = (LinearLayout)findViewById(R.id.lyt_content);
        txt_arabic.setTypeface(tf2);
        txt_counter.setTypeface(tf2, 1);
        getContent(id);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        img_slider_previous.setVisibility(4);
        img_slider_next.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                ActivityHadith activityhadith;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                activityhadith = ActivityHadith.this;
                activityhadith.interflag = 1 + activityhadith.interflag;
                animation = AnimationUtils.loadAnimation(ActivityHadith.this, R.anim.push_left_out);
                lyt_content.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        ActivityHadith activityhadith = ActivityHadith.this;
                        activityhadith.id = 1 + activityhadith.id;
                        if(id > 42)
                        {
                            id = 42;
                        }
                        getContent(id);
                        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ActivityHadith.this, R.anim.push_left_in);
                        lyt_content.startAnimation(animation);
                        changeSliderView();
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        img_slider_previous.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                ActivityHadith activityhadith;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                activityhadith = ActivityHadith.this;
                activityhadith.interflag = 1 + activityhadith.interflag;
                animation = AnimationUtils.loadAnimation(ActivityHadith.this, R.anim.push_right_out);
                lyt_content.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        ActivityHadith activityhadith = ActivityHadith.this;
                        activityhadith.id = -1 + activityhadith.id;
                        if(id < 0)
                        {
                            id = 1;
                        }
                        getContent(id);
                        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ActivityHadith.this, R.anim.push_right_in);
                        lyt_content.startAnimation(animation);
                        changeSliderView();
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        getRadioGroupId();
    }

    protected void onResume()
    {
        font();
        super.onResume();
    }

    public void setRadioGroupId(int i)
    {
        switch(i)
        {
        default:
            return;

        case R.id.menu_arabic: 
            lang = "arabic";
            getContent(id);
            return;

        case R.id.menu_english: 
            lang = "english";
            break;
        }
        getContent(id);
    }
}
