package com.example.marvel_app.data.remote

import com.example.marvel_app.util.Constants.BASE_URL
import java.security.MessageDigest

class MarvelAuthentication(private val privateKey: String, private val publicKey: String) {

    fun generateAuthUrl(path: String): String {
        val ts = System.currentTimeMillis().toString()
        val hash = md5(ts + privateKey + publicKey)
        return ("$BASE_URL$path?ts=$ts&apikey=$publicKey&hash=$hash")
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return  digest.joinToString("") { String.format("%02x", it) }
    }
}