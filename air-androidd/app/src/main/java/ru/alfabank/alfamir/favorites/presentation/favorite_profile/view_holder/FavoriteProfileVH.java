package ru.alfabank.alfamir.favorites.presentation.favorite_profile.view_holder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;

public class FavoriteProfileVH extends RecyclerView.ViewHolder implements FavoriteProfileAdapterContract.ProfileRowView {

    @BindView(R.id.favorite_profile_viewholder_image)
    ImageView image;
    @BindView(R.id.favorite_profile_viewholder_tv_title)
    TextView tvTitle;
    @BindView(R.id.favorite_profile_viewholder_tv_name)
    TextView tvName;
    @BindView(R.id.favorite_profile_viewholder_rl_main)
    RelativeLayout llMain;
    @BindView(R.id.favorite_profile_viewholder_initials)
    TextView tvInitials;

    private ImageCropper mImageCropper;
    private ViewHolderClickListener mListener;

    public FavoriteProfileVH(View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        mImageCropper = imageCropper;
        llMain.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
    }

    @Override
    public void setName(String name) {
        tvName.setText(name);
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setImage(Bitmap bitmap, boolean isAnimated) {

        tvInitials.setText("");
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
    public void setPlaceHolder(String initials) {
        Drawable drawable = image.getContext().getResources().getDrawable(R.drawable.rectangle_background);
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)), PorterDuff.Mode.SRC_IN);
        image.setImageDrawable(drawable);
        tvInitials.setText(initials);
    }

    public interface ViewHolderClickListener {
        void onItemClicked(int position);
    }
}