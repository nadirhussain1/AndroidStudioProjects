package com.nippt.arabicamharicdictionary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

	private Context mContext;

	public GridAdapter(Context mCtx) {
		mContext = mCtx;
	}

	private ArrayList<Integer> mList;

	public int getCount() {
		// TODO Auto-generated method stub
		if (mList == null)
			return 0;
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList == null)
			return null;
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (mList == null)
			return null;
		ImageView v;
		if (convertView == null) {
			v = new ImageView(mContext);

		} else {
			v = (ImageView) convertView;
		}
		v.setImageResource(mList.get(position));
		return v;
	}

	public ArrayList<Integer> getmList() {
		return mList;
	}

	public void setmList(ArrayList<Integer> mList) {
		this.mList = mList;
	}

}
