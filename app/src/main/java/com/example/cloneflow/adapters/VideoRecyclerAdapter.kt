package com.example.cloneflow.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.Videos

class VideoRecyclerAdapter(val items : List<Videos>) : RecyclerView.Adapter<VideoRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_video_list_item, parent, false)
        return ViewHolder(
            inflatedView
        )
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
        fun bind(item : Videos) {
            val thumbnailView = view.findViewById<ImageView>(R.id.video_list_thumbnail)
            Glide.with(view).load(item.thumbnail).into(thumbnailView)
            val titleTextView = view.findViewById<TextView>(R.id.video_list_title)
            titleTextView.text = item.title.toString()
            val artistTextView = view.findViewById<TextView>(R.id.video_list_artist)
            artistTextView.text = item.artist.toString()
            val runningTimeTextView = view.findViewById<TextView>(R.id.video_list_runningtime)
            runningTimeTextView.text = item.runningTime.toString()
            Log.d("로그", "ViewHolder - bind(${item.title}) called")
        }
    }
}