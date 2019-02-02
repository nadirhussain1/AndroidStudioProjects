package com.prayertimes.qibla.appsourcehub.activity;

import muslim.prayers.time.R;
import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {
	// Static fields are shared between all instances.
	static InterstitialAd ad;
	InterstitialAd interstitialAd;
	Context context;

	public AdManager(Context ctx ) {
		context = ctx;
		createAd();
	
	}

	public void createAd() {
		// Create an ad.
		interstitialAd = new InterstitialAd(context);
		interstitialAd.setAdUnitId(context.getResources().getString(R.string.interstitial_id));

		final AdRequest adRequest = new AdRequest.Builder().build();

		// Load the interstitial ad.
		interstitialAd.loadAd(adRequest);
		interstitialAd.setAdListener(new AdListener() {
			
			@Override
			public void onAdFailedToLoad(int errorCode) {
				// TODO Auto-generated method stub
				super.onAdFailedToLoad(errorCode);
				System.out.println("===fail"+errorCode);
			}
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				super.onAdLoaded();

				System.out.println("===load");
			}
			
			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub
				super.onAdClosed();
			}
		});
		
	
	}

	public InterstitialAd getAd() {
		return interstitialAd;
	}
}