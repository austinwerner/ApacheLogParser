package android.werner.apachelogparser.algorithm

import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.util.Constants
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object LogFrequencyCounter {
    fun getLogFrequencyData(logData: String):ArrayList<LogFrequency> {
        val userMap = createUserMap(logData)
        val frequencyMap = createFrequencyMap(userMap)
        return convertLogToSortedFrequencyData(frequencyMap)
    }

    // Iterate through logs and associate page sequences with User's IP address
    private fun createUserMap(logData: String):HashMap<String, ArrayList<String>> {
        val pattern = Pattern.compile(Constants.APACHE_LOG_REGEX, Pattern.MULTILINE);
        val matcher = pattern.matcher(logData);
        val userMap = HashMap<String, ArrayList<String>>();
        while (matcher.find()) {
            val ip: String = matcher.group(1).orEmpty()
            val page: String = matcher.group(6).orEmpty()
            val httpStatus: String = matcher.group(8).orEmpty()

            // Associate pages with User's IP address
            if (Integer.parseInt(httpStatus) == Constants.HTTP_OK) {
                if (userMap.containsKey(ip)) {
                    userMap[ip]?.let {
                        it.add(page)
                        userMap[ip] = it
                    }
                } else {
                    val list = arrayListOf<String>(page)
                    userMap[ip] = list
                }
            }
        }
        return userMap
    }

    // Iterate through every user and count the 3-page sequence frequency
    private fun createFrequencyMap(userMap: HashMap<String, ArrayList<String>>):HashMap<String,Int> {
        val frequencyMap = HashMap<String,Int>()
        for ((k,v) in userMap ) {
            println( "User $k")
            var i = 0
            while (i < v.size - 2) {
                // Create key out of 3 consecutive pages
                val key = v[i] + " " + v[i+1]  + " " + v[i+2]
                i++;

                if (frequencyMap.containsKey(key)) {
                    val curVal = frequencyMap[key]
                    frequencyMap[key] = curVal!!.plus(1)
                } else {
                    frequencyMap[key] = 1
                }
            }
        }
        return frequencyMap
    }

    // Converts log frequency map to a sorted list of LogFrequency objects
    private fun convertLogToSortedFrequencyData(frequencyMap: HashMap<String,Int>):ArrayList<LogFrequency> {
        val frequencyList = ArrayList<LogFrequency>()
        val sortedMap = frequencyMap.toList().sortedByDescending { (_, value) -> value}.toMap()
        for ((k,v) in sortedMap) {
            val pages = k.split(" ")
            val current = LogFrequency( pages[0], pages[1], pages[2], v )
            frequencyList.add(current)
        }
        return frequencyList
    }
}