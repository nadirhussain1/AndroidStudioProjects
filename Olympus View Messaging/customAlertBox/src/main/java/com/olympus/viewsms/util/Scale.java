package com.olympus.viewsms.util;

import android.content.Context;

public class Scale {
	public static int cvPXtoDP(Context cxt, int px){
		final float scale = cxt.getResources().getDisplayMetrics().density;
		return (int) ((px -0.5f)/scale);
	}
	
	public static int cvDPtoPX(Context cxt, int dp){
		final float scale = cxt.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
