package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.afollestad.materialdialogs.MaterialDialog;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.util.Iterator;
import java.util.List;

import tascom.example.tasbeeh.DataBase;
import tascom.example.tasbeeh.DataBaseModel;

import muslim.prayers.time.R;

public class TasbeeActivity extends Utils {
	public class MyBaseAdapter extends BaseAdapter {

		String formatted_excerpt;

		public int getCount() {
			return databasefetch.size();
		}

		public Object getItem(int i) {
			return null;
		}

		public long getItemId(int i) {
			return 0L;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			View view1 = ((LayoutInflater) getSystemService("layout_inflater"))
					.inflate(R.layout.listviewcontent, null);
			TextView textview = (TextView) view1.findViewById(R.id.tv_title);
			TextView textview1 = (TextView) view1.findViewById(R.id.tv_desc);
			TextView textview2 = (TextView) view1.findViewById(R.id.tv_count);
			textview.setText(((DataBaseModel) databasefetch.get(i)).getTitle());
			textview1.setText(((DataBaseModel) databasefetch.get(i))
					.getDescription());
			textview2.setText(String.valueOf(((DataBaseModel) databasefetch
					.get(i)).getCount()));
			Log.d("Fetching titles", (new StringBuilder()).append(textview)
					.toString());
			return view1;
		}

		public MyBaseAdapter() {
			super();
		}
	}

	RelativeLayout main;
	MyBaseAdapter adapter;
	ImageView adding;
	ImageView clear;
	String count;
	ImageView countMinus;
	ImageView counting;
	int countval;
	List databasefetch;
	DataBase db;
	ImageView deleting;
	String description;
	Bundle extra;
	int id;
	ListView lvpost;
	int n;
	ImageView next;
	TextView text1;
	TextView text2;
	TextView text3;
	TextView texttitle;
	String title;
	EditText txt_count;
	EditText txt_desc;
	EditText txt_title;
	String type;

	public TasbeeActivity() {
		n = 0;
		title = "";
		type = "normal";
		countval = 0;
		db = null;
	}

