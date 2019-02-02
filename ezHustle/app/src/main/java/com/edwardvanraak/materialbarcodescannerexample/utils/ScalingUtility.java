package com.edwardvanraak.materialbarcodescannerexample.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;


public class ScalingUtility {
    private static final String TAG_CONSTANT = "constant";
    private static final String TAG_WIDTH_CONSTANT = "constantWidth";
    private static final String TAG_HEIGHT_CONSTANT = "constantHeight";
    // These are constants that represent device for which xml has been designed.
    private static final double standardWidth = 720;
    private static final double standardHeight = 1280;
    private static final double standardDensity = 2.0;


    private double widthRatio = 0;
    private double heightRatio = 0;
    private double aspectRatioFactor = 0;
    private double textScalingFactor = 0;
    private float currentDensity = 0.0f;
    private int width = 0;
    private int height = 0;


    public ScalingUtility(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight() - getStatusBarHeight(activity);

        widthRatio = (width / standardWidth);
        heightRatio = (height / standardHeight);
        aspectRatioFactor = Math.min(widthRatio, heightRatio);
        currentDensity = activity.getResources().getDisplayMetrics().density;
        textScalingFactor = Math.min(widthRatio, heightRatio) * (standardDensity / currentDensity);
    }

    private int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void scaleRootView(View childView) {

        if (childView.getLayoutParams() instanceof RecyclerView.LayoutParams) {

            RecyclerView.LayoutParams linearParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            if (linearParams.width != MarginLayoutParams.WRAP_CONTENT && linearParams.width != MarginLayoutParams.MATCH_PARENT) {
                linearParams.width *= aspectRatioFactor;
            }

            if (linearParams.height != MarginLayoutParams.WRAP_CONTENT && linearParams.height != MarginLayoutParams.MATCH_PARENT) {
                linearParams.height *= aspectRatioFactor;
            }

            childView.setLayoutParams(linearParams);

        } else if (childView.getLayoutParams() instanceof MarginLayoutParams) {

            MarginLayoutParams linearParams = (MarginLayoutParams) childView.getLayoutParams();
            String tag = "";
            if (childView.getTag() != null) {
                tag = childView.getTag().toString();
            }
            if (linearParams != null) {

                if (!tag.contentEquals(TAG_CONSTANT)) {

                    if (!tag.contentEquals(TAG_WIDTH_CONSTANT) && linearParams.width != MarginLayoutParams.WRAP_CONTENT && linearParams.width != MarginLayoutParams.MATCH_PARENT) {
                        linearParams.width *= aspectRatioFactor;
                    }

                    if (!tag.equalsIgnoreCase(TAG_HEIGHT_CONSTANT) && linearParams.height != MarginLayoutParams.WRAP_CONTENT && linearParams.height != MarginLayoutParams.MATCH_PARENT) {
                        linearParams.height *= aspectRatioFactor;
                    }
                }

                linearParams.leftMargin *= widthRatio;
                linearParams.rightMargin *= widthRatio;
                linearParams.topMargin *= heightRatio;
                linearParams.bottomMargin *= heightRatio;

                childView.setLayoutParams(linearParams);

                int paddingTop = (int) (childView.getPaddingTop() * heightRatio);
                int paddingLeft = (int) (childView.getPaddingLeft() * widthRatio);
                int paddingRight = (int) (childView.getPaddingRight() * widthRatio);
                int paddingBottom = (int) (childView.getPaddingBottom() * heightRatio);

                childView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            }
        }

        if (childView instanceof TextView) {
            float textSize = ((TextView) childView).getTextSize();
            textSize *= textScalingFactor;
            if (width <= 240 || height <= 320) {
                textSize = textSize + 1.2f;
            }
            ((TextView) childView).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if (childView instanceof ViewGroup) {
            int childCount = ((ViewGroup) childView).getChildCount();
            for (int i = 0; i < childCount; i++) {
                scaleRootView(((ViewGroup) childView).getChildAt(i));
            }
        }

    }


}