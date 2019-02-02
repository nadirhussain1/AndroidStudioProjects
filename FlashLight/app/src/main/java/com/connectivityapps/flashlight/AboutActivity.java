package com.connectivityapps.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.ScalingUtility;
import com.flurry.android.FlurryAgent;

/**
 * Created by nadirhussain on 03/03/2015.
 */
public class AboutActivity extends Activity {
    private TextView appVersionTextView=null;
    private Button feedbackButton=null;
    private TextView aboutHeadTextView=null;
    private  String FEEDBACK_EMAIL="connectivity.apps@hotmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView=getLayoutInflater().inflate(R.layout.about_screen,null);
        ScalingUtility.getInstance(this).scaleView(rootView);
        setContentView(rootView);

        initViews();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
    private void initViews(){
        appVersionTextView=(TextView)findViewById(R.id.appVersionTextView);
        aboutHeadTextView=(TextView)findViewById(R.id.aboutTextView);
        feedbackButton=(Button)findViewById(R.id.feedbackButton);

        FontsUtil.applyFontToText(this,appVersionTextView,FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(this,aboutHeadTextView,FontsUtil.ROBOTO_REGULAR);
        FontsUtil.applyFontToText(this,feedbackButton,FontsUtil.ROBOTO_REGULAR);

        feedbackButton.setOnClickListener(feedbackClickListener);
        findViewById(R.id.backIconClickedView).setOnClickListener(backClicklListener);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String text =  getString(R.string.app_version_text) + " "+versionName;
            appVersionTextView.setText(text);
        }catch (Exception e){

        }
    }
    private View.OnClickListener feedbackClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendEmail();
        }
    };
    private View.OnClickListener backClicklListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AboutActivity.this.finish();
        }
    };
    private void sendEmail(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");


        sharingIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{FEEDBACK_EMAIL});
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.send_via_label)));
    }
}
