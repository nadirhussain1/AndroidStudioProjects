package com.olympus.viewsms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class StackAdapter extends BaseAdapter {
	private Context contxt;
	private List<SmsData> infos;
	private OnClickClose onCClose;
	private OnClickReply onCReply;
	private Theme cur_theme;

	public StackAdapter(Context c,List<SmsData> infos, Theme cur_theme) {
		contxt = c;
		this.infos=infos;
		this.cur_theme=cur_theme;
	}

	public int getCount() {
		return infos.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = vi.inflate(R.layout.item_stack, null, false);
		}

		SmsData sms=infos.get(position);

		if(sms!=null){
			RelativeLayout lnTitle = (RelativeLayout)view.findViewById(R.id.lnTitle); 
			int title_id=cur_theme.getTitle_layout();

			View yourView = vi.inflate(Utils.getLayoutID(contxt, "sms_layout_title"+title_id), null);
			lnTitle.removeAllViews();
			lnTitle.addView(yourView);

			TextView tv_number = (TextView)view.findViewById(R.id.tv_number);
			TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			ImageView img_avatar = (ImageView)view.findViewById(R.id.img_avatar);
			RelativeLayout ln_h1=(RelativeLayout)view.findViewById(R.id.ln_h1);

			TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
			TextView tv_reply = (TextView) view.findViewById(R.id.tv_reply);
			TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
			RelativeLayout ln_h2=(RelativeLayout)view.findViewById(R.id.ln_h2);
			RelativeLayout ln_content=(RelativeLayout)view.findViewById(R.id.ln_content);
			ImageView img_bg=(ImageView)view.findViewById(R.id.img_bg);
			ImageView img_s1=(ImageView)view.findViewById(R.id.img_s1);
			ImageView img_s2=(ImageView)view.findViewById(R.id.img_s2);
			ImageView img_s3=(ImageView)view.findViewById(R.id.img_s3);
			ScrollView contentScroll=(ScrollView)view.findViewById(R.id.scrollView1);
			RelativeLayout layout_bg=(RelativeLayout)view.findViewById(R.id.layout_bg);

			img_bg.setImageDrawable(Utils.getDrawable(contxt, "ic"+cur_theme.getId()+"_bg"));
			img_s1.setImageDrawable(Utils.getDrawable(contxt, "ic"+cur_theme.getId()+"_s1"));
			img_s2.setImageDrawable(Utils.getDrawable(contxt, "ic"+cur_theme.getId()+"_s2"));
			img_s3.setImageDrawable(Utils.getDrawable(contxt, "ic"+cur_theme.getId()+"_s3"));

			tv_number.setTextColor(Color.parseColor(cur_theme.getCtitle()));
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
			ln_h1.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH1()));
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cur_theme.getH2());
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			if(cur_theme.getButton_bottom()>0){
				params.setMargins(0, 0, 0,cur_theme.getButton_bottom());
			}
			ln_h2.setLayoutParams(params);

			if(cur_theme.getButton_width()<0){
				params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params.setMargins(Scale.cvDPtoPX(contxt, 15), 0, 0,0);
				tv_reply.setLayoutParams(params);
				tv_reply.setBackgroundResource(Utils.getDrawableResourceId(contxt, "ic"+cur_theme.getId()+"_reply"));

				params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.setMargins(0, 0,Scale.cvDPtoPX(contxt, 15),0);
				tv_ok.setLayoutParams(params);
				tv_ok.setBackgroundResource(Utils.getDrawableResourceId(contxt, "ic"+cur_theme.getId()+"_ok"));

				tv_ok.setText("");
				tv_reply.setText("");

				params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ABOVE, R.id.ln_h2);
				params.setMargins(0,0, 0, Scale.cvDPtoPX(contxt, 5));
				img_s2.setLayoutParams(params);
			}else{
				tv_ok.setTextColor(Color.parseColor(cur_theme.getCtext()));
				tv_reply.setTextColor(Color.parseColor(cur_theme.getCtext()));
			}
			if(cur_theme.getIs_button_divider()==1){
				img_s3.setVisibility(View.INVISIBLE);
			}
			if(cur_theme.getContent_width()>0){
				params=new RelativeLayout.LayoutParams(Scale.cvDPtoPX(contxt, cur_theme.getContent_width()),Scale.cvDPtoPX(contxt, cur_theme.getContent_height()));
				params.setMargins(Scale.cvDPtoPX(contxt, cur_theme.getContent_left()),Scale.cvDPtoPX(contxt, cur_theme.getContent_top()), 0, 0);
				contentScroll.setLayoutParams(params);
				contentScroll.setBackgroundResource(Utils.getDrawableResourceId(contxt, "ic"+cur_theme.getId()+"_content"));
			}
			if(cur_theme.getTheme_height()>0){
				FrameLayout.LayoutParams frameParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,Scale.cvDPtoPX(contxt, cur_theme.getTheme_height()));
				layout_bg.setLayoutParams(frameParam);
			}

			if(sms.getSenderName().equals("")){
				tv_number.setText(sms.getSenderNumber());
				if(tv_name!=null){
					tv_name.setVisibility(View.GONE);
				}
			}else{
				tv_number.setText(sms.getSenderName());
				if(tv_name!=null){
					tv_name.setText(sms.getSenderNumber());
					tv_name.setVisibility(View.VISIBLE);
				}
			}
			tv_time.setText(sms.getTimeString(sms.getTimestamp()));
			tv_content.setText(sms.getBody());

            int imageDimen= Scale.cvDPtoPX(contxt,50);
            if(sms.category== Constants.NATIVE_SMS_CAT) {
                Picasso.with(contxt).load(sms.getThumbnail()).placeholder(R.drawable.ic_contact_picture).resize(imageDimen,imageDimen).into(img_avatar);
            }else{
                int id=Integer.valueOf(sms.getThumbnail());
                img_avatar.setImageBitmap(Utils.getScaledSocialBitmap(contxt,id,imageDimen));
            }
          //  Picasso.with(contxt).load(sms.getThumbnail()).placeholder(R.drawable.ic_contact_picture).into(img_avatar);
		//	img_avatar.setImageBitmap(Utils.getContactThumb(contxt, sms.getThumbnail()));

			tv_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCClose.onClickClose(position);
				}
			});

			tv_reply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCReply.onClickReply(position);
				}
			});
		}
		return view;
	}

	public void setOnClickCloseListener(OnClickClose onCClose){
		this.onCClose = onCClose;
	}

	public interface OnClickClose{
		public void onClickClose(int pos);
	}

	public void setOnClickReplyListener(OnClickReply onCReply){
		this.onCReply = onCReply;
	}

	public interface OnClickReply{
		public void onClickReply(int pos);
	}
}
