package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.util.List;

import tascom.example.tasbeeh.DataBase;

import muslim.prayers.time.R;

public class FullscreenActivity extends Utils {

	Button btnclearcount;
	Button btnresetcount;
	int count;
	List databasefetch;
	DataBase db;
	RelativeLayout linear;
	int maincount;
	TextView txttitle;
	TextView txtview;
	String type;

	public FullscreenActivity() {
		count = 0;
		maincount = 0;
		type = "";
		db = null;
	}

	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("count", count);
		setResult(-1, intent);
		finish();
	}

	MediaPlayer mp;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_reset);
		Actionbar(getString(R.string.title_full_screen_tasbeeh));
		Analytics(getString(R.string.title_full_screen_tasbeeh));
		typeface();
		banner_ad();
		txtview = (TextView) findViewById(R.id.txtcount);
		txttitle = (TextView) findViewById(R.id.txttitle);
		linear = (RelativeLayout) findViewById(R.id.llayout1);
		btnresetcount = (Button) findViewById(R.id.btnreset_count);
		btnclearcount = (Button) findViewById(R.id.btnclear_count);
		final Vibrator vibrator = (Vibrator) getSystemService("vibrator");
		db = new DataBase(getApplication());
		db.openDataBase();
		databasefetch = db.fetchAllFeedList(0);
		Log.d("In main activity count val ",
				(new StringBuilder(String.valueOf(maincount))).toString());
		txtview.setTypeface(tf3);
		Bundle bundle1 = getIntent().getExtras();
		count = bundle1.getInt("count");
		maincount = bundle1.getInt("maincount");
		type = bundle1.getString("type");
		txttitle.setText(bundle1.getString("title").toUpperCase());
		txtview.setText(Integer.toString(count));
		mp = MediaPlayer.create(FullscreenActivity.this, R.raw.beep);
		linear.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (mp.isPlaying()) {
					mp.pause();
					mp.start();
				} else {
					mp.start();

				}

				if (type.equals("normal")) {
					FullscreenActivity fullscreenactivity3 = FullscreenActivity.this;
					fullscreenactivity3.count = 1 + fullscreenactivity3.count;
					txtview.setText((new StringBuilder(String.valueOf(count)))
							.toString());
				} else if (type.equals("list")) {
					if (count == 0) {
						FullscreenActivity fullscreenactivity2 = FullscreenActivity.this;
						fullscreenactivity2.count = 1 + fullscreenactivity2.count;
						txtview.setText((new StringBuilder(String
								.valueOf(count))).toString());
					} else if (!(count <= 0 || count >= maincount)) {
						FullscreenActivity fullscreenactivity = FullscreenActivity.this;
						fullscreenactivity.count = 1 + fullscreenactivity.count;
						Log.d("value",
								(new StringBuilder(String.valueOf(count)))
										.toString());
						txtview.setText((new StringBuilder(String
								.valueOf(count))).toString());
					}
					if (count == maincount) {
						vibrator.vibrate(300L);
						Toast.makeText(getApplicationContext(), "Reached", 0)
								.show();
					}
					if (maincount == 0) {
						txtview.setText(Integer.toString(0));
						FullscreenActivity fullscreenactivity1 = FullscreenActivity.this;
						fullscreenactivity1.count = 1 + fullscreenactivity1.count;
						txtview.setText((new StringBuilder(String
								.valueOf(count))).toString());
					}
				}
				LogUtils.i((new StringBuilder("count ")).append(count)
						.toString());
			}
		});
		btnresetcount
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view) {
						count = 0;
						txtview.setText((new StringBuilder(String
								.valueOf(count))).toString());
					}
				});
		btnclearcount
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view) {
						FullscreenActivity fullscreenactivity = FullscreenActivity.this;
						fullscreenactivity.count = -1
								+ fullscreenactivity.count;
						txtview.setText((new StringBuilder(String
								.valueOf(count))).toString());
					}
				});
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		Intent intent;
		switch (menuitem.getItemId()) {
		default:
			return super.onOptionsItemSelected(menuitem);

		case 16908332:
			intent = new Intent();
			break;
		}
		intent.putExtra("count", count);
		setResult(-1, intent);
		finish();
		return true;
	}
}
