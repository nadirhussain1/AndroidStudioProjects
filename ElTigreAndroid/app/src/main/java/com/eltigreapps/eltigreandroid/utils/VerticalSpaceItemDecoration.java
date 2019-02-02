package com.eltigreapps.eltigreandroid.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nadirhussain on 02/06/2018.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public VerticalSpaceItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset*2);

    }

}
