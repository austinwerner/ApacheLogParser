package android.werner.apachelogparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.werner.apachelogparser.repositories.LogsRepository
import android.werner.apachelogparser.viewmodels.LogsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //LogsClient.requestLogFile()


        theButton.setOnClickListener {
            buttonClick()
        }
    }

    fun buttonClick() {
        GlobalScope.launch {
            LogsViewModel.requestLogs()
        }
    }
}
