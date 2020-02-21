package ru.alfabank.alfamir.data.dto.old_trash.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.post.data.dto.PostInterface;
import ru.alfabank.alfamir.data.dto.old_trash.models.news_block.TextBlocks;

/**
 * Created by U_M0WY5 on 14.11.2017.
 */

public class AbstractModelNews implements PostInterface {

    @SerializedName("newsid")
    private String id;

    @SerializedName("newstitle")
    private String title;

    @SerializedName("pubtime")
    private String time;

    private String updatedtime;

    private int likecount;

    private int currentuserlike;

    private int commentscount;

    private String bodyhtml;

    private String rubrictitle;

    private String rubricid;

    private String mainimgbase64;

    private String tags;

    private ModelAuthor author;

    private String posturl;

    private String threadid;

    private String contenttype;

    private int cancomment;

    private int candelete;

    private String sitetitle;

    @SerializedName("androidricharray")
    List<TextBlock> textBlocks;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPubTime() {
        return time;
    }

    public String getUpdTime() {
        return updatedtime;
    }

    public int getLikes() {
        return likecount;
    }

    @Override
    public void setLikes(int likes) {
        likecount = likes;
    }

    public int getCurrentUserLike() {
        return currentuserlike;
    }

    @Override
    public void setCurrentUserLike(int status) {
        currentuserlike = status;
    }

    public int getCommentsCount() {
        return commentscount;
    }

    public String getBody() {
        return bodyhtml;
    }

    public String getImageUrl() {
        return mainimgbase64;
    }

    public String getTags() {
        return tags;
    }

    public String getPostUrl() {
        return posturl;
    }

    public String getThreadId() {
        return threadid;
    }

    public String getType() {
        return contenttype;
    }

    public int isCommentable() {
        return cancomment;
    }

    public String getHeadingTitle() {
        return sitetitle;
    }

    public int isDeletable() {
        return candelete;
    }

    public int getIsFavorite() {
        return 0;
    }

    public String getImageUrls() {
        return null;
    }

    public List<TextBlocks> getTextBlocks() {
        List<TextBlocks> result = new ArrayList<>();
        if(textBlocks==null){ return result; }

        for (TextBlock textBlock : textBlocks){
            result.add(textBlock);
        }

        return result;
    }

    public int getIsSubscribed() {
        return 0;
    }

    public Video getVideo_info() {
        return null;
    }

    public ModelAuthor getAuthor() {
        return author;
    }

}
