package com.connectivityapps.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.connectivityapps.flashlight.R;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.GlobalUtil;

/**
 * Created by nadirhussain on 24/02/2015.
 */
public class NavigationAdapter extends BaseAdapter {


    private Context mContext = null;
    private String[] titlesList = null;
    private String[] iconsList = null;
    private String[] highlightedColorsList = null;
    private LayoutInflater inflater = null;

    private static class ViewHolder {
        private ImageView drawerIconView;
        private TextView drawerTitleView;
        private View sectionDivider;
        private View rowBgView;
    }

    public NavigationAdapter(Context context, String[] list) {
        this.mContext = context;
        this.titlesList = list;
        iconsList = context.getResources().getStringArray(R.array.drawer_icon_list);
        highlightedColorsList = context.getResources().getStringArray(R.array.drawer_selected_colors_list);
        inflater = (LayoutInflater) ((Activity) context).getLayoutInflater();

    }

    @Override
    public int getCount() {
        return titlesList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_view_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.rowBgView = convertView.findViewById(R.id.scalingLayout);
            holder.drawerIconView = (ImageView) convertView.findViewById(R.id.iconView);
            holder.drawerTitleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.sectionDivider = convertView.findViewById(R.id.sectionLineView);

            FontsUtil.applyFontToText(mContext,holder.drawerTitleView,FontsUtil.ROBOTO_REGULAR);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.drawerIconView.setBackgroundResource(GlobalUtil.getDrawableId(mContext, iconsList[position]));
        holder.drawerTitleView.setText(titlesList[position]);

        holder.drawerTitleView.setTextColor(Color.parseColor("#FFFFFF"));
        holder.sectionDivider.setVisibility(View.GONE);

        if(position==4){
            holder.sectionDivider.setVisibility(View.VISIBLE);
        }
        if(position== SettingsController.getInstance().lastSelectedScreen){
            holder.rowBgView.setBackgroundColor(Color.parseColor(highlightedColorsList[position]));
        }else if(position<5){
            holder.rowBgView.setBackgroundColor(Color.parseColor("#323232"));
        }else{
           holder.rowBgView.setBackgroundColor(Color.parseColor("#1E1E1E"));
        }

        return convertView;
    }
}
