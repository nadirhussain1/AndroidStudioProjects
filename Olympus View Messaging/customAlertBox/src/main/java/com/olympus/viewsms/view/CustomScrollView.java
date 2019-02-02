package com.olympus.viewsms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

	private float xDistance, yDistance, lastX, lastY;
	
//	YScrollDetector gestureDetector;
//	GestureDetector gestureDetector;
	
	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	public CustomScrollView(Context context) {
		super(context);
//		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if (ev.getAction() == MotionEvent.ACTION_MOVE)
//			return true;
//		return super.dispatchTouchEvent(ev);
//	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            xDistance = yDistance = 0f;
            lastX = ev.getX();
            lastY = ev.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            final float curX = ev.getX();
            final float curY = ev.getY();
            xDistance += Math.abs(curX - lastX);
            yDistance += Math.abs(curY - lastY);
            lastX = curX;
            lastY = curY;
            if (xDistance > yDistance)
                return false;
        }

        return super.onInterceptTouchEvent(ev);

    }
	
//	@Override 
//    public boolean onInterceptTouchEvent(MotionEvent ev) { 
//        //Call super first because it does some hidden motion event handling 
//        boolean result = super.onInterceptTouchEvent(ev); 
//        //Now see if we are scrolling vertically with the custom gesture detector 
//        if (!mGestureDetector.onTouchEvent(ev)) { 
//            return false; 
//        } 
//        //If not scrolling vertically (more y than x), don't hijack the event. 
//        else { 
//            return result; 
//        } 
//    } 
//	
//	
//	private GestureDetector mGestureDetector;
//    View.OnTouchListener mGestureListener;
//
////    @Override
////    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
////    }
//
//    // Return false if we're scrolling in the x direction  
//    class YScrollDetector extends SimpleOnGestureListener {
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if(Math.abs(distanceY) > Math.abs(distanceX)) {
//                return true;
//            }
//            return false;
//        }
//    }

//    // Return false if we're scrolling in the x direction 
//    class YScrollDetector extends SimpleOnGestureListener { 
//        @Override 
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { 
//            try { 
//                if (Math.abs(distanceY) > Math.abs(distanceX)) { 
//                    return true; 
//                } else { 
//                    return false; 
//                } 
//            } catch (Exception e) { 
//                // nothing 
//            } 
//            return false; 
//        } 
//    } 

}
