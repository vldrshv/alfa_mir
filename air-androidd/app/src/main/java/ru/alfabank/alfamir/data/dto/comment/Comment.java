package ru.alfabank.alfamir.data.dto.comment;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;

/**
 * Created by U_M0WY5 on 23.04.2018.
 */

public class Comment implements Comparable<Comment>, CommentInterface{

    @SerializedName("bodytext")
    private String bodytext;
    @SerializedName("commentid")
    private String commentid;
    @SerializedName("commentParentid")
    private String commentParentid;
    @SerializedName("createtime")
    private String createtime;
    @SerializedName("updatedtime")
    private String updatedtime;
    @SerializedName("likecount")
    private int likecount;
    @SerializedName("currentuserlike")
    private int currentuserlike;
    @SerializedName("candelete")
    private int candelete;
    @SerializedName("author")
    private Author author;

    public String getBodytext() {
        return bodytext;
    }

    public void setBodytext(String bodytext) {
        this.bodytext = bodytext;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getCommentParentid() {
        return commentParentid;
    }

    public void setCommentParentid(String commentParentid) {
        this.commentParentid = commentParentid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getCurrentuserlike() {
        return currentuserlike;
    }

    public void setCurrentuserlike(int currentuserlike) {
        this.currentuserlike = currentuserlike;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getCandelete() {
        return candelete;
    }

    public void setCandelete(int candelete) {
        this.candelete = candelete;
    }

    @Override
    public int compareTo(@NonNull Comment o) {
        DateConverter dateConverter = new DateConverter();
        return dateConverter.convertToMillis(createtime, DATE_PATTERN_0, TIME_ZONE_GREENWICH).compareTo(dateConverter.convertToMillis(o.createtime, DATE_PATTERN_0, TIME_ZONE_GREENWICH));
    }

    public class ResponseComments {
        @SerializedName("comments")
        public List<Comment> commentsItems;

        public List<Comment> getComments() {
            return commentsItems;
        }
    }

}
