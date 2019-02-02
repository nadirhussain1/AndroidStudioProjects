package com.prayertimes.qibla.appsourcehub.activity;

import muslim.prayers.time.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondFragment extends Fragment implements OnClickListener {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.second_frag, container, false);

		LinearLayout ll_ramadan_times = (LinearLayout) v
				.findViewById(R.id.ll_ramadan_times);
		LinearLayout ll_hadith = (LinearLayout) v.findViewById(R.id.ll_hadith);
		LinearLayout ll_pillars_of_islam = (LinearLayout) v
				.findViewById(R.id.ll_pillars_of_islam);
		LinearLayout ll_name_of_allah = (LinearLayout) v
				.findViewById(R.id.ll_name_of_allah);

		LinearLayout ll_shahadah = (LinearLayout) v
				.findViewById(R.id.ll_shahadah);
		LinearLayout ll_message = (LinearLayout) v
				.findViewById(R.id.ll_message);
		LinearLayout ll_places = (LinearLayout) v.findViewById(R.id.ll_places);
		LinearLayout ll_popular = (LinearLayout) v
				.findViewById(R.id.ll_popular);

		ll_ramadan_times.setOnClickListener(this);
		ll_hadith.setOnClickListener(this);
		ll_pillars_of_islam.setOnClickListener(this);
		ll_name_of_allah.setOnClickListener(this);
		ll_shahadah.setOnClickListener(this);
		ll_places.setOnClickListener(this);
		ll_message.setOnClickListener(this);
		ll_popular.setOnClickListener(this);

		return v;
	}

	public static SecondFragment newInstance() {

		SecondFragment f = new SecondFragment();

		return f;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_ramadan_times:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.FragmentHome.class));
			break;
		case R.id.ll_hadith:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.ActivityHadith.class));
			break;
		case R.id.ll_pillars_of_islam:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.fivepillars.FivePillarsActivity.class));
			break;
		case R.id.ll_name_of_allah:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.ActivityNamesList.class));
			break;
		case R.id.ll_shahadah:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.ShahadahActivity.class));
			break;
		case R.id.ll_message:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.ActivityMessageList.class));
			break;
		case R.id.ll_places:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.HalalPlaceActivity.class));
			break;
		case R.id.ll_popular:
			getActivity().startActivity(new Intent(
					getActivity(),
					com.prayertimes.qibla.appsourcehub.activity.PopularActivity.class));
			break;
		}
	}
}