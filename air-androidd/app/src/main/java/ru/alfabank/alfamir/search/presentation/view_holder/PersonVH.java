package ru.alfabank.alfamir.search.presentation.view_holder;

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
import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;

public class PersonVH extends RecyclerView.ViewHolder implements SearchAdapterContract.SearchPersonRowView {

    @BindView(R.id.search_person_viewholder_rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.search_person_viewholder_image)
    ImageView imageAvatar;
    @BindView(R.id.search_person_viewholder_tv_initials)
    TextView tvInitilas;
    @BindView(R.id.search_person_viewholder_tv_name)
    TextView tvName;
    @BindView(R.id.search_person_viewholder_tv_position)
    TextView tvPosition;

    private ViewHolderClickListener mListener;
    private ImageCropper mImageCropper;

    public PersonVH(View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        mImageCropper = imageCropper;
        rlContainer.setOnClickListener(view -> mListener.onPersonClicked(getAdapterPosition()));
    }

    @Override
    public void setName(String name) {
        tvName.setText(name);
    }

    @Override
    public void setPosition(String position) {
        tvPosition.setText(position);
    }

    @Override
    public void setPicture(Bitmap bitmap, boolean isAnimated) {

        Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (4 * Constants.Initialization.getDENSITY()));
        imageAvatar.setImageBitmap(curvedBitmap);
        imageAvatar.setVisibility(View.VISIBLE);
        if (isAnimated) {
            imageAvatar.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageAvatar.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void setPlaceHolder(String initials) {
        Drawable drawable = imageAvatar.getContext().getResources().getDrawable(R.drawable.rectangle_background);
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN);
        imageAvatar.setImageDrawable(drawable);
        tvInitilas.setText(initials);
    }

    @Override
    public void clearImage() {
        imageAvatar.setImageBitmap(null);
        tvInitilas.setText("");
    }

    public interface ViewHolderClickListener {
        void onPersonClicked(int position);
    }

}