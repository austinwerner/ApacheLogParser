package android.werner.apachelogparser.util

sealed class NetworkResult {
    data class Success(val value: String) : NetworkResult()
    data class Error(val value: String) : NetworkResult()
}