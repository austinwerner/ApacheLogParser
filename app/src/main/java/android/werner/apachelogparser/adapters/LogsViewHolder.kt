package android.werner.apachelogparser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.werner.apachelogparser.R
import android.werner.apachelogparser.models.LogFrequency
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.log_list_item, parent, false)) {
    private var mPage1: TextView? = null
    private var mPage2: TextView? = null
    private var mPage3: TextView? = null
    private var mFrequency: TextView? = null

    init {
        mPage1 = itemView.findViewById(R.id.page1)
        mPage2 = itemView.findViewById(R.id.page2)
        mPage3 = itemView.findViewById(R.id.page3)
        mFrequency = itemView.findViewById(R.id.frequency)
    }

    fun bind(item: LogFrequency) {
        mPage1?.text = item.firstPage
        mPage2?.text = item.secondPage
        mPage3?.text = item.thirdPage
        mFrequency?.text = item.frequency.toString()
    }
}