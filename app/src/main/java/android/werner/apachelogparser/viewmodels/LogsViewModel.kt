package android.werner.apachelogparser.viewmodels

import android.view.View
import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.repositories.LogsRepository
import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogsViewModel : ViewModel() {

    private val mFrequencyList = MutableLiveData<List<LogFrequency>>()
    val frequencyList : LiveData<List<LogFrequency>>
        get() = mFrequencyList

    private val mState = MutableLiveData(States.DEFAULT)
    val state : LiveData<States>
        get() = mState

    fun requestLogs(view: View) {
        mState.postValue(States.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val response = LogsRepository.requestLogs()
            if (response.isNotEmpty()) {
                val parsedData = LogFrequencyCounter.getLogFrequencyData(response)
                mFrequencyList.postValue(parsedData)
                mState.postValue(States.DATA)
            } else {
                mState.postValue(States.ERROR)
            }
        }
    }

    fun clearList() {
        mState.postValue(States.DEFAULT)
        mFrequencyList.postValue(emptyList())
    }
}