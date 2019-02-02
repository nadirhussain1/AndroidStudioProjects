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

public class ScorerWagonWheel extends View {
    private final static int WHEEL_SIZE = 1080;
    private final static int OFFSET = 60;
    float touchX = 0;
    float touchY = 0;
    private Bitmap groundBitmap, ballBitmap;
    private int resizeWheelDimension;
    private int resizeOffset;
    private Delivery currentDelivery;

    public ScorerWagonWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        resizeWheelDimension = (int) ViewScaleHandler.resizeDimension(WHEEL_SIZE);
        resizeOffset = (int) ViewScaleHandler.resizeDimension(OFFSET);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball_icon);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.custom_ground);
        groundBitmap = Bitmap.createScaledBitmap(bitmap, resizeWheelDimension, resizeWheelDimension, false);
    }

    public void setDelivery(Delivery delivery) {
        this.currentDelivery = delivery;
        if (!TextUtils.isEmpty(currentDelivery.getWagonWheelPoint())) {
            String wagonWheelPoint = currentDelivery.getWagonWheelPoint();

            float xPercent = Float.parseFloat(wagonWheelPoint.split(",")[0]);
            float yPercent = Float.parseFloat(wagonWheelPoint.split(",")[1]);
            touchX = (xPercent * resizeWheelDimension) / 100;
            touchY = (yPercent * resizeWheelDimension) / 100;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewSize = getWidth();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(groundBitmap, 0, 0, paint);

        int batmanPositionY = (viewSize / 2) - (int) ViewScaleHandler.resizeDimension(80);
        int centerX = viewSize / 2;


        if (touchX != 0 && touchY != 0) {

            paint.setColor(Color.parseColor("#FF0000"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            canvas.drawLine(centerX, batmanPositionY, touchX, touchY, paint);

            float ballXPoint = touchX - (ballBitmap.getWidth() / 2);
            float ballYPoint = touchY - (ballBitmap.getHeight() / 2);
            canvas.drawBitmap(ballBitmap, ballXPoint, ballYPoint, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int cx = resizeWheelDimension / 2;
        int cy = resizeWheelDimension / 2;
        float xCompSquare = (event.getX() - cx) * (event.getX() - cx);
        float yCompSquare = (event.getY() - cy) * (event.getY() - cy);
        float totalSquare = xCompSquare + yCompSquare;

        int radius = cx - resizeOffset;
        if (totalSquare <= (radius * radius)) {
            touchX = event.getX();
            touchY = event.getY();
            updateDeliveryPoint();
            invalidate();
        }

        return true;
    }

    private void updateDeliveryPoint() {
        float xPercent = (touchX / resizeWheelDimension) * 100;
        float YPercent = (touchY / resizeWheelDimension) * 100;

        String point = "" + xPercent + "," + YPercent;
        currentDelivery.setWagonWheelPoint(point);
    }


}
