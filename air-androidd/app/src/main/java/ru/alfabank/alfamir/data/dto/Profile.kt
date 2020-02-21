package ru.alfabank.alfamir.data.dto

import com.google.common.base.Strings
import com.google.gson.annotations.SerializedName
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_3
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_7
import ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_MOSCOW
import ru.alfabank.alfamir.utility.enums.FormatElement
import ru.alfabank.alfamir.utility.static_utilities.DateConverter
import java.util.*

/**
 * Created by U_M0WY5 on 15.02.2018.
 */

class Profile {
    @SerializedName("fullname")
    var name: String? = null

    @SerializedName("accountname")
    internal var id: String? = null
    @SerializedName("email")
    var email: String? = null

    @SerializedName("manager")
    var manager: Manager? = null

    @SerializedName("functionalmanager")
    var functionalManager: FunctionalManager? = null

    @SerializedName("jobtitle")
    var title: String? = null

    @SerializedName("deptitle")
    var department: String? = null

    @SerializedName("workphone")
    internal var workPhone: String? = null
    @SerializedName("mobilephone")
    internal var mobilePhone: String? = null
    @SerializedName("address")
    var physicalAddress: String? = null

    @SerializedName("office")
    var workSpaceShort: String? = null

    @SerializedName("fulladdress")
    var workSpaceFull: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("birthdate")
    var birthday: String? = null

    @SerializedName("absenceinfo")
    internal var vacation: String? = null
    @SerializedName("aboutme")
    var aboutMe: String? = null

    @SerializedName("skills")
    var expertise: String? = null

    @SerializedName("photobase64")
    var imageUrl: String? = null

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("currentuserlike")
    var isLiked: Int = 0 // transform into boolean?

    @SerializedName("likecount")
    var likes: Int = 0
    @SerializedName("phoneverified")
    var isPhoneFormatted: String? = null

    @SerializedName("firstname")
    var firstName: String? = null

    @SerializedName("lastname")
    var lastName: String? = null

    @SerializedName("middlename")
    var middleName: String? = null


    internal var mWorkPhoneNumbers: List<String>? = null
    internal var mMobilePhoneNumbers: List<String>? = null

    val workPhoneOld: String?
        @Deprecated("")
        get() {
            val phones = checkForExtra(workPhone)
            mWorkPhoneNumbers = phones
            return if (phones.size != 0) {
                phones[0]
            } else workPhone
        }

    val mobilePhoneOld: String?
        @Deprecated("")
        get() {
            val phones = checkForExtra(mobilePhone)
            mMobilePhoneNumbers = phones
            return if (phones.size != 0) {
                phones[0]
            } else mobilePhone
        }

    val workPhoneNumbers: List<String>?
        get() {
            if (mWorkPhoneNumbers == null) {
                mWorkPhoneNumbers = checkForExtra(workPhone)
            }
            return mWorkPhoneNumbers
        }

    val mobilePhoneNumbers: List<String>?
        get() {
            if (mMobilePhoneNumbers == null) {
                mMobilePhoneNumbers = checkForExtra(mobilePhone)
            }
            return mMobilePhoneNumbers
        }

    val delegate: String?
        get() {
            if (Strings.isNullOrEmpty(vacation)) {
                return null
            } else {
                val dates = vacation!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (dates.size == 0) return null
                if (dates.size == 2) return null
                if (dates.size == 3) return dates[2]
            }
            return null
        }

    // TODO kinda lame workaround on exceptions
    // so it would count the last day also
    val isVacationCurrent: Boolean
        get() {
            val dates = vacation!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (dates.size < 2) {
                return false
            }

            val vacationsStart = DateConverter.convertToMillis(dates[0], DATE_PATTERN_0, TIME_ZONE_MOSCOW)
            val oneWeekBeforeVacationsStart = vacationsStart - 1000 * 60 * 60 * 24 * 7
            var vacationEnd = DateConverter.convertToMillis(dates[1], DATE_PATTERN_0, TIME_ZONE_MOSCOW)
            vacationEnd = vacationEnd + 1000 * 60 * 60 * 24
            val currentTime = Date().time

            return currentTime >= oneWeekBeforeVacationsStart && currentTime <= vacationEnd
        }

    val localTime: String?
        get() = if (timezone != null) {
            DateConverter.getCurrentTimeWithIntTimeZone(Integer.parseInt(timezone!!), DATE_PATTERN_7)
        } else
            null

    fun getId(): String? {
        return if (Strings.isNullOrEmpty(id)) {
            null
        } else id!!.toLowerCase()
    }

    fun getVacation(): String? {
        return if (Strings.isNullOrEmpty(vacation)) {
            null
        } else {
            formatVacation(vacation!!)
        }
    }

    private fun formatVacation(vacation: String): String? {
        val dates = vacation.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (dates.size == 0) {
            return null
        }

        val sBuilder = StringBuilder()
        for (i in 0..1) {
            sBuilder.append(DateConverter.formatDate(dates[i], DATE_PATTERN_0,
                    DATE_PATTERN_3, TIME_ZONE_MOSCOW, FormatElement.TV_SHOW_CURRENT_START_DATE))
            if (i == 0) {
                sBuilder.append(" - ")
            }
        }
        return sBuilder.toString()

    }

    inner class ProfileWrapper {
        @SerializedName("userdata")
        val profile: Profile? = null

    }

    inner class Manager {
        @SerializedName("id")
        internal var id: String = ""
        @SerializedName("fullname")
        var name: String = ""

        fun getId(): String {
            return id.toLowerCase()
        }

        fun setId(id: String) {
            this.id = id
        }

    }

    inner class FunctionalManager {
        @SerializedName("id")
        internal var id: String = ""
        @SerializedName("fullname")
        var name: String = ""

        fun getId(): String {
            return id.toLowerCase()
        }

        fun setId(id: String) {
            this.id = id
        }
    }

    private fun checkForExtra(phone: String?): List<String> {
        if (!Strings.isNullOrEmpty(phone)) {
            val phones = phone!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return ArrayList(Arrays.asList(*phones))
        }
        return ArrayList()
    }

}
