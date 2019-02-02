package com.olympus.viewsms.model;

import com.olympus.viewsms.SMS_Floating_Window;
import com.olympus.viewsms.SMS_MissedContact;
import com.olympus.viewsms.SMS_Single;
import com.olympus.viewsms.SMS_StackView;

public class SharedDialog {
	private static SharedDialog sharedData=null;

    public SMS_Single dlg_single;
    public SMS_MissedContact dlg_miss;
    public SMS_StackView dlg_stack;
    public SMS_Floating_Window dlg_reply;
    
	public static SharedDialog getInstance() {
		if(sharedData==null){
			sharedData=new SharedDialog();
		}
		return sharedData;
	}
	
	private SharedDialog(){
		dlg_single=new SMS_Single();
		dlg_miss=new   SMS_MissedContact();
		dlg_stack=new  SMS_StackView();
		dlg_reply=new  SMS_Floating_Window();
	}
}
