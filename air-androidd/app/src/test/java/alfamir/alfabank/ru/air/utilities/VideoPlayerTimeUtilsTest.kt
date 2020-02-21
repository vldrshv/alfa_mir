package alfamir.alfabank.ru.air.utilities

import org.junit.Test
import ru.alfabank.alfamir.utility.video_player.TimeUtils


class VideoPlayerTimeUtilsTest {
    @Test
    fun convertTime0() {
        val timeInMills = 0
        val timeUtils = TimeUtils()
        val res = "00:00"
        assert(timeUtils.getMinSec(timeInMills) == res)
    }
    @Test
    fun convertNonZeroSec() {
        val timeInMills = 1000
        val timeUtils = TimeUtils()
        val res = "00:01"
        assert(timeUtils.getMinSec(timeInMills) == res)
    }
    @Test
    fun convertNonZeroMinSec() {
        val timeInMills = 251000
        val timeUtils = TimeUtils()
        val res = "04:11"
        assert(timeUtils.getMinSec(timeInMills) == res)
    }
    @Test
    fun convertNonZeroMin() {
        val timeInMills = 600000
        val timeUtils = TimeUtils()
        val res = "10:00"
        assert(timeUtils.getMinSec(timeInMills) == res)
    }
}