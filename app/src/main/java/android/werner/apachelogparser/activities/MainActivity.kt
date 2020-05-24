package android.werner.apachelogparser.activities

import android.content.res.ObbScanner
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.werner.apachelogparser.R
import android.werner.apachelogparser.adapters.LogsAdapter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.util.States
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
        updateLayout(mViewModel.getState()?.value)

        theButton.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun updateLayout(layout: States?) {
        when (layout) {
            States.DEFAULT-> {
                theButton.visibility = View.VISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
            }
            States.LOADING-> {
                theButton.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
            }
            States.LIST-> {
                theButton.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.VISIBLE
            }
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
        mViewModel.getFreqencyList().observe(this, listObserver)

        val stateObserver = Observer<States> { newState ->
            updateLayout(newState)
        }
        mViewModel.getState().observe(this, stateObserver)
    }

    private fun handleButtonClick() {
        GlobalScope.launch {
            mViewModel.requestLogs()
        }
    }
}
