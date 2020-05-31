package android.werner.apachelogparser.network

import android.werner.apachelogparser.util.Constants
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

object ServiceGenerator {

    private val client = OkHttpClient()

    private val request = Request.Builder()
        .url(Constants.URL)
        .build()

    fun startRequest(): Call {
        return client.newCall(request)
    }
}