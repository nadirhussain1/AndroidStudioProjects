package com.olympus.viewsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondPageActivity extends Activity {
	public static String content="";
    public static int themeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_page);
        Log.d("WearDebug", "Second Activity on Create");
        Log.d("WearDebug", "SecondActivity ThemeId =" + themeId);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setLayoutElements(stub);

            }
        });

	}
	private void setLayoutElements(WatchViewStub stub){
		View roundThemeBackground =  stub.findViewById(R.id.roundThemeBackground);
        View squareThemeBackground=stub.findViewById(R.id.squareThemeBackground);
        if(roundThemeBackground !=null){
            roundWatchDesign(roundThemeBackground);
        }else if(squareThemeBackground !=null){
            squareWatchDesign(squareThemeBackground);
        }
		TextView  contentTextView = (TextView) stub.findViewById(R.id.messageContentTextView);
		contentTextView.setText(content);
	}
    private void roundWatchDesign(View backgroundView){
        if((themeId>=22 && themeId<=24)|| themeId==28 ){
            String themeResourceName="round_ic"+themeId+"_bg";
            int themeResourceId=getResources().getIdentifier(themeResourceName, "drawable",getPackageName());
            backgroundView.setBackgroundResource(themeResourceId);
        }else if(themeId!=22 && themeId!=24){
            String themeResourceName="round_ic"+themeId+"_message";
            int themeResourceId=getResources().getIdentifier(themeResourceName, "drawable",getPackageName());
            backgroundView.setBackgroundResource(themeResourceId);
        }
    }
    private void squareWatchDesign(View backgroundView){
        String themeResourceName="square_ic"+themeId+"_bg";
        int themeResourceId=getResources().getIdentifier(themeResourceName, "drawable",getPackageName());
        backgroundView.setBackgroundResource(themeResourceId);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
