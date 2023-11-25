package com.accessa.ibora.Settings.Rooms;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int spanCount;
    private final int spacing;

    public SpacesItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column + 1) * spacing / spanCount;

        if (position >= spanCount) {
            outRect.top = spacing; // Add spacing to the top for items on the second line and beyond
        }
    }
}

