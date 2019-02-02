package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.prayertimes.qibla.appsourcehub.model.AllahNames;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.util.List;
import muslim.prayers.time.R;

public class ActivityNames extends Utils {
	AdRequest adRequest;
	List dblist;
	Bundle extra;
	int id;
	ImageView img_allah;
	ImageView img_slider_next;
	ImageView img_slider_previous;
	ImageView img_tone;
	int interflag;
	RelativeLayout layout_allah;
	ListView list;
	LinearLayout lyt_content;
	int nid;
	boolean play;
	String title;
	TextView txt_counter;
	TextView txt_name_ar;
	TextView txt_name_en;
	TextView txt_name_meaning;
	MediaPlayer mediaplayer;

	public ActivityNames() {
		nid = 0;
		play = false;
		title = "";
		interflag = 1;
	}

	public void changeSliderView() {
		if (id == 0) {
			img_slider_previous.setVisibility(4);
			img_slider_next.setVisibility(0);
		} else {
			img_slider_previous.setVisibility(0);
			img_slider_next.setVisibility(0);
		}
		if (id == 98) {
			img_slider_next.setVisibility(4);
			img_slider_previous.setVisibility(0);
			return;
		} else {
			img_slider_next.setVisibility(0);
			img_slider_previous.setVisibility(0);
			return;
		}
	}

	public void getContent(int i) {
		txt_counter.setText((new StringBuilder(String.valueOf(i + 1))).append(
				"/99").toString());
		txt_name_ar.setText((new StringBuilder(String.valueOf(i + 1)))
				.append(".")
				.append(((AllahNames) ActivityNamesList.data.get(i)).title)
				.toString());
		LogUtils.i((new StringBuilder("getcontent  ")).append(
				((AllahNames) ActivityNamesList.data.get(i)).title).toString());
		title = ((AllahNames) ActivityNamesList.data.get(i)).title
				.toLowerCase().replace("-", "_").replace("\u2019", "_");

		txt_name_en
				.setText((new StringBuilder(
						String.valueOf(((AllahNames) ActivityNamesList.data
								.get(i)).description1)))
						.append("\n")
						.append(((AllahNames) ActivityNamesList.data.get(i)).description2)
						.toString());
		txt_name_meaning
				.setText(((AllahNames) ActivityNamesList.data.get(i)).meaning);
		loadImageFromAsset(((AllahNames) ActivityNamesList.data.get(i)).title,
				i + 1);
		txt_name_ar.setTextAppearance(this, styleheader[efont]);
		txt_counter.setTextAppearance(this, style[efont]);
		txt_name_en.setTextAppearance(this, style[efont]);
		txt_name_meaning.setTextAppearance(this, style[efont]);
	}

	public Drawable loadImageFromAsset(String s, int i) {
		Drawable drawable = null;
		String s1 = s;
		try {
			if (s1.contains("-")) {
				String as[] = s.split("-");
				if (as[0].trim().contains(" ")) {
					s1 = s1.replace("\u2019", "").replace(" ", "_");
				}
				if (as[1].trim().startsWith("\u2019")
						|| as[1].trim().endsWith("\u2019")
						|| as[1].trim().contains(" ")) {
					s1 = s1.replace("\u2019", "").replace(" ", "_");
				}
			}
			LogUtils.i((new StringBuilder(String.valueOf(i)))
					.append(" act title ").append(s1).toString());
			drawable = Drawable.createFromStream(
					getAssets().open(
							(new StringBuilder("images/"))
									.append(i)
									.append("_")
									.append(s1.toLowerCase().replace("-", "_")
											.replace("\u2019", "_"))
									.append("_480.jpg").toString()), null);
			img_allah.setImageDrawable(drawable);
		} catch (IOException ioexception) {
			return drawable;
		}
		return drawable;
	}

