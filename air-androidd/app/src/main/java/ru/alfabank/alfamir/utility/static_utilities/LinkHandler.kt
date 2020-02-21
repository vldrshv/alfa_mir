package ru.alfabank.alfamir.utility.static_utilities

import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.Constants.Companion.PHOTO_FORMAT_CUSTOM
import ru.alfabank.alfamir.Constants.Companion.PHOTO_FORMAT_ERROR
import ru.alfabank.alfamir.Constants.Companion.PHOTO_FORMAT_SQUARE
import java.util.*

object LinkHandler {

    @JvmStatic fun getPhotoLink(unformattedLink: String?): String {
        if (unformattedLink != null) {
            val stringArray = unformattedLink.split(";#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return if (stringArray.size != 0) {
                stringArray[0]
            } else {
                unformattedLink
            }
        } else {
            return ""
        }
    }

    fun getPhotoSize(unformattedLink: String): Map<String, Int> {
        val stringArray = unformattedLink.split(";#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val width = Integer.parseInt(stringArray[1])
        val height = Integer.parseInt(stringArray[2])

        val result = HashMap<String, Int>()
        result["width"] = width
        result["height"] = height

        return result
    }

    fun getPhotoPixelHeightMatchingScreenWidth(imageUrl: String): Int {
        val index = imageUrl.indexOf(";#")
        val width: String
        val height: String
        if (index > 0) {
            val parameters = imageUrl.substring(imageUrl.indexOf(";#"), imageUrl.length).split(";#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            width = parameters[1]
            height = parameters[2]
        } else {
            width = "1024"
            height = "1024"
        }

        val w = Integer.parseInt(width)
        val h = Integer.parseInt(height)
        val coef = Constants.Initialization.SCREEN_WIDTH_PHYSICAL.toFloat() / w
        return (h * coef).toInt()
    }

    fun isPhotoSquare(unformattedLink: String): Int {
        val stringArray = unformattedLink.split(";#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (stringArray.size == 3) {
            if (stringArray[1] === stringArray[2]) {
                PHOTO_FORMAT_SQUARE
            } else {
                PHOTO_FORMAT_CUSTOM
            }
        } else PHOTO_FORMAT_ERROR
    }

}
