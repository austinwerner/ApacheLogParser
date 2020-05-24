package android.werner.apachelogparser.viewmodels

import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.repositories.LogsRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogsViewModel : ViewModel() {

    private val mFrequencyList: MutableLiveData<ArrayList<LogFrequency>> by lazy {
        MutableLiveData<ArrayList<LogFrequency>>()
    }

    suspend fun requestLogs() {
        val response = LogsRepository.requestLogs()
        val parsedData = LogFrequencyCounter.getLogFrequencyData(response)
        mFrequencyList.postValue(parsedData)
    }

    fun getFreqencyList():LiveData<ArrayList<LogFrequency>> {
        return mFrequencyList
    }
}