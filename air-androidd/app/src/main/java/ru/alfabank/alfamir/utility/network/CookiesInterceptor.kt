package ru.alfabank.alfamir.utility.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.alfabank.alfamir.Constants.Companion.REGISTRATION_SUF
import java.io.IOException


object CookiesInterceptor {

    val saveCookies = SaveCookies()
    val addCookies = AddCookies()
    var cookies = hashSetOf<String>()

    class AddCookies : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val builder = chain.request().newBuilder()

            if (chain.request().url().encodedPath() == REGISTRATION_SUF) {
                cookies.clear()
            } else {
                for (cookie in cookies) {
                    builder.addHeader("Cookie", cookie)
                }
            }

            return chain.proceed(builder.build())
        }
    }

    class SaveCookies : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())

            if (originalResponse.request().url().encodedPath() == REGISTRATION_SUF && originalResponse.headers("Set-Cookie").isNotEmpty()) {

                for (header in originalResponse.headers("Set-Cookie")) {
                    cookies.add(header)
                }
            }

            return originalResponse
        }
    }
}