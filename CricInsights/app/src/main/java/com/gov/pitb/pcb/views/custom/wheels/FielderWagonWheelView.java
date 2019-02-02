package com.gov.pitb.pcb.views.custom.wheels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.server.players.Fielder;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.custom.misc.WagonWheelRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 17/07/2017.
 */

public class FielderWagonWheelView extends View {
    public final static int WHEEL_SIZE = 1080;
    private final static int OFFSET = 60;
    private float oldX, oldY;
    private float newX, newY;
    private int viewSize;
    private boolean isPositionChanged;
    private Bitmap fielderBitmap;
    private Bitmap groundBitmap;
    private int resizeOffset;


    private List<Fielder> fieldersList = new ArrayList<>();

    public FielderWagonWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isPositionChanged = false;
        int dimension = (int) ViewScaleHandler.resizeDimension(WHEEL_SIZE);
        resizeOffset = (int) ViewScaleHandler.resizeDimension(OFFSET);
        fielderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fielder_icon);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.custom_ground);
        groundBitmap = Bitmap.createScaledBitmap(bitmap, dimension, dimension, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewSize = getWidth();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(groundBitmap, 0, 0, paint);

        for (Fielder fielder : fieldersList) {
            WagonWheelRenderer.drawFielder(canvas, fielder, fielderBitmap);
        }
    }

    public void showFieldersOnField(List<Fielder> fielders) {
        this.fieldersList = fielders;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                newX = event.getX();
                newY = event.getY();
                updateFieldersPositions(oldX, oldY, newX, newY);
                oldX = newX;
                oldY = newY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                newX = event.getX();
                newY = event.getY();
                updateFieldersPositions(oldX, oldY, newX, newY);
                oldX = newX;
                oldY = newY;
                invalidate();
                break;
        }
        return true;
    }

    private void updateFieldersPositions(float oldX, float oldY, float newX, float newY) {
        int cx = viewSize / 2;
        int cy = viewSize / 2;
        float xCompSquare = (newX - cx) * (newX - cx);
        float yCompSquare = (newY - cy) * (newY - cy);
        float totalSquare = xCompSquare + yCompSquare;

        int radius = cx - resizeOffset;
        if (totalSquare <= (radius * radius)) {
            for (Fielder fielder : fieldersList) {
                if (fielder.checkDrag(oldX, oldY, newX, newY, fielderBitmap.getWidth(), fielderBitmap.getHeight())) {
                    isPositionChanged = true;
                    break;
                }
            }
        }

        // long currentFielderPositionId = MatchStateManager.getInstance().getMatchStateController().getCurrentfielderPositionId();
        if (isPositionChanged) {
            MatchStateManager.getInstance().getCurrentDelivery().setFielderPositionChanged(true);
        }
    }


}
