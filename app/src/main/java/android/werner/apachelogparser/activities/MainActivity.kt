package android.werner.apachelogparser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.werner.apachelogparser.R
import android.werner.apachelogparser.adapters.LogsAdapter
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.util.States
import android.werner.apachelogparser.viewmodels.LogsViewModel
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

        fetch_logs_button.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun updateLayout(layout: States?) {
        when (layout) {
            States.DEFAULT-> {
                fetch_logs_button.visibility = View.VISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
                loading_animation.visibility = View.INVISIBLE
            }
            States.LOADING-> {
                fetch_logs_button.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
                loading_animation.visibility = View.VISIBLE
            }
            States.LIST-> {
                fetch_logs_button.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.VISIBLE
                loading_animation.visibility = View.INVISIBLE
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
