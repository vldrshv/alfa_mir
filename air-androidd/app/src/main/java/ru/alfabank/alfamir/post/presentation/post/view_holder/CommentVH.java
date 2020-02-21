package ru.alfabank.alfamir.post.presentation.post.view_holder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

public class CommentVH extends RecyclerView.ViewHolder implements PostAdapterContract.CommentRowView {

    @BindView(R.id.view_holder_comment_tv_author_name)
    TextView tvAuthorName;
    @BindView(R.id.view_holder_comment_tv_date)
    TextView tvDate;
    @BindView(R.id.view_holder_comment_tv_body)
    TextView tvBody;
    @BindView(R.id.view_holder_comment_tv_author_initials)
    TextView tvAuthorInitials;
    @BindView(R.id.view_holder_comment_tv_likes_count)
    TextView tvLikesCount;
    @BindView(R.id.view_holder_comment_tv_reply)
    TextView tvReply;
    @BindView(R.id.view_holder_comment_tv_delete)
    TextView tvDelete;
    @BindView(R.id.view_holder_comment_image_profile)
    ImageView imageProfile;
    @BindView(R.id.view_holder_comment_image_like)
    ImageView imageLike;
    @BindView(R.id.view_holder_comment_ll_like)
    LinearLayout llLike;
    @BindView(R.id.view_holder_comment_ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.view_holder_comment_ll_base)
    LinearLayout llBase;

    private ViewHolderClickListener mListener;

    public CommentVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        imageProfile.setOnClickListener(v -> mListener.onProfileClicked(getAdapterPosition()));
        tvAuthorName.setOnClickListener(v -> mListener.onProfileClicked(getAdapterPosition()));
        llLike.setOnClickListener(v -> mListener.onCommentLiked(getAdapterPosition()));
        tvReply.setOnClickListener(v -> mListener.onReply(getAdapterPosition()));
        tvDelete.setOnClickListener(v -> mListener.onDeleteComment(getAdapterPosition()));
    }

    @Override
    public void setAuthorName(String name) {
        tvAuthorName.setText(name);
    }

    @Override
    public void setCommentText(String text) {
        tvBody.setText(text);
    }

    @Override
    public void setCommentDate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void setDeletable(int deletable) {
        if (deletable == 0) {
            tvDelete.setVisibility(View.INVISIBLE);
        } else {
            tvDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setLikeStatus(String likesCount, int isLiked) {
        if (Integer.parseInt(likesCount) == 0) {
            tvLikesCount.setVisibility(View.INVISIBLE);
        } else {
            tvLikesCount.setText(likesCount);
            tvLikesCount.setVisibility(View.VISIBLE);
        }

        if (isLiked == 0) {
            imageLike.setImageResource(R.drawable.ic_facebook_like);
        } else {
            imageLike.setImageResource(R.drawable.ic_facebook_like_filled);
        }
    }

    @Override
    public void clearAuthorImage() {
        imageProfile.setImageDrawable(null);
    }

    @Override
    public void clearAuthorInitials() {
        tvAuthorInitials.setText("");
    }

    @Override
    public void setAuthorImage(Bitmap encodedImage, boolean isAnimated) {

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
    public void setAuthorInitials(String initials) {
        tvAuthorInitials.setVisibility(View.VISIBLE);
        tvAuthorInitials.setText(initials);
    }

    @Override
    public void setAuthorPicPlaceholder(String name) {
        Handler handler = new Handler();
        new Thread(() -> {
            Drawable drawable = llBase.getContext().getResources().getDrawable(R.drawable.circle_background);
            drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(name)),
                    PorterDuff.Mode.SRC_IN);
            handler.post(() -> imageProfile.setImageDrawable(drawable));
        }).start();
    }

    @Override
    public void setMargins(boolean isSecondLevel) {

        RecyclerView.LayoutParams parameters = (RecyclerView.LayoutParams) llBase.getLayoutParams();
        if (isSecondLevel) {

            int marginLeft = (int) (26 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginTop = (int) (16 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginRight = (int) (12 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginBottom = (int) (0 * Constants.Initialization.getDENSITY()); // margin in pixels

            parameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            llBase.setLayoutParams(parameters);
        } else {
            int marginLeft = (int) (12 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginTop = (int) (16 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginRight = (int) (12 * Constants.Initialization.getDENSITY()); // margin in pixels
            int marginBottom = (int) (0 * Constants.Initialization.getDENSITY()); // margin in pixels

            parameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            llBase.setLayoutParams(parameters);
        }
    }

    public interface ViewHolderClickListener {
        void onProfileClicked(int position);

        void onCommentLiked(int position);

        void onReply(int position);

        void onDeleteComment(int position);
    }

}

