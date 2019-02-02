package com.prayertimes.qibla.appsourcehub.activity;

import muslim.prayers.time.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prayertimes.qibla.appsourcehub.mobvista.HandlerActivity;

public class FirstFragment extends Fragment implements OnClickListener {
	ImageView img_market;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.first_frag, container, false);

		LinearLayout ll_quran = (LinearLayout) v.findViewById(R.id.ll_quran);
		LinearLayout ll_prayer_times = (LinearLayout) v
				.findViewById(R.id.ll_prayer_times);
		LinearLayout ll_qibla_compass = (LinearLayout) v
				.findViewById(R.id.ll_qibla_compass);
		LinearLayout ll_calendar = (LinearLayout) v
				.findViewById(R.id.ll_calendar);
		LinearLayout ll_mosque_finder = (LinearLayout) v
				.findViewById(R.id.ll_mosque_finder);
		LinearLayout ll_date_converter = (LinearLayout) v
				.findViewById(R.id.ll_date_converter);
		LinearLayout ll_tasbeeh_counter = (LinearLayout) v
				.findViewById(R.id.ll_tasbeeh_counter);
		LinearLayout ll_duas = (LinearLayout) v.findViewById(R.id.ll_duas);

		LinearLayout ll_market = (LinearLayout) v.findViewById(R.id.ll_market);
		img_market = (ImageView) v.findViewById(R.id.img_market);

		ll_quran.setOnClickListener(this);
		ll_prayer_times.setOnClickListener(this);
		ll_qibla_compass.setOnClickListener(this);
		ll_calendar.setOnClickListener(this);
		ll_mosque_finder.setOnClickListener(this);
		ll_date_converter.setOnClickListener(this);
		ll_tasbeeh_counter.setOnClickListener(this);
		ll_duas.setOnClickListener(this);
		ll_market.setOnClickListener(this);

		return v;
	}

	public static FirstFragment newInstance() {

		FirstFragment f = new FirstFragment();

		return f;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_quran:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.QuranActivity.class)
									.putExtra("from", "0"));
			break;
		case R.id.ll_prayer_times:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.PrayerTimesActivity.class));
			break;
		case R.id.ll_market:

			img_market.setImageResource(R.drawable.gift);

			Intent intent1 = new Intent(getActivity(), HandlerActivity.class);
			getActivity().startActivity(intent1);
			break;
		case R.id.ll_qibla_compass:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.ActivityQibla.class));
			break;
		case R.id.ll_calendar:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.ActivityCalender.class));
			break;
		case R.id.ll_mosque_finder:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.MapActivity.class));
			break;
		case R.id.ll_date_converter:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.DateConverter.class));
			break;
		case R.id.ll_tasbeeh_counter:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.TasbeeActivity.class));
			break;

		case R.id.ll_duas:
			getActivity()
					.startActivity(
							new Intent(
									getActivity(),
									com.prayertimes.qibla.appsourcehub.activity.ActivityDuas.class));
			break;

		}
	}
}