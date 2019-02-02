package com.olympus.viewsms.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;


public class ScreenDecisionMaker {
	
	public void displaySmsScreen(Context context,SmsData sms){
		boolean added=false;
		for(int i=0;i<SharedData.getInstance().receivedMessages.size();i++){
            if(SharedData.getInstance().receivedMessages.get(i).size()>0) {
                if (SharedData.getInstance().receivedMessages.get(i).get(0).getSenderNumber().equalsIgnoreCase(sms.getSenderNumber())) {  //compare by phone number
                    int size = SharedData.getInstance().receivedMessages.get(i).size();
                    SharedData.getInstance().receivedMessages.get(i).add(size, sms);
                    added = true;
                    break;
                }
            }
		}
		if(!added){
			ArrayList<SmsData> firstList=new ArrayList<SmsData>();
			firstList.add(sms);
			int size=SharedData.getInstance().receivedMessages.size();
			SharedData.getInstance().receivedMessages.add(size,firstList);	
		}
		
		SharedData.getInstance().isNewMessageArrived=true;
		SharedData.getInstance().latestSmsData=sms;
		
		//check notification enable
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
		if(!prefs.getBoolean(Constants.NOTIFY_ENABLE, true)) return;

		//reset widget
	    Utils.updateMyWidgets(context);
		Utils.updateStackWidgets(context);
//		//check open dialog
		Utils.showSMSDialog(context);
	}

}
