package ru.alfabank.alfamir.poster.data.dto

import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("Login")
    var login: String? = null

    @SerializedName("EventId")
    var eventId: Int = 0

    @SerializedName("ConnectDate")
    var connectDate: String? = null

    @SerializedName("FullName")
    var fullName: String? = null

    @SerializedName("JobTitle")
    var jobTitle: String? = null

    @SerializedName("ProfilPicURL")
    var profilePicUrl: String? = null

    @SerializedName("SessionId")
    var sessionId: String? = null

}
