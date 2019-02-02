package com.olympus.viewsms.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.MissContact;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MissContactAdapter extends BaseAdapter {
	private List<MissContact> lst;
	private Activity activity;
	private OnRowClick oRC;
	private boolean isRecentChats;
	Theme cur_theme;

	public MissContactAdapter(Activity context,List<MissContact> items, Theme cur_theme,boolean isRecentChats) {
		this.lst = items;
		this.activity = context;
		this.cur_theme=cur_theme;
		this.isRecentChats=isRecentChats;
	}

	public static class ViewHolder {
		public TextView tv_name,tv_num,tv_time;
		public ImageView img_avatar;
		public LinearLayout ln_item,missedLayout;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();

		if (convertView == null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_miss_contact, null);

			view.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			view.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			view.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			view.img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
			view.ln_item= (LinearLayout) convertView.findViewById(R.id.ln_item);
			view.missedLayout=(LinearLayout)convertView.findViewById(R.id.missedLayout);
			view.tv_name.setSelected(true);
			view.tv_num.setSelected(true);
            
			if(isRecentChats){
				view.missedLayout.setVisibility(View.GONE);
			}
			convertView.setTag(view);
		} 
		else {
			view = (ViewHolder) convertView.getTag();
		}

		MissContact c=lst.get(position);
		view.tv_name.setText(c.getName());
		view.tv_num.setText(c.getNum_sms()+"");
		view.tv_time.setText(c.getTimeagoString());

        int imageDimen= Scale.cvDPtoPX(activity, 50);
        if(c.getCatgory()== Constants.NATIVE_SMS_CAT) {
            Picasso.with(activity).load(c.getThumbnail()).placeholder(R.drawable.ic_contact_picture).resize(imageDimen,imageDimen).into(view.img_avatar);
        }else{
            int id=Integer.valueOf(c.getThumbnail());
            view.img_avatar.setImageBitmap(Utils.getScaledSocialBitmap(activity, id, imageDimen));
        }
        //Picasso.with(activity).load(c.getThumbnail()).placeholder(R.drawable.ic_contact_picture).into(view.img_avatar);
		//view.img_avatar.setImageBitmap(Utils.getContactThumb(activity,c.getThumbnail()));

		view.tv_name.setTextColor(Color.parseColor(cur_theme.getCtext()));
		view.tv_time.setTextColor(Color.parseColor(cur_theme.getCtext()));

		view.ln_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("ItemClick",""+position);
				oRC.rowClick(position);
			}
		});
		

		return convertView;
	}

	@Override
	public int getCount() {
		return lst.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lst.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	

	public void setOnRowClick(OnRowClick oRC){
		this.oRC = oRC;
	}

	public interface OnRowClick{
		public void rowClick(int pos);
	}
}