package ru.alfabank.alfamir.favorites.presentation.favorite_profile.view_holder;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import ru.alfabank.alfamir.Constants;

public class ProfileDecorator extends RecyclerView.ItemDecoration {

    private final Paint mPaint;
    private final float mThickness;
    private final int mIntThickness;

    public ProfileDecorator(int color, int heightDp) {
        mPaint = new Paint();
        mPaint.setColor(color);
//        final float thickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                heightDp, context.getResources().getDisplayMetrics());
        mThickness = heightDp * Constants.Initialization.getDENSITY();
        mIntThickness = (int) mThickness;
        mPaint.setStrokeWidth(mThickness);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        // we want to retrieve the position in the list
        final int position = params.getViewAdapterPosition();

        // and add a separator to any view but the last one
        if (position < state.getItemCount()-1) {
            outRect.set(0, 0, 0, mIntThickness); // left, top, right, bottom
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        final int offset = (int) (mPaint.getStrokeWidth() / 2);

        // this will iterate over every visible view
        for (int i = 0; i < parent.getChildCount(); i++) {
            // get the view
            final View view = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            // get the position
            final int position = params.getViewAdapterPosition();

            // and finally draw the separator

            c.drawLine(view.getLeft() - mIntThickness, view.getBottom() + offset, view.getRight(), view.getBottom() + offset, mPaint); // bottom
        }
    }
}