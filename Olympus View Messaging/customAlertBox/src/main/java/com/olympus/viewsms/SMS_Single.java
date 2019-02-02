package com.olympus.viewsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wearable.Wearable;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SharedDialog;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;
import com.squareup.picasso.Picasso;

public class SMS_Single extends Activity implements ConnectionCallbacks,OnConnectionFailedListener{

	public static boolean is_shown=false;

	private int prevdialog_id;
	protected static final int RESULT_REPLY = 111;

	private SmsData sms=null;
	private int contact_index=0;
	private boolean replying=false;

	private TextView tv_number,tv_name,tv_time,tv_content,tv_ok,tv_reply;
	private ImageView img_bg,img_s1,img_s2,img_s3,img_avatar;
	private RelativeLayout ln_h1,ln_content,ln_h2,layout_bg;
	private ScrollView contentScroll;
	private GoogleApiClient mGoogleApiClient=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_single);
		connectGoogleApiClient();

        is_shown=true;
		try{
			contact_index=getIntent().getIntExtra("contact_index", 0);
			prevdialog_id=getIntent().getIntExtra("prevdialog_id", 0);
			if(SharedData.getInstance().receivedMessages.size()!=0){
				sms=SharedData.getInstance().receivedMessages.get(contact_index).get(0);
			}else if(SharedData.getInstance().fiveRecentConversations.size()!=0){
				sms=SharedData.getInstance().fiveRecentConversations.get(contact_index).get(0);
			}
		}catch(Exception e){}

		init();

		if(sms!=null){
            int imageDimen= Scale.cvDPtoPX(this,50);
            if(sms.category==Constants.NATIVE_SMS_CAT) {
                Picasso.with(this).load(sms.getThumbnail()).placeholder(R.drawable.ic_contact_picture).resize(imageDimen,imageDimen).into(img_avatar);
            }else{
                 int id=Integer.valueOf(sms.getThumbnail());
                 img_avatar.setImageBitmap(Utils.getScaledSocialBitmap(this,id,imageDimen));
            }
			tv_time.setText(sms.getTimeString(sms.getTimestamp()));
			tv_content.setText(sms.getBody());

			if(sms.getSenderName().equals("")){
				tv_number.setText(sms.getSenderNumber());
				if(tv_name!=null){
					tv_name.setVisibility(View.GONE);
				}
			}else{
				tv_number.setText(sms.getSenderName());
				if(tv_name!=null){
					tv_name.setText(sms.getSenderNumber());
					tv_name.setVisibility(View.VISIBLE);
				}
			}
		}


		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					hasRead();
				}
				catch(Exception ex){
					finish();
				}
			}
		});
		tv_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					replyInProcess();	
				}
				catch(Exception ex){
					finish();
				}
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(getString(R.string.finish_action_SMS_Single)+prevdialog_id);
		registerReceiver(receiver, filter);

	}
	private void replyInProcess(){
		if(sms.category==Constants.NATIVE_SMS_CAT){
			replying=true;
			SmsData sms_new=new SmsData(sms);

			int length=sms.getSenderNumber().length();
			int notificationId=Integer.valueOf(sms.getSenderNumber().substring(length-5));
			NotificationManagerCompat notificationManager =NotificationManagerCompat.from(SMS_Single.this);
			notificationManager.cancel(notificationId);

			Intent in=new Intent(SMS_Single.this, SharedDialog.getInstance().dlg_reply.getClass());
			in.putExtra("avatar", sms_new.getThumbnail());
			sms_new.setThumbnail(null);  //clear pic
			in.putExtra("SmsData", sms_new);
			startActivityForResult(in,RESULT_REPLY);
		}else{
			startActivity(getPackageManager().getLaunchIntentForPackage(sms.packageName));
			hasRead();
		}
	}
	
	@Override
	public void onDestroy() {
	    if (mGoogleApiClient.isConnected()) {
	        mGoogleApiClient.disconnect();
	    }
	 
	    super.onDestroy();
	}
	private void connectGoogleApiClient(){
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addApi(Wearable.API)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.build();

		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}

	private void init(){
		Theme cur_theme=Utils.getAppliedTheme(this, sms);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout lnTitle = (RelativeLayout)findViewById(R.id.lnTitle); 
		int title_id=cur_theme.getTitle_layout();
		View yourView = inflater.inflate(Utils.getLayoutID(this, "sms_layout_title"+title_id), null);
		lnTitle.removeAllViews();
		lnTitle.addView(yourView);


		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_number=(TextView)findViewById(R.id.tv_number);
		tv_time=(TextView)findViewById(R.id.tv_time);
		img_avatar=(ImageView)findViewById(R.id.img_avatar);
		ln_h1=(RelativeLayout)findViewById(R.id.ln_h1);

		ln_h2=(RelativeLayout)findViewById(R.id.ln_h2);
		tv_ok=(TextView)findViewById(R.id.tv_ok);
		tv_reply=(TextView)findViewById(R.id.tv_reply);
		tv_content=(TextView)findViewById(R.id.tv_content);
		img_bg=(ImageView)findViewById(R.id.img_bg);
		img_s1=(ImageView)findViewById(R.id.img_s1);
		img_s2=(ImageView)findViewById(R.id.img_s2);
		img_s3=(ImageView)findViewById(R.id.img_s3);
		ln_content=(RelativeLayout)findViewById(R.id.ln_content);
		contentScroll=(ScrollView)findViewById(R.id.scrollView1);
		layout_bg=(RelativeLayout)findViewById(R.id.layout_bg);

		img_bg.setImageDrawable(Utils.getDrawable(this, "ic"+cur_theme.getId()+"_bg"));
		img_s1.setImageDrawable(Utils.getDrawable(this, "ic"+cur_theme.getId()+"_s1"));
		img_s2.setImageDrawable(Utils.getDrawable(this, "ic"+cur_theme.getId()+"_s2"));
		img_s3.setImageDrawable(Utils.getDrawable(this, "ic"+cur_theme.getId()+"_s3"));

		tv_number.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_time.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(cur_theme.getTitle_layout()==3)
			tv_time.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_content.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(tv_name !=null){
			tv_name.setTextColor(Color.parseColor(cur_theme.getCtext()));
		}

		ln_content.setPadding(cur_theme.getL(), cur_theme.getT(), cur_theme.getR(), cur_theme.getB());
		tv_content.setPadding(cur_theme.getL2(), 0, cur_theme.getR2(), 0);
		ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH1()));

		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH2());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		if(cur_theme.getButton_bottom()>0){
			params.setMargins(0, 0, 0,cur_theme.getButton_bottom());
		}
		ln_h2.setLayoutParams(params);

		if(cur_theme.getButton_width()<0){
			params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.setMargins(Scale.cvDPtoPX(this, 15), 0, 0,0);
			tv_reply.setLayoutParams(params);
			tv_reply.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_reply"));

			params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(0, 0,Scale.cvDPtoPX(this, 15),0);
			tv_ok.setLayoutParams(params);
			tv_ok.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_ok"));

			tv_ok.setText("");
			tv_reply.setText("");

			params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ABOVE, R.id.ln_h2);
			params.setMargins(0,0, 0, Scale.cvDPtoPX(this, 5));
			img_s2.setLayoutParams(params);
		}else{
			tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
			tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
		}
		if(cur_theme.getIs_button_divider()==1){
			img_s3.setVisibility(View.INVISIBLE);
		}
		if(cur_theme.getContent_width()>0){
			params=new RelativeLayout.LayoutParams(Scale.cvDPtoPX(this, cur_theme.getContent_width()),Scale.cvDPtoPX(this, cur_theme.getContent_height()));
			params.setMargins(Scale.cvDPtoPX(this, cur_theme.getContent_left()),Scale.cvDPtoPX(this, cur_theme.getContent_top()), 0, 0);
			contentScroll.setLayoutParams(params);
			contentScroll.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_content"));
		}
		if(cur_theme.getTheme_height()>0){
			FrameLayout.LayoutParams frameParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,Scale.cvDPtoPX(this, cur_theme.getTheme_height()));
			layout_bg.setLayoutParams(frameParam);
		}
	}

	private void hasRead(){
		//reset sms data because it have only one sms
		if(contact_index<SharedData.getInstance().receivedMessages.size()){
			SharedData.getInstance().receivedMessages.remove(contact_index);
			Utils.updateMyWidgets(this);
		}
		//reset widget
		onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_REPLY: 
			replying=false;
			if (resultCode == RESULT_OK) {
				hasRead();
			}
			break;
		}
	}

	public void OnClickExit(View v){
		finish();
	}

	private void checkReturnMissedDialog(){
		if(SharedData.getInstance().receivedMessages.size()>1){  //have more than one contact
			Intent in=new Intent(getApplicationContext(), SharedDialog.getInstance().dlg_miss.getClass());
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplicationContext().startActivity(in);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		checkReturnMissedDialog();
	}

	@Override
	public void finish() {
		is_shown=false;
		super.finish();
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
	}

	@Override
	public void onStop() {
		try{
			unregisterReceiver(receiver);
		}catch(IllegalArgumentException e){}
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		FlurryAgent.onEndSession(this);
	}

	//receiver to finish actitivy from SmsInterceptor
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(is_shown && !replying) finish();
		}
	};
	@Override
	public void onConnected(Bundle arg0) {	
		Theme cur_theme=Utils.getAppliedTheme(this, SharedData.getInstance().latestSmsData);
		Utils.showWatchNotification(this,mGoogleApiClient,SharedData.getInstance().latestSmsData, cur_theme);
		SharedData.getInstance().isNewMessageArrived=false;
	}
	@Override
	public void onConnectionSuspended(int arg0) {
		
	}
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}	


}
