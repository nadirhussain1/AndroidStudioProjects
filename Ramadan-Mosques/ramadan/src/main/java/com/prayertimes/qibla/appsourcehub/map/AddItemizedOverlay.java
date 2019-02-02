package com.prayertimes.qibla.appsourcehub.map;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.google.android.maps.*;
import java.util.ArrayList;

public class AddItemizedOverlay extends ItemizedOverlay
{

    private Context context;
    private ArrayList mapOverlays;

    public AddItemizedOverlay(Drawable drawable)
    {
        super(boundCenterBottom(drawable));
        mapOverlays = new ArrayList();
    }

    public AddItemizedOverlay(Drawable drawable, Context context1)
    {
        this(drawable);
        context = context1;
    }

    public void addOverlay(OverlayItem overlayitem)
    {
        mapOverlays.add(overlayitem);
    }

    protected OverlayItem createItem(int i)
    {
        return (OverlayItem)mapOverlays.get(i);
    }

    protected boolean onTap(int i)
    {
        OverlayItem overlayitem = (OverlayItem)mapOverlays.get(i);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(overlayitem.getTitle());
        builder.setMessage(overlayitem.getSnippet());
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j)
            {
            }
        });
        builder.show();
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionevent, MapView mapview)
    {
        if(motionevent.getAction() == 1)
        {
            mapview.getProjection().fromPixels((int)motionevent.getX(), (int)motionevent.getY());
        }
        return false;
    }

    public void populateNow()
    {
        populate();
    }

    public int size()
    {
        return mapOverlays.size();
    }
}
