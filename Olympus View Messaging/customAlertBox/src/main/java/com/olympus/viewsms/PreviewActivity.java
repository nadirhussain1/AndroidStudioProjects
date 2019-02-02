package com.olympus.viewsms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;

public class PreviewActivity extends Activity{

	private int theme_id;
	private int avartar_id;

	private TextView tv_number,tv_time,tv_content,tv_ok,tv_reply,tv_name;
	private ImageView img_bg,img_s1,img_s2,img_s3,img_avatar;
	private RelativeLayout ln_content,ln_h1,ln_h2,layout_bg;
	ScrollView contentScroll;
	private String [] avartarNames=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_single);

		avartarNames=getResources().getStringArray(R.array.themes_avartar_names);
		try{
			theme_id=getIntent().getIntExtra(Constants.APPLIED_THEME_ID, Constants.DEFAULT_FIRST_THEME_ID);
			avartar_id=getIntent().getIntExtra("avartar_id", 0);
		}catch(Exception e){}

		init();
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	private void init(){
		Theme cur_theme=(new ThemeDAO(this)).getByID(theme_id);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout lnTitle = (RelativeLayout)findViewById(R.id.lnTitle); 
		int title_id=cur_theme.getTitle_layout();

		View yourView = inflater.inflate(Utils.getLayoutID(this, "sms_layout_title"+title_id), null);
		lnTitle.removeAllViews();
		lnTitle.addView(yourView);

		tv_number=(TextView)findViewById(R.id.tv_number);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_time=(TextView)findViewById(R.id.tv_time);
		ln_h1=(RelativeLayout)findViewById(R.id.ln_h1);

		ln_h2=(RelativeLayout)findViewById(R.id.ln_h2);
		tv_ok=(TextView)findViewById(R.id.tv_ok);
		tv_reply=(TextView)findViewById(R.id.tv_reply);
		tv_content=(TextView)findViewById(R.id.tv_content);
		img_bg=(ImageView)findViewById(R.id.img_bg);
		img_s1=(ImageView)findViewById(R.id.img_s1);
		img_s2=(ImageView)findViewById(R.id.img_s2);
		img_s3=(ImageView)findViewById(R.id.img_s3);
		img_avatar=(ImageView)findViewById(R.id.img_avatar);
		ln_content=(RelativeLayout)findViewById(R.id.ln_content);
		contentScroll=(ScrollView)findViewById(R.id.scrollView1);
		layout_bg=(RelativeLayout)findViewById(R.id.layout_bg);
		
		img_bg.setImageDrawable(Utils.getDrawable(this, "ic"+theme_id+"_bg"));
		img_s1.setImageDrawable(Utils.getDrawable(this, "ic"+theme_id+"_s1"));
		img_s2.setImageDrawable(Utils.getDrawable(this, "ic"+theme_id+"_s2"));
		img_s3.setImageDrawable(Utils.getDrawable(this, "ic"+theme_id+"_s3"));
		img_avatar.setImageDrawable(Utils.getDrawable(this, "avartar_"+avartar_id));

		tv_number.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_number.setText(avartarNames[avartar_id]);
		tv_time.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(cur_theme.getTitle_layout()==3)
			tv_time.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_content.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
		tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(tv_name !=null){
			tv_name.setTextColor(Color.parseColor(cur_theme.getCtext()));
		}
		
		ln_content.setPadding(cur_theme.getL(), cur_theme.getT(), cur_theme.getR(), cur_theme.getB());
		tv_content.setPadding(cur_theme.getL2(), 0, cur_theme.getR2(), 0);
		if(cur_theme.getId()==28){
			ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(Scale.cvDPtoPX(this, 230), cur_theme.getH1()));
		}else{
			ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH1()));
		}
		
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH2());
    	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	if(cur_theme.getButton_bottom()>0){
    		params.setMargins(0, 0, 0,cur_theme.getButton_bottom());
    	}
    	ln_h2.setLayoutParams(params);
    	
    	if(cur_theme.getButton_width()<0){
    	   params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	   params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	   params.setMargins(Scale.cvDPtoPX(this, 5), 0, 0,0);
    	   tv_reply.setLayoutParams(params);
    	   tv_reply.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_reply"));
    	  
    	   params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	   params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	   params.setMargins(0, 0,Scale.cvDPtoPX(this, 5),0);
    	   tv_ok.setLayoutParams(params);
    	   tv_ok.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_ok"));
    	   
    	   tv_ok.setText("");
    	   tv_reply.setText("");
    	   
    	   params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	   params.addRule(RelativeLayout.ABOVE, R.id.ln_h2);
    	   params.setMargins(0,0, 0, Scale.cvDPtoPX(this, 5));
    	   img_s2.setLayoutParams(params);
    	}else{
    		tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
    		tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
    	}
    	if(cur_theme.getIs_button_divider()==1){
    		img_s3.setVisibility(View.INVISIBLE);
    	}
    	if(cur_theme.getContent_width()>0){
    		params=new RelativeLayout.LayoutParams(Scale.cvDPtoPX(this, cur_theme.getContent_width()),Scale.cvDPtoPX(this, cur_theme.getContent_height()));
    		params.setMargins(Scale.cvDPtoPX(this, cur_theme.getContent_left()),Scale.cvDPtoPX(this, cur_theme.getContent_top()), 0, 0);
    		contentScroll.setLayoutParams(params);
    		contentScroll.setBackgroundResource(Utils.getDrawableResourceId(this, "ic"+cur_theme.getId()+"_content"));
    	}
    	if(cur_theme.getTheme_height()>0){
    		FrameLayout.LayoutParams frameParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,Scale.cvDPtoPX(this, cur_theme.getTheme_height()));
    		layout_bg.setLayoutParams(frameParam);
    	}
	}

	public void OnClickExit(View v){
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		//try{
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
		//}catch (Exception e) {}
	}

}
