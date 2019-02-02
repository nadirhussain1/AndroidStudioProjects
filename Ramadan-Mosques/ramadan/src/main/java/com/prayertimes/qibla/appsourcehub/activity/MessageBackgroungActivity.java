package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.adpater.CustomGrid;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class MessageBackgroungActivity extends Utils {

	Context mContext;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_backgroung_activity);

	
		Actionbar(getString(R.string.title_backgrounds));

		GridView gridView = (GridView) findViewById(R.id.gridView1);

		final int[] imageid = { R.drawable.message1, R.drawable.message2,
				R.drawable.message3, R.drawable.message4, R.drawable.message5 };

		final String message = getIntent().getExtras().getString("message");
		final int Image = getIntent().getExtras().getInt("image");

		CustomGrid adapter = new CustomGrid(getApplicationContext(), imageid);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MessageBackgroungActivity.this,
						MessageSendActivity.class);

				i.putExtra("backgroundImage", imageid[position]);
				i.putExtra("message", message);
				i.putExtra("image", Image);

				startActivity(i);
			}
		});
	}
}
