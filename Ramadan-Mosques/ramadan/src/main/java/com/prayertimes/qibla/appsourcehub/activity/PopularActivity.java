package com.prayertimes.qibla.appsourcehub.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterSuras;
import com.prayertimes.qibla.appsourcehub.database.Locationdbhelper;
import com.prayertimes.qibla.appsourcehub.model.SurasBean;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class PopularActivity extends Utils implements OnClickListener {

	Locationdbhelper dbOpenHelper;
	ArrayList<String> SuraList, ArabicNameList, TransliterationNameList;
	ArrayList<String> SecSuraList, TextList;
	ArrayList<SurasBean> listSuras;
	private SQLiteDatabase database;
	SurasBean bean;
	LinearLayout liner_Ayat_Al_Kursi, liner_Sura_At_Talaaq;
	ImageView img_Ayat_Al_Kursi, img_Sura_At_Talaaq;

	public static String DB_NAME = "quran_v8.sqlite";
	public static String TABLE_NAME = "chapters";
	public static String TABLE_NAME_2 = "quran_en_transliteration";
	public static String SURA = "sura";
	public static String SURA_2 = "sura";
	public static String TEXT = "text";
	public static String NAME_ARABIC = "name_arabic";
	public static String NAME_TRANSLITERATION = "name_transliteration";
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popular_activity);

		bean = new SurasBean();
		SuraList = new ArrayList<String>();
		ArabicNameList = new ArrayList<String>();
		TransliterationNameList = new ArrayList<String>();

		dbOpenHelper = new Locationdbhelper(PopularActivity.this, DB_NAME);
		database = dbOpenHelper.openDataBase();

		Actionbar(getString(R.string.title_lbl_popular));
		getSuras();

		for (int i = 0; i < listSuras.size(); i++) {
			SuraList.add(listSuras.get(i).getSura());
			ArabicNameList.add(listSuras.get(i).getName_arabic());
			TransliterationNameList.add(listSuras.get(i)
					.getName_transliteration());

		}

		liner_Ayat_Al_Kursi = (LinearLayout) findViewById(R.id.liner_Ayat_Al_Kursi);
		liner_Sura_At_Talaaq = (LinearLayout) findViewById(R.id.liner_Sura_At_Talaaq);
		img_Ayat_Al_Kursi = (ImageView) findViewById(R.id.img_Ayat_Al_Kursi);
		img_Sura_At_Talaaq = (ImageView) findViewById(R.id.img_Sura_At_Talaaq);

		liner_Ayat_Al_Kursi.setOnClickListener(this);
		liner_Sura_At_Talaaq.setOnClickListener(this);
		img_Ayat_Al_Kursi.setOnClickListener(this);
		img_Sura_At_Talaaq.setOnClickListener(this);

		list = (ListView) findViewById(R.id.listView1);
		AdapterSuras adapter = new AdapterSuras(getApplicationContext(),
				SuraList, ArabicNameList, TransliterationNameList,
				PopularActivity.this.tf, PopularActivity.this.tf2);
		list.setAdapter(adapter);

	}

	private void getSuras() {

		String where = "country_name=?";
		String[] args = { "Iran" };

		Cursor surasCursor = database.query(TABLE_NAME, new String[] { SURA,
				NAME_ARABIC, NAME_TRANSLITERATION }, null, null, null, null,
				null);

		surasCursor.moveToFirst();
		listSuras = new ArrayList<SurasBean>();
		if (!surasCursor.isAfterLast()) {
			do {
				SurasBean Bean = new SurasBean();
				Bean.sura = surasCursor.getString(0);
				Bean.name_arabic = surasCursor.getString(1);
				Bean.name_transliteration = surasCursor.getString(2);
				listSuras.add(Bean);
			} while (surasCursor.moveToNext());

		}
		surasCursor.close();

	}
//Prashant
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.liner_Ayat_Al_Kursi:

			Intent i = new Intent(PopularActivity.this,
					PopularDetailActivity.class);
			i.putExtra("suraName", TransliterationNameList.get(1));
			i.putExtra("suraPos", 1 + 1 + "");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);

			break;
		case R.id.liner_Sura_At_Talaaq:
			Intent i1 = new Intent(PopularActivity.this,
					PopularDetailActivity.class);
			i1.putExtra("suraName", TransliterationNameList.get(64));
			i1.putExtra("suraPos", 64 + 1 + "");
			i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i1);
			break;

		case R.id.img_Ayat_Al_Kursi:

			Intent i3 = new Intent(PopularActivity.this,
					PopularSurasBackgroungActivity.class);
			i3.putExtra("message", "And those who disbelieved are allies of one another. if you do not do so, there will be fithah on earth and great corruption.");
			i3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i3);

			break;
		case R.id.img_Sura_At_Talaaq:
			Intent i4 = new Intent(PopularActivity.this,
					PopularSurasBackgroungActivity.class);
			i4.putExtra("message", "They will not taste there in [any] coolness or drink.");
			i4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i4);
			break;

		}

	}

}
