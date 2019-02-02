package com.olympus.viewsms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String SENDER="SENDER";
	public static final String SENDER_NUMBER="SENDER_NUMBER";
	public static final String BODY="BODY";
	public static final String THEME="THEME";
	public static final String TITLE_COLOR="TITLE_COLOR";
	public static final String CONTENT_COLOR="CONTENT_COLOR";


	private String sender="";
	private String content="";
	private String titleColor="";
	private String contentColor="";
	private int  themeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		if (intent != null && intent.getExtras()!=null) {
			
			sender=intent.getExtras().getString(SENDER);
			content=intent.getExtras().getString(BODY);
			themeId=intent.getExtras().getInt(THEME);
			titleColor=intent.getExtras().getString(TITLE_COLOR);
			contentColor=intent.getExtras().getString(CONTENT_COLOR);

            Log.d("WearDebug", "MainActivity themeId=" + themeId);
			final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
			stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
				@Override
				public void onLayoutInflated(WatchViewStub stub) {
					setLayoutElements(stub);

				}
			});
			
			stub.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
		        @Override
		        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) { 
		            stub.onApplyWindowInsets(windowInsets);
		            return windowInsets;
		        }
		    });
			
		}
	}
	private void setLayoutElements(WatchViewStub stub){
		View roundThemeBackground =  stub.findViewById(R.id.roundThemeBackground);
        View squareThemeBackground=stub.findViewById(R.id.squareThemeBackground);

        if(roundThemeBackground !=null){
            roundWatchDesign(stub,roundThemeBackground);
        }else if(squareThemeBackground !=null){
            squareWatchDesign(squareThemeBackground);
        }

		TextView  titleTextView = (TextView) stub.findViewById(R.id.senderNameTextView);
		TextView  contentTextView = (TextView) stub.findViewById(R.id.messageContentTextView);


		
		titleTextView.setTextColor(Color.parseColor(titleColor));
		contentTextView.setTextColor(Color.parseColor(contentColor));
	
		titleTextView.setText(sender);
		contentTextView.setText(content);
	}

    private void roundWatchDesign(WatchViewStub stub,View backgroundView){
        RelativeLayout contentLayout=(RelativeLayout)stub.findViewById(R.id.contentLayout);
        if((themeId>=22 && themeId<=24)|| themeId==28 ){
            String themeBgName="round_ic"+themeId+"_bg";
            int themeBgResourceId=getResources().getIdentifier(themeBgName, "drawable",getPackageName());
            backgroundView.setBackgroundResource(themeBgResourceId);
        }

        if(themeId!=22 && themeId!=24){
            String contentBgName="round_ic"+themeId+"_message";
            int contentBgResourceId=getResources().getIdentifier(contentBgName, "drawable",getPackageName());
            contentLayout.setBackgroundResource(contentBgResourceId);
        }
    }
    private void squareWatchDesign(View background){
        String themeBgName="square_ic"+themeId+"_bg";
        int themeBgResourceId=getResources().getIdentifier(themeBgName, "drawable",getPackageName());
        background.setBackgroundResource(themeBgResourceId);
    }
}
