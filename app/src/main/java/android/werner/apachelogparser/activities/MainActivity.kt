package android.werner.apachelogparser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.werner.apachelogparser.R
import android.werner.apachelogparser.adapters.LogsAdapter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.viewmodels.LogsViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: LogsViewModel
    private lateinit var mAdapter : LogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this).get(LogsViewModel::class.java)

        initRecyclerView()
        subscribeObservers()

        theButton.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun initRecyclerView() {
        mAdapter = LogsAdapter()
        log_list_recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun subscribeObservers() {
        val listObserver = Observer<ArrayList<LogFrequency>> { newList ->
            mAdapter.setLogs(newList)
            println("List updated")
        }
        mViewModel.getFreqencyList().observe(this,listObserver)

    }

    private fun handleButtonClick() {
        GlobalScope.launch {
            mViewModel.requestLogs()
        }
    }
}
