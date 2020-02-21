package ru.alfabank.alfamir.search.presentation.view_holder;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;

public class PageVH extends RecyclerView.ViewHolder implements SearchAdapterContract.SearchPageRowView {

    @BindView(R.id.search_page_viewholder_rl_container)
    LinearLayout llContainer;
    @BindView(R.id.search_page_viewholder_image)
    ImageView imagePagePic;
    @BindView(R.id.search_page_viewholder_tv_title)
    TextView tvTitle;
    @BindView(R.id.search_page_viewholder_tv_date)
    TextView tvDate;

    private ViewHolderClickListener mListener;
    private ImageCropper mImageCropper;

    public PageVH( View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        mImageCropper = imageCropper;
        llContainer.setOnClickListener(view -> mListener.onPageClicked(getAdapterPosition()));
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setDate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void setPicture(Bitmap bitmap, boolean isAnimated) {

        Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (4 * Constants.Initialization.getDENSITY()));
        imagePagePic.setImageBitmap(curvedBitmap);
        if (isAnimated) {
            imagePagePic.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imagePagePic.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void clearImage() {
        imagePagePic.setImageBitmap(null);
    }

    @Override
    public void hideImage() {
        imagePagePic.setVisibility(View.GONE);
    }

    @Override
    public void showImage() {
        imagePagePic.setVisibility(View.VISIBLE);
    }


    public interface ViewHolderClickListener {
        void onPageClicked(int position);
    }

}
