package com.prayertimes.qibla.appsourcehub.adpater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.activity.MessageBackgroungActivity;
import com.prayertimes.qibla.appsourcehub.activity.PopularDetailActivity;
import com.prayertimes.qibla.appsourcehub.activity.PopularSurasBackgroungActivity;
import com.prayertimes.qibla.appsourcehub.model.Messages;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;

public class AdapterSurasDetail extends BaseAdapter {

	TextView txt_sura, sec_txt_sura, txt_sura_2;
	ArrayList<String> sec_SuraTextList, suraTextList, suraTextList_2;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	Typeface tf;
	Typeface tf1;

	public AdapterSurasDetail(Context context, ArrayList<String> suraTextList,
			ArrayList<String> sec_SuraTextList,
			ArrayList<String> suraTextList_2, Typeface typeface,
			Typeface typeface1) {

		// this.suraList = suraLIst;
		this.suraTextList = suraTextList;
		this.sec_SuraTextList = sec_SuraTextList;
		this.suraTextList_2 = suraTextList_2;

		tf = typeface;
		tf1 = typeface1;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
	}

	public int getCount() {
		return suraTextList.size();
	}

	public Object getItem(int i) {
		return Integer.valueOf(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.suras_detail_activity, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		txt_sura = (TextView) view1.findViewById(R.id.txt_sura);
		txt_sura_2 = (TextView) view1.findViewById(R.id.txt_sura_2);
		sec_txt_sura = (TextView) view1.findViewById(R.id.sec_txt_sura);

		txt_sura.setText(position + 1 + ". " + suraTextList.get(position));
		sec_txt_sura.setText(sec_SuraTextList.get(position));
		txt_sura_2.setText(position + 1 + ". " + suraTextList_2.get(position));

		lyt_listview
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view2) {
						Intent i = new Intent(mContext,
								PopularSurasBackgroungActivity.class);
						i.putExtra("message", suraTextList_2.get(position));
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(i);
					}
				});

		return view1;
	}

}
