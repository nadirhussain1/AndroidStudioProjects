package com.nippt.arabicamharicdictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippt.arabicamharicdictionary.R;

import java.util.List;

public class LeftMenuAdapter extends ArrayAdapter<String> {
	private List<String> categories;
	private int[] drawables = {R.drawable.icon_search,R.drawable.icon_history,
			R.drawable.icon_favorite, R.drawable.icon_word_of_day,
			R.drawable.icon_rate_app, R.drawable.icon_feedback};

	public LeftMenuAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		this.categories = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CategoryHolder holder;
		View row = convertView;
		if (row == null) {
			row = LayoutInflater.from(getContext()).inflate(R.layout.list_item_category, parent, false);
			
			holder = new CategoryHolder();
			holder.tvName = (TextView) row.findViewById(R.id.tvName);
			holder.ivIcon = (ImageView) row.findViewById(R.id.ivIcon);
			
			row.setTag(holder);
		} else {
			holder = (CategoryHolder) row.getTag();
		}
		holder.tvName.setText(categories.get(position));
		holder.ivIcon.setImageResource(drawables[position]);
		
		return row;
	}
	
	public static class CategoryHolder {
		public TextView tvName;
		public ImageView ivIcon;
	}

}
