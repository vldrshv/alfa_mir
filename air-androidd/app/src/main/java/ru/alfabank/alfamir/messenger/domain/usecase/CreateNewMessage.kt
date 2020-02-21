package ru.alfabank.alfamir.messenger.domain.usecase

import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_7
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.messenger.data.dto.Attachment
import ru.alfabank.alfamir.messenger.presentation.dto.Message
import ru.alfabank.alfamir.messenger.presentation.dto.Status
import ru.alfabank.alfamir.messenger.presentation.dto.User
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter
import javax.inject.Inject

class CreateNewMessage @Inject
internal constructor(private val mDateFormatter: DateFormatter) {

//    fun execute(message: String, chatId: String): Message {
//        return createMessage(message, chatId, null)
//    }

    fun createMessage(message: String, chatId: String, attachments: List<Attachment>): Message {

        val status = Status.Builder().userLogin(USER_LOGIN)
                .state("pending")
                .chatId(chatId)
                .msgId(0)
                .build()

        val currentUser = User.Builder()
                .id(USER_LOGIN)
                .build()

        val builder = Message.Builder()
                .text(message)
                .date(mDateFormatter.getCurrentLocalTime(DATE_PATTERN_7))
                .lDate(mDateFormatter.currentUtcTime)
                .chadId(chatId)
                .senderUser(currentUser)
                .status(status)

        if (!attachments.isNullOrEmpty())
            builder.attachments(attachments)

        return builder.build()
    }
}