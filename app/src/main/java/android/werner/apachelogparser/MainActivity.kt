package android.werner.apachelogparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.werner.apachelogparser.util.Constants
import androidx.annotation.IntegerRes
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(Constants.URL)
            .build()

        val list = ArrayList<String>()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    val myResponse = response.body!!.string()

                    val lines = myResponse.lines()
                    for (line in lines) {
                        list.add(line)
                    }

                    for (line in list ) {
                        //println(line)
                    }

                    // Creating a regular expression for the records
                    val regex = "^(\\S+) (\\S+) (\\S+) " +
                            "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" +
                            " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

                    val pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    val matcher = pattern.matcher(myResponse);

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

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        theText.text = myResponse
                    })
                }
            }
        })


    }
}
