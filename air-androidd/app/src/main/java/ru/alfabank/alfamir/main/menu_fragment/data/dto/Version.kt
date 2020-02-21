package ru.alfabank.alfamir.main.menu_fragment.data.dto

import com.google.gson.annotations.SerializedName

class Version {
    @SerializedName("AirVersion")
    val versionNumber: String? = null
    @SerializedName("Prompt")
    val commentOnVersionNumber: String? = null
    @SerializedName("WhatsNew")
    val newFeatureDescription: String? = null
    @SerializedName("Description")
    val appDescription: String? = null
    @SerializedName("Build")
    val build: String? = null
    @SerializedName("NeedToUpdate")
    val updateAvailable: Int = 0
}