	public void onCreate(Bundle bundle) {
		fullscreen();
		super.onCreate(bundle);
		setContentView(R.layout.activity_names);
		adRequest = (new com.google.android.gms.ads.AdRequest.Builder())
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();

		Actionbar(getString(R.string.title_lbl_names));
		Analytics(getString(R.string.title_lbl_names));
		typeface();
		banner_ad();
		extra = getIntent().getExtras();
		id = extra.getInt("fid");
		txt_counter = (TextView) findViewById(R.id.txt_counter);
		img_tone = (ImageView) findViewById(R.id.img_tone);
		img_allah = (ImageView) findViewById(R.id.img_allah);
		img_slider_next = (ImageView) findViewById(R.id.img_slider_next);
		img_slider_previous = (ImageView) findViewById(R.id.img_slider_previous);
		lyt_content = (LinearLayout) findViewById(R.id.lyt_content);
		txt_name_ar = (TextView) findViewById(R.id.txt_name_ar);
		txt_name_en = (TextView) findViewById(R.id.txt_name_en);
		txt_name_meaning = (TextView) findViewById(R.id.txt_name_meaning);
		txt_name_ar.setTypeface(tf, 1);
		txt_name_meaning.setTypeface(tf, 1);
		txt_counter.setTypeface(tf2, 1);
		txt_name_en.setTypeface(tf1);
		if (id == 0) {
			img_slider_previous.setVisibility(4);
		}
		img_slider_next.setVisibility(0);
		getContent(id);
		LogUtils.i((new StringBuilder(String.valueOf(id))).append(" fid ")
				.append(1 + id).toString());
		if (id == 98) {
			img_slider_next.setVisibility(4);
			img_slider_previous.setVisibility(0);
		}

		img_tone.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (play == false) {
					playTone(title);
					return;
				} else {
					Toast.makeText(ActivityNames.this,
							"Song is Playing Already", 0).show();
					return;
				}
			}
		});
		img_slider_next
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view) {
						ActivityNames activitynames;
						android.view.animation.Animation animation;
						Runnable runnable;
						activitynames = ActivityNames.this;
						activitynames.interflag = 1 + activitynames.interflag;
						animation = AnimationUtils.loadAnimation(
								ActivityNames.this, R.anim.push_left_out);
						lyt_content.startAnimation(animation);
						runnable = new Runnable() {
							public void run() {
								ActivityNames activitynames = ActivityNames.this;
								activitynames.id = 1 + activitynames.id;
								if (id > 99) {
									id = 99;
								}
								getContent(id);
								android.view.animation.Animation animation = AnimationUtils
										.loadAnimation(ActivityNames.this,
												R.anim.push_left_in);
								lyt_content.startAnimation(animation);
								changeSliderView();
							}
						};
						(new Handler()).postDelayed(runnable, 150L);
					}
				});
		img_slider_previous
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view) {
						ActivityNames activitynames;
						android.view.animation.Animation animation;
						Runnable runnable;

						activitynames = ActivityNames.this;
						activitynames.interflag = 1 + activitynames.interflag;
						animation = AnimationUtils.loadAnimation(
								ActivityNames.this, R.anim.push_right_out);
						lyt_content.startAnimation(animation);
						runnable = new Runnable() {
							public void run() {
								ActivityNames activitynames = ActivityNames.this;
								activitynames.id = -1 + activitynames.id;
								if (id < 0) {
									id = 1;
								}
								getContent(id);
								android.view.animation.Animation animation = AnimationUtils
										.loadAnimation(ActivityNames.this,
												R.anim.push_right_in);
								lyt_content.startAnimation(animation);
								changeSliderView();
							}
						};
						(new Handler()).postDelayed(runnable, 150L);
					}
				});
	}

	protected void onResume() {
		font();
		super.onResume();
	}

	public void onStop() {
		super.onStop();
	}

	public void playTone(String s) {
		String s1 = s;

		if (s1.contains("__")) {
			s1 = s1.replace("__", "_");
		}
		if (s1.substring(s1.length() - 1).equals("_")) {
			s1 = s1.substring(0, s1.length() - 1);
		}
		try {
			if (s1.contains("-")) {
				String as[] = s.split("-");
				if (as[0].trim().contains(" ")) {
					s1 = s1.replace(" ", "_");
				}
				if (as[1].trim().startsWith("\u2019")
						|| as[1].trim().endsWith("\u2019")
						|| as[1].trim().contains(" ")) {
					s1 = s1.replace(" ", "_");
				}
			}
			AssetFileDescriptor assetfiledescriptor = getAssets().openFd(
					(new StringBuilder("sound/"))
							.append(1 + id)
							.append("_")
							.append(s1.toLowerCase().replace("-", "_")
									.replace("\u2019", "_")).append(".mp3")
							.toString());
			mediaplayer = new MediaPlayer();
			mediaplayer.setDataSource(assetfiledescriptor.getFileDescriptor(),
					assetfiledescriptor.getStartOffset(),
					assetfiledescriptor.getLength());
			mediaplayer.prepare();
			mediaplayer.start();
			play = true;
			img_tone.setImageResource(R.drawable.ic_play_clicked);
			mediaplayer
					.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer mediaplayer1) {
							img_tone.setImageResource(R.drawable.ic_play);
							play = false;
							mediaplayer.release();
						}
					});
			return;
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}
}
