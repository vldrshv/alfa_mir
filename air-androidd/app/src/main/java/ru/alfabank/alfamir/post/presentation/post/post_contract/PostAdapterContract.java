package ru.alfabank.alfamir.post.presentation.post.post_contract;

import android.graphics.Bitmap;

import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.post.presentation.post.view_holder.CommentVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.FooterVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.HeaderVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.HtmlVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.PictureVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.QuoteVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.SingleHtmlVH;

public interface PostAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>,
            HeaderVH.ViewHolderClickListener,
            CommentVH.ViewHolderClickListener,
            FooterVH.ViewHolderClickListener,
            HtmlVH.ViewHolderClickListener,
            SingleHtmlVH.ViewHolderClickListener,
            PictureVH.ViewHolderClickListener,
            QuoteVH.ViewHolderClickListener {
        void bindListRowHeader(int position, HeaderRowView rowView);
        void bindListRowText(int position, TextRowView rowView);
        void bindListRowHtml(int position, HtmlRowView rowView);
        void bindListRowSingleHtml(int position, SingleHtmlRowView rowView);
        void bindListRowPicture(int position, PictureRowView rowView);
        void bindListRowFooter(int position, FooterRowView rowView);
        void bindListRowComment(int position, CommentRowView rowView);
        void bindListRowQuote(int position, QuoteRowView rowView);
        void bindListRowVideo(int position, PostAdapterContract.VideoRowView rowView);
    }
    //
    interface Adapter extends BaseAdapter {
        void onPostImageInject(int position, String javaScript);
        void onPostVideoUrlInject(int position, String javaScript);
        void onPostVideoPosterInject(int position, String javaScript);
        void openPostOptions(int isDeletable, String favoriteStatus, String subscribeStatus);
        void setCommentLikeState(int position, String likesCount, int currentUserLike);
        void setPostLikeState(int position, String likesCount, int currentUserLike);
        void setWebViewOnPause(int position);
        void setWebViewOnResume(int position);
    }

    interface HeaderRowView {
        void setAuthorName(String name);
        void setAuthorInitials(String initials);
        void setPostDate(String date);
        void setHeadingTitle(String headingTitle);
        void setPostTitle(String title);
        void hideHeadingTitle();
        void hidePostTitle();
        void setAuthorAvatar(Bitmap encodedImage, boolean isAnimated);
        void setAuthorPlaceholder(String initials);
        void showOptions(int isDeletable, String favoriteStatus, String subscribeStatus);
        void showOptionsButton();
        void hideOptionsButton();
    }

    interface PictureRowView {
        void setPostImage(Bitmap encodedImage, boolean isAnimated);
        void clearImage();
        void setPostImageSizeParameters(int height);
    }

    interface TextRowView {
        void setPostBody(String body);
        void isSubHeader(Boolean isSubHeader);
    }

    interface HtmlRowView {
        void setHtml(String html);
//        void setOnPause(); // TODO
    }

    interface SingleHtmlRowView {
        void setHtml(String html);
        void injectJavaScriptPostImage(String javaScript);
        void injectJavaScriptVideoUrl(String javaScript);
        void injectJavaScriptVideoPoster(String javaScript);
        void setOnPause();
        void setOnResume();
    }

    interface FooterRowView {
        void setLikeStatus(String likesCount, int isLiked);
        void setCommentCount(String count);
        void showCommentsCount();
        void showLikesCount();
        void hideCommentsCount();
        void hideLikesCount();
        void enableLikes();
        void disableLikes();
        void enableComments();
        void disableComments();
    }

    interface QuoteRowView {
        void setAuthorPlaceholder(String initials);
        void setAuthorName(String name);
        void setAuthorInitials(String initials);
        void setAuthorJobPosition(String jobPosition);
        void setQuoteBody(String body);
        void setAuthorAvatar(Bitmap encodedImage, Boolean isAnimated);
    }

    interface CommentRowView {
        void setAuthorName(String name);
        void setCommentText(String text);
        void setCommentDate(String date);
        void setDeletable(int deletable);
        void setLikeStatus(String likesCount, int isLiked);
        void clearAuthorImage();
        void clearAuthorInitials();
        void setAuthorImage(Bitmap encodedImage, boolean isAnimated);
        void setAuthorInitials(String initials);
        void setAuthorPicPlaceholder(String name);
        void setMargins(boolean isSecondLevel);
    }

    interface VideoRowView {
        void configureVideo(String url, String token);
        void releaseVideo();
    }

}
