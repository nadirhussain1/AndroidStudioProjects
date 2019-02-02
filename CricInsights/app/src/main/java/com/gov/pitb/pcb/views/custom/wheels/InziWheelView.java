package com.gov.pitb.pcb.views.custom.wheels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 20/07/2017.
 */

public class InziWheelView extends View {
    private final static int WHEEL_SIZE = 850;
    private Bitmap groundBitmap;
    private int resizeDimension;
    private int lineOriginX;
    private int lineOriginY;
    private List<Delivery> ballsList = new ArrayList<>();
    private Context context;

    public InziWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        resizeDimension = (int) ViewScaleHandler.resizeDimension(context, WHEEL_SIZE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.custom_ground);
        groundBitmap = Bitmap.createScaledBitmap(bitmap, resizeDimension, resizeDimension, false);

        lineOriginX = resizeDimension / 2;
        lineOriginY = (resizeDimension / 2) - (int) ViewScaleHandler.resizeDimension(45);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(groundBitmap, 0, 0, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        for (Delivery delivery : ballsList) {
            if (delivery.getRuns() != 5) {
                paint.setColor(decideColor(delivery.getRuns()));

                String wagonWheelPoint = delivery.getWagonWheelPoint();
                if (!TextUtils.isEmpty(wagonWheelPoint)) {
                    float lineEndX = Float.parseFloat(wagonWheelPoint.split(",")[0]) * 0.787f;
                    float lineEndY = Float.parseFloat(wagonWheelPoint.split(",")[1]) * 0.787f;

                    canvas.drawLine(lineOriginX, lineOriginY, lineEndX, lineEndY, paint);
                }
            }
        }
    }

    public void refreshWheel(List<Delivery> ballsList) {
        this.ballsList = ballsList;
        invalidate();
    }

    private int decideColor(int runs) {
        switch (runs) {
            case 0:
                return context.getResources().getColor(R.color.zeros_color);
            case 1:
                return context.getResources().getColor(R.color.singles_color);
            case 2:
                return context.getResources().getColor(R.color.doubles_color);
            case 3:
                return context.getResources().getColor(R.color.triples_color);
            case 4:
                return context.getResources().getColor(R.color.fours_color);
            default:
                return context.getResources().getColor(R.color.six_color);
        }
    }


}
