package com.olympus.viewsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.StackView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.olympus.viewsms.adapter.StackAdapter;
import com.olympus.viewsms.adapter.StackAdapter.OnClickClose;
import com.olympus.viewsms.adapter.StackAdapter.OnClickReply;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SharedDialog;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SMS_StackView extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

	public static boolean is_shown=false;
	private int prevdialog_id;
	protected static final int RESULT_REPLY = 111;

	private List<SmsData> smss;
	private StackAdapter adapter;
	private StackView stackView;

	private int contact_index=0;
	private int reply_pos;
	private boolean replying=false;
    private GoogleApiClient mGoogleApiClient=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_stack_view);
        connectGoogleApiClient();

		is_shown=true;
		Log.i("===========", "shown");

		smss=new ArrayList<SmsData>();
		try{
			contact_index=getIntent().getIntExtra("contact_index", 0);
			prevdialog_id=getIntent().getIntExtra("prevdialog_id", 0);
		}catch(Exception e){}

		if(SharedData.getInstance().receivedMessages.size()>0){
			for(int j=SharedData.getInstance().receivedMessages.get(contact_index).size()-1;j>=0;j--)
			{
				SmsData sms=SharedData.getInstance().receivedMessages.get(contact_index).get(j);
				if(sms!=null) smss.add(sms);
			}
		}
		
		Theme cur_theme=Utils.getAppliedTheme(this, smss.get(0));

		adapter=new StackAdapter(this,smss,cur_theme);
		stackView=(StackView)findViewById(R.id.stackview);
		stackView.setAdapter(adapter);
		adapter.setOnClickCloseListener(new OnClickClose() {
			@Override
			public void onClickClose(int pos) {
				try{
					hasRead(pos);
				}
				catch(Exception ex){
					Log.e("===================", "can hasRead("+pos+")");
					finish();
				}
			}
		});
		adapter.setOnClickReplyListener(new OnClickReply() {
			@Override
			public void onClickReply(int pos) {
				try{
					replyInProcess(pos);
				}
				catch(Exception ex){
					Log.e("===================", "can reply("+pos+")");
					finish();
				}
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(getString(R.string.finish_action_SMS_StackView)+prevdialog_id);
		registerReceiver(receiver, filter);	

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
	private void replyInProcess(int pos){
		SmsData sms_new=new SmsData(SharedData.getInstance().receivedMessages.get(contact_index).get(pos));
		if(sms_new.category==Constants.NATIVE_SMS_CAT){
			replying=true;
			reply_pos=pos;  //use for has read func
			
			Intent in=new Intent(SMS_StackView.this, SharedDialog.getInstance().dlg_reply.getClass());
			in.putExtra("avatar", sms_new.getThumbnail());
			sms_new.setThumbnail(null);  //clear pic
			in.putExtra("SmsData", sms_new);
			startActivityForResult(in,RESULT_REPLY);
		}else{
			startActivity(getPackageManager().getLaunchIntentForPackage(sms_new.packageName));
			hasRead(pos);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_REPLY: 
			replying=false;
			if (resultCode == RESULT_OK) {
				hasRead(reply_pos);
			}
			break;
		}
	}

	private void hasRead(int pos){
		smss.remove(pos);
		SharedData.getInstance().receivedMessages.get(contact_index).remove(pos);
		//reset widget
		Utils.updateMyWidgets(this);

		if(smss.size()>0) adapter.notifyDataSetChanged();
		else{
			SharedData.getInstance().receivedMessages.remove(contact_index);
			onBackPressed();
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
		Log.i("===========", "finish");
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
    public void onConnected(Bundle bundle) {
        if(SharedData.getInstance().isNewMessageArrived && SharedData.getInstance().latestSmsData!=null){
            Theme cur_theme=Utils.getAppliedTheme(this, SharedData.getInstance().latestSmsData);
            Utils.showWatchNotification(this,mGoogleApiClient,SharedData.getInstance().latestSmsData, cur_theme);
            SharedData.getInstance().isNewMessageArrived=false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
