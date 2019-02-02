package com.prayertimes.qibla.appsourcehub.activity;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class ShahadahActivity extends Utils {

	Uri uri;
	LinearLayout ll_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shahadah_activity);

		ll_main = (LinearLayout) findViewById(R.id.ll_main);

		Actionbar(getString(R.string.title_lbl_shahadah));

		uri = getImageUri(getApplicationContext(), getBitmap(ll_main));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_share, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0x102002c:
			finish();
			break;

		case R.id.action_share:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.putExtra(Intent.EXTRA_SUBJECT,
					getResources().getString(R.string.lbl_shahdah_txt1));
			intent.setType("image/*");
			startActivity(Intent.createChooser(intent, "Send image"));
			return true;
		}
		return false;
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(
				inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public Bitmap getBitmap(View v) {
		Bitmap b = null;
		if (v.getMeasuredHeight() <= 0) {
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			b = Bitmap.createBitmap(v.getMeasuredWidth(),
					v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);

		}
		return b;
	}

}
