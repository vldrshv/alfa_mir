package ru.alfabank.alfamir.utility.static_utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.alfabank.alfamir.alfa_tv.data.dto.ShowCurrentState;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowUriRaw;
import ru.alfabank.alfamir.calendar_event.data.dto.CalendarEventRaw;
import ru.alfabank.alfamir.data.dto.FavoritePerson;
import ru.alfabank.alfamir.data.dto.Profile;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.data.dto.comment.Comment;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.data.dto.old_trash.models.FeedHeader;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelCommunityType;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelFavorite;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelNewNotificationsCount;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelPhoto;
import ru.alfabank.alfamir.data.dto.old_trash.models.ResponsePicUpload;
import ru.alfabank.alfamir.data.dto.old_trash.models.ResponseSearch;
import ru.alfabank.alfamir.data.dto.old_trash.models.SubscriptionModel;
import ru.alfabank.alfamir.data.dto.push_notification.NotificationComment;
import ru.alfabank.alfamir.data.dto.push_notification.NotificationPost;
import ru.alfabank.alfamir.data.dto.push_notification.NotificationProfile;
import ru.alfabank.alfamir.data.dto.push_notification.NotificationSurvey;
import ru.alfabank.alfamir.data.dto.video.VideoUrlRaw;
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw;
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw;
import ru.alfabank.alfamir.favorites.data.dto.PostInfo;
import ru.alfabank.alfamir.feed.data.dto.FeedWithHeader;
import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;
import ru.alfabank.alfamir.feed_new.data.dto.FeedRaw;
import ru.alfabank.alfamir.image.data.dto.EncodedImageRaw;
import ru.alfabank.alfamir.initialization.data.dto.ShortUserInfoRaw;
import ru.alfabank.alfamir.main.home_fragment.data.dto.TopNewsRaw;
import ru.alfabank.alfamir.main.menu_fragment.data.dto.Version;
import ru.alfabank.alfamir.messenger.data.dto.ChatLightRaw;
import ru.alfabank.alfamir.messenger.data.dto.ChatRaw;
import ru.alfabank.alfamir.messenger.data.dto.ImageAttachment;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.PollDataRaw;
import ru.alfabank.alfamir.messenger.data.serialization.PollDataAdapter;
import ru.alfabank.alfamir.notification.data.dto.ModelNotification;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;
import ru.alfabank.alfamir.post.data.dto.PostInterface;
import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.poster.data.dto.Poster;
import ru.alfabank.alfamir.profile.data.dto.ProfileWrapper;
import ru.alfabank.alfamir.profile.data.dto.UserLikeStatusRaw;
import ru.alfabank.alfamir.search.data.dto.SearchResultRaw;
import ru.alfabank.alfamir.service.dto.ChatMessage;
import ru.alfabank.alfamir.survey.data.dto.Survey;
import ru.alfabank.alfamir.survey.data.dto.SurveyCover;
import ru.alfabank.alfamir.utility.lifecycle.AppSettings;
import ru.alfabank.alfamir.utility.serializer.InterfaceAdapter;

public class JsonWrapper {
    private String json;
    private static Gson gson = new Gson();

    public JsonWrapper(String json) {
        this.json = json;
    }

    public static AppSettings getSettings(String json) {
        try {
            return gson.fromJson(json, AppSettings.class);
        }catch (Exception e){
            Log.e("JsonWrapper","error decoding settings json: " + e.getMessage());
            return null;
        }
    }

