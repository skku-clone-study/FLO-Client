package com.example.cloneflow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.services.Songs

class ChartRecyclerAdapter(val items : List<Songs>) : RecyclerView.Adapter<ChartRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_chart_list_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
        }
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(item : Songs) {
            val thumbnailView = view.findViewById<ImageView>(R.id.chart_list_thumbnail)
            Glide.with(view).load(item.cover).into(thumbnailView)
            val chartRankTextView = view.findViewById<TextView>(R.id.chart_rank_text)
            chartRankTextView.text = item.ranking.toString()
            val titleTextView = view.findViewById<TextView>(R.id.chart_title_text)
            titleTextView.text = item.title.toString()
            val artistTextView = view.findViewById<TextView>(R.id.chart_artist_text)
            artistTextView.text = item.artist.toString()
            Log.d("로그", "ViewHolder - bind(${item.title}) called")
        }
    }

}