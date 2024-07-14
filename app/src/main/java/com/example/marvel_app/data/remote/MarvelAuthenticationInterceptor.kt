package com.example.marvel_app.data.remote

import com.example.marvel_app.util.Constants.PRIVATE_KEY
import com.example.marvel_app.util.Constants.PUBLIC_KEY
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest

class MarvelAuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val ts = System.currentTimeMillis().toString()
        val hash = md5(ts + PRIVATE_KEY + PUBLIC_KEY)

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("ts", ts)
            .addQueryParameter("apikey", PUBLIC_KEY)
            .addQueryParameter("hash", hash)
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { String.format("%02x", it) }
    }
}
