package com.example.cloneflow.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.AlbumResult

class InfoArtistAlbumRecyclerAdapter(val items : List<AlbumResult>) : RecyclerView.Adapter<InfoArtistAlbumRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_album_list_item, parent, false)
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
        fun bind(item : AlbumResult){
            view.findViewById<TextView>(R.id.artist_album_title).text = item.title
            view.findViewById<TextView>(R.id.artist_album_artist).text = item.artist
            Glide.with(view).load(item.cover).into(view.findViewById(R.id.artist_album_thumbnail))
            val infotext = "${item.releasedAt} | ${item.albumType}"
            view.findViewById<TextView>(R.id.artist_album_info).text = infotext
        }
    }
}