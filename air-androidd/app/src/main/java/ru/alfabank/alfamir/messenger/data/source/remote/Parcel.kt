package ru.alfabank.alfamir.messenger.data.source.remote

import com.google.gson.annotations.SerializedName

class Parcel() {

    constructor(req: Request) : this() {
        this.request = req
    }

    @SerializedName("wcf")
    var wcf: String? = null
    @SerializedName("platform")
    var platform: String? = null
    @SerializedName("airversion")
    var ariVersion: String? = null
    @SerializedName("userlogin")
    var userLogin: String? = null
    @SerializedName("request")
    var request: Request? = null


    class Builder {

        private val parcel = Parcel()

        fun wcf(wcf: String): Builder {
            parcel.wcf = wcf
            return this
        }

        fun platform(platform: String): Builder {
            parcel.platform = platform
            return this
        }

        fun ariVersion(ariVersion: String): Builder {
            parcel.ariVersion = ariVersion
            return this
        }

        fun userLogin(userLogin: String): Builder {
            parcel.userLogin = userLogin
            return this
        }

        fun platform(request: Request): Builder {
            parcel.request = request
            return this
        }

        fun build(): Parcel {
            return parcel
        }
    }
}