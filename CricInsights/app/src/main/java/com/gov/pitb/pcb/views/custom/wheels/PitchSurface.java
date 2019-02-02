package com.gov.pitb.pcb.views.custom.wheels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

/**
 * Created by nadirhussain on 13/06/2017.
 */


public class PitchSurface extends View {
    private static final int PITCH_WIDTH = 947;
    private static final int PITCH_HEIGHT = 1150;

    private Bitmap pitchLhBitmap, pitchRhBitmap, ballBitmap, ballDropPoint;
    private float dropX = -1, dropY = -1, heightX = -1, heightY = -1;
    private Delivery delivery;
    private String battingStyle;
    private int pitchScaledWidth;
    private int pitchScaledheight;

    public PitchSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        pitchScaledWidth = (int) ViewScaleHandler.resizeDimension(PITCH_WIDTH);
        pitchScaledheight = (int) ViewScaleHandler.resizeDimension(PITCH_HEIGHT);

        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball_icon);
        ballDropPoint = BitmapFactory.decodeResource(getResources(), R.drawable.drop_pitch_ball);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch_lh);
        pitchLhBitmap = Bitmap.createScaledBitmap(bitmap, pitchScaledWidth, pitchScaledheight, false);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch_rh);
        pitchRhBitmap = Bitmap.createScaledBitmap(bitmap, pitchScaledWidth, pitchScaledheight, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        //Draw Pitch Image
        if (battingStyle.equalsIgnoreCase("left")) {
            canvas.drawBitmap(pitchLhBitmap, 0, 0, paint);
        } else {
            canvas.drawBitmap(pitchRhBitmap, 0, 0, paint);
        }


        if (dropX != -1 && dropY != -1) {
            float x = dropX - (ballDropPoint.getWidth() / 2);
            float y = dropY - (ballDropPoint.getHeight() / 2);
            canvas.drawBitmap(ballDropPoint, x, y, paint);
        }
        if (heightX != -1 && heightY != -1) {
            float x = heightX - (ballBitmap.getWidth() / 2);
            float y = heightY - (ballBitmap.getHeight() / 2);
            canvas.drawBitmap(ballBitmap, x, y, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int heightEndYPoint = (getTop() + (getHeight() / 3));
        if (event.getY() < heightEndYPoint) {
            heightX = event.getX();
            heightY = event.getY();
        } else {
            dropX = event.getX();
            dropY = event.getY();
        }
        updatePoints();
        invalidate();
        return true;
    }

    public void setDelivery(Delivery delivery, String battingStyle) {
        this.delivery = delivery;
        this.battingStyle = battingStyle;

        if (!TextUtils.isEmpty(delivery.getBallDropPint())) {
            String dropPoint = delivery.getBallDropPint();

            float xPercent = Float.parseFloat(dropPoint.split(",")[0]);
            float yPercent = Float.parseFloat(dropPoint.split(",")[1]);
            dropX = (xPercent * pitchScaledWidth) / 100;
            dropY = (yPercent * pitchScaledheight) / 100;

        }
        if (!TextUtils.isEmpty(delivery.getBallHeightPint())) {
            String heightPoint = delivery.getBallHeightPint();
            float xPercent = Float.parseFloat(heightPoint.split(",")[0]);
            float yPercent = Float.parseFloat(heightPoint.split(",")[1]);
            heightX = (xPercent * pitchScaledWidth) / 100;
            heightY = (yPercent * pitchScaledheight) / 100;
        }
        invalidate();
    }

    private void updatePoints() {
        delivery.setBallDropPint(getDropPoint());
        delivery.setBallHeightPint(getHeightPoint());
    }

    public String getDropPoint() {
        float xPercent = (dropX / pitchScaledWidth) * 100;
        float YPercent = (dropY / pitchScaledheight) * 100;

        return "" + xPercent + "," + YPercent;
    }

    public String getHeightPoint() {
        float xPercent = (heightX / pitchScaledWidth) * 100;
        float YPercent = (heightY / pitchScaledheight) * 100;

        return "" + xPercent + "," + YPercent;
    }

}
