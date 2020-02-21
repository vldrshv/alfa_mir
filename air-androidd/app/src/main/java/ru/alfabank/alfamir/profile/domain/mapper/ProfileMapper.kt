package ru.alfabank.alfamir.profile.domain.mapper

import com.google.common.base.Strings
import io.reactivex.functions.Function
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_3
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_7
import ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_MOSCOW
import ru.alfabank.alfamir.profile.data.dto.ProfileRaw
import ru.alfabank.alfamir.profile.data.dto.SubProfile
import ru.alfabank.alfamir.profile.presentation.dto.Profile
import ru.alfabank.alfamir.utility.enums.FormatElement
import ru.alfabank.alfamir.utility.static_utilities.DateConverter
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import java.util.*
import javax.inject.Inject

class ProfileMapper @Inject
constructor() : Function<ProfileRaw, Profile> {

    override fun apply(profileRaw: ProfileRaw): Profile {
        val name = profileRaw.name
        val login = profileRaw.login
        val email = profileRaw.email
        val position = profileRaw.position
        val department = profileRaw.department
        val fullAddress = profileRaw.fullAdress
        val shortAddress = profileRaw.shortAdress
        val workSpaceAddress = profileRaw.workSpaceAdress
        val city = profileRaw.city
        val birthday = profileRaw.birthday
        val vacationUnformatted = profileRaw.vacation
        val aboutMe = profileRaw.aboutMe
        val expertise = profileRaw.expertise
        var picUrl = profileRaw.picUrl
        val timezone = profileRaw.timezone
        val firstName = profileRaw.firstName
        val lastName = profileRaw.lastName
        val middleName = profileRaw.middleName
        val isFavoured = profileRaw.isFavoured
        val isLiked = profileRaw.isLiked
        val likes = profileRaw.likes
        val isPhoneFormatted = profileRaw.isPhoneFormatted
        val notFormattedWorkPhone = profileRaw.workPhone
        val notFormattedMobilePhone = profileRaw.mobilePhone
        val administrativeManager = profileRaw.administrativeManager
        val functionalManager = profileRaw.functionalManager
        val assistants: List<SubProfile> = profileRaw.assistants?.asList() ?: arrayListOf()

        picUrl = LinkHandler.getPhotoLink(picUrl)

        val localTime: String
        localTime = if (Strings.isNullOrEmpty(timezone)) {
            ""
        } else {
            DateConverter.getCurrentTimeWithIntTimeZone(Integer.parseInt(timezone!!), DATE_PATTERN_7)
        }

        val vacation: String?
        vacation = if (Strings.isNullOrEmpty(vacationUnformatted)) {
            ""
        } else {
            getVacationFormatted(vacationUnformatted!!)
        }

        val mobilePhone: List<String>
        mobilePhone = if (Strings.isNullOrEmpty(notFormattedMobilePhone)) {
            ArrayList()
        } else {
            getMultiplePhoneNumbers(notFormattedMobilePhone!!)
        }

        val workPhone: List<String>
        workPhone = if (Strings.isNullOrEmpty(notFormattedWorkPhone)) {
            ArrayList()
        } else {
            getMultiplePhoneNumbers(notFormattedWorkPhone!!)
        }

        return Profile.Builder()
                .name(name)
                .login(login)
                .email(email)
                .administrativeManager(administrativeManager)
                .functionalManager(functionalManager)
                .position(position)
                .department(department)
                .workPhone(workPhone)
                .mobilePhone(mobilePhone)
                .fullAddress(fullAddress)
                .shortAddress(shortAddress)
                .workSpaceAddress(workSpaceAddress)
                .city(city)
                .birthday(birthday)
                .vacation(vacation)
                .aboutMe(aboutMe)
                .expertise(expertise)
                .picUrl(picUrl)
                .localTime(localTime)
                .isLiked(isLiked)
                .likes(likes)
                .isPhoneFormatted(isPhoneFormatted)
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .isFavoured(isFavoured)
                .assistants(assistants)
                .build()
    }

    private fun getMultiplePhoneNumbers(notFormattedPhone: String): List<String> {
        val phones = notFormattedPhone.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return ArrayList(Arrays.asList(*phones))
    }

    private fun getVacationFormatted(unformattedVacation: String): String? { // TODO refactor
        val dates = unformattedVacation.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (dates.isEmpty()) return null

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
}