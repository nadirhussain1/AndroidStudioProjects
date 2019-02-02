package com.olympus.viewsms.model;

import com.olympus.viewsms.util.Constants;

public class MissContact {
	private String name;
	private String thumbnailUri;
	private int num_sms;
	private long time_ago;
    private int catgory= Constants.NATIVE_SMS_CAT;

	public MissContact(){
		
	}
	
	public MissContact(String name, String thumbnailUri,int num_sms, long time_ago) {
		super();
		this.name = name;
		this.thumbnailUri = thumbnailUri;
		this.num_sms = num_sms;
		this.time_ago = time_ago;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThumbnail() {
		return thumbnailUri;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnailUri = thumbnail;
	}
	public int getNum_sms() {
		return num_sms;
	}
	public void setNum_sms(int num_sms) {
		this.num_sms = num_sms;
	}
	public long getTime_ago() {
		return time_ago;
	}
	public void setTime_ago(long time_ago) {
		this.time_ago = time_ago;
	}
    public void setCatgory(int catgory) {
        this.catgory = catgory;
    }

    public int getCatgory() {
        return catgory;
    }
	
	//==========================================================//

	public String getTimeagoString(){
		int ss=(int)time_ago/1000;
		int hh=ss/3600;ss-=hh*3600;
		int mm=ss/60;ss-=mm*60;
		if(hh>0)
		{
			if(mm>0) return hh+"h "+mm+"m ago";
			else return hh+"h ago";
		}
		else if(mm>0)
		{
			if(ss>0) return mm+"m "+ss+"s ago";
			else return mm+"m ago";
		}
		else return ss+"s ago";
	}
}
