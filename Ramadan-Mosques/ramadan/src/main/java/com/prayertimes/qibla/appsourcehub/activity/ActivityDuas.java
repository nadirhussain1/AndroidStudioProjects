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
public class ActivityDuas extends Utils
{

    AdRequest adRequest;
    String ar_title[] = {
        "While Sleeping", "Before Sleeping", "When Waking up", "Dua in Morning and Evening", "Dua in Evening", "Dua in Morning", "Dua when awaken at night", "Daily reciting Dua", "When Entering the toilet", "When Exiting from the toilet", 
        "When leaving home", "When the gathering comes to an end", "When entering in a Mosque", "When exciting from the Mosque", "When consuming food", "Invocation for someone who provides us food", "Before starting sexual intercourse", "When getting Angry", "During a state of confusion and evil thought", "When a donkey brays", 
        "When experiencing a bad dream", "When visiting the patient", "When suffering equivalent of death", "When someone helps us", "When everyone is against us", "To earn benefits in every stage", "When suffering from Calamity", "Dua for asking for a better partner when husband dies", "Dua wanting rain", "During heavy rain", 
        "During wars and violence", "During Storm", "When starting the journey", "When returning after the journey", "When staying out the town", "When slaughtering an animal", "Upon hearing a good news or glad experience", "Seeking what is beneficial from Allah before doing any work (Istikharah)", "Upon Sneezing", "When visiting a graveyard", 
        "To wish a newly married couple", "When start taking ablution", "After finishing ablution"
    };
    int id;
    ImageView img_slider_next;
    ImageView img_slider_previous;
    int interflag;
    LinearLayout lyt_content;
    TextView txt_counter;
    TextView txt_data;
    TextView txt_title;

    public ActivityDuas()
    {
        id = 1;
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
        try
        {
            txt_title.setText((new StringBuilder(String.valueOf(i))).append(".").append(ar_title[i]).toString());
            txt_title.setTextAppearance(this, styleheader[efont]);
            txt_counter.setText((new StringBuilder(String.valueOf(i))).append("/43").toString());
            txt_data.setTextAppearance(this, style[efont]);
            txt_counter.setTextAppearance(this, style[efont]);
            InputStream inputstream = getAssets().open((new StringBuilder("duas/file")).append(i).append(".txt").toString());
            byte abyte0[] = new byte[inputstream.available()];
            inputstream.read(abyte0);
            inputstream.close();
            String s = new String(abyte0);
            txt_data.setText(s);
            return;
        }
        catch(IOException ioexception)
        {
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        //fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_duas);
        adRequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();
      
        Actionbar(getString(R.string.lbl_duas));
        Analytics(getString(R.string.lbl_duas));
        typeface();
        banner_ad();
        txt_title = (TextView)findViewById(R.id.txt_title);
        img_slider_next = (ImageView)findViewById(R.id.img_slider_next);
        img_slider_previous = (ImageView)findViewById(R.id.img_slider_previous);
        txt_counter = (TextView)findViewById(R.id.txt_counter);
        lyt_content = (LinearLayout)findViewById(R.id.lyt_content);
        txt_data = (TextView)findViewById(R.id.txt_data);
        txt_title.setText((new StringBuilder(String.valueOf(id))).append(".").append(ar_title[id]).toString());
        txt_counter.setTypeface(tf2, 1);
        txt_data.setTypeface(tf);
        getContent(id);
        img_slider_previous.setVisibility(4);
        img_slider_next.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                ActivityDuas activityduas;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                activityduas = ActivityDuas.this;
                activityduas.interflag = 1 + activityduas.interflag;
                animation = AnimationUtils.loadAnimation(ActivityDuas.this, R.anim.push_left_out);
                lyt_content.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        ActivityDuas activityduas = ActivityDuas.this;
                        activityduas.id = 1 + activityduas.id;
                        if(id > 43)
                        {
                            id = 43;
                        }
                        getContent(id);
                        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ActivityDuas.this, R.anim.push_left_in);
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
                ActivityDuas activityduas;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                activityduas = ActivityDuas.this;
                activityduas.interflag = 1 + activityduas.interflag;
                animation = AnimationUtils.loadAnimation(ActivityDuas.this, R.anim.push_right_out);
                lyt_content.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        ActivityDuas activityduas = ActivityDuas.this;
                        activityduas.id = -1 + activityduas.id;
                        if(id < 0)
                        {
                            id = 1;
                        }
                        getContent(id);
                        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ActivityDuas.this, R.anim.push_right_in);
                        lyt_content.startAnimation(animation);
                        changeSliderView();
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
    }

    protected void onResume()
    {
        font();
        super.onResume();
    }
}
