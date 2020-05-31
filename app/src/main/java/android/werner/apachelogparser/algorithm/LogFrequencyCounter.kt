package android.werner.apachelogparser.algorithm

import android.werner.apachelogparser.extensions.addToList
import android.werner.apachelogparser.extensions.incrementIntValue
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.util.Constants
import java.util.regex.Pattern

object LogFrequencyCounter {
    fun getLogFrequencyData(logData: String):List<LogFrequency> {
        val userMap = createUserMap(logData)
        val frequencyMap = createFrequencyMap(userMap)
        return convertLogToSortedFrequencyData(frequencyMap)
    }

    // Iterate through logs and associate page sequences with User's IP address
    private fun createUserMap(logData: String):Map<String, List<String>> {
        val pattern = Pattern.compile(Constants.APACHE_LOG_REGEX, Pattern.MULTILINE);
        val matcher = pattern.matcher(logData);
        val userMap = mutableMapOf<String, MutableList<String>>()
        while (matcher.find()) {
            val ip: String = matcher.group(1).orEmpty()
            val page: String = matcher.group(6).orEmpty()
            val httpStatus: String = matcher.group(8).orEmpty()

            // Associate pages with User's IP address
            if (Integer.parseInt(httpStatus) == Constants.HTTP_OK) {
                userMap.addToList(ip,page)
            }
        }
        return userMap
    }

    // Iterate through every user and count the 3-page sequence frequency
    private fun createFrequencyMap(userMap: Map<String, List<String>>):Map<String,Int> {
        val frequencyMap = mutableMapOf<String,Int>()
        userMap.forEach{
            var i = 0
            while (i < it.value.size - 2) {
                // Create key out of 3 consecutive pages
                val key = it.value[i] + " " + it.value[i+1]  + " " + it.value[i+2]
                i++
                frequencyMap.incrementIntValue(key)
            }
        }
        return frequencyMap
    }

    // Converts log frequency map to a sorted list of LogFrequency objects
    private fun convertLogToSortedFrequencyData(frequencyMap: Map<String,Int>):List<LogFrequency> {
        return frequencyMap.map{
            it.key.split(" ").let{ pages ->
                LogFrequency( pages[0], pages[1], pages[2], it.value )
            }
        }.sortedByDescending { it.frequency }
    }
}