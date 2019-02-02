package com.nippt.arabicamharicdictionary.free;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nippt.arabicamharicdictionary.R;
import com.nippt.arabicamharicdictionary.adapter.LeftMenuAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("DefaultLocale")
public class BaseActivity extends AppCompatActivity {
	public SlidingMenu slidingMenu;
	private ListView lvLeftMenu;
	private LeftMenuAdapter adapter;
	private String[] mCategoriyNames;
	private static final String EMAIL_FEED_BACK = "dave.antonio3@gmail.com";
	private static final String SUBJECT_EMAIL = "Somali dictionary";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setAboveOffset(50);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		slidingMenu.setMenu(R.layout.left_menu);

		mCategoriyNames = getResources().getStringArray(R.array.category_array);

		getViews();
	}

	private void getViews() {
		lvLeftMenu = (ListView) this.findViewById(R.id.lvLeftMenu);

		List<String> categories = Arrays.asList(mCategoriyNames);
		adapter = new LeftMenuAdapter(this, R.layout.list_item_category, categories);
		lvLeftMenu.setAdapter(adapter);

		lvLeftMenu.setOnItemClickListener(onItemClick);
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			slidingMenu.toggle(true);

			switch (position) {
			case 0:
				if (BaseActivity.this instanceof SearchActivity)
					return;

				switchToActivity(SearchActivity.class);
				break;
			case 1:
				if (BaseActivity.this instanceof HistoryActivity)
					return;

				switchToActivity(HistoryActivity.class);
				break;
			case 2:
				if (BaseActivity.this instanceof FavoriteActivity)
					return;

				switchToActivity(FavoriteActivity.class);
				break;
			case 3:
				SearchActivity.showWordOfToday = true;
				if (BaseActivity.this instanceof SearchActivity) {
					SearchActivity activity =(SearchActivity)BaseActivity.this;
					activity.showDialogWordOfToday();
					return;
				}

				switchToActivity(SearchActivity.class);
				break;

			case 4:
				showRateAppDialog();
				break;
			case 5:
				sendFeedback();
				break;
			default:
				break;
			}
		}

	};

	private void sendFeedback() {
		List<Intent> targetedSendEmailIntents = new ArrayList<Intent>();
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		List<ResolveInfo> resInfo = this.getPackageManager()
				.queryIntentActivities(emailIntent, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				Intent targetedSendEmail = new Intent(
						android.content.Intent.ACTION_SEND);
				targetedSendEmail.setType("text/plain");

				if (info.activityInfo.packageName.toLowerCase()
						.contains("mail")
						|| info.activityInfo.name.toLowerCase()
								.contains("mail")) {
					targetedSendEmail.putExtra(
							android.content.Intent.EXTRA_EMAIL,
							new String[] { EMAIL_FEED_BACK });
					targetedSendEmail
							.putExtra(android.content.Intent.EXTRA_SUBJECT,
									SUBJECT_EMAIL);
					targetedSendEmail.setPackage(info.activityInfo.packageName);
					targetedSendEmailIntents.add(targetedSendEmail);
				}
			}
		}

		Intent chooserIntent = Intent.createChooser(
				targetedSendEmailIntents.remove(0),
				"Choose app to send your feedback");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				targetedSendEmailIntents.toArray(new Parcelable[] {}));
		startActivity(chooserIntent);
	}

	private void showRateAppDialog() {
		Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
		  startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
		  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
		}
	}

	private void showUpgradeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Upgrade to Pro vesrion");
		builder.setMessage("Do you want to upgrade to pro version?");
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id=com.nippt.amharicdictionary")));
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void switchToActivity(Class<?> actClass) {
		Intent launchActivity = new Intent(this, actClass);
		startActivity(launchActivity);
		finish();
	}
}
