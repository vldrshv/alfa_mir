package ru.alfabank.alfamir.post.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.dto.old_trash.models.news_block.TextBlocks;
import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;
import ru.alfabank.alfamir.utility.enums.FormatElement;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_4;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;

public class PostRaw implements PostInterface, FeedElement {

    private static final int VIEW_TYPE = Constants.Feed_element.FEED_POST;

    @SerializedName("newsid")
    String id;
    @SerializedName("newstitle")
    String title;
    @SerializedName("pubtime")
    String pubTime;
    @SerializedName("updatedtime")
    String updTime;
    @SerializedName("likecount")
    int likes;
    @SerializedName("currentuserlike")
    int currentUserLike;
    @SerializedName("commentscount")
    int commentsCount;
    @SerializedName("bodyhtml")
    String body;

//    @SerializedName("rubrictitle") // it is currently in the API, but empty and for some reason not yet deleted;
//    String headingTitle;
//    @SerializedName("rubricid")
//    String headingId;

     @SerializedName("mainimgbase64")
    String imageUrl;
    @SerializedName("tags")
    String tags;
    @SerializedName("posturl")
    String url;
    @SerializedName("threadid")
    String threadId;
    @SerializedName("contenttype")
    String type;
    @SerializedName("cancomment")
    int commentable;
    @SerializedName("sitetitle")
    String headingTitle;
    @SerializedName("candelete")
    int deletable;
    @SerializedName("isfavorite") // apparently, doesn't work for some god damn reason
    int isFavorite;
    @SerializedName("imageurls")
    String imageUrls;
    @SerializedName("androidricharray")
    List<TextBlock> textBlocks;
    @SerializedName("issubscribed")
    int isSubscribed;
    @SerializedName("video_info")
    Video video_info;
    @SerializedName("author")
    ModelAuthor author;
    @SerializedName("card_view_text")
    String shortText;
    @SerializedName("page_objects_array")
    InteractionElement interactionElement;
    @SerializedName("content_objects_array")
    ContentElement [] contentElements;
    @SerializedName("show_settings")
    Options options;

    @SerializedName("json")
    JsonPage json;

    public JsonPage getJson() {
        return json;
    }
    public void setJson(JsonPage json) {
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPubTime() {
        return pubTime;
    }

    public String getDate(){
        return DateConverter.formatDate(pubTime,
                DATE_PATTERN_0, DATE_PATTERN_4, TIME_ZONE_GREENWICH, FormatElement.POST);
    }

    public String getUpdTime() {
        return updTime;
    }

    public int getLikes() {
        return likes;
    }

    @Override
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCurrentUserLike() {
        return currentUserLike;
    }

    @Override
    public void setCurrentUserLike(int status) {
        currentUserLike = status;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getBody() {
        return body;
//        // TODO comment for testing
//        if(textBlocks == null) return "";
//        for (TextBlock textBlock : textBlocks){
//            String type = textBlock.getType();
//            if(type.equals("text")) return HtmlWrapper.convert(textBlock.getContent());
//        }
//        return null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTags() {
        return tags;
    }

    public String getPostUrl() {
        return url;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getType() {
        return type;
    }

    public int isCommentable() {
        return commentable;
    }

    public String getHeadingTitle() {
        return headingTitle;
    }

    public int isDeletable() {
        return deletable;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public List<TextBlocks> getTextBlocks() {

        List<TextBlocks> result = new ArrayList<>();
        if(textBlocks==null) return result;
        for (TextBlock textBlock : textBlocks){
            result.add(textBlock);
        }
        return result;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public Video getVideo_info() {
        return video_info;
    }

    public ModelAuthor getAuthor() {
        return author;
    }

    public String getBodyHtml(){
        return body;
    }

    public InteractionElement getInteractionElement() {
        return interactionElement;
    }

    public ContentElement[] getContentElements() {
        return contentElements;
    }

    public String getShortText() {
        return shortText;
    }

    public Options getOptions() {
        return options;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }
}
