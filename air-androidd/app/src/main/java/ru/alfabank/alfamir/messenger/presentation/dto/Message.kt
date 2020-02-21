package ru.alfabank.alfamir.messenger.presentation.dto

import android.graphics.Bitmap
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_FILE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_IMAGE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT
import ru.alfabank.alfamir.messenger.data.dto.Attachment
import java.util.*

class Message private constructor(val chatId: String?,
                                  val recipientId: String?,
                                  val date: String?,
                                  val text: String?,
                                  val lDate: Long?,
                                  private var mViewId: Long,
                                  val id: Long,
                                  var currentStatus: Int,
                                  val senderUser: User,
                                  var statusList: List<Status>?,
                                  val attachments: List<Attachment>) : DisplayableMessageItem() {
    var image: Bitmap? = null

    fun setViewId(mViewId: Long) {
        this.mViewId = mViewId
    }

    override fun getType(): Int {
        val v = if (!attachments.isNullOrEmpty()) {
            if (attachments[0].type == "image") MESSAGE_IMAGE
            else MESSAGE_FILE
        } else MESSAGE_TEXT
        return v
    }

    override fun getViewId(): Long {
        return mViewId
    }

    class Builder {
        private var mChatId: String? = null
        private var mRecipientId: String? = null
        private var mText: String? = null
        private var mDate: String? = null
        private var mLDate: Long = 0
        private var mViewId: Long = 0
        private var mId: Long = 0
        private var mCurrentStatus: Int = 0
        private lateinit var mSenderUser: User
        private var mStatusList: List<Status>? = null
        private var mAttachments: List<Attachment> = arrayListOf()

        fun id(id: Long): Builder {
            mId = id
            return this
        }

        fun chadId(chatId: String): Builder {
            mChatId = chatId
            return this
        }

        fun senderUser(senderUser: User): Builder {
            mSenderUser = senderUser
            return this
        }

        fun recipientId(recipientId: String?): Builder {
            mRecipientId = recipientId
            return this
        }

        fun lDate(lDate: Long): Builder {
            mLDate = lDate
            return this
        }

        fun text(text: String): Builder {
            mText = text
            return this
        }

        fun date(date: String): Builder {
            mDate = date
            return this
        }

        fun status(status: Status): Builder {
            val statusList = ArrayList<Status>()
            statusList.add(status)
            mStatusList = statusList
            return this
        }

        fun statusList(statusList: List<Status>): Builder {
            mStatusList = statusList
            return this
        }

        fun attachments(attachments: List<Attachment>): Builder {
            mAttachments = attachments
            return this
        }

        fun build(): Message {
            mViewId = UUID.randomUUID().mostSignificantBits and java.lang.Long.MAX_VALUE

            for (status in mStatusList!!) {
                val elementStatus = status.state
                if (mCurrentStatus < elementStatus) {
                    mCurrentStatus = elementStatus
                }
            }
            return Message(mChatId, mRecipientId, mDate, mText, mLDate, mViewId, mId, mCurrentStatus, mSenderUser, mStatusList, mAttachments)
        }
    }
}
