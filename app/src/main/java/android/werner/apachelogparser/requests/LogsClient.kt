package android.werner.apachelogparser.requests

import android.util.Log
import android.werner.apachelogparser.util.NetworkResult
import ru.gildor.coroutines.okhttp.await

object LogsClient {

    suspend fun requestLogs():NetworkResult {
        try{
            val result = ServiceGenerator.startRequest().await()
            if (result.isSuccessful) {
                return NetworkResult.Success(result.body!!.string())
            } else {
                return NetworkResult.Error( "Error code: ${result.code}")
            }
        } catch (t: Throwable) {
            Log.d("LogsClient", "Error has occurred")
        }

        return NetworkResult.Error("An error has occurred. Please try again.")
    }
}