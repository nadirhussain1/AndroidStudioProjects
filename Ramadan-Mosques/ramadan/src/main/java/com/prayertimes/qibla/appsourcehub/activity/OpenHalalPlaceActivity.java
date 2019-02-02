package com.prayertimes.qibla.appsourcehub.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class OpenHalalPlaceActivity extends Utils {
	WebView webView;
	String url;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_halal_palace_activity);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		Actionbar(getIntent().getExtras().getString("name"));
		url = getIntent().getExtras().getString("url");
		
		
		pDialog = new ProgressDialog(OpenHalalPlaceActivity.this);
		pDialog.setMessage("Loading Halal Place...");
		pDialog.setCancelable(false);
		pDialog.show();
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
	}
	
	private class MyWebViewClient extends WebViewClient {	
		 @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return true;
		    }

		 @Override
		public void onPageFinished(WebView view, String url) {
			 pDialog.dismiss();
			super.onPageFinished(view, url);
		}

		 @Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	}



}
