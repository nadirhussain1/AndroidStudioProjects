package com.olympus.viewsms.parsesdk;

import com.crashlytics.android.Crashlytics;
import com.olympus.viewsms.MainActivity;
import com.olympus.viewsms.R;
import com.parse.Parse;
import com.parse.PushService;
import io.fabric.sdk.android.Fabric;



public class Application extends android.app.Application {
	
  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
	// Initialize the Parse SDK.
	Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key)); 
	// Specify an Activity to handle all pushes by default.
	PushService.setDefaultPushCallback(this, MainActivity.class);
  }
}