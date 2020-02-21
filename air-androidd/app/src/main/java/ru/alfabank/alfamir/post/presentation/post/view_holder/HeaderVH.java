package ru.alfabank.alfamir.post.presentation.post.view_holder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

public class HeaderVH extends RecyclerView.ViewHolder implements PostAdapterContract.HeaderRowView {

    @BindView(R.id.rc_news_item_post_header_ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.view_holder_post_header_ll_heading)
    LinearLayout llHeading;
    @BindView(R.id.rc_news_item_post_header_image_profile_icon)
    ImageView imageProfile;
    @BindView(R.id.rc_news_item_post_header_image_options)
    ImageView imageOptions;
    @BindView(R.id.rc_news_item_post_header_item_tv_name)
    TextView tvAuthorName;
    @BindView(R.id.rc_news_item_post_header_item_tv_date)
    TextView tvPostDate;
    @BindView(R.id.rc_news_item_post_header_tv_heading_title)
    TextView tvDepartment;
    @BindView(R.id.rc_news_item_post_header_tv_profile_initials)
    TextView tvInitials;
    @BindView(R.id.rc_news_item_post_header_tv_news_item_title)
    TextView tvPostTitle;

    private ViewHolderClickListener mListener;

    public HeaderVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        llProfile.setOnClickListener(v -> mListener.onProfileClicked());
        llHeading.setOnClickListener(v -> mListener.onHeadingClicked());
        imageOptions.setOnClickListener(v -> mListener.onOptionsClicked());
        imageProfile.setOnClickListener(v -> mListener.onProfileClicked());
    }

    @Override
    public void setAuthorAvatar(Bitmap encodedImage, boolean isAnimated) {

        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(encodedImage);
        imageProfile.setImageDrawable(roundedImage);
        imageProfile.setVisibility(View.VISIBLE);

        if (isAnimated) {
            imageProfile.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageProfile.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }


    @Override
    public void setAuthorPlaceholder(String initials) {
        Drawable drawable = imageProfile.getContext().getResources().getDrawable(R.drawable.background_profile_empty);
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN);
        imageProfile.setImageDrawable(drawable);
    }

    @Override
    public void setAuthorName(String name) {
        tvAuthorName.setText(name);
    }

    @Override
    public void setAuthorInitials(String initials) {
        tvInitials.setText(initials);
    }

    @Override
    public void setPostDate(String date) {
        tvPostDate.setText(date);
    }

    @Override
    public void setHeadingTitle(String headingTitle) {
        tvDepartment.setText(headingTitle);
    }

    @Override
    public void setPostTitle(String title) {
        tvPostTitle.setText(title);
    }

    @Override
    public void hideHeadingTitle() {
        llHeading.setVisibility(View.GONE);
    }

    @Override
    public void hidePostTitle() {
        tvPostTitle.setVisibility(View.GONE);
    }

    @Override
    public void showOptions(int isDeletable, String favoriteStatus, String subscribeStatus) {
        PopupMenu popup = new PopupMenu(imageOptions.getContext(), imageOptions);
        popup.inflate(R.menu.popup_menu_three_options);

        MenuItem menuItemDelete = popup.getMenu().findItem(R.id.two_options_first);
        if (isDeletable == 0) {
            menuItemDelete.setVisible(false);
        } else {
            menuItemDelete.setVisible(true);
            menuItemDelete.setTitle("Удалить пост");
        }

        MenuItem menuItemFavorite = popup.getMenu().findItem(R.id.two_options_second);
        menuItemFavorite.setTitle(favoriteStatus);

        MenuItem menuItemSubscribe = popup.getMenu().findItem(R.id.two_options_third);
        menuItemSubscribe.setTitle(subscribeStatus);

        popup.show();

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.two_options_first: {
                    mListener.onMenuItemDeleteClicked();
                    break;
                }
                case R.id.two_options_second: {
                    mListener.onMenuItemFavoriteClicked();
                    break;
                }
                case R.id.two_options_third: {
                    mListener.onMenuItemSubscribeClicked();
                    break;
                }
            }
            return false;
        });
    }

    @Override
    public void showOptionsButton() {
        imageOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOptionsButton() {
        imageOptions.setVisibility(View.GONE);
    }

    public interface ViewHolderClickListener {
        void onProfileClicked();

        void onHeadingClicked();

        void onOptionsClicked();

        void onMenuItemDeleteClicked();

        void onMenuItemFavoriteClicked();

        void onMenuItemSubscribeClicked();
    }
}