package com.prayertimes.qibla.appsourcehub.adpater;

import java.io.IOException;
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
import com.prayertimes.qibla.appsourcehub.model.Messages;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;

public class AdapterMessages extends BaseAdapter {

	TextView messages;
	TextView messages_title;
	ImageView btn_image;
	List data;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	boolean play;
	Typeface tf;
	Typeface tf1;

	public AdapterMessages(Context context, List list, Typeface typeface,
			Typeface typeface1) {
		play = false;
		tf = typeface;
		tf1 = typeface1;
		data = list;
		mContext = context;
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

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.message_list, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		messages = (TextView) view1.findViewById(R.id.messages);
		btn_image = (ImageView) view1.findViewById(R.id.btn_image);
		messages_title = (TextView) view1.findViewById(R.id.messages_title);
		messages.setText((new StringBuilder(String.valueOf(position + 1)))
				.append(".").append(((Messages) data.get(position)).title)
				.toString());
		messages_title.setText(((Messages) data.get(position)).meaning);
		btn_image.setImageDrawable(loadImageFromAsset(
				((Messages) data.get(position)).title, position + 1));

		lyt_listview
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View view2) {

						Intent i = new Intent(mContext,MessageBackgroungActivity.class);
						i.putExtra("message",(new StringBuilder(String.valueOf(position + 1))).append(".").append(((Messages) data.get(position)).title)
										.toString());
						i.putExtra("image", position + 1);

						mContext.startActivity(i);

						/*
						 * LogUtils.i((new StringBuilder("id ")).append( 1 +
						 * position).toString()); mContext.startActivity((new
						 * Intent( mContext,
						 * com.prayertimes.qibla.appsourcehub.activity
						 * .ActivityMessages.class)) .putExtra("fid", position)
						 * .putExtra("title", ((Messages)
						 * data.get(position)).title) .putExtra("meaning",
						 * ((Messages) data.get(position)).meaning));
						 */
					}
				});
		return view1;
	}

	public Drawable loadImageFromAsset(String s, int i) {
		String s1;
		String as[];
		String s2;
		Drawable drawable;
		try {
			s1 = i + "";
			System.out.println("-----" + s1);

			drawable = Drawable.createFromStream(
					mContext.getAssets().open(
							(new StringBuilder("list_message_images/"))
									.append(s1).append(".png").toString()),
					null);

			return drawable;
		} catch (IOException ioexception) {
			return null;
		}
	}

	/*
	 * public void playTone(String s, int i) { String s1 = s; try { String as[]
	 * = s.split("-"); if(as[0].trim().contains(" ")) { s1 =
	 * s1.replace("\u2019", "").replace(" ", "_"); }
	 * if(as[1].trim().startsWith("\u2019") || as[1].trim().endsWith("\u2019"))
	 * { s1 = s1.replace("\u2019", "").replace("", "_"); } String s2 =
	 * s1.toLowerCase().replace("-", "_").replace("\u2019", "_");
	 * AssetFileDescriptor assetfiledescriptor =
	 * mContext.getAssets().openFd((new
	 * StringBuilder("sound/")).append(i).append
	 * ("_").append(s2).append(".mp3").toString()); MediaPlayer mediaplayer =
	 * new MediaPlayer();
	 * mediaplayer.setDataSource(assetfiledescriptor.getFileDescriptor(),
	 * assetfiledescriptor.getStartOffset(), assetfiledescriptor.getLength());
	 * mediaplayer.prepare(); mediaplayer.start();
	 * ActivityNamesList.adapter.notifyDataSetChanged();
	 * 
	 * return; } catch(IOException ioexception) { ioexception.printStackTrace();
	 * } }
	 */
}
