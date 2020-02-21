package ru.alfabank.alfamir.notification.presentation.view_holder;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;

public class NotificationVH extends RecyclerView.ViewHolder
        implements NotificationAdapterContract.NotificationRowView {

    private ViewHolderClickListener mListener;
    @BindView(R.id.notification_viewholder_image)
    ImageView image;
    @BindView(R.id.notification_viewholder_fl_main)
    FrameLayout flContainer;
    @BindView(R.id.notification_viewholder_tv_date)
    TextView tvDate;
    @BindView(R.id.notification_viewholder_tv_text)
    TextView tvText;
    @BindView(R.id.notification_viewholder_image_santa)
    ImageView imageSantaHat;
    private ImageCropper mImageCropper;


    public NotificationVH( View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        flContainer.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
        mImageCropper = imageCropper;
    }

    @Override
    public void setText(String text) {
        tvText.setText(text);
    }

    @Override
    public void setText(Spanned text) {
        tvText.setText(text);
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
    public void showImage() {
        image.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImage() {
        image.setVisibility(View.GONE);
    }

    @Override
    public void showProfilePlaceHolder() {

    }

    @Override
    public void clearImage() {
        image.setImageBitmap(null);
    }

    @Override
    public void showSantaHat() {
        imageSantaHat.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSantaHat() {
        imageSantaHat.setVisibility(View.GONE);
    }

    public interface ViewHolderClickListener {
        void onItemClicked(int position);
    }

}