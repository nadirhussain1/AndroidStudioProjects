package com.prayertimes.qibla.appsourcehub.support;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NonScrollListView extends ListView
{

    public NonScrollListView(Context context)
    {
        super(context);
    }

    public NonScrollListView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public NonScrollListView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    public void onMeasure(int i, int j)
    {
        super.onMeasure(i, android.view.View.MeasureSpec.makeMeasureSpec(0x1fffffff, 0x80000000));
        getLayoutParams().height = getMeasuredHeight();
    }
}
