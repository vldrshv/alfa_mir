package ru.alfabank.alfamir.favorites.presentation.favorite_post.view_holder;

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
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;

public class FavoritePostVH extends RecyclerView.ViewHolder implements FavoritePostAdapterContract.PageRowView {

    @BindView(R.id.favorite_post_viewholder_image)
    ImageView image;
    @BindView(R.id.favorite_post_viewholder_tv_title)
    TextView tvTitle;
    @BindView(R.id.favorite_post_viewholder_tv_date)
    TextView tvDate;
    @BindView(R.id.favorite_post_viewholder_rl_main)
    LinearLayout llMain;
    private ImageCropper mImageCropper;

    private ViewHolderClickListener mListener;

    public FavoritePostVH( View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        mImageCropper = imageCropper;
        llMain.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
    }

    @Override
    public void clearData() {
        tvTitle.setText("");
        tvDate.setText("");
        image.setImageBitmap(null);
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
    public void setImage(Bitmap bitmap, boolean isAnimated) {

        Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (4 * Constants.Initialization.getDENSITY()));
        image.setImageBitmap(curvedBitmap);
        image.setVisibility(View.VISIBLE);
        if (isAnimated) {
            image.setAlpha(0f);
            int mShortAnimationDuration = 200;
            image.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void hideImage() {
        image.setVisibility(View.GONE);
    }

    @Override
    public void showImage() {
        image.setVisibility(View.VISIBLE);
    }


    public interface ViewHolderClickListener {
        void onItemClicked(int position);
    }

}