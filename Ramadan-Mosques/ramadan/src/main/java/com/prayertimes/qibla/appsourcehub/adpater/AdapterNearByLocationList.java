package com.prayertimes.qibla.appsourcehub.adpater;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import muslim.prayers.time.R;

public class AdapterNearByLocationList extends BaseAdapter {

	ArrayList<String> placeNameList,placeDistList,placeAddressList;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	boolean play;
	Typeface tf;
	Typeface tf1;

	public AdapterNearByLocationList(Context context, ArrayList<String> list,
			ArrayList<String> nearByPlaceDistList, ArrayList<String> PlaceAddressList, Typeface typeface, Typeface typeface1) {
		play = false;
		tf = typeface;
		tf1 = typeface1;
		placeNameList = list;
		placeDistList = nearByPlaceDistList;
		placeAddressList = PlaceAddressList;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
	}

	public int getCount() {
		return placeNameList.size();
	}

	public Object getItem(int i) {
		return Integer.valueOf(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.near_by_loaction_list_item, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		TextView place = (TextView) view1.findViewById(R.id.messages);
		TextView txt_dist = (TextView) view1.findViewById(R.id.txt_dist);
		TextView txt_address =(TextView) view1.findViewById(R.id.txt_address);
		
		place.setText(placeNameList.get(position).toString());
		txt_dist.setText(placeDistList.get(position).toString()+" Kms");
		txt_address.setText(placeAddressList.get(position));
		
		return view1;
	}

}
