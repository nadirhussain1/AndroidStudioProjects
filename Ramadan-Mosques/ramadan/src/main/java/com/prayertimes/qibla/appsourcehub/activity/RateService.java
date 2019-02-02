package com.prayertimes.qibla.appsourcehub.activity;



import muslim.prayers.time.R;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
public class RateService extends Service {

    private WindowManager windowManager;
    private LayoutInflater inflater;
    ImageView imageView;
    RelativeLayout relativeLayout;
    private int i = 0;

    public RateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("pradeep", "onDestroy of service ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("pradeep", "onCreate of service ");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = 0;
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.rate_me, null);
        windowManager.addView(relativeLayout, params);
        imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
        SlideToAbove();
    }


    public void SlideToAbove() {

        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(900);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        imageView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                i = i + 1;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();

                if (i < 2)

                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SlideToAbove();
                        }
                    }, 500);

                } else {
                    imageView.setVisibility(View.GONE);
                    windowManager.removeView(relativeLayout);
                    stopSelf();
                }

            }

        });

    }
}
