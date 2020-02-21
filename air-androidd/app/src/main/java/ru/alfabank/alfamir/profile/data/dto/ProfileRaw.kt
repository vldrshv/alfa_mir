package ru.alfabank.alfamir.profile.data.dto

import com.google.gson.annotations.SerializedName

class ProfileRaw {
    @SerializedName("fullname")
    var name: String? = null

    @SerializedName("accountname")
    var login: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("manager")
    var administrativeManager: SubProfile? = null

    @SerializedName("functionalmanager")
    var functionalManager: SubProfile? = null

    @SerializedName("jobtitle")
    var position: String? = null

    @SerializedName("deptitle")
    var department: String? = null

    @SerializedName("workphone")
    var workPhone: String? = null

    @SerializedName("mobilephone")
    var mobilePhone: String? = null

    @SerializedName("address")
    var fullAdress: String? = null

    @SerializedName("office")
    var shortAdress: String? = null

    @SerializedName("fulladdress")
    var workSpaceAdress: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("birthdate")
    var birthday: String? = null

    @SerializedName("absenceinfo")
    var vacation: String? = null

    @SerializedName("aboutme")
    var aboutMe: String? = null

    @SerializedName("skills")
    var expertise: String? = null

    @SerializedName("photobase64")
    var picUrl: String? = null

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("currentuserlike")
    var isLiked: Int = 0

    @SerializedName("likecount")
    var likes: Int = 0

    @SerializedName("phoneverified")
    var isPhoneFormatted: Int = 0

    @SerializedName("firstname")
    var firstName: String? = null

    @SerializedName("lastname")
    var lastName: String? = null

    @SerializedName("middlename")
    var middleName: String? = null

    @SerializedName("infavorite")
    var isFavoured: Int = 0

    @SerializedName("assistants")
    var assistants: Array<SubProfile>? = arrayOf()
}
