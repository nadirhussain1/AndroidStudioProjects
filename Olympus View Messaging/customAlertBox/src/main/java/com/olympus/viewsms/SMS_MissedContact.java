package com.olympus.viewsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.olympus.viewsms.adapter.MissContactAdapter;
import com.olympus.viewsms.adapter.MissContactAdapter.OnRowClick;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.model.MissContact;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SharedDialog;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMS_MissedContact extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    public static boolean is_shown=false;
	private int prevdialog_id;

	private ArrayList<ArrayList<SmsData>> receivedMessages;
	private List<MissContact> miss_contacts;
	private MissContactAdapter adapter;
	private Theme cur_theme;
	private SwipeListView swipeListView;

	private  int mCurrentX = 0;
	private  int mCurrentY = 0;
	private  PopupWindow window=null;
	private  View missedContactWindowView=null;
	private View mainParentView =null;
	Animation bounceAnimation=null;
	private boolean isRecentChats=false;
    private GoogleApiClient mGoogleApiClient=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainParentView = new View(this);
		setContentView(mainParentView);

        connectGoogleApiClient();
		
		findInitialCenterOfScreen();
		bounceAnimation=AnimationUtils.loadAnimation(this,R.anim.bounce_anim);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		missedContactWindowView = inflater.inflate(R.layout.new_sms_separate_contacts, null, true);
		swipeListView=(SwipeListView)missedContactWindowView.findViewById(R.id.listView1);
		init();
		is_shown=true;

		isRecentChats=getIntent().getBooleanExtra("recentChats", false);
		if(isRecentChats){
			receivedMessages=SharedData.getInstance().fiveRecentConversations;
		}else{
			receivedMessages=SharedData.getInstance().receivedMessages;
		}
		try{
			prevdialog_id=getIntent().getIntExtra("prevdialog_id", 0);
		}catch(Exception e){}

		//create missed list
		miss_contacts=new ArrayList<MissContact>();
		Date d=new Date(System.currentTimeMillis());
		long now=d.getTime(); //get now
		for(int i=0;i<receivedMessages.size();i++){
			MissContact miss=new MissContact();
			String name=receivedMessages.get(i).get(0).getSenderName();
			miss.setName(name.equals("")==false?name:receivedMessages.get(i).get(0).getSenderNumber());
			miss.setThumbnail(receivedMessages.get(i).get(0).getThumbnail());
			miss.setNum_sms(receivedMessages.get(i).size());
            miss.setCatgory(receivedMessages.get(i).get(0).category);
			long time=Long.MAX_VALUE;
			for(int j=0;j<receivedMessages.get(i).size();j++){
				if(now-receivedMessages.get(i).get(j).getTimestamp()<time){
					time=now-receivedMessages.get(i).get(j).getTimestamp();
				}

			}
			miss.setTime_ago(time);
			miss_contacts.add(miss);
		}

		//sort by time asc
		for(int i=0;i<receivedMessages.size()-1;i++)
			for(int j=i+1;j<receivedMessages.size();j++)
				if(miss_contacts.get(i).getTime_ago()>miss_contacts.get(j).getTime_ago()){
					MissContact miss=new MissContact();
					miss=miss_contacts.get(i);
					miss_contacts.set(i, miss_contacts.get(j));
					miss_contacts.set(j, miss);

					//same, sort share data
					ArrayList<SmsData> sms=SharedData.getInstance().receivedMessages.get(i);
					SharedData.getInstance().receivedMessages.set(i, SharedData.getInstance().receivedMessages.get(j));
					SharedData.getInstance().receivedMessages.set(j, sms);
				}


		adapter=new MissContactAdapter(this, miss_contacts,cur_theme,isRecentChats);
		swipeListView.setAdapter(adapter);


		swipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS);
		swipeListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_DISMISS);
		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onDismiss(int[] reverseSortedPositions) {
				for (int position : reverseSortedPositions) {
					miss_contacts.remove(position);
					if(!isRecentChats){
                        if(position<SharedData.getInstance().receivedMessages.size()) {
                            SharedData.getInstance().receivedMessages.remove(position);
                            Utils.updateMyWidgets(SMS_MissedContact.this);
                        }
					}
					break;
				}
				adapter.notifyDataSetChanged();
				if(miss_contacts.size()<=0) closeScreen(true);
			}
		});

		//don't use OnItemClickListener in swipeListView, let's use this
		adapter.setOnRowClick(new OnRowClick() {
			@Override
			public void rowClick(int pos) {
				Log.d("ClickTest",""+pos);
				closeScreen(false);
				if(!isRecentChats){
					Intent in=new Intent(getApplicationContext(), SharedDialog.getInstance().dlg_stack.getClass());
					if(SharedData.getInstance().receivedMessages.get(pos).size()==1){
						in=new Intent(getApplicationContext(), SharedDialog.getInstance().dlg_single.getClass());
					}
					in.putExtra("contact_index", pos);
					in.putExtra("recentChats", true);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(in);
				}else{
					replyInProcess(pos);
				}


			}
		});		

		IntentFilter filter = new IntentFilter();
		filter.addAction(getString(R.string.finish_action_SMS_MissedContact)+prevdialog_id);
		registerReceiver(receiver, filter);	

		window = new PopupWindow(missedContactWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setAnimationStyle(R.style.ExitAnimStyle);


		mainParentView.post(new Runnable() {
			@Override
			public void run() {
				window.showAtLocation(mainParentView, Gravity.TOP|Gravity.LEFT, mCurrentX, mCurrentY);
				missedContactWindowView.startAnimation(bounceAnimation);
			}
		});


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
		SmsData sms=SharedData.getInstance().fiveRecentConversations.get(pos).get(0);
		if(sms.category==Constants.NATIVE_SMS_CAT){
			
			Intent in=new Intent(SMS_MissedContact.this, SharedDialog.getInstance().dlg_reply.getClass());
			in.putExtra("avatar", sms.getThumbnail());
			sms.setThumbnail(null);  //clear pic
			in.putExtra("SmsData", sms);
			startActivity(in);
		}else{
			startActivity(getPackageManager().getLaunchIntentForPackage(sms.packageName));
			
		}
	}

	private void findInitialCenterOfScreen(){
		Display display = getWindowManager().getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight()-getStatusBarHeight();
		float density=getResources().getDisplayMetrics().density;

		int popWidth=(int) (320*density);
		int popHeight=(int) (310*density);

		mCurrentX=(width-popWidth)/2;
		mCurrentY=(height-popHeight)/2;
	}
	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	private void init(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		int  theme_id=prefs.getInt(Constants.APPLIED_OLYMPUS_THEME_ID, Constants.DEFAULT_FIRST_THEME_ID);
		cur_theme=(new ThemeDAO(this)).getByID(theme_id);
		int topPadding=cur_theme.getT();

		if(cur_theme.getId()==22){
			topPadding-=Scale.cvDPtoPX(this,30);

			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.BELOW, R.id.img_s1);
			params.setMargins(0, 0, 0, Scale.cvDPtoPX(this, 30));
			swipeListView.setLayoutParams(params);
		}

		ImageView img_s1=(ImageView)missedContactWindowView.findViewById(R.id.img_s1);
		RelativeLayout ln_content=(RelativeLayout)missedContactWindowView.findViewById(R.id.content_container);
		ln_content.setOnClickListener(containerBounceClickListener);

		ln_content.setBackgroundDrawable(Utils.getDrawable(this, "ic"+theme_id+"_bg"));
		img_s1.setImageDrawable(Utils.getDrawable(this, "ic"+theme_id+"_s1"));
		ln_content.setPadding(cur_theme.getL(),topPadding, cur_theme.getR(), cur_theme.getB());


		ImageView widgetHead=(ImageView)missedContactWindowView.findViewById(R.id.widgetIconHead);
		widgetHead.setOnTouchListener(windowFloatToucListener);

		Button cancelButton=(Button)missedContactWindowView.findViewById(R.id.closeButton);
		cancelButton.setOnClickListener(cancelButtonClickListener);
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

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(is_shown) closeScreen(true);
		}
	};
	private void closeScreen(boolean shouldAnimate){
		is_shown=false;
		if(window !=null){
			if(!shouldAnimate){
				window.setAnimationStyle(0);
				window.update();
			}
			window.dismiss();
		}
		SMS_MissedContact.this.finish();
	}
	OnClickListener cancelButtonClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			closeScreen(true);
		}
	};
	OnTouchListener windowFloatToucListener=new OnTouchListener() {
		private float mDx;
		private float mDy;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				mDx = mCurrentX - event.getRawX();
				mDy = mCurrentY - event.getRawY();
			} else if (action == MotionEvent.ACTION_MOVE) {
				mCurrentX = (int) (event.getRawX() + mDx);
				mCurrentY = (int) (event.getRawY() + mDy);
				window.update(mCurrentX, mCurrentY, -1, -1);
			}
			return true;
		}
	};
	OnClickListener containerBounceClickListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			missedContactWindowView.startAnimation(bounceAnimation);

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
