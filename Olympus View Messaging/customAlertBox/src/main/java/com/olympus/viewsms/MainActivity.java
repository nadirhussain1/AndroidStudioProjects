package com.olympus.viewsms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.olympus.viewsms.adapter.NavDrawerListAdapter;
import com.olympus.viewsms.fragment.FlashViewTutFragment;
import com.olympus.viewsms.fragment.MoreThemeFragment;
import com.olympus.viewsms.fragment.MyThemeFragment;
import com.olympus.viewsms.fragment.OlympusThemeFragment;
import com.olympus.viewsms.fragment.OlympusViewTutFragment;
import com.olympus.viewsms.model.NavDrawerItem;
import com.olympus.viewsms.services.OlympusAccessibilityService;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity{

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mTitle;
	private String[] drawerItemTitles;
	private String [] actionBarTitles={"My Themes","My Themes","More Themes","Olympus View Themes","Olympus 3D View","Flash View"};
	private int [] drawerIcons={R.drawable.notification_icon,R.drawable.mytheme_icon,R.drawable.morethemes_icon,R.drawable.olympus_icon,R.drawable.threed_message_icon,R.drawable.flash_message_icon,R.drawable.share_icon};
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private Fragment currentFragment=null;
	private boolean isMoreThemeSelected=false;

	private static final String TAG = "MainActivity";
	private SharedPreferences prefs;
	private boolean isFragmentChange=false;
	private int currentFragIndex=0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_main);
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        int prefRunCount= prefs.getInt(Constants.RUN_COUNT,0);
        prefRunCount=prefRunCount%5;
        if(prefRunCount==0){
            enableAccessibilityService();
        }
        prefRunCount++;
        prefsEditor.putInt(Constants.RUN_COUNT,prefRunCount);
        prefsEditor.commit();

		Utils.updateMyWidgets(this);
		mTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerLayout.setFocusableInTouchMode(false);
		mDrawerList=(ListView)findViewById(R.id.left_drawer);

		populateListItems();
		initiateNavigationDrawer();

		if(savedInstanceState==null){
			initialScreenDisplay();
		}

		boolean first_run=prefs.getBoolean(Constants.FIRST_RUN, true);
		if(first_run){

			prefsEditor.putBoolean(Constants.FIRST_RUN, false);
			prefsEditor.commit();

			Toast.makeText(this, getString(R.string.widget_added),Toast.LENGTH_SHORT).show();
		}
	}
	private void enableAccessibilityService(){
		if(!OlympusAccessibilityService.isAccessibilitySettingsOn(this)){
			displayAccessAlert();
		}
	}
	private void displayAccessAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Settings");
		alertDialogBuilder
				.setMessage("To get notified about social apps \nSwitch Accessibility settings to ON")
				.setCancelable(false)
				.setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
						Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
						startActivityForResult(intent, 1337);
					}
				  })
                .setNegativeButton("Not now",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       dialog.cancel();
                    }
                });
				
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if(requestCode==120){
			Toast.makeText(this,"MMS Sent Successfully", Toast.LENGTH_SHORT).show();
		}
		if(requestCode!=MoreThemeFragment.RC_REQUEST) return;

		// Pass on the activity result to the helper for handling
		if (currentFragment !=null && currentFragment instanceof MoreThemeFragment){
			if(!((MoreThemeFragment)currentFragment).mHelper.handleActivityResult(requestCode, resultCode, data)) {
				// not handled, so handle it ourselves (here's where you'd
				// perform any handling of activity results not related to in-app
				// billing...
				super.onActivityResult(requestCode, resultCode, data);
			}else {
				Log.d(TAG, "onActivityResult handled by IABUtil.");
			}
		}

	}
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		FlurryAgent.onEndSession(this);
	}
	@Override
	public void onResume(){
        super.onResume();
	}

	
	@Override
	public void onBackPressed() {
		if(currentFragment!=null && currentFragment instanceof MyThemeFragment && ((MyThemeFragment)currentFragment).slidingmenu.isMenuShowing()){ 
			((MyThemeFragment)currentFragment).toggleSlidingmenu();
		}else{
            MainActivity.this.finish();
        }
	}
	

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_actions,menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			toggleDrawerLayout();
			return true;
		case R.id.action_morethemes:
			actionMoreThemes();
			return true;
		case R.id.action_contacts:
			if(currentFragment!=null && currentFragment instanceof MyThemeFragment){
				((MyThemeFragment)currentFragment).toggleSlidingmenu();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem moreThemesOption=menu.findItem(R.id.action_morethemes);
		MenuItem contactMenuOption=menu.findItem(R.id.action_contacts);
		if(isMoreThemeSelected){
			moreThemesOption.setVisible(false);
			contactMenuOption.setVisible(false);
		}else{
			moreThemesOption.setVisible(true);
			contactMenuOption.setVisible(true);
		}
		return true;
	}

	private void toggleDrawerLayout(){
		isFragmentChange=false;
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	private void populateListItems(){
		drawerItemTitles=getResources().getStringArray(R.array.nav_drawer_items);
		navDrawerItems=new ArrayList<NavDrawerItem>();
		for(int count=0;count<drawerItemTitles.length;count++){
			boolean isCheckBox=false;
			if(count==0){
				isCheckBox=true;
			}
			NavDrawerItem item=new NavDrawerItem(drawerItemTitles[count], drawerIcons[count], isCheckBox);
			navDrawerItems.add(item);	
		}
	}

	private void initiateNavigationDrawer(){
		adapter = new NavDrawerListAdapter(this,navDrawerItems);
		mDrawerList.setAdapter(adapter);
		mTitle=getTitle();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_navigation_drawer, R.string.app_name, R.string.app_name ){
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				super.onDrawerClosed(view);

				if(isFragmentChange){
					setFragment(currentFragment, currentFragIndex);
					System.gc();
					invalidateOptionsMenu();
				}
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mTitle);
				super.onDrawerOpened(drawerView);
			}

		};
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	private void displayView(int position){
		removeCurrentFragment();

		isFragmentChange=true;
		currentFragIndex=position;
		if(position==0 || position==1){		
			currentFragment=new MyThemeFragment();
			isMoreThemeSelected=false;		
		}else if(position==2){
			isMoreThemeSelected=true;
			currentFragment=new MoreThemeFragment();
		}else if(position==3){
			currentFragment=OlympusThemeFragment.newInstance();     
			isMoreThemeSelected=true;
		}else if(position==4){
			currentFragment=new OlympusViewTutFragment();     
			isMoreThemeSelected=false;
		}else if(position==5){
			currentFragment=new FlashViewTutFragment();     
			isMoreThemeSelected=false;
		}
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);

	}
	private void initialScreenDisplay(){
		currentFragIndex=0;
		isMoreThemeSelected=false;
		currentFragment=new MyThemeFragment();
		mDrawerList.setItemChecked(currentFragIndex, true);
		mDrawerList.setSelection(currentFragIndex);
		setFragment(currentFragment, currentFragIndex);
	}
	private void actionMoreThemes(){
		removeCurrentFragment();

		currentFragIndex=2;
		System.gc();
		isMoreThemeSelected=true;
		currentFragment=new MoreThemeFragment();
		mDrawerList.setItemChecked(currentFragIndex, true);
		mDrawerList.setSelection(currentFragIndex);
		setFragment(currentFragment, currentFragIndex);
		System.gc();
		invalidateOptionsMenu();
	}

	private void setFragment(Fragment fragment,int position){
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content,fragment,actionBarTitles[position]).commitAllowingStateLoss();
		mDrawerLayout.closeDrawer(mDrawerList);
		setTitle(actionBarTitles[position]);
	}
	private void removeCurrentFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().remove(currentFragment).commit();
		currentFragment=null;
	}
	public void customItemClick(int position,View view){
		isFragmentChange=false;
		if(position==0){
			CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);
			setNotifyEnable(checkBox);
			mDrawerLayout.closeDrawer(mDrawerList);
		}else if(position<6){
			displayView(position);
		}else if(position==6){
			OnClickShare();
		}
	}
	public void setNotifyEnable(CheckBox notifCheckBox){
		notifCheckBox.setChecked(!notifCheckBox.isChecked());
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(Constants.NOTIFY_ENABLE,notifCheckBox.isChecked());
		prefsEditor.commit();

		if(notifCheckBox.isChecked()){
			Toast.makeText(this, getString(R.string.notify_en), Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, getString(R.string.notify_dis), Toast.LENGTH_SHORT).show();
		}
	}

	public void OnClickShare(){
		mDrawerLayout.closeDrawer(mDrawerList);

		Intent intent=new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		// Add data to the intent, the receiving app will decide what to do with it.
		intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_cmt)+"https://play.google.com/store/apps/details?id="+getApplication().getPackageName());

		startActivity(Intent.createChooser(intent, getString(R.string.share_how)));
	}
}
