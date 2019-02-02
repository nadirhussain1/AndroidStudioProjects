package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.*;

import com.prayertimes.qibla.appsourcehub.activity.ActivityNames;
import com.prayertimes.qibla.appsourcehub.activity.ActivityNamesList;
import com.prayertimes.qibla.appsourcehub.model.AllahNames;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;

import java.io.IOException;
import java.util.List;

import muslim.prayers.time.R;

public class AdapterNames extends BaseAdapter {

	TextView allah_name;
	TextView allah_title;
	ImageView btn_image;
	ImageView btn_play;
	List data;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	boolean play;
	Typeface tf;
	Typeface tf1;
	MediaPlayer mediaplayer;

	public AdapterNames(Context context, List list, Typeface typeface,
			Typeface typeface1) {
		play = false;
		tf = typeface;
		tf1 = typeface1;
		data = list;
		mContext = context;
		mediaplayer = new MediaPlayer();
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int i) {
		return Integer.valueOf(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.names_list, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		allah_name = (TextView) view1.findViewById(R.id.allah_name);
		btn_play = (ImageView) view1.findViewById(R.id.btn_play);
		btn_image = (ImageView) view1.findViewById(R.id.btn_image);
		allah_title = (TextView) view1.findViewById(R.id.allah_title);
		allah_name.setText((new StringBuilder(String.valueOf(position + 1)))
				.append(".").append(((AllahNames) data.get(position)).title)
				.toString());
		allah_title.setText(((AllahNames) data.get(position)).meaning);
		btn_image.setImageDrawable(loadImageFromAsset(
				((AllahNames) data.get(position)).title, position + 1));
		btn_play.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view2) {
				if (!mediaplayer.isPlaying()) {
					mediaplayer.reset();
					playTone(((AllahNames) data.get(position)).title,
							1 + position);
					return;
				} else {
					Toast.makeText(mContext, "Song is Playing Already", 0)
							.show();
					return;
				}
			}
		});
		lyt_listview.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view2) {
						LogUtils.i((new StringBuilder("id ")).append(
								1 + position).toString());
						mContext.startActivity((new Intent(
								mContext,
								com.prayertimes.qibla.appsourcehub.activity.ActivityNames.class))
								.putExtra("fid", position)
								.putExtra("title",
										((AllahNames) data.get(position)).title)
								.putExtra(
										"meaning",
										((AllahNames) data.get(position)).meaning)
								.putExtra(
										"desc1",
										((AllahNames) data.get(position)).description1)
								.putExtra(
										"desc2",
										((AllahNames) data.get(position)).description2));
					}
				});
		return view1;
	}

	protected CharSequence getString(int toastSongisplaying) {
		// TODO Auto-generated method stub
		return null;
	}

	public Drawable loadImageFromAsset(String s, int i) {
		String s1;
		String as[];
		String s2;
		Drawable drawable;
		try {
			LogUtils.i((new StringBuilder("title ")).append(s).toString());
			s1 = s;
			as = s.split("-");
			if (as[0].trim().contains(" ")) {
				s1 = s1.replace("\u2019", "").replace(" ", "_");
			}
			if (as[1].trim().startsWith("\u2019")
					|| as[1].trim().endsWith("\u2019")
					|| as[1].trim().contains(" ")) {
				s1 = s1.replace("\u2019", "").replace(" ", "_");
			}
			s2 = s1.toLowerCase().replace("-", "_").replace("\u2019", "_");
			LogUtils.i((new StringBuilder(String.valueOf(i)))
					.append(" adat title ").append(s2).toString());
			drawable = Drawable.createFromStream(
					mContext.getAssets().open(
							(new StringBuilder("list_image/")).append(i)
									.append("_").append(s2).append("_480.jpg")
									.toString()), null);
			return drawable;
		} catch (IOException ioexception) {
			return null;
		}
	}

	public void playTone(String s, int i) {
		String s1 = s;

		try {
			String as[] = s.split("-");
			if (as[0].trim().contains(" ")) {
				s1 = s1.replace("\u2019", "").replace(" ", "_");
			}
			if (as[1].trim().startsWith("\u2019")
					|| as[1].trim().endsWith("\u2019")) {
				s1 = s1.replace("\u2019", "");
			}

			String s2 = s1.toLowerCase().replace("-", "_")
					.replace("\u2019", "_");

			StringBuilder se = new StringBuilder("sound/").append(i)
					.append("_").append(s2).append(".mp3");

			AssetFileDescriptor assetfiledescriptor = mContext.getAssets()
					.openFd((new StringBuilder("sound/")).append(i).append("_")
							.append(s2).append(".mp3").toString());
			
			mediaplayer.setDataSource(assetfiledescriptor.getFileDescriptor(),
					assetfiledescriptor.getStartOffset(),
					assetfiledescriptor.getLength());
			mediaplayer.prepare();
			mediaplayer.start();
			btn_play.setImageResource(R.drawable.ic_play_clicked);
			play = true;
			ActivityNamesList.adapter.notifyDataSetChanged();
			mediaplayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer mediaplayer1) {
							btn_play.setImageResource(R.drawable.ic_play);
							play = false;
							ActivityNamesList.adapter.notifyDataSetChanged();
						}
					});
			return;
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}
}
