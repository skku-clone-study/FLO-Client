package com.example.cloneflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.PlayList

class PlaylistRecyclerAdapter(val items: List<PlayList>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> { // 단일 곡
                SingleMusicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_music_item, parent, false))
            }
            else -> { // 그룹
                ListMusicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_list_item, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = items[position]
        when(obj.isGroup){
            0 -> { // 단일 곡
                (holder as SingleMusicViewHolder).songTitle.text = obj.songs!![0].title
                holder.songArtist.text = obj.songs!![0].artist
                Glide.with(holder.itemView).load(obj.songs!![0].cover).into(holder.songThumbnail)
            }
            else -> {  // 그룹
                (holder as ListMusicViewHolder).playListTitle.text = obj.groupName
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].isGroup!!
    }

    inner class SingleMusicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val songTitle = itemView.findViewById<TextView>(R.id.playlist_title)
        val songThumbnail = itemView.findViewById<ImageView>(R.id.playlist_thumbnail)
        val songArtist = itemView.findViewById<TextView>(R.id.playlist_artist)
    }

    inner class ListMusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val playListTitle = itemView.findViewById<TextView>(R.id.playlist_title)
    }

}