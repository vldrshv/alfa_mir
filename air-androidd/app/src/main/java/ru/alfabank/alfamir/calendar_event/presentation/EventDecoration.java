package ru.alfabank.alfamir.calendar_event.presentation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import ru.alfabank.alfamir.Constants;

public class EventDecoration extends RecyclerView.ItemDecoration {

    private final Paint mPaint;
    private final float mThickness;
    private final int mIntThickness;

    public EventDecoration(int color, int heightDp) {
        mPaint = new Paint();
        mPaint.setColor(color);
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
            outRect.set(mIntThickness, mIntThickness, 0, mIntThickness); // left, top, right, bottom
        } else {
            outRect.set(mIntThickness, mIntThickness, mIntThickness, mIntThickness);
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

            c.drawLine(view.getLeft(), view.getTop() - offset, view.getRight(), view.getTop() - offset, mPaint); // top
            c.drawLine(view.getLeft() - mIntThickness, view.getBottom() + offset, view.getRight(), view.getBottom() + offset, mPaint); // bottom
            c.drawLine(view.getLeft() - offset, view.getBottom(), view.getLeft() - offset, view.getTop() - mIntThickness, mPaint); // left

            // if it's the last item
            if(position == state.getItemCount() - 1) {
                c.drawLine(view.getRight() + offset, view.getBottom() + mIntThickness, view.getRight() + offset, view.getTop() - mIntThickness, mPaint); // right
            }
        }
    }
}