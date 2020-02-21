package ru.alfabank.alfamir.feed.presentation.feed.view_holders;

import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedAdapterContract;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

public class HeaderVH extends RecyclerView.ViewHolder implements FeedAdapterContract.HeaderRowView {

    @BindView(R.id.feed_header_viewholder_image_profile)
    ImageView imageProfile;
    @BindView(R.id.feed_header_viewholder_image_cover)
    ImageView imageCover;
    @BindView(R.id.feed_header_viewholder_tv_title)
    TextView tvTitle;
    @BindView(R.id.feed_header_viewholder_tv_description)
    TextView tvDescription;
    @BindView(R.id.feed_header_viewholder_tv_status)
    TextView tvStatus;
    @BindView(R.id.feed_header_viewholder_button_status)
    LinearLayout llStatusButton;
    @BindView(R.id.feed_header_viewholder_fl_button)
    FrameLayout flButton;
    @BindView(R.id.feed_header_viewholder_fl_main)
    FrameLayout flMain;
    @BindView(R.id.feed_header_viewholder_fl_profile)
    FrameLayout flProfile;
    @BindView(R.id.feed_header_viewholder_fl_status)
    FrameLayout flStatus;

    private ViewHolderClickListener mListener;

    public HeaderVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        llStatusButton.setOnClickListener(v -> mListener.onSubscribeClicked(getAdapterPosition()));
    }

    @Override
    public void setAuthorImage(Bitmap bitmap, boolean isAnimated) {

        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(bitmap);
        imageProfile.setImageDrawable(roundedImage);
        imageProfile.setVisibility(View.VISIBLE);
        if (isAnimated) {
            imageProfile.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageProfile.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration);
        }
    }

    @Override
    public void setBackgroundImage(Bitmap bitmap, boolean isAnimated) {

        imageCover.setImageBitmap(bitmap);
        imageCover.setVisibility(View.VISIBLE);
        if (isAnimated) {
            int viewHeight = flMain.getMeasuredHeight();
            ViewGroup.LayoutParams params = imageCover.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = viewHeight;
            imageCover.requestLayout();
            imageCover.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageCover.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration);
        }
    }

    @Override
    public void setSubscriptionStateButtonVisibility(boolean isVisible) {
        if (isVisible) flStatus.setVisibility(View.VISIBLE);
        else flStatus.setVisibility(View.GONE);
    }

    @Override
    public void setSubscriptionState(int isSubscribed) {
        switch (isSubscribed) {
            case 0: {
                tvStatus.setText("Подписаться");
                flButton.setVisibility(View.GONE);
                break;
            }
            case 1: {
                tvStatus.setText("Вы подписаны");
                flButton.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void setFeedTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setFeedDescription(String description) {
        tvDescription.setText(description);
    }

    @Override
    public void hideDescription() {
        tvDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideAuthorImage() {
        flProfile.setVisibility(View.GONE);
    }

    @Override
    public void showOptions(String subscribeOption) {
        PopupMenu popup = new PopupMenu(llStatusButton.getContext(), llStatusButton);
        popup.inflate(R.menu.popup_menu_one_option);
        MenuItem menuItemSubscribe = popup.getMenu().findItem(R.id.one_option_first);
        menuItemSubscribe.setTitle(subscribeOption);
        popup.show();
        popup.setOnMenuItemClickListener(item -> {
            mListener.onSubscribeOptionClicked(getAdapterPosition());
            return false;
        });
    }

    public interface ViewHolderClickListener {
        void onSubscribeClicked(int position);

        void onSubscribeOptionClicked(int position);
    }

}
