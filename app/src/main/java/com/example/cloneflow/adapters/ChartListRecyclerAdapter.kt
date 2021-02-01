package com.example.cloneflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.R
import com.example.cloneflow.services.Chart
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

class ChartListRecyclerAdapter(val items : List<Chart>) : RecyclerView.Adapter<ChartListRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_chart_list_cardview, parent, false)
        return ViewHolder(
            inflatedView
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        /*
        val listener = View.OnClickListener { it ->
            // StreamingActivity로 items[position].musicIdx
        }
         */
        holder.apply {
            bind(item)// bind(item, listener)
        }
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(item : Chart) { //(item : Chart, listener : View.OnClickListener)
            val chartTitleText = view.findViewById<TextView>(R.id.chart_chart_name)
            chartTitleText.text = when(item.playlistIdx) {
                1 -> "FLO 차트"
                2 -> "실시간 급상승 차트"
                else -> "해외 인기 차트"
            }
            val chartUpdatedText = view.findViewById<TextView>(R.id.chart_updated)
            chartUpdatedText.text = item.updated
            val recyclerView = view.findViewById<RecyclerView>(R.id.chart_recyclerview)
            recyclerView.adapter =
                ChartRecyclerAdapter(item.songs!!)
            recyclerView.layoutManager = GridLayoutManager(view.context, 5, GridLayoutManager.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
            val pagerDotIndicator = view.findViewById<ScrollingPagerIndicator>(R.id.indicator)
            pagerDotIndicator.attachToRecyclerView(recyclerView)
            val chartPlayBtn = view.findViewById<ImageButton>(R.id.chart_song_play_btn)
            //chartPlayBtn.setOnClickListener(listener)
        }
    }
}