package android.werner.apachelogparser.network

import android.util.Log
import android.werner.apachelogparser.util.NetworkResult
import ru.gildor.coroutines.okhttp.await

object LogsClient {

    suspend fun requestLogs():NetworkResult {
        try{
            val result = ServiceGenerator.startRequest().await()
            return when (result.isSuccessful) {
                true -> NetworkResult.Success(result.body!!.charStream())
                else -> NetworkResult.Error( "Error code: ${result.code}")
            }
        } catch (t: Throwable) {
            Log.e("LogsClient", "Error has occurred")
        }

        return NetworkResult.Error("An error has occurred. Please try again.")
    }
}