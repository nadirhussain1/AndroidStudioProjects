package com.olympusthemes.southpark;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private int[]textViewId=new int[]{R.id.appHeadingView, R.id.secondHeadTextView, R.id.TipeFirstLineTextView, R.id.TipeSecondLineTextView,R.id.downloadApplyButton};
    private String OLYMPUS_PACKAGE="com.olympus.viewsms";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=getLayoutInflater().inflate(R.layout.activity_main,null);
        ScalingUtility.getInstance(this).scaleView(view);
        setContentView(view);

        applyFontToText();
        if(checkOlympusInstallation()){
            ((Button)findViewById(R.id.downloadApplyButton)).setText(R.string.apply_text);
        }
        ((Button)findViewById(R.id.downloadApplyButton)).setOnClickListener(downloadApplyClickListener);
    }
    private void applyFontToText(){
      for(int count=0;count<textViewId.length;count++){
          TextView textView=(TextView)findViewById(textViewId[count]);
         FontsUtil.applyFontToText(this, textView, FontsUtil.HELVETICA_NEUE_STD);
      }
    }
    private boolean checkOlympusInstallation(){
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(OLYMPUS_PACKAGE, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
    private View.OnClickListener downloadApplyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             if(checkOlympusInstallation()){
                 launchMainOlympusApp();
             }else{
                 marketUrlOfMainApp();
             }
        }
    };
    private void launchMainOlympusApp(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(OLYMPUS_PACKAGE, OLYMPUS_PACKAGE+".SplashActivity"));
        startActivity(intent);
    }
    private void marketUrlOfMainApp(){
        Uri uri = Uri.parse("market://details?id=" + OLYMPUS_PACKAGE);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }

    }
}
