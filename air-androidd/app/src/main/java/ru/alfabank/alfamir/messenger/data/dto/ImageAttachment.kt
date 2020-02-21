package ru.alfabank.alfamir.messenger.data.dto

import com.google.gson.annotations.SerializedName

class ImageAttachment : Attachment() {

    @SerializedName("FileBase64")
    var base64: String = ""

    @SerializedName("CodeKey")
    var key: String = ""

    @SerializedName("CodeVector")
    var vector: String = ""

}