	private void showCustomView() {
		MaterialDialog materialdialog = (new com.afollestad.materialdialogs.MaterialDialog.Builder(
				this))
				.title("Add New Tasbeeh")
				.cancelable(true)
				.positiveText("Ok")
				.positiveColor(Color.parseColor("#379e24"))
				.customView(R.layout.menucount, true)
				.negativeText(0x1040000)
				.negativeColor(Color.parseColor("#379e24"))
				.callback(
						new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
							public void onNegative(
									MaterialDialog materialdialog1) {
							}

							public void onPositive(
									MaterialDialog materialdialog1) {
								String s = txt_title.getText().toString();
								String s1 = txt_desc.getText().toString();
								String s2 = txt_count.getText().toString();
								Log.d("Title",
										(new StringBuilder(String.valueOf(s)))
												.toString());
								Log.d("Describtion",
										(new StringBuilder(String.valueOf(s1)))
												.toString());
								Log.d("count_number",
										(new StringBuilder(String.valueOf(s2)))
												.toString());
								if (s.equals("") || s1.equals("")
										|| s2.equals("")) {
									Toast.makeText(getApplicationContext(),
											"Enter the Details", 0).show();
									showCustomView();
									return;
								} else {
									db.inserTasbeehList(s, s1,
											Integer.parseInt(s2));
									Toast.makeText(getApplicationContext(),
											"Tasbee Added successfully", 0)
											.show();
									return;
								}
							}
						}).build();
		txt_title = (EditText) materialdialog.getCustomView().findViewById(
				R.id.title);
		txt_desc = (EditText) materialdialog.getCustomView().findViewById(
				R.id.Describtion);
		txt_count = (EditText) materialdialog.getCustomView().findViewById(
				R.id.countno);
		materialdialog.show();
	}

	public void counterUpdate() {
		count = String.valueOf(n);
		if (n < 10) {
			text3.setText((new StringBuilder(String.valueOf(n))).toString());
			text2.setText("0");
			text1.setText("0");
			return;
		}
		if (n > 9 && n < 100) {
			text3.setText(count.substring(1));
			text2.setText(count.substring(0, 1));
			return;
		} else {
			text3.setText(count.substring(2));
			text2.setText(count.substring(1, 2));
			text1.setText(count.substring(0, 1));
			return;
		}
	}

	public void delete() {
		(new com.afollestad.materialdialogs.MaterialDialog.Builder(this))
				.title("Are you sure want to delete the Tasbeeh list")
				.cancelable(false)
				.positiveText("Ok")
				.positiveColor(Color.parseColor("#379e24"))
				.negativeText(0x1040000)
				.negativeColor(Color.parseColor("#379e24"))
				.callback(
						new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
							public void onPositive(MaterialDialog materialdialog) {
								super.onPositive(materialdialog);
								Log.d("Receiving id value", (new StringBuilder(
										String.valueOf(id))).toString());
								db.deleteTasbeehList(id);
								text1.setText("0");
								text2.setText("0");
								text3.setText("0");
								databasefetch = db.fetchAllFeedList(0);
								Log.d("Size",
										(new StringBuilder(String
												.valueOf(databasefetch.size())))
												.toString());
								Toast.makeText(getApplicationContext(),
										"Deleted successfully", 0).show();
								if (databasefetch.size() > 0) {
									Iterator iterator;
									if (databasefetch.size() > id) {
										TasbeeActivity tasbeeactivity1 = TasbeeActivity.this;
										tasbeeactivity1.id = 1 + tasbeeactivity1.id;
									} else {
										TasbeeActivity tasbeeactivity = TasbeeActivity.this;
										tasbeeactivity.id = -1
												+ tasbeeactivity.id;
									}
									iterator = db.gettasbeeh(id).iterator();
									do {
										if (!iterator.hasNext()) {
											return;
										}
										DataBaseModel databasemodel = (DataBaseModel) iterator
												.next();
										n = 0;
										Log.d("Size", databasemodel.getTitle());
										countval = databasemodel.getCount();
										id = databasemodel.getKey_id();
										texttitle.setText(databasemodel
												.getTitle().toUpperCase());
										title = databasemodel.getTitle();
									} while (true);
								} else {
									n = 0;
									countval = 0;
									id = 0;
									texttitle.setText("");
									type = "list";
									extra = null;
									return;
								}
							}
						}).build().show();
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		super.onActivityResult(i, j, intent);
		LogUtils.i((new StringBuilder("reqestcode ")).append(i).toString());
		if (i == 1) {
			n = intent.getIntExtra("count", 0);
			counterUpdate();
		} else if (i == 2) {
			n = intent.getIntExtra("count", 0);
			counterUpdate();
			countval = intent.getIntExtra("countvalue", 0);
			title = intent.getStringExtra("maintitle");
			texttitle.setText(title.toUpperCase());
			n = 0;
			id = intent.getIntExtra("idvalue", 0);
			type = "list";
			return;
		}
	}

	MediaPlayer mp;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_tasbee);
		Actionbar(getString(R.string.lbl_tasbee));
		Analytics(getString(R.string.lbl_tasbee));
		typeface();
		banner_ad();
		adding = (ImageView) findViewById(R.id.three);
		deleting = (ImageView) findViewById(R.id.one);
		clear = (ImageView) findViewById(R.id.two);
		next = (ImageView) findViewById(R.id.four);
		counting = (ImageView) findViewById(R.id.countbtn);
		countMinus = (ImageView) findViewById(R.id.countMinus);
		text1 = (TextView) findViewById(R.id.t1);
		text2 = (TextView) findViewById(R.id.t2);
		text3 = (TextView) findViewById(R.id.t3);
		texttitle = (TextView) findViewById(R.id.tastitle);
		text1.setTypeface(tf3);
		text2.setTypeface(tf3);
		text3.setTypeface(tf3);
		extra = getIntent().getExtras();
		if (extra != null && extra.getInt("countvalue") > 0) {
			countval = extra.getInt("countvalue");
			title = extra.getString("maintitle");
			texttitle.setText(title.toUpperCase());
			n = 0;
			id = extra.getInt("idvalue");
			type = "list";
		}

		mp = MediaPlayer.create(TasbeeActivity.this, R.raw.beep);
		db = new DataBase(getApplication());
		db.openDataBase();
		final Vibrator vibrator = (Vibrator) getSystemService("vibrator");
		adding.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				showCustomView();
			}
		});
		countMinus.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (mp.isPlaying()) {
					mp.pause();
					mp.start();
				} else {
					mp.start();
				}

				if (extra != null || type.equals("list")) {
					if (n > 0) {
						TasbeeActivity tasbeeactivity = TasbeeActivity.this;
						tasbeeactivity.n = -1 + tasbeeactivity.n;
						counterUpdate();
					}
				} else if (n > 0) {
					TasbeeActivity tasbeeactivity1 = TasbeeActivity.this;
					tasbeeactivity1.n = -1 + tasbeeactivity1.n;
					counterUpdate();
					return;
				}
			}
		});
		counting.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {

				if (mp.isPlaying()) {
					mp.pause();
					mp.start();
				} else {
					mp.start();
				}

				if (extra != null || type.equals("list")) {
					if (n < countval) {
						TasbeeActivity tasbeeactivity = TasbeeActivity.this;
						tasbeeactivity.n = 1 + tasbeeactivity.n;
						counterUpdate();
					}
					if (n == countval) {
						vibrator.vibrate(300L);
						Toast.makeText(getApplicationContext(), getString(R.string.toast_tasbeereached), 0)
								.show();
					}
					return;
				} else {
					TasbeeActivity tasbeeactivity1 = TasbeeActivity.this;
					tasbeeactivity1.n = 1 + tasbeeactivity1.n;
					counterUpdate();
					return;
				}
			}
		});
		next.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(
						TasbeeActivity.this,
						com.prayertimes.qibla.appsourcehub.activity.FullscreenActivity.class);
				intent.putExtra("count", n);
				intent.putExtra("maincount", countval);
				intent.putExtra("title", title);
				intent.putExtra("type", type);
				startActivityForResult(intent, 1);
				Log.d("Sending value",
						(new StringBuilder(String.valueOf(n))).toString());
			}
		});
		clear.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				text1.setText("0");
				text2.setText("0");
				text3.setText("0");
				n = 0;
			}
		});
		deleting.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (countval > 0) {
					delete();
					return;
				} else {
					Toast.makeText(getApplicationContext(),
							"Select any Tasbeeh", 0).show();
					return;
				}
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tasbee, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		if (menuitem.getItemId() == R.id.action_menu) {
			databasefetch = db.fetchAllFeedList(0);
			Log.d("Size",
					(new StringBuilder(String.valueOf(databasefetch.size())))
							.toString());
			if (databasefetch.size() > 0) {
				startActivityForResult(
						new Intent(
								this,
								com.prayertimes.qibla.appsourcehub.activity.ListActivity.class),
						2);
			} else {
				Toast.makeText(this, "No Tasbeeh to List", 0).show();
			}
			return true;
		} else {
			return super.onOptionsItemSelected(menuitem);
		}
	}

	public void reset() {
	}

}
