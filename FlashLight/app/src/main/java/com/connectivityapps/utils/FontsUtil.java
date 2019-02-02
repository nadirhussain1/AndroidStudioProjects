package com.connectivityapps.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontsUtil {
    public static String ROBOTO_REGULAR="Roboto-Regular.ttf";
    public static String ROBOTO_LIGHT="Roboto-Light.ttf";
    public static String ROBOTO_BOLD="Roboto-Bold.ttf";
    public static String ROBOTO_THIN="Roboto-Thin.ttf";

	public static void applyFontToText(Context mContext,TextView view,String fontName){
        try {
            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), fontName);
            view.setTypeface(typeFace);
        }catch(Exception exception){

        }
	}
}
