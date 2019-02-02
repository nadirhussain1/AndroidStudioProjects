package com.olympus.viewsms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.SmsData;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
	Context mContext=null;
	ArrayList<SmsData> smsDataList=null;

	public ChatListAdapter(Context context,ArrayList<SmsData> smsList){
		mContext=context;
		smsDataList=smsList;
	}

	@Override
	public int getCount() {

		return smsDataList.size();
	}

	@Override
	public Object getItem(int position) {

		return smsDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.message_content_row, null, false);
		}
		RelativeLayout receivedMessageLayout=(RelativeLayout)convertView.findViewById(R.id.receivedLayout);
		RelativeLayout sentMessageLayout=(RelativeLayout)convertView.findViewById(R.id.sentMessageLayout);
        
		TextView messageContent;
		TextView timeStampView;
		
		if(smsDataList.get(position).getType()==1){
			receivedMessageLayout.setVisibility(View.VISIBLE);
			sentMessageLayout.setVisibility(View.GONE);
			
			messageContent=(TextView)receivedMessageLayout.findViewById(R.id.recTextContentView);
			timeStampView=(TextView)receivedMessageLayout.findViewById(R.id.recTimeStampView);
		}else{
			receivedMessageLayout.setVisibility(View.GONE);
			sentMessageLayout.setVisibility(View.VISIBLE);
			
			messageContent=(TextView)sentMessageLayout.findViewById(R.id.sentTextContentView);
			timeStampView=(TextView)sentMessageLayout.findViewById(R.id.sentTimeStampView);
		}
		messageContent.setText(smsDataList.get(position).getBody());
		timeStampView.setText(SmsData.getTimeString(smsDataList.get(position).getTimestamp()));
		return convertView;
	}

}
