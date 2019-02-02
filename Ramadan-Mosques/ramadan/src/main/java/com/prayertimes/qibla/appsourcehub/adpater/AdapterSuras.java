package com.prayertimes.qibla.appsourcehub.adpater;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.activity.PopularDetailActivity;

public class AdapterSuras extends BaseAdapter {

	TextView txt_sura;
	ArrayList<String> suraList, transliterationNameList, arabicNameList;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	Typeface tf;
	Typeface tf1;

	public AdapterSuras(Context context, ArrayList<String> suraLIst,
			ArrayList<String> arabicNameList,
			ArrayList<String> transliterationNameList, Typeface typeface,
			Typeface typeface1) {

		this.suraList = suraLIst;
		this.arabicNameList = arabicNameList;
		this.transliterationNameList = transliterationNameList;

		tf = typeface;
		tf1 = typeface1;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
	}

	public int getCount() {
		return suraList.size();
	}

	public Object getItem(int i) {
		return Integer.valueOf(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.suras_list, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		txt_sura = (TextView) view1.findViewById(R.id.txt_sura);

		txt_sura.setText(transliterationNameList.get(position));

		lyt_listview
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view2) {
						Intent i = new Intent(mContext,
								PopularDetailActivity.class);
						i.putExtra("suraName",transliterationNameList.get(position));
						i.putExtra("suraPos", position+1+"");
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(i);
					}
				});

		return view1;
	}

}
