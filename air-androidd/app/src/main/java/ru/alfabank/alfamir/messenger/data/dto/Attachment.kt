package ru.alfabank.alfamir.messenger.data.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Attachment() : Parcelable {

    @SerializedName("DataCreation")
    var creationDate: String = ""

    @SerializedName("Extension")
    var extension: String = ""

    @SerializedName("Guid")
    var guid: String = ""

    @SerializedName("Height")
    var height: Int = 0

    @SerializedName("Icon")
    var icon: String = ""

    @SerializedName("Name")
    var name: String = ""

    @SerializedName("Preview")
    var preview: String = ""

    @SerializedName("Type")
    var type: String = ""

    @SerializedName("Width")
    var width: Int = 0

    var encodedValue = ""

    var file: Any? = null

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Attachment> = object : Parcelable.Creator<Attachment> {
            override fun createFromParcel(source: Parcel): Attachment = Attachment(source)
            override fun newArray(size: Int): Array<Attachment?> = arrayOfNulls(size)
        }
    }
}