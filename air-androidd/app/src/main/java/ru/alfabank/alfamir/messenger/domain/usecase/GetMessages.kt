package ru.alfabank.alfamir.messenger.domain.usecase

import android.annotation.SuppressLint
import android.graphics.Bitmap
import io.reactivex.Observable
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository
import ru.alfabank.alfamir.messenger.domain.mapper.MessageMapper
import ru.alfabank.alfamir.messenger.presentation.dto.Message
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import java.util.*
import javax.inject.Inject

class GetMessages @Inject
internal constructor(private val mMessengerRepository: MessengerRepository,
                     private val mMessageMapper: MessageMapper,
                     private val mLogWrapper: LogWrapper, private val mGetImage: GetImage) {

    private val image: Observable<Bitmap>?
        get() = null

    @SuppressLint("CheckResult")
    fun execute(type: String, chatId: String, messageId: Long, direction: Int): Observable<List<Message>> {
        val amount = 60

        var list: MutableList<MessageRaw> = mutableListOf()

        val messages = mMessengerRepository.loadMoreMessages(type, chatId, messageId, amount, direction)
                .flatMapIterable { messageRawList -> messageRawList }
                .map(mMessageMapper)
                .toList()
                .toObservable()

        return messages
    }

    private fun getNewMessageIds(messageList: List<Message>): List<Long> {
        val msgIds = ArrayList<Long>()
        for (message in messageList) {
            val msgId = message.id
            msgIds.add(msgId)
        }
        return msgIds
    }
}