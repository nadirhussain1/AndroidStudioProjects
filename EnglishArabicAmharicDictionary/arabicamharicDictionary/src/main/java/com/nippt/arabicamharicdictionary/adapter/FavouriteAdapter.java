package com.nippt.arabicamharicdictionary.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippt.arabicamharicdictionary.R;
import com.nippt.arabicamharicdictionary.free.database.History;
import com.nippt.arabicamharicdictionary.free.listener.FavListener;

import java.util.ArrayList;

public class FavouriteAdapter extends BaseAdapter {
	private ArrayList<History> mDataList;
	private Context mContext;
	private Typeface mFont;
	private FavListener mListener;

	public FavouriteAdapter(Context mCtx) {
		mContext = mCtx;
		mFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gfzemen.ttf");
	}

	public void setData(ArrayList<History> data) {
		mDataList = data;
	}

	public void setFavouriteListener(FavListener listener) {
		mListener = listener;
	}

	public ArrayList<History> getData() {
		return mDataList;
	}

	@Override
	public int getCount() {
		if (mDataList == null)
			return 0;
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mDataList == null)
			return null;
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fav_list_item, parent, false);
			holder = new ViewHolder();
			holder.engWord = (TextView) convertView.findViewById(R.id.Word);
			holder.amWord = (TextView) convertView.findViewById(R.id.Meaning);
			holder.removeBtn = (ImageView) convertView.findViewById(R.id.RemoveButton);
			holder.amWord.setTypeface(mFont);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.engWord.setText(mDataList.get(position).getEnglishWord());
		holder.amWord.setText(mDataList.get(position).getArabicWord());
		holder.wordId = mDataList.get(position).getWordId();
		holder.removeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListener.removeFavourite(mDataList.get(position).getWordId());
			}
		});

		return convertView;
	}

	static class ViewHolder {

		TextView engWord;
		TextView amWord;
		ImageView removeBtn;
		int wordId;

	}

}
