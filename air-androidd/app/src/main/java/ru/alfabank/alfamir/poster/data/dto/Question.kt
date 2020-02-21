package ru.alfabank.alfamir.poster.data.dto

import com.google.gson.annotations.SerializedName

class Question {

    @SerializedName("Id")
    var id: Int = 0

    @SerializedName("EventId")
    var eventId: Int = 0

    @SerializedName("Body")
    var body: String? = null

    @SerializedName("LikeCount")
    var likeCount: Int = 0

    @SerializedName("IsSetLike")
    var isSetLike: Int = 0

    @SerializedName("User")
    var user: User = User()

    @SerializedName("Date")
    var date: String? = null

    @SerializedName("IsAnonymous")
    var isAnonymous: Int = 0

    @SerializedName("IsHidden")
    var isHidden: Int = 0

    @SerializedName("IsAnswered")
    var isAnswered: Int = 0

    @SerializedName("IsPin")
    var isPin: Int = 0

    @SerializedName("IsApproved")
    var isApproved: Int = 0

}