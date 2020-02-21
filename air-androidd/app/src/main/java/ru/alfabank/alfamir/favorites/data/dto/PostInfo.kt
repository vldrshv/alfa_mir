package ru.alfabank.alfamir.favorites.data.dto

import com.google.gson.annotations.SerializedName

data class PostInfo(
        @SerializedName("iscurrentlike") var isCurrentLike: Int = 0,
        @SerializedName("commentscount") var commentsCount: Int = 0,
        @SerializedName("likescount") var likesCount: Int = 0,
        @SerializedName("isfavorite") var isFavorite: Int = 0,
        @SerializedName("postid") var postId: String = ""
)