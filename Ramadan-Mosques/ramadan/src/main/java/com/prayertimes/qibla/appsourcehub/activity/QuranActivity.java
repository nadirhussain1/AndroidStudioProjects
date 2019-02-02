package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R.string;
import com.prayertimes.qibla.appsourcehub.utils.QuranInfo;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import muslim.prayers.time.R;

public class QuranActivity extends Utils {
	public class MyBaseAdapter extends BaseAdapter {

		String formatted_excerpt;

		public int getCount() {
			return QuranInfo.SURA_NAMES.length;
		}

		public Object getItem(int i) {
			return null;
		}

		public long getItemId(int i) {
			return 0L;
		}

		public View getView(final int i, View view, ViewGroup viewgroup) {
			View view1 = ((LayoutInflater) getSystemService("layout_inflater"))
					.inflate(R.layout.quranchapterlistitem, null);
			TextView textview = (TextView) view1
					.findViewById(R.id.tv_chapter_count);
			TextView textview1 = (TextView) view1
					.findViewById(R.id.tv_chapter_title);
			TextView textview2 = (TextView) view1
					.findViewById(R.id.tv_chapter_desc);
			TextView textview3 = (TextView) view1
					.findViewById(R.id.tv_chapter_title_arabic);
			LinearLayout linearlayout = (LinearLayout) view1
					.findViewById(R.id.quranchapterlist);
			textview.setTextAppearance(QuranActivity.this,
					QuranActivity.this.styleheader[QuranActivity.this.efont]);
			textview1.setTextAppearance(QuranActivity.this,
					QuranActivity.this.style[QuranActivity.this.efont]);
			textview2.setTextAppearance(QuranActivity.this,
					QuranActivity.this.style[QuranActivity.this.efont]);
			textview3.setTextAppearance(QuranActivity.this,
					QuranActivity.this.style[QuranActivity.this.efont]);
			textview.setText((new StringBuilder(String.valueOf(String
					.valueOf(i + 1)))).append(".").toString());
			textview1.setText(QuranInfo.SURA_NAMES[i]);
			textview2.setText((new StringBuilder(String
					.valueOf(QuranInfo.SURA_DESC[i]))).append(" (")
					.append(String.valueOf(QuranInfo.SURA_NUM_AYAHS[i]))
					.append(") ").toString());
			textview3.setTypeface(QuranActivity.this.tf_arabic);
			textview3.setText(QuranInfo.SURA_NAMES_AR[i]);
			linearlayout
					.setOnClickListener(new android.view.View.OnClickListener() {
						public void onClick(View view2) {

							Intent intent;
							Object aobj[];
							quranactivity = QuranActivity.this;
							quranactivity.interflag = 1 + quranactivity.interflag;
							QuranActivity.this.pos = 1 + i;
							aobj = new Object[1];
							aobj[0] = Integer.valueOf(QuranActivity.this.pos);
					
							intent = new Intent(QuranActivity.this,com.prayertimes.qibla.appsourcehub.activity.IndividualChapter.class);
							intent.putExtra("count",i+1+"");
							intent.putExtra("title", QuranInfo.SURA_NAMES[i]);
							intent.putExtra("play", "0");
							startActivity(intent);
						}
					});
			return view1;
		}

		public MyBaseAdapter() {
			super();
		}
	}

	AdRequest adRequest;
	MyBaseAdapter adapter;
	int interflag;
	ListView lv_quran_chapters;
	QuranInfo quranInfo;
	QuranActivity quranactivity;

	public QuranActivity() {
		interflag = 1;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		if (getIntent().getExtras().getString("from").equals("1")) {

			int pos = getIntent().getExtras().getInt("pos");
			Intent intent;
			Object aobj[];

			quranactivity = QuranActivity.this;
			quranactivity.interflag = 1 + quranactivity.interflag;
			QuranActivity.this.pos = 1 + pos;
			aobj = new Object[1];
			aobj[0] = Integer.valueOf(QuranActivity.this.pos);

			intent = new Intent(QuranActivity.this, IndividualChapter.class);
			// intent.putExtra("count", String.format("%03d", aobj));
			
			intent.putExtra("count", pos+1+"");
			intent.putExtra("title", QuranInfo.SURA_NAMES[pos]);
			intent.putExtra("play", "1");
			startActivity(intent);
			finish();
		}

		setContentView(R.layout.activity_quran);
		adRequest = (new AdRequest.Builder()).build();

		Actionbar(getString(R.string.title_lbl_quran));
		Analytics(getString(R.string.title_lbl_quran));
		typeface();
		banner_ad();
		lv_quran_chapters = (ListView) findViewById(R.id.lv_quran_chapters);
		adapter = new MyBaseAdapter();
		lv_quran_chapters.setAdapter(adapter);

	}

	protected void onResume() {
		font();
		if (isOnline()) {
			findViewById(R.id.ad_layout).setVisibility(0);
		} else {
			findViewById(R.id.ad_layout).setVisibility(8);
		}
		super.onResume();
	}
}
