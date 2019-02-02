package com.prayertimes.qibla.appsourcehub.activity;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class MessageActivity extends Utils {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		Actionbar(getString(R.string.title_lbl_message));

	}

}
