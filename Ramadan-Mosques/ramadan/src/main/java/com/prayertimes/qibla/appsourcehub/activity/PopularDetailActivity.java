package com.prayertimes.qibla.appsourcehub.activity;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterSuras;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterSurasDetail;
import com.prayertimes.qibla.appsourcehub.database.Locationdbhelper;
import com.prayertimes.qibla.appsourcehub.model.SurasBean;
import com.prayertimes.qibla.appsourcehub.model.SurasDetailBean;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class PopularDetailActivity extends Utils {

	Locationdbhelper dbOpenHelper,dbOpenHelper_2;
	ArrayList<String> SuraList, SuraTextList;
	ArrayList<String> SuraList_2, SuraTextList_2;
	ArrayList<String> Sec_SuraList, Sec_SuraTextList;
	ArrayList<SurasDetailBean> listSuras,Sec_listSuras,listSuras_2;
	private SQLiteDatabase database,database_2;
	SurasDetailBean bean;

	public static String DB_NAME = "quran_v8.sqlite";
	//---------1----------------
	public static String TABLE_NAME = "quran_en_transliteration";
	public static String SURA = "sura";
	public static String TEXT = "text";
	//---------2---------------
	public static String SEC_TABLE_NAME = "quran_uthmani";
	public static String SEC_SURA = "sura";
	public static String SEC_TEXT = "text";
	//---------3--------
	public static String DB_NAME_2 = "quran.english.db";
	public static String TABLE_NAME_2 = "verses";
	public static String SURA_2 = "sura";
	public static String TEXT_2 = "text";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popular_detail_activity);

		bean = new SurasDetailBean();
		SuraList = new ArrayList<String>();
		SuraTextList = new ArrayList<String>();
		Sec_SuraList = new ArrayList<String>();
		Sec_SuraTextList = new ArrayList<String>();
		SuraTextList_2 = new ArrayList<String>();

		dbOpenHelper = new Locationdbhelper(PopularDetailActivity.this, DB_NAME);
		database = dbOpenHelper.openDataBase();

		dbOpenHelper_2 = new Locationdbhelper(PopularDetailActivity.this, DB_NAME_2);
		database_2 = dbOpenHelper_2.openDataBase();
		
		String title = getIntent().getExtras().getString("suraName");
		String suraPos = getIntent().getExtras().getString("suraPos");
		Actionbar(title);
		getSuras();
		getSuras_2();
		getSuras_sec();

		for (int i = 0; i < listSuras.size(); i++) {

			if (listSuras.get(i).getSura().equals(suraPos)) {
				SuraTextList.add(listSuras.get(i).getText());
				Sec_SuraTextList.add(Sec_listSuras.get(i).getSec_text());
				SuraTextList_2.add(listSuras_2.get(i).getText_2());
			}

		}
		
		

		ListView list = (ListView) findViewById(R.id.listView1);

		AdapterSurasDetail adapter = new AdapterSurasDetail(
				getApplicationContext(), SuraTextList,Sec_SuraTextList,SuraTextList_2,
				PopularDetailActivity.this.tf, PopularDetailActivity.this.tf2);
		list.setAdapter(adapter);

	}

	private void getSuras() {

		String where = "country_name=?";
		String[] args = { "Iran" };

		Cursor surasCursor = database.query(TABLE_NAME, new String[] { SURA,
				TEXT }, null, null, null, null, null);

		surasCursor.moveToFirst();
		listSuras = new ArrayList<SurasDetailBean>();
		if (!surasCursor.isAfterLast()) {
			do {
				SurasDetailBean Bean = new SurasDetailBean();
				Bean.sura = surasCursor.getString(0);
				Bean.text = surasCursor.getString(1);
				listSuras.add(Bean);
			} while (surasCursor.moveToNext());

		}
		surasCursor.close();

	}
	
	private void getSuras_2() {

		String where = "country_name=?";
		String[] args = { "Iran" };

		Cursor surasCursor = database_2.query(TABLE_NAME_2, new String[] { SURA_2,
				TEXT_2 }, null, null, null, null, null);

		surasCursor.moveToFirst();
		listSuras_2 = new ArrayList<SurasDetailBean>();
		if (!surasCursor.isAfterLast()) {
			do {
				SurasDetailBean Bean = new SurasDetailBean();
				Bean.sura_2 = surasCursor.getString(0);
				Bean.text_2 = surasCursor.getString(1);
				listSuras_2.add(Bean);
			} while (surasCursor.moveToNext());

		}
		surasCursor.close();

	}
	
	
	private void getSuras_sec() {

		String where = "country_name=?";
		String[] args = { "Iran" };

		Cursor surasCursor = database.query(SEC_TABLE_NAME, new String[] { SEC_SURA,
				SEC_TEXT }, null, null, null, null, null);

		surasCursor.moveToFirst();
		Sec_listSuras = new ArrayList<SurasDetailBean>();
		if (!surasCursor.isAfterLast()) {
			do {
				SurasDetailBean Bean = new SurasDetailBean();
				Bean.sec_sura = surasCursor.getString(0);
				Bean.sec_text = surasCursor.getString(1);
				Sec_listSuras.add(Bean);
			} while (surasCursor.moveToNext());

		}
		surasCursor.close();

	}

}
