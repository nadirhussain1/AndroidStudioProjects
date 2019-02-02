package com.prayertimes.qibla.appsourcehub.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.adpater.IndividualChapterAdapter;
import com.prayertimes.qibla.appsourcehub.database.Database;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.QuranInfo;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class IndividualChapter extends Utils implements
		android.media.MediaPlayer.OnBufferingUpdateListener,
		android.media.MediaPlayer.OnCompletionListener {

	public static ImageView btn_pause;
	public static ImageView btn_play;
	public static long currentDuration;
	public static String lang_name = "English";
	public static MediaPlayer mediaPlayer;
	public static SeekBar seekBar;
	public static long totalDuration;
	public static TextView txt_curenttime;
	public static TextView txt_songname;
	public static TextView txt_totaltime;
	IndividualChapterAdapter adapter;
	ArrayList arabic;
	String chaptercount;
	private Database db;
	private Database db1;
	List dbfr;
	List dbgr;
	List dbin;
	List dblist;
	List dblist1;
	List dbma;
	List dbsp;
	List dbtr;
	List dbur;
	ArrayList english;
	private final Handler handler = new Handler();
	ArrayList lang;
	private int lengthOfAudio;
	ListView lv_single_chapter;
	boolean mplayer;
	String position;
	private final Runnable r = new Runnable() {
		public void run() {
			updateSeekProgress();
		}
	};
	ArrayList text_lang;
	String title;

	public IndividualChapter() {
		lang = new ArrayList();
		text_lang = new ArrayList();
		title = "";
		mplayer = false;
	}

	private void showCustomView() {
		MaterialDialog materialdialog = (new com.afollestad.materialdialogs.MaterialDialog.Builder(
				this))
				.title("Select Language")
				.customView(R.layout.language, true)
				.positiveText(R.string.choose)
				.negativeText(R.string.cancel)
				.callback(
						new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
							public void onNegative(
									MaterialDialog materialdialog1) {
							}

							public void onPositive(
									MaterialDialog materialdialog1) {
								setadapter();
							}
						}).build();
		((CheckBox) materialdialog.getCustomView().findViewById(R.id.ch_eng))
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("english");
							text_lang.add("english-true");
							return;
						} else {
							text_lang.add("english-false");
							return;
						}
					}
				});
		CheckBox checkbox = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_fren);
		if (LoadPref("french_lang").equals("true")) {
			checkbox.setChecked(true);
		}
		checkbox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton compoundbutton,
					boolean flag) {
				if (flag) {
					lang.add("french");
					SavePref("french_lang", "true");
					text_lang.add("french-true");
					return;
				} else {
					SavePref("french_lang", "false");
					text_lang.add("french-false");
					return;
				}
			}
		});
		CheckBox checkbox1 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_germ);
		if (LoadPref("german_lang").equals("true")) {
			checkbox1.setChecked(true);
		}
		checkbox1
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("german");
							SavePref("german_lang", "true");
							text_lang.add("german-true");
							return;
						} else {
							SavePref("german_lang", "false");
							text_lang.add("german-false");
							return;
						}
					}
				});
		CheckBox checkbox2 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_indo);
		if (LoadPref("indonesian_lang").equals("true")) {
			checkbox2.setChecked(true);
		}
		checkbox2
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("indonesian");
							SavePref("indonesian_lang", "true");
							text_lang.add("indonesian-true");
							return;
						} else {
							SavePref("indonesian_lang", "false");
							text_lang.add("indonesian-false");
							return;
						}
					}
				});
		CheckBox checkbox3 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_mala);
		if (LoadPref("malay_lang").equals("true")) {
			checkbox3.setChecked(true);
		}
		checkbox3
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("malay");
							SavePref("malay_lang", "true");
							text_lang.add("malay-true");
							return;
						} else {
							SavePref("malay_lang", "false");
							text_lang.add("malay-false");
							return;
						}
					}
				});
		CheckBox checkbox4 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_span);
		if (LoadPref("spanish_lang").equals("true")) {
			checkbox4.setChecked(true);
		}
		checkbox4
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("spanish");
							text_lang.add("spanish-true");
							SavePref("spanish_lang", "true");
							return;
						} else {
							SavePref("spanish_lang", "false");
							text_lang.add("spanish-false");
							return;
						}
					}
				});
		CheckBox checkbox5 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_turk);
		if (LoadPref("trukish_lang").equals("true")) {
			checkbox5.setChecked(true);
		}
		checkbox5
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("trukish");
							SavePref("trukish_lang", "true");
							text_lang.add("trukish-true");
							return;
						} else {
							SavePref("trukish_lang", "false");
							text_lang.add("trukish-false");
							return;
						}
					}
				});
		CheckBox checkbox6 = (CheckBox) materialdialog.getCustomView()
				.findViewById(R.id.ch_urdu);
		if (LoadPref("urdu_lang").equals("true")) {
			checkbox6.setChecked(true);
		}
		checkbox6
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton compoundbutton,
							boolean flag) {
						if (flag) {
							lang.add("urdu");
							SavePref("urdu_lang", "true");
							text_lang.add("urdu-true");
							return;
						} else {
							SavePref("urdu_lang", "false");
							text_lang.add("urdu-false");
							return;
						}
					}
				});
		materialdialog.show();
	}

	public void Translation(String s) {
		db1 = new Database(this, "quran.english.db");
		dblist1 = db1.getTransliteration(Integer.parseInt(chaptercount));
		if (LoadPref("french_lang").equals("true")) {
			db1 = new Database(this, "quran.french.db");
			dbfr = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("german_lang").equals("true")) {
			db1 = new Database(this, "quran.german.db");
			dbgr = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("indonesian_lang").equals("true")) {
			db1 = new Database(this, "quran.indonesian.db");
			dbin = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("malay_lang").equals("true")) {
			db1 = new Database(this, "quran.malay.db");
			dbma = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("spanish_lang").equals("true")) {
			db1 = new Database(this, "quran.spanish.db");
			dbsp = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("trukish_lang").equals("true")) {
			db1 = new Database(this, "quran.trukish.db");
			dbtr = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		if (LoadPref("urdu_lang").equals("true")) {
			db1 = new Database(this, "quran.urdu.db");
			dbur = db1.getTransliteration(Integer.parseInt(chaptercount));
		}
		adapter = new IndividualChapterAdapter(this, dblist, arabic, dblist1,
				dbfr, dbgr, dbin, dbma, dbsp, dbtr, dbur, afont, efont,
				tf_arabic);
		Log.i("Size",
				(new StringBuilder(String.valueOf(dblist.size()))).toString());
		if (dblist.size() > 0) {
			lv_single_chapter.setAdapter(adapter);
		}
	}

	public void Transliteration() {
		db = new Database(this, "quran.script.db");
		dblist = db.getTransliteration(Integer.parseInt(chaptercount));
	}

	public int getProgressPercentage(long l, long l1) {
		Double.valueOf(0.0D);
		long l2 = (int) (l / 1000L);
		long l3 = (int) (l1 / 1000L);
		return Double.valueOf(100D * ((double) l2 / (double) l3)).intValue();
	}

	public String milliSecondsToTimer(long l) {
		String s = "";
		int i = (int) (l / 0x36ee80L);
		int j = (int) (l % 0x36ee80L) / 60000;
		int k = (int) ((l % 0x36ee80L % 60000L) / 1000L);
		if (i > 0) {
			s = (new StringBuilder(String.valueOf(i))).append(":").toString();
		}
		String s1;
		if (k < 10) {
			s1 = (new StringBuilder("0")).append(k).toString();
		} else {
			s1 = (new StringBuilder()).append(k).toString();
		}
		return (new StringBuilder(String.valueOf(s))).append(j).append(":")
				.append(s1).toString();
	}

	public void onBackPressed() {

		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			handler.removeCallbacks(r);
		} else if (mediaPlayer != null && mplayer == false) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			handler.removeCallbacks(r);
		}
		super.onBackPressed();
	}

	public void onBufferingUpdate(MediaPlayer mediaplayer, int i) {
		seekBar.setSecondaryProgress(i);
	}

	public void onCompletion(MediaPlayer mediaplayer) {

		try {
			mediaPlayer.stop();
			mediaPlayer = null;
			btn_play.setVisibility(0);
			btn_pause.setVisibility(8);
			txt_totaltime.setText("0.00");
			txt_curenttime.setText("0.00");
			seekBar.setProgress(0);

			int lenght = QuranInfo.SURA_NAMES.length;
			int newPos = Integer.parseInt(getIntent().getExtras().getString(
					"count"));
			if (newPos < lenght) {
				Intent intent = new Intent(IndividualChapter.this,
						QuranActivity.class);
				intent.putExtra("from", "1");
				intent.putExtra("pos", newPos);
				startActivity(intent);
				finish();
			}

			return;
		} catch (IndexOutOfBoundsException indexoutofboundsexception) {
			return;
		} catch (IllegalArgumentException illegalargumentexception) {
			return;
		} catch (SecurityException securityexception) {
			return;
		} catch (IllegalStateException illegalstateexception) {
			return;
		}

	}

	protected void onCreate(Bundle bundle) {
		BufferedReader bufferedreader;
		String s3;
		super.onCreate(bundle);
		setContentView(R.layout.activity_individual_chapter);
		lv_single_chapter = (ListView) findViewById(R.id.single_chapter_list);
		Bundle bundle1 = getIntent().getExtras();
		chaptercount = bundle1.getString("count");
		title = bundle1.getString("title");
		Actionbar(title);
		Analytics(title);
		typeface();
		banner_ad();
		font();
		btn_play = (ImageView) findViewById(R.id.btn_play);
		btn_pause = (ImageView) findViewById(R.id.btn_pause);
		txt_curenttime = (TextView) findViewById(R.id.txt_curenttime);
		txt_totaltime = (TextView) findViewById(R.id.txt_totaltime);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		txt_songname = (TextView) findViewById(R.id.txt_songname);
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getString("count") != null) {
				position = getIntent().getExtras().getString("count");
				LogUtils.i((new StringBuilder("counts")).append(position)
						.toString());
			}
			if (getIntent().getExtras().getString("title") != null) {
				String s5 = getIntent().getExtras().getString("title");
				LogUtils.i((new StringBuilder("titles")).append(s5).toString());
			}
		}
		/*url = (new StringBuilder("http://truemuslims.net/Quran/Arabic/")).append(test)
				.append(3).append(".mp3").toString();*/
		LogUtils.i((new StringBuilder("url")).append(getPos(position)).toString());
		txt_songname.setText(title);
		seekBar.setOnTouchListener(new android.view.View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionevent) {
				try {
					if (IndividualChapter.mediaPlayer != null
							&& IndividualChapter.mediaPlayer.isPlaying()) {
						SeekBar seekbar = (SeekBar) view;
						lengthOfAudio = IndividualChapter.mediaPlayer
								.getDuration();
						IndividualChapter.mediaPlayer
								.seekTo((lengthOfAudio / 100)
										* seekbar.getProgress());
						int i = IndividualChapter.mediaPlayer
								.getCurrentPosition();
						IndividualChapter.mediaPlayer.seekTo(i);
						Log.i("on touch",
								(new StringBuilder(String.valueOf(i)))
										.append(" ")
										.append((lengthOfAudio / 100)
												* seekbar.getProgress())
										.toString());
					}
				} catch (Exception exception) {
				}
				return false;
			}
		});
		btn_pause.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (mplayer && IndividualChapter.mediaPlayer != null
						&& IndividualChapter.mediaPlayer.isPlaying()) {
					IndividualChapter.mediaPlayer.pause();
					mplayer = false;
					IndividualChapter.btn_pause.setVisibility(8);
					IndividualChapter.btn_play.setVisibility(0);
				}
			}
		});

		btn_play.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				if (IndividualChapter.mediaPlayer != null) {
					IndividualChapter.mediaPlayer.start();
					mplayer = true;
					IndividualChapter.btn_pause.setVisibility(0);
					IndividualChapter.btn_play.setVisibility(8);
					return;
				} else {
					(new Handler()).postDelayed(new Runnable() {
						public void run() {
							if (getPos(position).isEmpty()) {
								Toast.makeText(IndividualChapter.this,
										getString(R.string.toast_selectsong), 0)
										.show();
								return;
							} else {
								LogUtils.i((new StringBuilder("playurl"))
										.append(getPos(position)).toString());
								Toast.makeText(IndividualChapter.this,
										getString(R.string.toast_wait), 0)
										.show();
								play(getPos(position));
								return;
							}
						}
					}, 1000L);
					return;
				}
			}
		});
		english = new ArrayList();
		arabic = new ArrayList();
		dblist = new ArrayList();
		dbfr = new ArrayList();
		dbgr = new ArrayList();
		dbin = new ArrayList();
		dbma = new ArrayList();
		dbsp = new ArrayList();
		dbtr = new ArrayList();
		dbur = new ArrayList();
		text_lang.add("french-false");
		text_lang.add("german-false");
		text_lang.add("indonesian-false");
		text_lang.add("malay-false");
		text_lang.add("spanish-false");
		text_lang.add("trukish-false");
		text_lang.add("urdu-false");
		Transliteration();
		Translation("quran.english.db");
		String s = chaptercount;
		String s1;
		java.io.InputStream inputstream;
		String s2;
		if (s.length() == 1) {
			s1 = (new StringBuilder("00")).append(s).toString();
		} else if (s.length() == 2) {
			s1 = (new StringBuilder("0")).append(s).toString();
		} else {
			s1 = (new StringBuilder()).append(s).toString();
		}
		try {
			inputstream = getAssets().open(
					(new StringBuilder("text_arabic/")).append(s1)
							.append(".txt").toString());
			bufferedreader = new BufferedReader(new InputStreamReader(
					inputstream));
			s2 = bufferedreader.readLine();
			s3 = s2;
			while (s3 != null) {
				String s4;
				Log.d("Arabic", s3);
				arabic.add(s3);
				s4 = bufferedreader.readLine();
				s3 = s4;
			}
		} catch (Exception e) {
		}
		adapter = new IndividualChapterAdapter(this, dblist, arabic, dblist1,
				dbfr, dbgr, dbin, dbma, dbsp, dbtr, dbur, afont, efont,
				tf_arabic);
		Log.i("Size",
				(new StringBuilder(String.valueOf(dblist.size()))).toString());
		if (dblist.size() > 0) {
			lv_single_chapter.setAdapter(adapter);
		}

		if (getIntent().getExtras().getString("play").equals("1")) {
			if (IndividualChapter.mediaPlayer != null) {
				IndividualChapter.mediaPlayer.start();
				mplayer = true;
				IndividualChapter.btn_pause.setVisibility(0);
				IndividualChapter.btn_play.setVisibility(8);
				return;
			} else {
				(new Handler()).postDelayed(new Runnable() {
					public void run() {
						if (getPos(position).isEmpty()) {
							Toast.makeText(IndividualChapter.this,
									getString(R.string.toast_selectsong), 0)
									.show();
							return;
						} else {
							LogUtils.i((new StringBuilder("playurl")).append(
									getPos(position)).toString());
							Toast.makeText(IndividualChapter.this,
									getString(R.string.toast_wait), 0).show();
							play(getPos(position));
							return;
						}
					}
				}, 1000L);
				return;
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.quran_settings, menu);
		return true;
	}

	protected void onDestroy() {
		handler.removeCallbacks(r);
		super.onDestroy();
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		int i = menuitem.getItemId();
		if (i == R.id.langugage_settings) {
			showCustomView();
			return true;
		}
		if (i == 0x102002c) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(menuitem);
	}

	public void play(String s) {
		if (!isNetworkAvailable()) {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					Log.i("", " player stop ");
				}
				mediaPlayer = null;
			}
			txt_totaltime.setText("0.00");
			txt_curenttime.setText("0.00");
			mediaPlayer = null;
			mplayer = false;
			seekBar.setProgress(0);
			showalert();
			return;
		}
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				Log.i("", " player stop ");
			}
			mediaPlayer = null;
		}
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		try {
			LogUtils.i((new StringBuilder("url")).append(s).toString());
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setDataSource(s);
			mediaPlayer.prepare();
			mediaPlayer.start();
			btn_pause.setVisibility(0);
			btn_play.setVisibility(8);
			updateSeekProgress();
			mplayer = true;
		} catch (Exception e) {
		}
	}

	public void setadapter() {
		int i = 0;
		do {
			int j = lang.size();
			if (i >= j) {
				if (LoadPref("french_lang").equals("false")) {
					dbfr.clear();
				}
				if (LoadPref("german_lang").equals("false")) {
					dbgr.clear();
				}
				if (LoadPref("indonesian_lang").equals("false")) {
					dbin.clear();
				}
				if (LoadPref("malay_lang").equals("false")) {
					dbma.clear();
				}
				if (LoadPref("spanish_lang").equals("false")) {
					dbsp.clear();
				}
				if (LoadPref("trukish_lang").equals("false")) {
					dbtr.clear();
				}
				if (LoadPref("urdu_lang").equals("false")) {
					dbur.clear();
				}
				adapter = null;
				adapter = new IndividualChapterAdapter(this, dblist, arabic,
						dblist1, dbfr, dbgr, dbin, dbma, dbsp, dbtr, dbur,
						afont, efont, tf_arabic);
				LogUtils.i((new StringBuilder("dbfr ")).append(dbfr.size())
						.toString());
				if (dblist.size() > 0) {
					lv_single_chapter.setAdapter(adapter);
				}
				return;
			}
			lang_name = ((String) lang.get(i)).trim();
			db1 = new Database(this, (new StringBuilder("quran."))
					.append((String) lang.get(i)).append(".db").toString());
			LogUtils.i((new StringBuilder("lang quran."))
					.append((String) lang.get(i)).append(".db").toString());
			if (((String) lang.get(i)).equals("french")) {
				dbfr = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("german")) {
				dbgr = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("indonesian")) {
				dbin = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("malay")) {
				dbma = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("spanish")) {
				dbsp = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("trukish")) {
				dbtr = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			if (((String) lang.get(i)).equals("urdu")) {
				dbur = db1.getTransliteration(Integer.parseInt(chaptercount));
			}
			i++;
		} while (true);
	}

	public void updateSeekProgress() {
		try {
			if (mediaPlayer != null) {
				totalDuration = mediaPlayer.getDuration();
				currentDuration = mediaPlayer.getCurrentPosition();
				int i = getProgressPercentage(currentDuration, totalDuration);
				seekBar.setProgress(i);
				txt_totaltime.setText((new StringBuilder()).append(
						milliSecondsToTimer(totalDuration)).toString());
				txt_curenttime.setText((new StringBuilder()).append(
						milliSecondsToTimer(currentDuration)).toString());
				handler.postDelayed(r, 1000L);
			}
			return;
		} catch (Exception exception) {
			return;
		}
	}
	
	public String getPos(String poString)
	{
		String url1;
		int pos=Integer.valueOf(poString);
		if (pos<=9) 
		{
			url1 = (new StringBuilder("http://truemuslims.net/Quran/Arabic/00"))
					.append(pos).append(".mp3").toString();
			
		}else if (pos>9 && pos<99) {
			 url1 = (new StringBuilder("http://truemuslims.net/Quran/Arabic/0"))
					.append(pos).append(".mp3").toString();
		}else {
			 url1 = (new StringBuilder("http://truemuslims.net/Quran/Arabic/"))
					.append(pos).append(".mp3").toString();
		}
		System.out.println("====URL:"+url1);
		return url1;
		
	}
	

}
