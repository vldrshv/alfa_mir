package ru.alfabank.alfamir.messenger.domain.usecase

import android.text.format.DateUtils
import ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10
import ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_4
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_FILE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_IMAGE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem
import ru.alfabank.alfamir.messenger.presentation.dto.Message
import ru.alfabank.alfamir.messenger.presentation.dto.MessageTimeBubble
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddTimeBubbleDecorator @Inject
internal constructor(private val mDateFormatter: DateFormatter) {


    fun execute(displayableMessageItems: List<DisplayableMessageItem>, firstMsgId: Long): List<DisplayableMessageItem> {
        val itemsSorted = ArrayList<DisplayableMessageItem>()

        for (i in displayableMessageItems.indices) {
            val item = displayableMessageItems[i]
            val type = item.type
            if (type == MESSAGE_TEXT || type == MESSAGE_FILE || type == MESSAGE_IMAGE) {
                itemsSorted.add(item)
                var message = item as Message
                if (message.id == firstMsgId) { // if it's the first item, add date bubble atop
                    val lDate = message.lDate!!
                    val date = when {
                        DateUtils.isToday(lDate) -> "Сегодня"
                        DateUtils.isToday(lDate + TimeUnit.DAYS.toMillis(1)) -> "Вчера"
                        else -> mDateFormatter.formatDate(lDate, DATE_PATTERN_10, DATE_PATTERN_4)
                    }
                    itemsSorted.add(MessageTimeBubble.Builder()
                            .date(date)
                            .build())
                }

                var nextItem: DisplayableMessageItem?

                for (o in i + 1 until displayableMessageItems.size) { // look for the next msg item
                    nextItem = displayableMessageItems[o]
                    val ntype = nextItem.type
                    if (ntype == MESSAGE_TEXT || ntype == MESSAGE_FILE || ntype == MESSAGE_IMAGE) {
                        message = item
                        val nextMessage = nextItem as Message?
                        val lDate = message.lDate!!
                        val newLDate = nextMessage!!.lDate!!
                        val date = when {
                            DateUtils.isToday(lDate) -> "Сегодня"
                            DateUtils.isToday(lDate + TimeUnit.DAYS.toMillis(1)) -> "Вчера"
                            else -> mDateFormatter.formatDate(lDate, DATE_PATTERN_10, DATE_PATTERN_4)
                        }
                        val newDate = when {
                            DateUtils.isToday(newLDate) -> "Сегодня"
                            DateUtils.isToday(newLDate + TimeUnit.DAYS.toMillis(1)) -> "Вчера"
                            else -> mDateFormatter.formatDate(newLDate, DATE_PATTERN_10, DATE_PATTERN_4)
                        }
                        if (date != newDate) {
                            itemsSorted.add(MessageTimeBubble.Builder()
                                    .date(date)
                                    .build())
                        }
                        break
                    }
                }
            }
        }
        return itemsSorted
    }
}