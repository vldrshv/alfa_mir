package ru.alfabank.alfamir.messenger.data.source.remote

import com.google.gson.annotations.SerializedName

class Request() {

    constructor(value: String, codeKey: String, codeVector: String) : this() {
        this.value = value
        this.codekey = codeKey
        this.codevector = codeVector
    }

    @SerializedName("action")
    var action: String? = null
    @SerializedName("guid")
    var guid: String? = null
    @SerializedName("value")
    var value: String? = null
    @SerializedName("attachmentguids")
    var attachmentguids: String? = null
    @SerializedName("codekey")
    var codekey: String? = null
    @SerializedName("codevector")
    var codevector: String? = null

    class Builder {

        private val request = Request()

        fun action(action: String): Builder {
            request.action = action
            return this
        }

        fun guid(guid: String): Builder {
            request.guid = guid
            return this
        }

        fun value(value: String): Builder {
            request.value = value
            return this
        }

        fun attachmentguids(attachmentGuids: String): Builder {
            request.attachmentguids = attachmentGuids
            return this
        }

        fun codekey(codekey: String): Builder {
            request.codekey = codekey
            return this
        }

        fun codevector(codevector: String): Builder {
            request.codevector = codevector
            return this
        }

        fun build(): Request {
            return request
        }
    }
}