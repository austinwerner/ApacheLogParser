package android.werner.apachelogparser.requests

import android.werner.apachelogparser.util.States
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.gildor.coroutines.okhttp.await
import java.io.IOException
import java.util.regex.Pattern

object LogsClient {

    private var mState = MutableLiveData<States>()

    init {
        mState.postValue(States.DEFAULT)
    }

    suspend fun requestLogs():String {
        mState.postValue(States.LOADING)
        val result = ServiceGenerator.startRequest().await()
        if (result.isSuccessful) {
            mState.postValue(States.LIST)
            return result.body!!.string()
        }
        mState.postValue(States.DEFAULT)
        return ""
    }

    fun getState():LiveData<States> {
        return mState
    }
}