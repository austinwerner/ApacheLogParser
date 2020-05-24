package android.werner.apachelogparser.repositories

import android.werner.apachelogparser.requests.LogsClient
import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData

object LogsRepository {

    suspend fun requestLogs():String {
        return LogsClient.requestLogs()
    }

    fun getState(): LiveData<States> {
        return LogsClient.getState()
    }
}