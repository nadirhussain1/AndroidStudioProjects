package com.olympusthemes.southpark;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontsUtil {
    public static String HELVETICA_NEUE_STD="HelveticaNeueLTSt.otf";


	public static void applyFontToText(Context mContext,TextView view,String fontName){
		Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(), fontName);
		view.setTypeface(typeFace);
	}
}
