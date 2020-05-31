package android.werner.apachelogparser.repositories

import android.werner.apachelogparser.network.LogsClient
import android.werner.apachelogparser.util.NetworkResult

object LogsRepository {

    suspend fun requestLogs():NetworkResult {
        return LogsClient.requestLogs()
    }
}