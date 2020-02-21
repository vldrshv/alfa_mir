package ru.alfabank.alfamir.notification.old;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.data.source.repositories.old_trash.PostPhotoRepository;
import ru.alfabank.alfamir.data.source.repositories.old_trash.ProfilePhotoRepository;
import ru.alfabank.alfamir.notification.data.dto.ModelNotification;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.survey.presentation.SurveyActivity;
import ru.alfabank.alfamir.utility.callbacks.InflatingPostPic;
import ru.alfabank.alfamir.utility.callbacks.InflatingProfilePic;
import ru.alfabank.alfamir.utility.enums.FormatElement;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler;
import ru.alfabank.alfamir.utility.static_utilities.NotificationsTextEditor;

import static ru.alfabank.alfamir.Constants.Blur;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_2;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;
import static ru.alfabank.alfamir.Constants.Feed.NEWS_TYPE_SEARCH_MEDIA;
import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_NOTIFICATION;
import static ru.alfabank.alfamir.Constants.QUIZ_ID;
import static ru.alfabank.alfamir.Constants.INTENT_SOURCE;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BIRTHDAY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_SURVEY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_VACATION;
import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_ID;

/**
 * Created by U_M0WY5 on 21.12.2017.
 */

public class AdapterNotifications extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<ModelNotification> localNotifications;
    private ProfilePhotoRepository profilePhotoRepository;
    private PostPhotoRepository postPhotoRepository;
    private static AdapterNotificationCallback adapterCallback;

    public AdapterNotifications(List<ModelNotification> localNotifications, PostPhotoRepository postPhotoRepository,
                                ProfilePhotoRepository profilePhotoRepository,
                                AdapterNotificationCallback adapterCallback) {
        this.localNotifications = localNotifications;
        this.profilePhotoRepository = profilePhotoRepository;
        this.postPhotoRepository = postPhotoRepository;
        this.adapterCallback = adapterCallback;
    }

    @Override
    public int getItemViewType(int position) {
        return localNotifications.get(position) == null ? Constants.VIEW_TYPE_LOADING : Constants.VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_ITEM) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_viewholder, parent, false);
            return new ViewHolder(rowView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_viewholder, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    public void notifyAllItemsChanged(int readStatus) {
        for (ModelNotification modelNotification : localNotifications) {
            modelNotification.setViewed(readStatus);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvDate.setText(DateConverter.formatDate(localNotifications.get(position).getCreateTime(),
                    DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH, FormatElement.NOTIFICATION));
            if (localNotifications.get(position).getViewed() == 0) {
                viewHolder.flSelected.setVisibility(View.VISIBLE);
            } else {
                viewHolder.flSelected.setVisibility(View.GONE);
            }

            int notificationType = localNotifications.get(position).getType();
            switch (notificationType) {
                case NOTIFICATION_TYPE_BIRTHDAY:
                case NOTIFICATION_TYPE_VACATION:
                case NOTIFICATION_TYPE_NEWS_COMMENT:
                case NOTIFICATION_TYPE_BLOG_COMMENT:
                case NOTIFICATION_TYPE_COMMUNITY_COMMENT:
                    if (notificationType == NOTIFICATION_TYPE_NEWS_COMMENT ||
                            notificationType == NOTIFICATION_TYPE_BLOG_COMMENT ||
                            notificationType == NOTIFICATION_TYPE_COMMUNITY_COMMENT) {

                        viewHolder.tvExtraInfo.setText(" — " + localNotifications.get(position).getAuthor().getName());
                        viewHolder.tvTitle.setText(NotificationsTextEditor.getCommentNotificationText(localNotifications.get(position).
                                getMessageParameters()));
                    } else if (notificationType == NOTIFICATION_TYPE_BIRTHDAY) {

                        viewHolder.tvTitle.setText(NotificationsTextEditor.getBirthdayNotification(localNotifications.get(position).
                                getMessageParameters(), localNotifications.get(position).getCreateTime()));
                        viewHolder.tvExtraInfo.setText("");

                    } else if (notificationType == NOTIFICATION_TYPE_VACATION) {
                        viewHolder.tvTitle.setText(NotificationsTextEditor.getVacationNotification(localNotifications.get(position).getMessageParameters()));
                        viewHolder.tvExtraInfo.setText("");
                    }
                    showProfilePic(viewHolder, position);
                    break;
                case NOTIFICATION_TYPE_NEWS:
                case NOTIFICATION_TYPE_BLOG:
                case NOTIFICATION_TYPE_COMMUNITY:
                case NOTIFICATION_TYPE_MEDIA:
                    viewHolder.tvTitle.setText(localNotifications.get(position).getPostTitle());
                    viewHolder.tvExtraInfo.setText(" — " + localNotifications.get(position).getDepartment());
                    if (localNotifications.get(position).checkIfThankYou()) {
                        showProfilePic(viewHolder, position);
                    } else {
                        showPostPic(viewHolder, position);
                    }
                    break;
                case NOTIFICATION_TYPE_SURVEY:
                    viewHolder.tvTitle.setText(localNotifications.get(position).getPostTitle());
                    viewHolder.tvExtraInfo.setText(" — " + localNotifications.get(position).getDepartment());
                    showPostPic(viewHolder, position);
                    break;
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void showPostPic(ViewHolder viewHolder, int position) {
        if (localNotifications.get(position).getImg() != null && !localNotifications.get(position).getImg().equals("")) {
            viewHolder.image.setImageDrawable(null);
            viewHolder.tvInitials.setVisibility(View.GONE);
            viewHolder.flImage.setVisibility(View.INVISIBLE);
            viewHolder.imageBackground.setVisibility(View.GONE);
            viewHolder.flBlur.setVisibility(View.GONE);

            switch (LinkHandler.INSTANCE.isPhotoSquare(localNotifications.get(position).getImg())) {
                case Constants.PHOTO_FORMAT_SQUARE: {
                    postPhotoRepository.inflateImageAnimated(
                            LinkHandler.INSTANCE.getPhotoLink(localNotifications.get(position).getImg()), 192, 192, viewHolder, position);
                    break;
                }
                case Constants.PHOTO_FORMAT_CUSTOM: {
                    postPhotoRepository.inflateImageBlurred(LinkHandler.INSTANCE.getPhotoLink(
                            localNotifications.get(position).getImg()), 192, 192, viewHolder, position, Blur.STRONG);
                    break;
                }
            }
        } else {
            viewHolder.flImage.setVisibility(View.GONE);
        }
    }

    private void showProfilePic(ViewHolder viewHolder, int position) {
        viewHolder.image.setImageDrawable(null);
        viewHolder.flImage.setVisibility(View.VISIBLE);
        viewHolder.imageBackground.setVisibility(View.GONE);
        viewHolder.flBlur.setVisibility(View.GONE);

        if (localNotifications.get(position).getImg() != null && !localNotifications.get(position).getImg().equals("")) {
            profilePhotoRepository.inflateProfileAnimated(LinkHandler.INSTANCE.getPhotoLink(localNotifications.get(position).getImg()), viewHolder, position);
            viewHolder.tvInitials.setVisibility(View.GONE);
        } else {
            viewHolder.tvInitials.setVisibility(View.VISIBLE);
            String initials = InitialsMaker.formInitials(localNotifications.get(position).getAuthor().getName());
            viewHolder.tvInitials.setText(initials);
            Drawable drawable = viewHolder.mainView.getContext().getResources().getDrawable(R.drawable.background_profile_empty);
            drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                    PorterDuff.Mode.SRC_IN);
            viewHolder.image.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return localNotifications.size();
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements InflatingPostPic, InflatingProfilePic, PostPhotoRepository.InflatingPostPicBlurred {

        View mainView;
        TextView tvTitle;
        TextView tvDate;
        TextView tvExtraInfo;
        TextView tvInitials;
        ImageView image;
        ImageView imageBackground;
        FrameLayout flImage;
        FrameLayout flBlur;
        FrameLayout flSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            tvTitle = itemView.findViewById(R.id.rc_item_notification_tv_title);
            tvDate = itemView.findViewById(R.id.rc_item_notification_tv_date);
            tvExtraInfo = itemView.findViewById(R.id.rc_item_notification_tv_extra_info);
            image = itemView.findViewById(R.id.rc_item_notification_image_page_image);
            imageBackground = itemView.findViewById(R.id.rc_item_notification_image_page_background);
            flImage = itemView.findViewById(R.id.rc_item_notification_fl_page_pic);
            flBlur = itemView.findViewById(R.id.rc_item_notification_fl_blur_background);
            flSelected = itemView.findViewById(R.id.rc_item_notification_selected_background);
            tvInitials = itemView.findViewById(R.id.rc_item_notification_tv_profile_initials);
            setClickListeners();
        }

        private void setClickListeners() {
            mainView.setOnClickListener(view -> {

                localNotifications.get(getAdapterPosition()).setViewed(1);
                adapterCallback.markAsRead(localNotifications.get(getAdapterPosition()).getNotificationid(), getAdapterPosition());

                int notificationType = localNotifications.get(getAdapterPosition()).getType();
                Intent intent;
                switch (notificationType) {
                    case NOTIFICATION_TYPE_NEWS:
                    case NOTIFICATION_TYPE_BLOG:
                    case NOTIFICATION_TYPE_COMMUNITY:
                    case NOTIFICATION_TYPE_NEWS_COMMENT:
                    case NOTIFICATION_TYPE_BLOG_COMMENT:
                    case NOTIFICATION_TYPE_COMMUNITY_COMMENT:
                        intent = new Intent(mainView.getContext(), PostActivity.class);
                        String postId = localNotifications.get(getAdapterPosition()).getOpenParams().getPostid();
                        String feedUrl = localNotifications.get(getAdapterPosition()).getOpenParams().getPosturl();
                        String feedType = localNotifications.get(getAdapterPosition()).getOpenParams().getType();

                        intent.putExtra(POST_ID, postId);
                        intent.putExtra(FEED_URL, feedUrl);
                        intent.putExtra(FEED_ID, feedUrl);
                        intent.putExtra(FEED_TYPE, feedType);
                        mainView.getContext().startActivity(intent);
                        break;
                    case NOTIFICATION_TYPE_BIRTHDAY:
                    case NOTIFICATION_TYPE_VACATION:
                        intent = new Intent(mainView.getContext(), ProfileActivity.class);
                        intent.putExtra(POST_ID, localNotifications.get(getAdapterPosition()).getOpenParams().getUserId());
                        mainView.getContext().startActivity(intent);
                        break;
                    case NOTIFICATION_TYPE_MEDIA:
                    case NOTIFICATION_TYPE_MEDIA_COMMENT:
                        intent = new Intent(mainView.getContext(), PostActivity.class);
                        intent.putExtra(POST_ID, localNotifications.get(getAdapterPosition()).getOpenParams().getPostid());
                        intent.putExtra(FEED_TYPE, NEWS_TYPE_SEARCH_MEDIA);
                        mainView.getContext().startActivity(intent);
                        break;
                    case NOTIFICATION_TYPE_SURVEY:
                        intent = new Intent(mainView.getContext(), SurveyActivity.class);
                        String quizId = localNotifications.get(getAdapterPosition()).getOpenParams().getPostid();
                        intent.putExtra(QUIZ_ID, quizId);
                        intent.putExtra(INTENT_SOURCE, INTENT_SOURCE_NOTIFICATION);
                        mainView.getContext().startActivity(intent);
                        break;
                }
            });
        }

        @Override
        public void inflatePostPicture(Bitmap img, int position) {
            if (getAdapterPosition() == position) {
                image.setImageBitmap(img);
            }
        }

        @Override
        public void inflatePostPictureAnimated(Bitmap img, int position, boolean firstTime) {
            if (getAdapterPosition() == position) {
                image.setImageBitmap(img);
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.fadein);
                image.startAnimation(myFadeInAnimation);
            }
        }

        @Override
        public void inflateProfilePic(RoundedBitmapDrawable img, int position) {
            if (getAdapterPosition() == position) {
                image.setImageDrawable(img);
            }
        }

        @Override
        public void inflateProfilePicAnimated(RoundedBitmapDrawable img, int position) {
            if (getAdapterPosition() == position) {
                image.setImageDrawable(img);
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.fadein);
                image.startAnimation(myFadeInAnimation);
            }
        }

        @Override
        public void inflatePostPicBlurred(Bitmap img, Bitmap blurredImg, int position) {
            if (getAdapterPosition() == position) {

                flImage.setVisibility(View.VISIBLE);
                imageBackground.setVisibility(View.VISIBLE);
                flBlur.setVisibility(View.VISIBLE);

                image.setImageBitmap(img);
                imageBackground.setImageBitmap(blurredImg);
            }
        }

        @Override
        public void inflatePostPicBlurredAnimated(Bitmap img, Bitmap blurredImg, int position) {
            if (getAdapterPosition() == position) {
                image.setImageBitmap(img);
                imageBackground.setImageBitmap(blurredImg);

                flImage.setVisibility(View.VISIBLE);
                imageBackground.setVisibility(View.VISIBLE);
                flBlur.setVisibility(View.VISIBLE);

                Animation myFadeInAnimation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.fadein);
                flImage.startAnimation(myFadeInAnimation);
            }
        }
    }

    public int getLastNotificationId() {
        return localNotifications.get(localNotifications.size() - 2).getNotificationid();
    }

    public void changeLoadingState(boolean isLoading) {
        if (isLoading) {
            localNotifications.add(null);
            notifyItemInserted(localNotifications.size() - 1);
        } else {
            if (localNotifications.get(localNotifications.size() - 1) == null) {
                localNotifications.remove(localNotifications.size() - 1);
                notifyItemRemoved(localNotifications.size());
            }
        }
    }

    public void uploadNotifications(List<ModelNotification> notifications) {
        changeLoadingState(false);
        for (int i = 0; i < notifications.size(); i++) {
            this.localNotifications.add(notifications.get(i));
            notifyItemInserted(this.localNotifications.size() - 1);
        }
    }

    public interface AdapterNotificationCallback {
        void markAsRead(int messageId, int position);
    }

}
