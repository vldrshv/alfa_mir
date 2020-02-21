package ru.alfabank.alfamir.main.main_feed_fragment.view_holder;

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
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

public class PostViewHolder extends RecyclerView.ViewHolder implements MainFeedListContract.PostRowView {

    @BindView(R.id.view_holder_post_ll_post_body)
    LinearLayout llBody;
    @BindView(R.id.view_holder_post_ll_comment)
    LinearLayout llComment;
    @BindView(R.id.view_holder_post_ll_like)
    LinearLayout llLike;
    @BindView(R.id.view_holder_post_ll_author_info)
    LinearLayout llAuthorInfo;
    @BindView(R.id.view_holder_post_ll_department)
    LinearLayout llHeadingTitle;

    @BindView(R.id.view_holder_post_image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.view_holder_post_image_comment)
    ImageView imageComment;
    @BindView(R.id.view_holder_post_image_like)
    ImageView imageLike;
    @BindView(R.id.view_holder_post_image_more)
    ImageView imageMore;
    @BindView(R.id.view_holder_post_image_post_image)
    ImageView imagePostImage;

    @BindView(R.id.view_holder_post_tv_author_name)
    TextView tvAuthorName;
    @BindView(R.id.view_holder_post_tv_comments_count)
    TextView tvCommentsCount;
    @BindView(R.id.view_holder_post_tv_date)
    TextView tvPostDate;
    @BindView(R.id.view_holder_post_tv_department)
    TextView tvDepartment;
    @BindView(R.id.view_holder_post_tv_initials)
    TextView tvInitials;
    @BindView(R.id.view_holder_post_tv_likes_count)
    TextView tvLikesCount;
    @BindView(R.id.view_holder_post_tv_post_body)
    TextView tvPostBody;
    @BindView(R.id.view_holder_post_tv_post_title)
    TextView tvPostTitle;

    private ViewHolderClickListener mListener;
    private int disabledColor = Color.argb(255, 153, 153, 153);
    private int enabledColor = Color.argb(255, 0, 122, 255);

    public PostViewHolder(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        imageAvatar.setOnClickListener(view -> listener.onAuthorClicked(getAdapterPosition()));
        imageMore.setOnClickListener(view -> listener.onOptionsClicked(getAdapterPosition()));
        llBody.setOnClickListener(view -> listener.onPostClicked(getAdapterPosition()));
        llComment.setOnClickListener(view -> listener.onCommentsClicked(getAdapterPosition()));
        llLike.setOnClickListener(view -> listener.onLikeClicked(getAdapterPosition()));
        llAuthorInfo.setOnClickListener(view -> listener.onAuthorClicked(getAdapterPosition()));
        tvDepartment.setOnClickListener(view -> listener.onHeadlineTitleClicked(getAdapterPosition()));
    }

    @Override
    public void setAuthorName(String name) {
        tvAuthorName.setText(name);
    }

    @Override
    public void clearAuthorInitials() {
        tvInitials.setText("");
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
    public void setPostBody(String body) {
        tvPostBody.setText(body);
    }

    @Override
    public void setLikes(String count) {
        tvLikesCount.setText(count);
    }

    @Override
    public void setLikeStatus(String likesCount, int isLiked) {
        if (Integer.parseInt(likesCount) == 0) {
            tvLikesCount.setVisibility(View.INVISIBLE);
        } else {
            tvLikesCount.setVisibility(View.VISIBLE);
        }

        tvLikesCount.setText(likesCount);
        if (isLiked == 0) {
            imageLike.setImageResource(R.drawable.ic_facebook_like);
        } else {
            imageLike.setImageResource(R.drawable.ic_facebook_like_filled);
        }
    }

    @Override
    public void setComments(String count) {
        tvCommentsCount.setText(count);
    }

    @Override
    public void setPostImageSizeParameters(int height) {
        imagePostImage.setVisibility(View.VISIBLE);
        imagePostImage.requestLayout();
        imagePostImage.getLayoutParams().height = height;
    }

    @Override
    public void setAuthorAvatar(Bitmap bitmap, boolean isAnimated) {

        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(bitmap);
        imageAvatar.setImageDrawable(roundedImage);
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
    public void setAuthorPlaceholder(String initials) {
        Drawable drawable = imageAvatar.getContext().getResources().getDrawable(R.drawable.circle_background);
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN);
        imageAvatar.setImageDrawable(drawable);
        tvInitials.setText(initials);
    }

    @Override
    public void setPostImage(Bitmap bitmap, boolean isAnimated) {

        if(bitmap.getHeight() < bitmap.getWidth()){
            imagePostImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        imagePostImage.setImageBitmap(bitmap);
        imagePostImage.setVisibility(View.VISIBLE);

        if (!isAnimated) return;

        imagePostImage.setAlpha(0f);
        int mShortAnimationDuration = 200;
        imagePostImage.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
    }

    @Override
    public void clearPostImage() {
        imagePostImage.setImageBitmap(null);
    }

    @Override
    public void clearAuthorImage() {
        imageAvatar.setImageDrawable(null);
    }

    @Override
    public void hidePostImage() {
        imagePostImage.setVisibility(View.GONE);
    }

    @Override
    public void showCommentsCount() {
        tvCommentsCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLikesCount() {
        tvLikesCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCommentsCount() {
        tvCommentsCount.setVisibility(View.GONE);
    }

    @Override
    public void hideLikesCount() {
        tvLikesCount.setVisibility(View.GONE);
    }

    @Override
    public void hideHeadingTitle() {
        llHeadingTitle.setVisibility(View.GONE);
    }

    @Override
    public void hidePostTitle() {
        tvPostTitle.setVisibility(View.GONE);
    }

    @Override
    public void hidePostBody() {
        tvPostBody.setVisibility(View.GONE);
    }

    @Override
    public void showPostBody() {
        tvPostBody.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOptions(int isDeletable, String favoriteStatus, String subscribeStatus) {
        PopupMenu popup = new PopupMenu(imageMore.getContext(), imageMore);
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
                    mListener.onMenuItemDeleteClicked(getAdapterPosition());
                    break;
                }
                case R.id.two_options_second: {
                    mListener.onMenuItemFavoriteClicked(getAdapterPosition());
                    break;
                }
                case R.id.two_options_third: {
                    mListener.onMenuItemSubscribeClicked(getAdapterPosition());
                    break;
                }
            }
            return false;
        });
    }

    @Override
    public void enableLikes() {
        imageLike.setColorFilter(enabledColor);
    }

    @Override
    public void disableLikes() {
        imageLike.setColorFilter(disabledColor);
        llLike.setOnClickListener(null);
    }

    @Override
    public void enableComments() {
        imageComment.setColorFilter(enabledColor);
    }

    @Override
    public void disableComments() {
        imageComment.setColorFilter(disabledColor);
        llComment.setOnClickListener(null);
    }

    @Override
    public void showOptions() {
        imageMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOptions() {
        imageMore.setVisibility(View.GONE);
    }

    public interface ViewHolderClickListener {
        void onPostClicked(int position);

        void onAuthorClicked(int position);

        default void onHeadlineTitleClicked(int position) {
        }

        void onCommentsClicked(int position);

        void onLikeClicked(int position);

        void onOptionsClicked(int position);

        void onMenuItemDeleteClicked(int position);

        void onMenuItemFavoriteClicked(int position);

        void onMenuItemSubscribeClicked(int position);
    }

}
