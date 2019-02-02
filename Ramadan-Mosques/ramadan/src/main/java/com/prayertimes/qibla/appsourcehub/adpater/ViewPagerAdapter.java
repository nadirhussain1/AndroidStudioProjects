package com.prayertimes.qibla.appsourcehub.adpater;

import muslim.prayers.time.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewPagerAdapter extends PagerAdapter implements OnClickListener {

	Context ctx;
	int imageArray[];
	LayoutInflater inflater;
	LinearLayout linearPager2, linearPager1, circle_first, circle_second;
	ImageView imgMarkel;
	boolean isClick = true;

	public ViewPagerAdapter(Context act, int[] imgArra) {
		imageArray = imgArra;
		ctx = act;
	}

	public int getCount() {
		return imageArray.length;
	}

	public Object instantiateItem(View collection, int position) {

		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item,
				(ViewGroup) collection, false);
		linearPager1 = (LinearLayout) itemView.findViewById(R.id.linearPager1);
		linearPager2 = (LinearLayout) itemView.findViewById(R.id.linearPager2);
		/*
		 * circle_first = (LinearLayout)
		 * itemView.findViewById(R.id.circle_first); circle_second =
		 * (LinearLayout) itemView .findViewById(R.id.circle_second);
		 */

		((ViewPager) collection).addView(itemView);
		if (position == 0) {

			linearPager1.setVisibility(View.VISIBLE);
			linearPager2.setVisibility(View.GONE);

		} else {
			linearPager1.setVisibility(View.GONE);
			linearPager2.setVisibility(View.VISIBLE);
		}
		LinearLayout ll_quran = (LinearLayout) itemView
				.findViewById(R.id.ll_quran);
		LinearLayout ll_prayer_times = (LinearLayout) itemView
				.findViewById(R.id.ll_prayer_times);
		LinearLayout ll_qibla_compass = (LinearLayout) itemView
				.findViewById(R.id.ll_qibla_compass);
		LinearLayout ll_calendar = (LinearLayout) itemView
				.findViewById(R.id.ll_calendar);
		LinearLayout ll_mosque_finder = (LinearLayout) itemView
				.findViewById(R.id.ll_mosque_finder);
		LinearLayout ll_date_converter = (LinearLayout) itemView
				.findViewById(R.id.ll_date_converter);
		LinearLayout ll_tasbeeh_counter = (LinearLayout) itemView
				.findViewById(R.id.ll_tasbeeh_counter);
		LinearLayout ll_duas = (LinearLayout) itemView
				.findViewById(R.id.ll_duas);
		LinearLayout ll_ramadan_times = (LinearLayout) itemView
				.findViewById(R.id.ll_ramadan_times);
		LinearLayout ll_hadith = (LinearLayout) itemView
				.findViewById(R.id.ll_hadith);
		LinearLayout ll_pillars_of_islam = (LinearLayout) itemView
				.findViewById(R.id.ll_pillars_of_islam);
		LinearLayout ll_name_of_allah = (LinearLayout) itemView
				.findViewById(R.id.ll_name_of_allah);

		LinearLayout ll_shahadah = (LinearLayout) itemView
				.findViewById(R.id.ll_shahadah);
		LinearLayout ll_message = (LinearLayout) itemView
				.findViewById(R.id.ll_message);
		LinearLayout ll_places = (LinearLayout) itemView
				.findViewById(R.id.ll_places);
		LinearLayout ll_popular = (LinearLayout) itemView
				.findViewById(R.id.ll_popular);
		LinearLayout ll_market = (LinearLayout) itemView
				.findViewById(R.id.ll_market);

		imgMarkel = (ImageView) itemView.findViewById(R.id.imgMarkel1);

		
		
		imgMarkel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgMarkel.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.gift));
				notifyDataSetChanged();
				System.out.println("==== Select : " + isClick );
			}
		});
		
		
		/*ll_market.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("==== Select : " );
				img_market.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
				
				Intent intent1 = new Intent(ctx, HandlerActivity.class);
				ctx.startActivity(intent1);
			}
		});*/
		
		ll_quran.setOnClickListener(this);
		ll_prayer_times.setOnClickListener(this);
		ll_qibla_compass.setOnClickListener(this);
		ll_calendar.setOnClickListener(this);
		ll_mosque_finder.setOnClickListener(this);
		ll_date_converter.setOnClickListener(this);
		ll_tasbeeh_counter.setOnClickListener(this);
		ll_duas.setOnClickListener(this);
		ll_ramadan_times.setOnClickListener(this);
		ll_hadith.setOnClickListener(this);
		ll_pillars_of_islam.setOnClickListener(this);
		ll_name_of_allah.setOnClickListener(this);
		ll_shahadah.setOnClickListener(this);
		ll_places.setOnClickListener(this);
		ll_message.setOnClickListener(this);
		ll_popular.setOnClickListener(this);
		
		return itemView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_quran:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.QuranActivity.class)
					.putExtra("from", "0"));
			break;
		case R.id.ll_prayer_times:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.PrayerTimesActivity.class));
			break;
		case R.id.ll_qibla_compass:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityQibla.class));
			break;
		case R.id.ll_calendar:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityCalender.class));
			break;
		case R.id.ll_mosque_finder:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.MapActivity.class));
			break;
		case R.id.ll_date_converter:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.DateConverter.class));
			break;
		case R.id.ll_tasbeeh_counter:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.TasbeeActivity.class));
			break;
		case R.id.ll_duas:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityDuas.class));
			break;
		case R.id.ll_ramadan_times:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.FragmentHome.class));
			break;
		case R.id.ll_hadith:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityHadith.class));
			break;
		case R.id.ll_pillars_of_islam:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.fivepillars.FivePillarsActivity.class));
			break;
		case R.id.ll_name_of_allah:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityNamesList.class));
			break;
		case R.id.ll_shahadah:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ShahadahActivity.class));
			break;
		case R.id.ll_message:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.ActivityMessageList.class));
			break;
		case R.id.ll_places:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.HalalPlaceActivity.class));
			break;
		case R.id.ll_popular:
			ctx.startActivity(new Intent(
					ctx,
					com.prayertimes.qibla.appsourcehub.activity.PopularActivity.class));
			break;
		/*case R.id.ll_market:

			Intent intent1 = new Intent(ctx, HandlerActivity.class);
			ctx.startActivity(intent1);
			break;*/
		}
	}
}
