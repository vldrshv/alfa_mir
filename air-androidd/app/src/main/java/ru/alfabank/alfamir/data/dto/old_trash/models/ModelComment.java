package ru.alfabank.alfamir.data.dto.old_trash.models;

import androidx.annotation.NonNull;

import ru.alfabank.alfamir.data.dto.comment.CommentInterface;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;

/**
 * Created by U_M0WY5 on 01.08.2017.
 */

public class ModelComment implements Comparable<ModelComment>, CommentInterface {
    private String bodytext;
    private String commentid;
    private String commentParentid;
    private String createtime;
    private String updatedtime;
    private int likecount;
    private int currentuserlike;
    private int candelete;
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
    public int compareTo(@NonNull ModelComment o) {
        DateConverter dateConverter = new DateConverter();
        return dateConverter.convertToMillis(createtime, DATE_PATTERN_0, TIME_ZONE_GREENWICH).
                compareTo(dateConverter.convertToMillis(o.createtime, DATE_PATTERN_0, TIME_ZONE_GREENWICH));
    }
}
