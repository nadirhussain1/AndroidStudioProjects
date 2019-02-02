package com.olympus.viewsms.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.olympus.viewsms.R;
import com.olympus.viewsms.dialogs.UnlockFreeDialog;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

	public static int MODE_AVAILABLE=0;
	public static int MODE_MORE=1;
	public static int MODE_OLYMPUS=2;
	public static UnlockFreeDialog unlockDialog=null;

	private List<Theme> lst=null;
	private Activity activity;
	private int theme_adapter_mode=0;

	private OnClickBuy onCBuy;
	private OnClickUnlockFree onUnlockFree;
	private OnClickPreview onCPreview;
	private String [] avatarNames=null;
	private HashMap<String, Bitmap> bgDrawablesList=null;
	private SharedPreferences prefs;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name,orText,tv_ok,tv_reply,tv_content;
        public Button btn_buy,btn_unlock;
        public FrameLayout fl_buy;
        public LinearLayout ln_apply,ln_item, ln_item_inside;
        public RelativeLayout lnTitle,layout_bg,ln_content,ln_h2;
        public ImageView img_bg,img_s1,img_s2,img_s3,wear_IconCenter,wear_IconRight;
        public ScrollView contentScroll;

        public ViewHolder(View convertView) {
            super(convertView);

            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            orText=(TextView) convertView.findViewById(R.id.ortext);
            tv_reply=(TextView)convertView.findViewById(R.id.tv_reply);
            tv_ok=(TextView)convertView.findViewById(R.id.tv_ok);
            tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            ln_item_inside = (LinearLayout) convertView.findViewById(R.id.ln_item_inside);
            ln_apply = (LinearLayout) convertView.findViewById(R.id.ln_apply);
            ln_item = (LinearLayout) convertView.findViewById(R.id.ln_item);
            lnTitle=(RelativeLayout) convertView.findViewById(R.id.lnTitle);
            layout_bg=(RelativeLayout)convertView.findViewById(R.id.layout_bg);
            ln_content=(RelativeLayout)convertView.findViewById(R.id.ln_content);
            ln_h2=(RelativeLayout)convertView.findViewById(R.id.ln_h2);
            fl_buy = (FrameLayout) convertView.findViewById(R.id.fl_buy);

            btn_buy = (Button) convertView.findViewById(R.id.btn_buy);
            btn_unlock=(Button)convertView.findViewById(R.id.unlock_free_button);
            img_bg=(ImageView)convertView.findViewById(R.id.img_bg);
            img_s1=(ImageView)convertView.findViewById(R.id.img_s1);
            img_s2=(ImageView)convertView.findViewById(R.id.img_s2);
            img_s3=(ImageView)convertView.findViewById(R.id.img_s3);
            wear_IconCenter=(ImageView)convertView.findViewById(R.id.wearIconCenterView);
            wear_IconRight=(ImageView)convertView.findViewById(R.id.wearIconRightView);

            contentScroll=(ScrollView)convertView.findViewById(R.id.scrollView1);
        }

    }

	public ThemeAdapter(Activity activity, int theme_adapter_mode,List<Theme> items) {
		this.lst = items;
		this.activity = activity;
		this.theme_adapter_mode=theme_adapter_mode;
		bgDrawablesList=new HashMap<String, Bitmap>();
		prefs=PreferenceManager.getDefaultSharedPreferences(activity);
        avatarNames=activity.getResources().getStringArray(R.array.themes_avartar_names);
        populateMap();
	}
    private void populateMap(){

    }
	public void recycleBitmaps(){
		for(Map.Entry<String, Bitmap> entry : bgDrawablesList.entrySet()){
			entry.getValue().recycle();
			entry.setValue(null);
		}
		bgDrawablesList.clear();
		bgDrawablesList=null;
	}

    @Override
    public ThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_theme, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Theme theme=lst.get(position);

        initSMSDialog(holder, theme,position);
        holder.tv_name.setText(theme.getName());
        holder.btn_unlock.setVisibility(View.GONE);
        holder.orText.setVisibility(View.GONE);



        if(theme_adapter_mode==MODE_AVAILABLE){    //available
            holder.btn_buy.setVisibility(View.VISIBLE);
            holder.btn_buy.setText(activity.getString(R.string.choose_contact));

            if(Utils.wearThemesIdsPackagesMap.containsKey(theme.getId())){
                holder.wear_IconCenter.setVisibility(View.GONE);
                holder.wear_IconRight.setVisibility(View.VISIBLE);
            }else{
                holder.wear_IconCenter.setVisibility(View.GONE);
                holder.wear_IconRight.setVisibility(View.GONE);
            }
        }
        else if(theme_adapter_mode==MODE_OLYMPUS){    //olympus
            //check applied_theme
            int applied_theme_id=prefs.getInt((theme_adapter_mode==0)?Constants.APPLIED_THEME_ID:Constants.APPLIED_OLYMPUS_THEME_ID, Constants.DEFAULT_FIRST_THEME_ID);
            if(applied_theme_id!=-1 && applied_theme_id==theme.getId()){
                holder.btn_buy.setVisibility(View.GONE);
                holder.ln_apply.setVisibility(View.VISIBLE);
            }
            else{
                holder.btn_buy.setVisibility(View.VISIBLE);
                holder.btn_buy.setText(activity.getString(R.string.choose));
            }

            if(Utils.wearThemesIdsPackagesMap.containsKey(theme.getId())){
                holder.wear_IconCenter.setVisibility(View.GONE);
                holder.wear_IconRight.setVisibility(View.VISIBLE);
            }else{
                holder.wear_IconCenter.setVisibility(View.GONE);
                holder.wear_IconRight.setVisibility(View.GONE);
            }
        }
        else{
            //More Themes Section
            holder.btn_buy.setText(activity.getString(R.string.demo_string));
            holder.btn_buy.setBackgroundResource(R.drawable.choosecontact_button);
            holder.btn_unlock.setVisibility(View.VISIBLE);
            holder.fl_buy.setVisibility(View.VISIBLE);

            if(theme.getIs_coming_soon()==1){
                holder.fl_buy.setVisibility(View.GONE);
            }
            if(!Utils.wearThemesIdsPackagesMap.containsKey(theme.getId())){
                holder.orText.setVisibility(View.VISIBLE);
                holder.wear_IconCenter.setVisibility(View.GONE);
                holder.wear_IconRight.setVisibility(View.GONE);
            }else{
                holder.wear_IconCenter.setVisibility(View.VISIBLE);
                holder.wear_IconRight.setVisibility(View.GONE);
            }
        }

        holder.btn_buy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theme_adapter_mode != MODE_AVAILABLE) {
                    onCPreview.onClickPreview(position);
                } else {
                    onCBuy.onClickBuy(position);
                }

            }
        });

        holder.btn_unlock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onUnlockFree.onClickUnlockFree(position);
                EasyTracker.getInstance(activity).send(MapBuilder.createEvent("ui_action", "button_press", "Use It " + theme.getName(), null).build());
            }
        });
        holder.wear_IconCenter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                wearIconClick(theme.getName(),theme.getId(),position);

            }
        });
        holder.wear_IconRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                wearIconClick(theme.getName(),theme.getId(),position);

            }
        });

    }
    private void wearIconClick(String themeName,int themeId,int position){
        String themePackage=Utils.wearThemesIdsPackagesMap.get(themeId);
        boolean isInstalled=Utils.isAppInstalled(activity,themePackage);
        if(isInstalled){
            if(theme_adapter_mode == MODE_AVAILABLE || theme_adapter_mode==MODE_OLYMPUS){
                String message=activity.getString(R.string.already_have_wear_theme);
                Utils.alert(activity,message);
            }else{
                onUnlockFree.onClickUnlockFree(position);
            }
        }else{
              showDownloadThemeAlert(themePackage);
        }
        EasyTracker.getInstance(activity).send(MapBuilder.createEvent("ui_action", "button_press", "Wear unlock " + themeName, null).build());
    }
    private void showDownloadThemeAlert(final String packageName){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage("Download this theme for Android Wear,Free !")
                .setCancelable(false)
                .setNegativeButton("Later",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                           dialog.cancel();
                    }})
                .setPositiveButton("Yea!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        downloadWearTheme(packageName);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void downloadWearTheme(String packageName){
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, " unable to find market app", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

	private void initSMSDialog(ViewHolder convertView, Theme cur_theme,int position){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int title_id=cur_theme.getTitle_layout();

		View yourView = inflater.inflate(Utils.getLayoutID(activity, "sms_layout_title"+title_id), null);
        convertView.lnTitle.removeAllViews();
        convertView.lnTitle.addView(yourView);

        TextView tv_number=(TextView)yourView.findViewById(R.id.tv_number);
        TextView tv_name=(TextView)yourView.findViewById(R.id.tv_name);
        TextView tv_time=(TextView)yourView.findViewById(R.id.tv_time);
        RelativeLayout ln_h1=(RelativeLayout)yourView.findViewById(R.id.ln_h1);
        ImageView avatar=(ImageView)yourView.findViewById(R.id.img_avatar);

		String res_drawable_name="ic"+cur_theme.getId()+"_bg";
		Bitmap bgBitmap=bgDrawablesList.get(res_drawable_name);
		if(bgBitmap==null || bgBitmap.isRecycled()){
			bgBitmap=Utils.getBitmap(activity, res_drawable_name);
			bgDrawablesList.put(res_drawable_name, bgBitmap);
		}

        convertView.img_bg.setImageBitmap(bgBitmap);
        convertView.img_s1.setImageDrawable(Utils.getDrawable(activity, "ic"+cur_theme.getId()+"_s1"));
        convertView.img_s2.setImageDrawable(Utils.getDrawable(activity, "ic"+cur_theme.getId()+"_s2"));
        convertView.img_s3.setImageDrawable(Utils.getDrawable(activity, "ic"+cur_theme.getId()+"_s3"));
		avatar.setImageDrawable(Utils.getDrawable(activity, "avartar_"+(position%Constants.TOTAL_DEMO_AVARTARS)));

		tv_number.setTextColor(Color.parseColor(cur_theme.getCtitle()));
		tv_number.setText(avatarNames[position%Constants.TOTAL_DEMO_AVARTARS]);
		tv_time.setTextColor(Color.parseColor(cur_theme.getCtext()));
		if(cur_theme.getTitle_layout()==3)
			tv_time.setTextColor(Color.parseColor(cur_theme.getCtitle()));
        convertView.tv_content.setTextColor(Color.parseColor(cur_theme.getCtext()));

		if(tv_name !=null){
			tv_name.setTextColor(Color.parseColor(cur_theme.getCtext()));
		}

        convertView.ln_content.setPadding(cur_theme.getL(), cur_theme.getT(), cur_theme.getR(), cur_theme.getB());
        convertView.tv_content.setPadding(cur_theme.getL2(), 0, cur_theme.getR2(), 0);
		if(cur_theme.getId()==28){
			ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(Scale.cvDPtoPX(activity, 230), cur_theme.getH1()));
		}else{
			ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH1()));
		}

		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH2());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		if(cur_theme.getButton_bottom()>0){
			params.setMargins(0, 0, 0,cur_theme.getButton_bottom());
		}
        convertView.ln_h2.setLayoutParams(params);

		if(cur_theme.getButton_width()<0){
			params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.setMargins(Scale.cvDPtoPX(activity, 15), 0, 0,0);
            convertView.tv_reply.setLayoutParams(params);
            convertView.tv_reply.setBackgroundResource(Utils.getDrawableResourceId(activity, "ic"+cur_theme.getId()+"_reply"));

			params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(0, 0,Scale.cvDPtoPX(activity, 15),0);
            convertView.tv_ok.setLayoutParams(params);
            convertView.tv_ok.setBackgroundResource(Utils.getDrawableResourceId(activity, "ic"+cur_theme.getId()+"_ok"));

            convertView.tv_ok.setText("");
            convertView.tv_reply.setText("");

			params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ABOVE, R.id.ln_h2);
			params.setMargins(0,0, 0, Scale.cvDPtoPX(activity, 5));
            convertView.img_s2.setLayoutParams(params);
		}else{
            convertView.tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
            convertView.tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
            convertView.tv_ok.setBackgroundResource(0);
            convertView. tv_reply.setBackgroundResource(0);
		}
		if(cur_theme.getIs_button_divider()==1){
            convertView.img_s3.setVisibility(View.INVISIBLE);
		}else{
            convertView.img_s3.setVisibility(View.VISIBLE);
        }
		if(cur_theme.getContent_width()>0){
			params=new RelativeLayout.LayoutParams(Scale.cvDPtoPX(activity, cur_theme.getContent_width()),Scale.cvDPtoPX(activity, cur_theme.getContent_height()));
			params.setMargins(Scale.cvDPtoPX(activity, cur_theme.getContent_left()),Scale.cvDPtoPX(activity, cur_theme.getContent_top()), 0, 0);
            convertView.contentScroll.setLayoutParams(params);
            convertView.contentScroll.setBackgroundResource(Utils.getDrawableResourceId(activity, "ic"+cur_theme.getId()+"_content"));
		}else{
            params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            convertView. contentScroll.setLayoutParams(params);
            convertView.contentScroll.setPadding(Scale.cvDPtoPX(activity,10),0,Scale.cvDPtoPX(activity,10),Scale.cvDPtoPX(activity,2));
            convertView.contentScroll.setBackgroundResource(0);
        }
		if(cur_theme.getTheme_height()>0){
			int layout_height=Scale.cvDPtoPX(activity, cur_theme.getTheme_height());
			FrameLayout.LayoutParams frameParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,layout_height);
            convertView.layout_bg.setLayoutParams(frameParam);
		}else{
            int layout_height=Scale.cvDPtoPX(activity, 200);
            FrameLayout.LayoutParams frameParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,layout_height);
            convertView.layout_bg.setLayoutParams(frameParam);
        }
	}



	public void setOnClickBuyListener(OnClickBuy onCBuy){
		this.onCBuy = onCBuy;
	}
	public void setOnClickUnlockFreeListener(OnClickUnlockFree onClickFree){
		this.onUnlockFree=onClickFree;
	}

	public interface OnClickBuy{
		public void onClickBuy(int pos);
	}
	public interface OnClickUnlockFree{
		public void onClickUnlockFree(int pos);
	}
	public void setOnClickPreviewListener(OnClickPreview onCPreview){
		this.onCPreview = onCPreview;
	}

	public interface OnClickPreview{
		public void onClickPreview(int pos);
	}

}