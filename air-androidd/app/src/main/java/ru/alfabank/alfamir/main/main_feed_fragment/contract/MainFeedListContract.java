package ru.alfabank.alfamir.main.main_feed_fragment.contract;

import android.content.Context;
import android.graphics.Bitmap;

import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.PostViewHolder;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.SurveyViewHolder;

public interface MainFeedListContract {

    interface Presenter extends BaseListPresenter<Adapter>,
            PostViewHolder.ViewHolderClickListener,
            SurveyViewHolder.ViewHolderClickListener {
        void takeListAdapter(Adapter adapter);
        int getListSize();
        int getItemViewType(int position);
        void bindListRowSurveyView(int position, SurveyRowView rowView);
        void bindListRowPostView(int position, PostRowView rowView);
    }

    interface Adapter extends BaseAdapter {
        void onAuthorAvatarDownloaded(int position, Bitmap binaryImage, boolean isAnimated);
        void onPostImageDownloaded(int position, Bitmap binaryImage, boolean isAnimated);
        void onSurveyImageDownload(int position, Bitmap binaryImage, boolean isAnimated);
        void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus);
//        void openPostActivityUi(int position, String feedType, String postId, String postUrl, String postType, boolean startWithComments);
        void openSurveyActivityUi(String surveyId);
        void openProfileActivityUi(String id);
        void openNewsActivityUi(String title, String type, String postUrl);
        void setLikeStatus(int position, String likesCount, int isLiked);
        void removePostAtPosition(int position);
        Context getContext();
    }

    interface SurveyRowView {
        void setSurveyTypeLocalizedName(String name);
        void setTitle(String surveyTitle);
        void showEndDate(String date);
        void setCompleted(String text);
        void hideEndDate();
        void showProgressBar(String text, int percentage);
        void hideProgressBar();
        void setImage(Bitmap encodedImage, boolean isAnimated);
    }

    interface PostRowView {
        void setAuthorName(String name);
        void clearAuthorInitials();
        void clearAuthorImage();
        void setPostDate(String date);
        void setHeadingTitle(String headingTitle);
        void setPostTitle(String title);
        void setPostBody(String body);
        void setLikes(String count);
        void setLikeStatus(String likesCount, int isLiked);
        void setComments(String count);
        void setPostImageSizeParameters(int height);
        void setAuthorAvatar(Bitmap encodedImage, boolean isAnimated);
        void setAuthorPlaceholder(String initials);
        void setPostImage(Bitmap encodedImage, boolean isAnimated);
        void clearPostImage();
        void hidePostImage();
        void showCommentsCount();
        void showLikesCount();
        void hideCommentsCount();
        void hideLikesCount();
        void hideHeadingTitle();
        void hidePostTitle();
        void hidePostBody();
        void showPostBody();
        void showOptions(int isDeletable, String favoriteStatus, String subscribeStatus);

        void enableLikes();
        void disableLikes();
        void enableComments();
        void disableComments();
        void showOptions();
        void hideOptions();
    }

}
