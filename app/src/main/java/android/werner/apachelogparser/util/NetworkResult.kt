package android.werner.apachelogparser.util

import java.io.Reader

sealed class NetworkResult {
    data class Success(val value: Reader) : NetworkResult()
    data class Error(val value: String) : NetworkResult()
}