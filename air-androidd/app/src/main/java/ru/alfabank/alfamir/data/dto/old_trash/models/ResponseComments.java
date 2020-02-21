package ru.alfabank.alfamir.data.dto.old_trash.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by U_M0WY5 on 01.08.2017.
 */

public class ResponseComments {
    @SerializedName("comments")
    public List<ModelComment> commentsItems;

    public List<ModelComment> getCommentsItems() {
        return commentsItems;
    }
}
