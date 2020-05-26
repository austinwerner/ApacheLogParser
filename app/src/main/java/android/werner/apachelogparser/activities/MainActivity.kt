package android.werner.apachelogparser.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.werner.apachelogparser.R
import android.werner.apachelogparser.adapters.LogsAdapter
import android.werner.apachelogparser.extensions.isConnected
import android.werner.apachelogparser.models.LogFrequency
import android.werner.apachelogparser.util.States
import android.werner.apachelogparser.viewmodels.LogsViewModel
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: LogsViewModel
    private lateinit var mAdapter : LogsAdapter
    private var mMenu : Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this).get(LogsViewModel::class.java)

        initRecyclerView()
        subscribeObservers()

        fetch_logs_button.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun updateState(state: States?) {
        when (state) {
            States.DEFAULT-> {
                fetch_logs_button.visibility = View.VISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
                fast_scroller.visibility = View.INVISIBLE
                loading_animation.visibility = View.INVISIBLE
            }
            States.LOADING-> {
                fetch_logs_button.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
                fast_scroller.visibility = View.INVISIBLE
                loading_animation.visibility = View.VISIBLE
            }
            States.DATA-> {
                fetch_logs_button.visibility = View.INVISIBLE
                log_list_recycler_view.visibility = View.VISIBLE
                fast_scroller.visibility = View.VISIBLE
                loading_animation.visibility = View.INVISIBLE
            }
            States.ERROR-> {
                fetch_logs_button.visibility = View.VISIBLE
                log_list_recycler_view.visibility = View.INVISIBLE
                fast_scroller.visibility = View.INVISIBLE
                loading_animation.visibility = View.INVISIBLE
                Toast.makeText(this, getText(R.string.error_message), Toast.LENGTH_LONG).show()
            }
        }
        updateClearButton()
    }

    private fun initRecyclerView() {
        mAdapter = LogsAdapter()
        log_list_recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        fast_scroller.setRecyclerView(log_list_recycler_view)
        log_list_recycler_view.setOnScrollListener(fast_scroller.onScrollListener)
    }

    private fun subscribeObservers() {
        val listObserver = Observer<ArrayList<LogFrequency>> { newList ->
            mAdapter.setLogs(newList)
        }
        mViewModel.getFrequencyList().observe(this, listObserver)

        val stateObserver = Observer<States> { newState ->
            updateState(newState)
        }
        mViewModel.getState().observe(this, stateObserver)
    }

    private fun handleButtonClick() {
        if (isConnected) {
            GlobalScope.launch {
                mViewModel.requestLogs()
            }
        } else {
            Toast.makeText(this, getText(R.string.internet_unavailable), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.clear_button, menu)
        mMenu = menu
        updateClearButton()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                mViewModel.clearList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateClearButton() {
        mMenu?.findItem(R.id.action_clear)?.isVisible =
            mViewModel.getState().value == States.DATA
    }
}
