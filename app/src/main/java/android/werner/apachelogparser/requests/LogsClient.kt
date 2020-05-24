package android.werner.apachelogparser.requests

import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.gildor.coroutines.okhttp.await
import java.io.IOException
import java.util.regex.Pattern

object LogsClient {

    suspend fun requestLogs():String {
        val result = ServiceGenerator.startRequest().await()
        if (result.isSuccessful) {
            return result.body!!.string()
        }
        return ""
    }
}