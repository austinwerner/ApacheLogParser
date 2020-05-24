package android.werner.apachelogparser.viewmodels

import android.werner.apachelogparser.repositories.LogsRepository
import android.werner.apachelogparser.util.Constants
import java.util.regex.Pattern

object LogsViewModel {

    suspend fun requestLogs() {
        val response = LogsRepository.requestLogs()

        val pattern = Pattern.compile(Constants.APACHE_LOG_REGEX, Pattern.MULTILINE);
        val matcher = pattern.matcher(response);

        val map = HashMap<String, ArrayList<String>>();
        while (matcher.find()) {
            val ip = matcher.group(1)
            val page : String = matcher.group(6)
            val response = matcher.group(8)

            // only take OK resopnses
            if (Integer.parseInt(response) == 200) {

                // associate pages to ip
                if (map.containsKey(ip)) {
                    map[ip]?.let {
                        it.add(page)
                        map[ip] = it
                    }
                } else {
                    val list = ArrayList<String>()
                    list.add(page)
                    map[ip] = list
                }
            }
        }

        val frequencyMap = HashMap<String,Int>()
        for ((k,v) in map ) {
            println( "User $k")
            var i = 0
            while (i < v.size - 2) {
                // create key out of three consecutive pages
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

        for ((k,v) in frequencyMap) {
            println( "Frequency: $v ---- $k")
        }
    }
}