package android.werner.apachelogparser.viewmodels

import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.repositories.LogsRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LogsViewModel {

    private val mFrequencyList = MutableLiveData<List<LogFrequency>>()

    suspend fun requestLogs() {
        val response = LogsRepository.requestLogs()
        mFrequencyList.value = LogFrequencyCounter.getLogFrequencyData(response)
    }

    fun getFreqencyList():LiveData<List<LogFrequency>> {
        return mFrequencyList
    }
}