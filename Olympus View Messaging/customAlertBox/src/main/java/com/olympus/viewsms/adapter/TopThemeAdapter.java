package com.olympus.viewsms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.olympus.viewsms.R;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.fragment.ThemeFragment;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Utils;

import java.util.List;

public class TopThemeAdapter extends PagerAdapter {

	static int[] top_theme_ids={-1,19, 6,14, 5};
	private ThemeFragment themeFragment;
	private String [] avartarNames=null;
	Context context;

	public TopThemeAdapter(Context context, ThemeFragment themeFragment) {
		this.context = context;
		this.themeFragment=themeFragment;
		avartarNames=context.getResources().getStringArray(R.array.themes_avartar_names);
	}

	public int getCount() {
		return 5;
	}

	public Object instantiateItem(ViewGroup collection, int position) {
		LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_top_theme_page, null);

		ImageView iv_blurbg=(ImageView)view.findViewById(R.id.iv_blurbg);
		final LinearLayout ln_item=(LinearLayout)view.findViewById(R.id.ln_item);
		View dialogTempleView=view.findViewById(R.id.smsDialogTemple);
		if(position==0){
			ln_item.setVisibility(View.GONE);
			dialogTempleView.setVisibility(View.GONE);
			iv_blurbg.setBackgroundResource(R.drawable.parallax_image);
			iv_blurbg.setImageDrawable(null);
		}else{
			iv_blurbg.setImageDrawable(Utils.getDrawable(context, "blur_"+(position+1)));
			initSMSDialog(view, top_theme_ids[position],position);
			ln_item.setTag(position);
			ln_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int theme_id=top_theme_ids[(Integer)ln_item.getTag()];

					ThemeDAO themeDAO=new ThemeDAO(context);
					List<Theme> themes=themeDAO.getAvailableTheme();
					for(Theme t:themes)
						if(t.getId()==theme_id){
							Toast.makeText(context, "This theme has been unlocked and already in Available Themes", Toast.LENGTH_SHORT).show();
							return;
						}

					themeFragment.clickBuyTheme(theme_id);
				}
			});	
		}
		((ViewPager) collection).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}



	private void initSMSDialog(View view, int theme_id,int position){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Theme cur_theme=(new ThemeDAO(context)).getByID(theme_id);
		RelativeLayout lnTitle = (RelativeLayout)view.findViewById(R.id.lnTitle); 
		int title_id=cur_theme.getTitle_layout();

		View yourView = inflater.inflate(Utils.getLayoutID(context, "sms_layout_title"+title_id), null);
		lnTitle.removeAllViews();
		lnTitle.addView(yourView);

		TextView tv_number,tv_time,tv_content,tv_ok,tv_reply;
		ImageView img_bg,img_s1,img_s2,img_s3,img_avartar;
		RelativeLayout ln_h1,ln_content,ln_h2;

		tv_number=(TextView)yourView.findViewById(R.id.tv_number);
		tv_time=(TextView)yourView.findViewById(R.id.tv_time);
		ln_h1=(RelativeLayout)yourView.findViewById(R.id.ln_h1);

		ln_h2=(RelativeLayout)view.findViewById(R.id.ln_h2);
		tv_ok=(TextView)view.findViewById(R.id.tv_ok);
		tv_reply=(TextView)view.findViewById(R.id.tv_reply);
		tv_content=(TextView)view.findViewById(R.id.tv_content);
		img_bg=(ImageView)view.findViewById(R.id.img_bg);
		img_s1=(ImageView)view.findViewById(R.id.img_s1);
		img_s2=(ImageView)view.findViewById(R.id.img_s2);
		img_s3=(ImageView)view.findViewById(R.id.img_s3);
		img_avartar=(ImageView)view.findViewById(R.id.img_avatar);
		ln_content=(RelativeLayout)view.findViewById(R.id.ln_content);

		img_bg.setImageDrawable(Utils.getDrawable(context, "ic"+theme_id+"_bg"));
		img_s1.setImageDrawable(Utils.getDrawable(context, "ic"+theme_id+"_s1"));
		img_s2.setImageDrawable(Utils.getDrawable(context, "ic"+theme_id+"_s2"));
		img_s3.setImageDrawable(Utils.getDrawable(context, "ic"+theme_id+"_s3"));
		img_avartar.setImageDrawable(Utils.getDrawable(context, "avartar_"+(position%Constants.TOTAL_DEMO_AVARTARS)));

		tv_number.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_number.setText(avartarNames[position%Constants.TOTAL_DEMO_AVARTARS]);
		tv_time.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(cur_theme.getTitle_layout()==3)
			tv_time.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_content.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));

		ln_content.setPadding(cur_theme.getL(), cur_theme.getT(), cur_theme.getR(), cur_theme.getB());
		tv_content.setPadding(cur_theme.getL2(), 0, cur_theme.getR2(), 0);
		ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH1()));

		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH2());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		if(cur_theme.getButton_bottom()>0){
			params.setMargins(0, 0, 0,cur_theme.getButton_bottom());
		}
		ln_h2.setLayoutParams(params);

		if(cur_theme.getButton_width()<0){
			params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			tv_reply.setLayoutParams(params);
			tv_reply.setBackgroundResource(Utils.getDrawableResourceId(context, "ic"+cur_theme.getId()+"_reply"));

			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			tv_ok.setLayoutParams(params);
			tv_ok.setBackgroundResource(Utils.getDrawableResourceId(context, "ic"+cur_theme.getId()+"_ok"));
		}
		if(cur_theme.getIs_button_divider()==1){
			img_s3.setVisibility(View.INVISIBLE);
		}
	}
}