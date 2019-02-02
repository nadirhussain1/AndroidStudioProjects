package com.prayertimes.qibla.appsourcehub.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.w3c.dom.Text;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class PopularSendActivity extends Utils {
	LinearLayout ll_main;
	TextView txt_messsage;
	Uri uri;
	Bitmap b;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popular_send_activity);
		Actionbar(getString(R.string.title_backgrounds));

		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		txt_messsage = (TextView) findViewById(R.id.txt_messsage);

		String msg = getIntent().getExtras().getString("message");
		int id = getIntent().getExtras().getInt("backgroundImage");

		txt_messsage.setText(msg);
		ll_main.setBackgroundResource(id);
		ll_main.setDrawingCacheEnabled(true);

		uri = getImageUri(getApplicationContext(),getBitmap(ll_main));
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(
				inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_message_send, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0x102002c:
			finish();
			break;
		case R.id.action_message_send:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.putExtra(Intent.EXTRA_SUBJECT, message);
			intent.setType("image/*");
			startActivity(Intent.createChooser(intent, "Send image"));
			return true;
		}
		return false;
	}

	public Drawable loadImageFromAsset(int i) {
		Drawable drawable;
		try {
			String s = i + "";
			drawable = Drawable
					.createFromStream(
							getAssets().open(
									(new StringBuilder("list_message_images/"))
											.append(s).append(".png")
											.toString()), null);

			return drawable;
		} catch (IOException ioexception) {
			return null;
		}
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
