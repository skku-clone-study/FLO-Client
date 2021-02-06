package com.example.cloneflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.MusicResult

class InfoArtistTrackRecyclerAdapter(val items : List<MusicResult>) : RecyclerView.Adapter<InfoArtistTrackRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view : View = v
        fun bind(item : MusicResult){
            view.findViewById<TextView>(R.id.chart_title_text).text = item.title
            view.findViewById<TextView>(R.id.chart_artist_text).text = item.artist
            view.findViewById<TextView>(R.id.chart_rank_text).visibility = View.INVISIBLE
            Glide.with(view).load(item.cover).into(view.findViewById(R.id.chart_list_thumbnail))
        }
    }

}