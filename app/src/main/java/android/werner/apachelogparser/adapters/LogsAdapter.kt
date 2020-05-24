package android.werner.apachelogparser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.werner.apachelogparser.models.LogFrequency
import androidx.recyclerview.widget.RecyclerView

class LogsAdapter() : RecyclerView.Adapter<LogsViewHolder>() {

    private var mList = ArrayList<LogFrequency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LogsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val logFrequency: LogFrequency = mList[position]
        holder.bind(logFrequency)
    }

    override fun getItemCount(): Int = mList.size

    fun setLogs(logs: ArrayList<LogFrequency>) {
        mList = logs
        notifyDataSetChanged()
    }
}