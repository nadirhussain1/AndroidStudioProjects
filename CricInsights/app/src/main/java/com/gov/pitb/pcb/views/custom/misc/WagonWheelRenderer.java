package com.gov.pitb.pcb.views.custom.misc;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import com.gov.pitb.pcb.data.server.players.Fielder;


/**
 * Created by nadirhussain on 17/07/2017.
 */

public final class WagonWheelRenderer {
    private WagonWheelRenderer(){

    }



    public static void drawFielder(Canvas canvas, Fielder fielder, Bitmap fielderBitmap) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        float fielderX=fielder.getPositionX()-(fielderBitmap.getWidth()/2);
        float fielderY=fielder.getPositionY()-(fielderBitmap.getHeight()/2);

        canvas.drawBitmap(fielderBitmap, fielderX, fielderY, paint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(18f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Align.CENTER);

        String name = fielder.getFielderName();
        Rect result = new Rect();
        textPaint.getTextBounds(name, 0, name.length(), result);

        float yPoint=fielderY + fielderBitmap.getHeight()+result.height();
        float xPoint=fielderX+(result.width()/4);
        canvas.drawText(name, xPoint,yPoint , textPaint);


    }
}
