package android.werner.apachelogparser.viewmodels

import android.view.View
import android.werner.apachelogparser.R
import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.repositories.LogsRepository
import android.werner.apachelogparser.util.NetworkResult
import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogsViewModel : ViewModel() {

    private val mFrequencyList = MutableLiveData<List<LogFrequency>>()
    val frequencyList : LiveData<List<LogFrequency>>
        get() = mFrequencyList

    private val mState = MutableLiveData(States.DEFAULT)
    val state : LiveData<States>
        get() = mState

    private val mErrorMessage = MutableLiveData("")
    val errorMessage : LiveData<String>
        get() = mErrorMessage

    fun requestLogs(view: View) {
        assert(R.id.fetch_logs_button == view.id)
        mState.postValue(States.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            when( val response = LogsRepository.requestLogs() ) {
                is NetworkResult.Success -> {
                    mFrequencyList.postValue(LogFrequencyCounter.getLogFrequencyData(response.value))
                    mState.postValue(States.DATA)
                }
                is NetworkResult.Error -> {
                    mState.postValue(States.DEFAULT)
                    mErrorMessage.postValue(response.value)
                }
            }
        }
    }

    fun clearList() {
        mState.postValue(States.DEFAULT)
        mFrequencyList.postValue(emptyList())
    }
}