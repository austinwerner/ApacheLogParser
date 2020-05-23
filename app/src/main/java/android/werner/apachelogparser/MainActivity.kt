package android.werner.apachelogparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.werner.apachelogparser.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.stream.Stream.builder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(Constants.URL)
            .build()

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

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        theText.text = myResponse
                    })
                }
            }
        })


    }
}
