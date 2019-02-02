package com.prayertimes.qibla.appsourcehub.mobvista;

import java.util.HashMap;
import java.util.Map;

import muslim.prayers.time.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobvista.msdk.MobVistaConstans;
import com.mobvista.msdk.MobVistaSDK;
import com.mobvista.msdk.out.MobVistaSDKFactory;
import com.mobvista.msdk.out.MvWallHandler;

public class HandlerActivity extends Activity {
	ViewGroup nat;
	MvWallHandler mvHandler;
	Button btn_pre, btn_intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handler);
		
		nat = (ViewGroup) findViewById(R.id.nat);

		//preloadWall();

		loadHandler();
	}
/*
	public void preloadWall() {
		MobVistaSDK sdk = MobVistaSDKFactory.getMobVistaSDK();
		Map<String, Object> preloadMap = new HashMap<String, Object>();
		preloadMap.put(MobVistaConstans.PROPERTIES_LAYOUT_TYPE,
				MobVistaConstans.LAYOUT_APPWALL);
		preloadMap.put(MobVistaConstans.PROPERTIES_UNIT_ID,
				R.string.mobvista_unit_id);
		sdk.preload(preloadMap);
		openWall();
	}*/

	@SuppressLint("InflateParams")
	public void loadHandler() {
		Map<String, Object> properties = MvWallHandler
				.getWallProperties(getResources().getString(
						R.string.mobvista_property_id));
		mvHandler = new MvWallHandler(properties, this, nat);
		View view = getLayoutInflater().inflate(R.layout.customer_entry, null);
		view.findViewById(R.id.imageview).setTag(
				MobVistaConstans.WALL_ENTRY_ID_IMAGEVIEW_IMAGE);
		view.findViewById(R.id.newtip_area).setTag(
				MobVistaConstans.WALL_ENTRY_ID_VIEWGROUP_NEWTIP);
		mvHandler.setHandlerCustomerLayout(view);
		mvHandler.load();
		openWall();

	}

	public void openWall() {
		try {
			Map<String, Object> properties = MvWallHandler
					.getWallProperties(getResources().getString(
							R.string.mobvista_property_id));
			MvWallHandler mvHandler = new MvWallHandler(properties,
					HandlerActivity.this);
			mvHandler.startWall();
			finish();
		} catch (Exception e) {
			Log.e("MVActivity", "", e);
		}
	}
}
