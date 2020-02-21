package ru.alfabank.alfamir.utility.decorators;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by U_M0WY5 on 21.11.2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private int spanCount;
    private boolean includeEdge;

    public SpacesItemDecoration(int spacing, int spanCount, boolean includeEdge) {
        this.spacing = spacing;
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount-1) {
                outRect.top = spacing; // item top
            }
        }

//        if(parent.getChildLayoutPosition(view)==0||parent.getChildLayoutPosition(view)%numberOfColumns==0){
//            outRect.left = space;
//            outRect.right = space/2;
//        } else if(parent.getChildLayoutPosition(view)%(numberOfColumns-1)==0) {
//            outRect.left = space/2;
//            outRect.right = space;
//        } else {
//                outRect.left = space/2;
//                outRect.right = space/2;
//        }

//        outRect.left = spacing;
//        outRect.right = spacing;
//        outRect.bottom = spacing;


        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.top = space;
//        } else {
//            outRect.top = 0;
//        }
    }



}
