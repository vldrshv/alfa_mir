package ru.alfabank.alfamir.profile.data.dto

import com.google.gson.annotations.SerializedName

class AdministrativeManagerRaw {
    @SerializedName("id")
    var login: String? = null
    @SerializedName("fullname")
    var name: String? = null
    @SerializedName("photobase64")
    var picUrl: String? = null
    @SerializedName("jobtitle")
    var title: String? = null
}
