package android.werner.apachelogparser.repositories

import android.werner.apachelogparser.requests.LogsClient

object LogsRepository {

    suspend fun requestLogs():String {
        return LogsClient.requestLogs()
    }
}