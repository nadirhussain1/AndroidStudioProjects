package com.olympus.viewsms.model;

import android.content.Context;

import java.util.ArrayList;

public class SharedData {
	private static SharedData sharedData=null;
	public Context mContext=null;
	
    public  ArrayList<ArrayList<SmsData>>  receivedMessages=null;
    public ArrayList<ArrayList<SmsData>> fiveRecentConversations=null;
    public  ArrayList<SmsData>  stackWidgetMessages=null;
    public boolean isNewMessageArrived=false;
    public SmsData latestSmsData=null;
    
	public static SharedData getInstance() {
		if(sharedData==null){
			sharedData=new SharedData();
		}
		return sharedData;
	}
	
	private SharedData(){
		receivedMessages=new ArrayList<ArrayList<SmsData>>();
		fiveRecentConversations=new ArrayList<ArrayList<SmsData>>();
		stackWidgetMessages=new ArrayList<SmsData>();
	}

	public int getSizeAll(){
		int num=0;
		for(int i=0;i<receivedMessages.size();i++)
			num+=receivedMessages.get(i).size();
		return num;
	}
}
