package ru.alfabank.alfamir.calendar_event.data.dto

import com.google.gson.annotations.SerializedName

class CalendarEventRaw {

    @SerializedName("Id")
    var id: Int = 0
    @SerializedName("Title")
    var title: String? = null
    @SerializedName("Location")
    var location: String? = null
    @SerializedName("EventDate")
    var startDate: String? = null
    @SerializedName("EndDate")
    var endDate: String? = null
    @SerializedName("Description")
    var description: String? = null
    @SerializedName("PicURL")
    var picURL: String? = null
    @SerializedName("Tapable")
    var tapable: Int = 0
    @SerializedName("IsSliDo")
    var isSliDo: String = ""

}
