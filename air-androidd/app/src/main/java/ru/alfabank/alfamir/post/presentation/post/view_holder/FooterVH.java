package ru.alfabank.alfamir.post.presentation.post.view_holder;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;

public class FooterVH extends RecyclerView.ViewHolder implements PostAdapterContract.FooterRowView {

    @BindView(R.id.view_holder_footer_tv_likes_count) TextView tvLikeCount;
    @BindView(R.id.view_holder_footer_tv_comments_count) TextView tvCommentCount;
    @BindView(R.id.view_holder_footer_image_like) ImageView imageLike;
    @BindView(R.id.view_holder_footer_image_comment) ImageView imageComment;
    @BindView(R.id.view_holder_footer_ll_like) LinearLayout llLike;

    private ViewHolderClickListener mListener;
    private int disabledColor = Color.argb(255, 153, 153, 153);
    private int enabledColor = Color.argb(255, 0, 122, 255);

    public FooterVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        llLike.setOnClickListener(v -> mListener.onPostLiked(getAdapterPosition()));
    }

    @Override
    public void setLikeStatus(String likesCount, int isLiked) {
        if(Integer.parseInt(likesCount)==0) {
            tvLikeCount.setVisibility(View.INVISIBLE);
        } else {
            tvLikeCount.setText(likesCount);
            tvLikeCount.setVisibility(View.VISIBLE);
        }

        if(isLiked==0){
            imageLike.setImageResource(R.drawable.ic_facebook_like);
        } else {
            imageLike.setImageResource(R.drawable.ic_facebook_like_filled);
        }
    }

    @Override
    public void setCommentCount(String count) {
        tvCommentCount.setText(count);
        tvCommentCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCommentsCount() {
        tvCommentCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLikesCount() {
        tvLikeCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCommentsCount() {
        tvCommentCount.setVisibility(View.GONE);
    }

    @Override
    public void hideLikesCount() {
        tvLikeCount.setVisibility(View.GONE);
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
        imageLike.setOnClickListener(null);
    }

    public interface ViewHolderClickListener {
        void onPostLiked(int position);
    }

}

