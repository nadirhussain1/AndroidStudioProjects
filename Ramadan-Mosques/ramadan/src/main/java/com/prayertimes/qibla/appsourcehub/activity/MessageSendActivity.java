package com.prayertimes.qibla.appsourcehub.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import muslim.prayers.time.R;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

public class MessageSendActivity extends Utils {
	LinearLayout ll_main;
	ImageView imageView;
	TextView txt_messsage;
	Uri uri;
	Bitmap b;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_send_activity);
		Actionbar(getString(R.string.title_backgrounds));

		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		imageView = (ImageView) findViewById(R.id.image);
		txt_messsage = (TextView) findViewById(R.id.txt_messsage);

		String msg = getIntent().getExtras().getString("message");
		int image = getIntent().getExtras().getInt("image");
		int id = getIntent().getExtras().getInt("backgroundImage");

		String filename = msg;
		String[] parts = filename.split("\\.");
		message = parts[1];

		txt_messsage.setText(message);
		ll_main.setBackgroundResource(id);
		imageView.setImageDrawable(loadImageFromAsset(image));

		uri = getImageUri(getApplicationContext(), getBitmap(ll_main));
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

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuItem settingsMenuItem = menu.findItem(R.id.action_message_send);
		SpannableString s = new SpannableString(settingsMenuItem.getTitle());
		s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
		settingsMenuItem.setTitle(s);

		return super.onPrepareOptionsMenu(menu);
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
