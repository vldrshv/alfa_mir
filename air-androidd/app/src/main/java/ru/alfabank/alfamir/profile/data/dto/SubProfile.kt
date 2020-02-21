package ru.alfabank.alfamir.profile.data.dto

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

class SubProfile {
    @SerializedName("id")
    var login: String? = null

    @SerializedName("fullname")
    var name: String? = null

    @SerializedName("photobase64")
    var picUrl: String? = null

    @SerializedName("jobtitle")
    var title: String? = null

    var photo: Bitmap? = null
    var background: Drawable? = null

    override fun equals(other: Any?): Boolean {
        if (other is SubProfile) {
            return this.login == other.login
        }
        return false
    }

    override fun hashCode(): Int {
        return (this.login + this.name).toByteArray().contentHashCode()
    }
}