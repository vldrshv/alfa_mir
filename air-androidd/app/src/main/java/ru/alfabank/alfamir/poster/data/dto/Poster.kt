package ru.alfabank.alfamir.poster.data.dto

import com.google.gson.annotations.SerializedName
import ru.alfabank.alfamir.R

class Poster {

    @SerializedName("Id")
    var id: Int = 0

    @SerializedName("Title")
    var title: String? = null

    @SerializedName("EventStartDate")
    var eventStartDate: String? = null

    @SerializedName("EventEndTime")
    var eventEndTime: String? = null

    @SerializedName("Description")
    var description: String? = null

    @SerializedName("BackgroundImgUrl")
    var backgroundImgUrl: String? = null

    @SerializedName("VideoUrl")
    var videoUrl: String? = null

    @SerializedName("DescForPresentation")
    var descForPresentation: String? = null

    @SerializedName("isAdmin")
    var isAdmin: String? = null

    @SerializedName("Questions")
    var questions: List<Question>? = null

    @SerializedName("Admins")
    var admins: String? = null

    @SerializedName("Moderation")
    var moderation: String? = null

    @SerializedName("Location")
    var location: String? = null

    @SerializedName("IsGettingQuestion")
    var isGettingQuestion: String? = null

    @SerializedName("IsSliDo")
    var isSliDo: String = ""

    @SerializedName("ImagesList")
    var imageList: List<Image> = arrayListOf()

    var sort: Int = R.string.sort_type_popular
}

class Image {

    @SerializedName("ID")
    var id: String = ""

    @SerializedName("Height")
    var height: Int = 0

    @SerializedName("Width")
    var width: Int = 0

    @SerializedName("Url")
    var url: String = ""
}