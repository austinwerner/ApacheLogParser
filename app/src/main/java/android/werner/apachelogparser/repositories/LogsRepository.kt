package android.werner.apachelogparser.repositories

import android.werner.apachelogparser.requests.LogsClient
import android.werner.apachelogparser.util.NetworkResult

object LogsRepository {

    suspend fun requestLogs():NetworkResult {
        return LogsClient.requestLogs()
    }
}