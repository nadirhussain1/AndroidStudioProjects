package com.olympus.viewsms.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympus.viewsms.MainActivity;
import com.olympus.viewsms.R;
import com.olympus.viewsms.model.NavDrawerItem;
import com.olympus.viewsms.util.Constants;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {       
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		if(navDrawerItems.get(position).isCheckBoxVisible()){
			checkBox.setVisibility(View.VISIBLE);
			checkBox.setChecked(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.NOTIFY_ENABLE, true));
		}else{
			checkBox.setVisibility(View.GONE);
		}
		convertView.setTag(position);
		convertView.setOnClickListener(rowItemClickListener);
		return convertView;
	}
	OnClickListener rowItemClickListener=new OnClickListener() {

		@Override
		public void onClick(View view) {
			int position=(Integer)view.getTag();
			((MainActivity)context).customItemClick(position, view);

		}
	};

}
