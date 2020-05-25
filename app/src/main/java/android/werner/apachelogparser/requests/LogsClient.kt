package android.werner.apachelogparser.requests

import ru.gildor.coroutines.okhttp.await

object LogsClient {

    suspend fun requestLogs():String {
        val result = ServiceGenerator.startRequest().await()
        if (result.isSuccessful) {
            return result.body!!.string()
        }
        // Handle error case with empty string
        return ""
    }
}