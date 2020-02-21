package ru.alfabank.alfamir.utility.video_player

class TimeUtils {
    val MIN = 60
    val HOUR = 60 * MIN

    fun getHours(timeMills: Int): Int {
        return timeMills / 1000 / 60 / 60
    }
    fun getMinutes(timeMills: Int): Int {
        return (timeMills/1000 - getHours(timeMills) * HOUR) / 60
    }
    fun getSeconds(timeMills: Int): Int {
        return (timeMills / 1000 - getHours(timeMills)*HOUR - getMinutes(timeMills)*MIN)
    }
    fun getMinSec(timeMills: Int): String {
//        if (timeMills < 0)
//            return "00:00"

        val min = getMinutes(timeMills)
        val sec = getSeconds(timeMills)

        val minS = if (min < 10) "0${min}" else "$min"
        val secS = if (sec < 10) "0${sec}" else "$sec"

        return "$minS:$secS"
    }
}