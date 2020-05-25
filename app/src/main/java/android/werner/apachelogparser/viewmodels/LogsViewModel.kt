package android.werner.apachelogparser.viewmodels

import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.repositories.LogsRepository
import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogsViewModel : ViewModel() {

    private val mFrequencyList: MutableLiveData<ArrayList<LogFrequency>> by lazy {
        MutableLiveData<ArrayList<LogFrequency>>()
    }

    private val mState = MutableLiveData<States>()

    init {
        mState.postValue(States.DEFAULT)
    }

    suspend fun requestLogs() {
        mState.postValue(States.LOADING)
        val response = LogsRepository.requestLogs()
        if (response.isNotEmpty()) {
            val parsedData = LogFrequencyCounter.getLogFrequencyData(response)
            mFrequencyList.postValue(parsedData)
            mState.postValue(States.LIST)
        } else {
            mState.postValue(States.ERROR)
        }
    }

    fun getFrequencyList():LiveData<ArrayList<LogFrequency>> {
        return mFrequencyList
    }

    fun getState():LiveData<States> {
        return mState
    }
}