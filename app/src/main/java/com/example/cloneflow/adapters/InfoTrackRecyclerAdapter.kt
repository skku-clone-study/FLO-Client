package com.example.cloneflow.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.R
import com.example.cloneflow.StreamingActivity
import com.example.cloneflow.infoactivities.InfoSongActivity
import com.example.cloneflow.services.AlbumTrackResult

class InfoTrackRecyclerAdapter(val items : List<AlbumTrackResult>) : RecyclerView.Adapter<InfoTrackRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.frag_search_album_songs_item, parent, false)
        Log.d("로그", "InfoTrackRecyclerAdapter - onCreateViewHolder() called - $itemCount items")
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item, position)
        }
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view : View = v
        fun bind(item : AlbumTrackResult, pos : Int) {
            val trackIdx = view.findViewById<TextView>(R.id.album_song_idx)
            val isTitle = view.findViewById<TextView>(R.id.album_song_is_title_mark)
            val trackTitle = view.findViewById<TextView>(R.id.album_song_title_text)
            val trackArtist = view.findViewById<TextView>(R.id.album_song_artist_text)

            trackIdx.text = String.format("%02d", pos+1)
            if(item.isTitleOfAlbum == "FALSE") {isTitle.visibility = View.GONE}
            trackTitle.text = item.title
            trackArtist.text = item.artist
            val musicIdx = item.musicIdx
            val musicPlayBtn = view.findViewById<ImageButton>(R.id.album_song_play_btn)
            musicPlayBtn.setOnClickListener{
                val musicPlayIntent = Intent(view.context, StreamingActivity::class.java)
                musicPlayIntent.putExtra("musicIdx", musicIdx)
                view.context.startActivity(musicPlayIntent)
            }
            val musicDetailBtn = view.findViewById<ImageButton>(R.id.album_song_info_btn)
            musicDetailBtn.setOnClickListener{
                val musicDetailIntent = Intent(view.context, InfoSongActivity::class.java)
                musicDetailIntent.putExtra("musicIdx", musicIdx)
                view.context.startActivity(musicDetailIntent)
            }
            Log.d("로그", "ViewHolder - bind(${item.title}) called")
        }
    }

}