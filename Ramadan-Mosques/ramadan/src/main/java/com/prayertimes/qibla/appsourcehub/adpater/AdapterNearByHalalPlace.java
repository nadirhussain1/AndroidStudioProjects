package com.prayertimes.qibla.appsourcehub.adpater;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.internal.adapters.m;
import muslim.prayers.time.R;
import muslim.prayers.time.R.id;
import com.prayertimes.qibla.appsourcehub.activity.HalalPlaceActivity;
import com.prayertimes.qibla.appsourcehub.activity.OpenHalalPlaceActivity;

public class AdapterNearByHalalPlace extends BaseAdapter {

	ArrayList<String> placeNameList, placeDistList, placeAddressList, idList;
	LayoutInflater inflater;
	LinearLayout lyt_listview;
	Context mContext;
	boolean play;
	Typeface tf;
	Typeface tf1;
	static String canonicalUrl;
	String url, response;
	int pos;

	public AdapterNearByHalalPlace(Context context, ArrayList<String> list,
			ArrayList<String> nearByPlaceDistList,
			ArrayList<String> addressList, ArrayList<String> idList,
			Typeface typeface, Typeface typeface1) {
		play = false;
		tf = typeface;
		tf1 = typeface1;
		placeNameList = list;
		placeDistList = nearByPlaceDistList;
		placeAddressList = addressList;
		this.idList = idList;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
	}

	public int getCount() {
		return placeNameList.size();
	}

	public Object getItem(int i) {
		return Integer.valueOf(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup viewgroup) {
		View view1 = inflater
				.inflate(R.layout.near_by_loaction_list_item, null);
		lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
		TextView place = (TextView) view1.findViewById(R.id.messages);
		TextView txt_dist = (TextView) view1.findViewById(R.id.txt_dist);
		TextView txt_address = (TextView) view1.findViewById(R.id.txt_address);

		place.setText(placeNameList.get(position).toString());
	    Double dis = Double.valueOf(placeDistList.get(position).toString())/1000;
		txt_dist.setText(String.valueOf(dis)+" Kms");
		if (!placeAddressList.get(position).equals(null)) {
			txt_address.setText(placeAddressList.get(position));
		}

	/*	if (idList.size() == 0) {
			place.setText("No halal restaurants were found around your current location");
		}*/

		lyt_listview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				url = "https://api.foursquare.com/v2/venues/"
						+ idList.get(position)
						+ "?oauth_token=J4XFGABO3KXT1LI4PHIGY55D0LED4501CUGYFWIDFC5K1DY1&v=20160614";
					pos= position;
				new Task().execute(url);
				
				
			}
		});

		return view1;
	}

	private static void getData(String response) {

		try {
			// make an jsonObject in order to parse the response
			JSONObject jsonObject = new JSONObject(response);

			// make an jsonObject in order to parse the response
			if (jsonObject.has("response")) {
				if (jsonObject.getJSONObject("response").has("venue")) {
					JSONObject jsonObj = jsonObject.getJSONObject("response")
							.getJSONObject("venue");

					canonicalUrl = jsonObj.getString("canonicalUrl");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();

		}

		// trim the whitespaces
		return replyString.trim();
	}

	private class Task extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			response = makeCall(url);
			getData(response);
			return null;
		}

		protected void onPostExecute(String params) {
			super.onPostExecute(params);
			
			Intent i = new Intent(mContext, OpenHalalPlaceActivity.class);
			i.putExtra("url", canonicalUrl);
			i.putExtra("name", placeNameList.get(pos).toString());
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);

		}
	}

}
