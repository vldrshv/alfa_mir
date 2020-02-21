package ru.alfabank.alfamir.data.dto.push_notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 20.04.2018.
 */

public class NotificationComment {
    @SerializedName("type")
    String type;
    @SerializedName("commentid")
    String commentId;
    @SerializedName("postid")
    String postId;
    @SerializedName("posturl")
    String postUrl;

    public String getType() {
        return type;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostUrl() {
        return postUrl;
    }
}