    public static MessageRaw getMessage(String json) {
        try {
            return gson.fromJson(json, MessageRaw.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServPublicKey(String json) {
        try {
            return JsonParser.parseString(json).getAsJsonObject().get("publickey").getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    public static ChatMessage getChatMessage(String json) {
        try {
            return gson.fromJson(json, ChatMessage.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ProfileWrapper getProfileWrapperRaw(String json) {
        try {
            return gson.fromJson(json, ProfileWrapper.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<ChatLightRaw> getChatLightList(String json) {
        try {
            ChatLightRaw[] chatLightRaw = gson.fromJson(json, ChatLightRaw[].class);
            return Arrays.asList(chatLightRaw);
        } catch (Exception e) {
            return null;
        }
    }

    public static Version getVersion(String json) {
        try {
            return gson.fromJson(json, Version.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<CalendarEventRaw> calendarEventList(String json) {
        try {
            CalendarEventRaw[] calendarEvents = gson.fromJson(json, CalendarEventRaw[].class);
            return Arrays.asList(calendarEvents);
        } catch (Exception e) {
            return null;
        }
    }

    public static Poster posterList(String json) {
        try {
            return gson.fromJson(json, Poster.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<TopNewsRaw> topNews(String json) {
        try {
            TopNewsRaw[] topNewsRaws = gson.fromJson(json, TopNewsRaw[].class);
            return Arrays.asList(topNewsRaws);
        } catch (Exception e) {
            return null;
        }
    }

    public static SearchResultRaw getSearchResultRaw(String json) {
        try {
            return gson.fromJson(json, SearchResultRaw.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<PollDataRaw> getPollDataRaw(String json) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(PollDataRaw.class,
                    new PollDataAdapter()).create();
            PollDataRaw[] pollDataRaw = gson.fromJson(json, PollDataRaw[].class);
            return Arrays.asList(pollDataRaw);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<ShowRaw> getShows(String json) {
        try {
            ShowRaw[] pollDataRaw = gson.fromJson(json, ShowRaw[].class);
            return Arrays.asList(pollDataRaw);
        } catch (Exception e) {
            return null;
        }
    }

    public static EncodedImageRaw getEncodedImageRaw(String json) {
        try {
            return gson.fromJson(json, EncodedImageRaw.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ChatRaw getChatRaw(String json) {
        try {
            return gson.fromJson(json, ChatRaw.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageAttachment getImageAttachment(String json) {
        try {
            return gson.fromJson(json, ImageAttachment.class);
        } catch (Exception e) {
            Log.e("Chat", "getImageAttachment Error: " + e.getMessage() + json);
            return null;
        }
    }

    public static NotificationComment getNotificationComment(String json) {
        try {
            return gson.fromJson(json, NotificationComment.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ShortUserInfoRaw getShortUserInfoRaw(String json) {
        try {
            return gson.fromJson(json, ShortUserInfoRaw.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ShowCurrentState getVideoCurrentState(String json) {
        try {
            return gson.fromJson(json, ShowCurrentState.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static VideoUrlRaw getVideoUrlDeserialize(String json) {
        try {
            return gson.fromJson(json, VideoUrlRaw.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ShowUriRaw getShowUrlDeserialize(String json) {
        try {
            return gson.fromJson(json, ShowUriRaw.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<FavoriteProfileRaw> getFavoritePeopleList(String json) {
        try {
            FavoriteProfileRaw[] favoriteProfileRawArray = gson.fromJson(json, FavoriteProfileRaw[].class);
            return Arrays.asList(favoriteProfileRawArray);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<FavoritePostRaw> getFavoritePostList(String json) {
        try {
            FavoritePostRaw[] favoritePostRaw = gson.fromJson(json, FavoritePostRaw[].class);
            return Arrays.asList(favoritePostRaw);
        } catch (Exception e) {
            return null;
        }
    }

    public static PostInfo getPostInfo(String json) {
        try {
            return gson.fromJson(json, PostInfo[].class)[0];
        } catch (Exception e) {
            e.printStackTrace();
            return new PostInfo();
        }
    }

    public static PostRaw getPostDeserialize(String json) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(PostInterface.ContentElement.class,
                    new InterfaceAdapter<PostInterface.ContentElement>()).create();
            ResponsePosts posts = gson.fromJson(json, ResponsePosts.class);
            List<PostRaw> postsArray = posts.getPosts();
            if (postsArray.isEmpty()) {
                return null;
            } else {
                return postsArray.get(0);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static FeedRaw getFeed(String json) {
        try {
            return gson.fromJson(json, FeedRaw.class);

        } catch (Exception e) {
            return null;
        }
    }

    public static FeedWithHeader getFeedWithHeader(String json) {
        try {
            return gson.fromJson(json, FeedWithHeader.class);

        } catch (Exception e) {
            return null;
        }
    }

    public static List<FeedElement> getPosts(String json) {
        try {
            ResponsePosts posts = gson.fromJson(json, ResponsePosts.class);
            List<PostRaw> postsArray = posts.getPosts();

            List<FeedElement> result = new ArrayList<>(postsArray);
            if (postsArray.isEmpty()) {
                return null;
            } else {
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Survey getQuiz(String json) {
        try {
            return gson.fromJson(json, Survey.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static SurveyCover getQuizCover(String json) {
        try {
            SurveyCover[] quizCover = gson.fromJson(json, SurveyCover[].class);
            if (quizCover.length == 0) return null;
            return quizCover[0];
        } catch (Exception e) {
            return null;
        }
    }

    public static NotificationProfile getNotificationProfile(String json) {
        try {
            return gson.fromJson(json, NotificationProfile.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static NotificationPost getNotificationPost(String json) {
        try {
            return gson.fromJson(json, NotificationPost.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static NotificationSurvey getNotificationSurvey(String json) {
        try {
            return gson.fromJson(json, NotificationSurvey.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Comment> getComments(String json) {
        try {
            Comment.ResponseComments response = gson.fromJson(json, Comment.ResponseComments.class);
            List<Comment> commentsItems = response.getComments();
            Collections.sort(commentsItems);
            return sortSecondLvlComment2(commentsItems);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<NotificationRaw> getNotificationRawList(String json) {
        try {
            NotificationRaw[] notificationsSettings = gson.fromJson(json, NotificationRaw[].class);
            return new ArrayList<>(Arrays.asList(notificationsSettings));
        } catch (Exception e) {
            return null;
        }
    }

    public static NotificationsSettings getNotificationSettings(String json) {
        try {
            return gson.fromJson(json, NotificationsSettings.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ModelFavorite> getFavorites() {
        gson = new Gson();
        try {
            ModelFavorite[] a = gson.fromJson(json, ModelFavorite[].class);
            List<ModelFavorite> favoriteItems = new ArrayList<>(Arrays.asList(a));
            Collections.sort(favoriteItems);
            return favoriteItems;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<FavoritePerson> getFavouritePersons() {
        gson = new Gson();
        try {
            FavoritePerson[] a = gson.fromJson(json, FavoritePerson[].class);
            List<FavoritePerson> favoriteItems = new ArrayList<>(Arrays.asList(a));
            Collections.sort(favoriteItems);
            return favoriteItems;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public ResponseSearch getSearchResults() {
        gson = new Gson();
        return gson.fromJson(json, ResponseSearch.class);
    }

    public List<SubscriptionModel> getSubscriptions() {
        gson = new Gson();
        try {
            SubscriptionModel[] subscriptionArray = gson.fromJson(json, SubscriptionModel[].class);
            return new ArrayList<>(Arrays.asList(subscriptionArray));
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public ModelPhoto getPhoto() {
        return gson.fromJson(json, ModelPhoto.class);
    }

    public List<ShortProfile> getProfiles() {
        try {
            ShortProfile[] profilesArray = gson.fromJson(json, ShortProfile[].class);
            return new ArrayList<>(Arrays.asList(profilesArray));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Profile getProfile() {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, Profile.ProfileWrapper.class).getProfile();
        } catch (Exception e) {
            return null;
        }
    }

    public static UserLikeStatusRaw getUserLikeStatusRaw(String json) {

        try {
            return gson.fromJson(json, UserLikeStatusRaw.class);
        } catch (Exception e) {
            return null;
        }

    }

    public List<ModelNotification> getNotifications() {
        gson = new Gson();

        try {
            ModelNotification[] notificationArray = gson.fromJson(json, ModelNotification[].class);
            return new ArrayList<>(Arrays.asList(notificationArray));
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public int getNewNotificationsCount() {
        gson = new Gson();

        try {
            ModelNewNotificationsCount newNotificationsCount = gson.fromJson(json, ModelNewNotificationsCount.class);
            return newNotificationsCount.getNotycount();
        } catch (Exception e) {
            return 0;
        }

    }

    public List<ModelCommunityType> getCommunityTypes() {

        ModelCommunityType[] response = gson.fromJson(json, ModelCommunityType[].class);
        return new ArrayList<>(Arrays.asList(response));
    }

    public String getFileUrl() {
        try {
            ResponsePicUpload response = gson.fromJson(json, ResponsePicUpload.class);
            return response.url;
        } catch (Exception e) {
            return null;
        }
    }

    public static ImageAttachment getFile(String json){
        try{
            return gson.fromJson(json, ImageAttachment.class);
        }
        catch (Exception e){
            Log.e("GetFile", e.getMessage());
            return new ImageAttachment();
        }
    }

    private static List<Comment> sortSecondLvlComment2(List<Comment> unsorted) {
        List<Comment> sorted = new ArrayList<>(unsorted);
        String parrentId;
        boolean found;
        for (int i = unsorted.size() - 1; i > 0; i--) { // go through all list of comments, in the search of comments with parentID
            if (!unsorted.get(i).getCommentParentid().equals("0")) { // if a comment with parent id is found
                parrentId = unsorted.get(i).getCommentParentid();
                found = false;
                int fathderId;
                int childId = 0;
                for (int o = i - 1; o >= 0; o--) { // check if the parent is in the list
                    if (unsorted.get(o).getCommentid().equals(parrentId)) { // if the parent is found in the initial list
                        fathderId = -1;
                        childId = -1;
                        for (int j = sorted.size() - 1; j >= 0; j--) { // find the same pare in the updated list
                            if (sorted.get(j).getCommentid().equals(unsorted.get(o).getCommentid())) { // parent
                                fathderId = j;
                                found = true;
                            } else if (sorted.get(j).getCommentid().equals(unsorted.get(i).getCommentid())) { // child
                                childId = j;
                            }
                        }

                        if (fathderId != -1 && childId != -1) { // if a pare is present, then change the position of child element
                            sorted.add(fathderId + 1, sorted.get(childId));
                            sorted.remove(childId + 1);
                        }
                    }
                }
                if (!found && childId != -1) { // if the parent id is not found, then it is a bug and the child comment without parent should be deleted
                    for (int g = sorted.size() - 1; g > 0; g--) {
                        if (sorted.get(g).getCommentid().equals(unsorted.get(i).getCommentid())) { // child
                            sorted.remove(g);
                        }
                    }
                }
            }
        }
        return sorted;
    }

    public class ResponsePosts {

        @SerializedName("header")
        public FeedHeader header;

        @SerializedName("news")
        List<PostRaw> posts;

        public FeedHeader getHeader() {
            return header;
        }

        List<PostRaw> getPosts() {
            for (PostRaw r : posts)
                if (r.getJson() != null)
                    System.out.println(r);
            return posts;
        }
    }

}
