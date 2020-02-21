package ru.alfabank.alfamir.utility.lifecycle

import com.google.gson.annotations.SerializedName
import ru.alfabank.alfamir.utility.toBoolean

class AppSettings {

    @SerializedName("isnewyearcome")
    private var isNewYearCome: String = ""

    @SerializedName("AlfaTvOnAir")
    private var alfaTvIsOnAir: String = ""

    @SerializedName("MessengerWork")
    private var isMessengerEnabled: String = ""

    @SerializedName("MessengerLongPullWork")
    private var isMessengerLongPoolEnabled: String = ""

    @SerializedName("AfishaLongPullWork")
    private var isPosterLongPoolEnabled: String = ""

    @SerializedName("LongPullErrorsTimeout")
    private var longPoolTimeout: String = ""

    @SerializedName("LogsItemsCount")
    private var logItemsCount: String = ""

    fun isNewYearCome(): Boolean {
        return isNewYearCome.toBoolean()
    }

    fun isAlfaTvEnabled(): Boolean {
        return alfaTvIsOnAir.toBoolean()
    }

    fun isMessengerEnabled(): Boolean {
        return isMessengerEnabled.toBoolean()
    }

    fun isMessengerLpEnabled(): Boolean {
        return isMessengerLongPoolEnabled.toBoolean()
    }

    fun isPosterLpEnabled(): Boolean {
        return isPosterLongPoolEnabled.toBoolean()
    }

    fun getMessengerLpTimeout(): Int {
        return longPoolTimeout.toInt()
    }

    fun getLogItemsCount(): Int {
        return logItemsCount.toInt()
    }

}