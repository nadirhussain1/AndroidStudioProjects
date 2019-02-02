package com.olympus.viewsms.model;

import com.olympus.viewsms.util.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SmsData implements Serializable{
	private String senderNumber;
	private String senderName;
	private String body;
	private long timestamp;
	private String thumbnailUri;
	private int type;
	public int category=Constants.NATIVE_SMS_CAT;
	public String packageName="";

	public SmsData (String number,String senderName,String body,long timeStamp, String thumbnailUri){
		this.senderNumber=number;
		this.senderName=senderName;
		this.body=body;
		this.timestamp=timeStamp;
		this.thumbnailUri=thumbnailUri;
	}
	public SmsData(String number,String senderName,String body,long timeStamp,int type){
		this.senderNumber=number;
		this.senderName=senderName;
		this.body=body;
		this.timestamp=timeStamp;
		this.type=type;
	}
	public SmsData (SmsData sms){
		this.senderNumber=sms.getSenderNumber();
		this.senderName=sms.getSenderName();
		this.body=sms.getBody();
		this.timestamp=sms.getTimestamp();
		this.thumbnailUri=sms.getThumbnail();
		this.category=sms.category;
		this.packageName=sms.packageName;
	}


	public static String getTimeString(long time){
		Date date = new Date(time);	
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		sdf.setCalendar(cal);
		cal.setTime(date);
		String s=sdf.format(date);
		return (s.charAt(0)=='0'?s.substring(1, s.length()):s);
	}


	public String getSenderNumber() {
		return senderNumber;
	}
	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getThumbnail() {
		return thumbnailUri;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnailUri = thumbnail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


}
