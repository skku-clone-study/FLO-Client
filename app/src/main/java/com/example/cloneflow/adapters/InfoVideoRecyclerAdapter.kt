package com.example.cloneflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.Video


class InfoVideoRecyclerAdapter(val items : List<Video>) : RecyclerView.Adapter<InfoVideoRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_search_album_videos_item, parent, false)
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
        val view : View = v
        fun bind(item : Video){
            view.findViewById<TextView>(R.id.video_list_title).text = item.title
            view.findViewById<TextView>(R.id.video_list_artist).text = item.artist
            Glide.with(view).load(item.thumbnail).into(view.findViewById(R.id.video_list_thumbnail))
            view.findViewById<TextView>(R.id.video_list_runningtime).text = item.runningTime
        }
    }
}