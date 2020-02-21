package ru.alfabank.alfamir.messenger.data.source.remote

import io.reactivex.Observable
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.messenger.data.Encryptor
import ru.alfabank.alfamir.messenger.data.dto.*
import ru.alfabank.alfamir.messenger.data.dto.Attachment
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerDataSource
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import javax.inject.Inject

class MessengerRemoteDataSource @Inject constructor(
        private val service: WebService,
        private val mLog: LogWrapper,
        private val mFakeDataSource: FakeDataSource) : MessengerDataSource {

    override fun reportOnline(): Observable<String> {
        val public: String = Encryptor.getAppRsaPublic()
        val request = RequestFactory.connection(public)
        return service.requestX(request).map { response ->
            Encryptor.setServPublicRSAKey(JsonWrapper.getServPublicKey(response))
            response
        }
    }

    override fun longPoll(pollDataId: List<String>): Observable<List<PollDataRaw>> {
        val request = RequestFactory.formLongPollRequest(pollDataId)
        return service.requestX(request).map { response ->
            JsonWrapper.getPollDataRaw(response)
        }.map { v -> decrypt(v) }
    }

    override fun loadChats(requestValues: MessengerDataSource.LoadChatListRequestValues): Observable<List<ChatLightRaw>> {
        val request = RequestFactory.formLoadChatList()
        return service.requestX(request)
                .map { response ->
                    JsonWrapper.getChatLightList(response)
                }
                .flatMapIterable { it }
                .map { chatLightRaw ->
                    if (!chatLightRaw.key.isNullOrBlank() && !chatLightRaw.vector.isNullOrBlank()) {
                        chatLightRaw.lastMessage = Encryptor.decrypt(chatLightRaw.key, chatLightRaw.vector, chatLightRaw.lastMessage)
                    }
                    chatLightRaw
                }
                .toList()
                .toObservable()

    }

    override fun loadChat(userId: List<String>, chatId: String, amount: Int, type: String): Observable<ChatRaw> {
        val request = RequestFactory.formCreateChat(userId, chatId, amount, type)
        return service.requestX(request).map { response ->
            JsonWrapper.getChatRaw(response)
        }.map { chat ->
            chat.messages.forEach { decryptMessage(it) }
            chat
        }
    }

    override fun loadMoreMessages(type: String, chatId: String, messageId: Long, amount: Int, direction: Int): Observable<List<MessageRaw>> {
        val request = RequestFactory.formLoadMoreChatMessages(type, chatId, messageId, amount, direction)
        return service.requestX(request)
                .map { response ->
                    val chatRaw = JsonWrapper.getChatRaw(response)
                    chatRaw!!.messages
                }
                .map { messages ->
                    messages.forEach { decryptMessage(it) }
                    messages
                }
    }

    override fun sendMessage(chatId: String, text: String, type: String): Observable<MessageRaw> {
        val parcel = Parcel(Encryptor.encrypt(text))
        parcel.request!!.guid = chatId
        val request = RequestFactory.sendMessage(parcel, type)
        return service.requestX(request).map { response ->
            JsonWrapper.getMessage(response)
        }.map { message ->
            decryptMessage(message)
            message
        }
    }

    override fun sendMessage(chatId: String, text: String, attachments: List<Attachment>, type: String): Observable<MessageRaw> {
        val responses = arrayListOf<Observable<String>>()

        attachments.forEach { attachment ->
            val request = Encryptor.encrypt(attachment.encodedValue)
            val answer = saveFile(request.value!!, request.codekey!!, request.codevector!!, attachment.extension)
            responses.add(answer)
        }

        return Observable
                .zip(responses) { strings ->
                    val result = arrayListOf<String>()
                    for (i in strings) {
                        result.add(i as String)
                    }
                    result
                }
                .flatMap { results -> send(chatId, text, results, type) }
    }

    private fun send(chatId: String, text: String, list: List<String>, type: String): Observable<MessageRaw> {
        val request = RequestFactory.sendMessage(chatId, text, list, type)
        return service.requestX(request)
                .map { response ->
                    JsonWrapper.getMessage(response)
                }
    }

    override fun sendMessageReadStatus(ids: List<Long>, chatId: String): Observable<String> {
        val request = RequestFactory.formSendMessageReadStatus(ids, chatId)
        return service.requestX(request)
    }

    override fun reportOffline(): Observable<String> {
        val request = RequestFactory.formReportOfflineRequest()
        return service.requestX(request)
    }

    private fun saveFile(value: String, key: String, vector: String, extension: String): Observable<String> {
        val timestamp = (System.currentTimeMillis() / 1000).toString() + extension
        val request = RequestFactory.saveFile(timestamp, value, key, vector)
        return service.requestX(request)
                .map { v -> String.format("'%s'", v) }
    }

    private fun decrypt(encrypted: List<PollDataRaw>): List<PollDataRaw> {
        for (item in encrypted) {
            if (item.value is MessageRaw) {
                decryptMessage(item.value as MessageRaw)
            }
        }
        return encrypted
    }

    private fun decryptMessage(message: MessageRaw) {
        if (!message.codeKey.isNullOrEmpty() && !message.codeVector.isNullOrEmpty()) {
            message.text = Encryptor.decrypt(message.codeKey, message.codeVector, message.text)
        }
    }
}
