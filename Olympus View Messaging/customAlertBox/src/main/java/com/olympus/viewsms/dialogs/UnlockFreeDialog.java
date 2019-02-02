package com.olympus.viewsms.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.olympus.viewsms.R;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickUnlockFree;
import com.olympus.viewsms.model.ContactApplied;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class UnlockFreeDialog {
	private Dialog unlockDialog=null;
	private ListView contactsListView=null;
	private Context mContext=null;
	private List<ContactApplied> contact_list;
	private OnClickUnlockFree onClickUnlockFree;
	private int position;
	private Theme theme;
	private int totalPartsOfMessage=0;
	private int currentPart=0;
	private int currentIndexofContacts=-1;
	private String message="";
	BroadcastReceiver receiver=null;
	String SENT = "SMS_SENT";

	public UnlockFreeDialog(Context context,OnClickUnlockFree onClickUnlock,int position,Theme theme){
		this.mContext=context;
		this.position=position;
		this.theme=theme;
		onClickUnlockFree=onClickUnlock;
		message="Whoa, unlocked this awesome theme in Olympus View Messaging. Its Amazing! Check it out "+Constants.APP_MARKET_URL;

		unlockDialog=new Dialog(mContext,android.R.style.Theme_Translucent_NoTitleBar);
		unlockDialog.setContentView(R.layout.unlock_free_contacts_dialog);
		unlockDialog.setOnDismissListener(dismissListener);

		contactsListView=(ListView)unlockDialog.findViewById(R.id.contactsListView);
		getContactsDetails();
	}
	public void showDialog(){
		unlockDialog.show();
	}
	public void sendMessageAndUnlockTheme(){
		onClickUnlockFree.onClickUnlockFree(position);
		EasyTracker.getInstance(mContext).send(MapBuilder.createEvent("ui_action","button_press","Unlock Free "+theme.getName(), null).build());

		unlockDialog.cancel();
		registerBroadCastReceivers();
		sendSms();
	}
	private void sendSms(){
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
		int count=0;
		for(count=(currentIndexofContacts+1);count<contact_list.size();count++){
			if(contact_list.get(count).getCheck()){
				currentIndexofContacts=count;
				currentPart=0;
				Log.d("Check", "CurrentIndex="+currentIndexofContacts);
				SmsManager smsManager = SmsManager.getDefault();
				ArrayList<String> msgparts = smsManager.divideMessage(message);
				totalPartsOfMessage=msgparts.size();

				for (int j = 0; j < totalPartsOfMessage; j++) {
					sentIntents.add(sentPI);
				}
				Log.d("Check", "Message Total Parts="+totalPartsOfMessage);
				smsManager.sendMultipartTextMessage(contact_list.get(count).getNumber(), null, msgparts, sentIntents, null);
				break;
			}
		}
		if(count>=contact_list.size()){
			mContext.unregisterReceiver(receiver);
		}

	}
	private void registerBroadCastReceivers(){
		receiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					currentPart++;
					Log.d("Check", "Current Part="+currentPart);
					if (currentPart == totalPartsOfMessage ) {
						sendSms();
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(mContext, "Generic failure",Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(mContext, "No service",Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(mContext, "Null PDU",Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(mContext, "Radio off",Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		mContext.registerReceiver(receiver, new IntentFilter(SENT));

	}
	private void getContactsDetails() {

//		//A Progress dialog with a spinning wheel, to instruct the user about the app's current state
	final ProgressDialog dialog = ProgressDialog.show(mContext,mContext.getResources().getString(R.string.plz_wait),mContext.getResources().getString(R.string.retrieve_contact), true);
//
//		//A new worker thread is created to retrieve and display the contacts.
//		new Thread(new Runnable() {
//			public void run() {
//				Looper.prepare();

				contact_list = new ArrayList<ContactApplied>();
				ArrayList<String> contactNames = new ArrayList<String>();
				ArrayList<String> contactNumbers = new ArrayList<String>();

				Cursor phones = mContext.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
						null, Phone.DISPLAY_NAME + " COLLATE NOCASE ASC");

				if(phones.getCount()==0)
				{
					dialog.dismiss();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.not_found_contact), Toast.LENGTH_SHORT).show();
					return;
				}
				phones.moveToFirst();
				do {
					String name = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

					String number = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					contactNames.add(name);
					contactNumbers.add(number);

				} while (phones.moveToNext());

				String[] names = new String[contactNames.size()];
				names = contactNames.toArray(names);
				String[] numbers = new String[contactNumbers.size()];
				numbers = contactNumbers.toArray(numbers);
				for (int i = 0; i < names.length; i++) {
					//ContactApplied item = new ContactApplied(-1, names[i], numbers[i].replace(" ",	""), 0);  //-1 is not nesceesssary
					//contact_list.add(item);
				}

//				contactsListView.post(new Runnable() {
//					@Override
//					public void run() {
//						ContactListAdapter adapter = new ContactListAdapter(mContext,R.layout.item_contact_menu, contact_list,true);
//						contactsListView.setAdapter(adapter);
//					}
//				});
				dialog.dismiss();
			//}
		//}).start();
	}
	OnDismissListener dismissListener=new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {

		}
	};


}
