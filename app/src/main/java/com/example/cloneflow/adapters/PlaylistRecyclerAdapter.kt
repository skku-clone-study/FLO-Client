package com.example.cloneflow.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneflow.R
import com.example.cloneflow.services.PlayList

class PlaylistRecyclerAdapter(val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val SINGLE_SONG : Int = 0
        val LIST : Int = 1
        val LIST_SONG : Int = 2
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType){
            SINGLE_SONG -> { // 단일 곡
                SingleMusicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_music_item, parent, false))
            }
            LIST -> { // 그룹 이름
                ListHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_list_item, parent, false))
            }
            else -> { // 그룹 곡
                ListChildViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_music_item_padding, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = items[position]
        when(obj.type){
            SINGLE_SONG -> { // 단일 곡
                Log.d("로그", "PlaylistRecyclerAdapter - bind single song ${obj.title}")
                (holder as SingleMusicViewHolder).songTitle.text = obj.title
                holder.songArtist.text = obj.artist
                Glide.with(holder.itemView).load(obj.cover).into(holder.songThumbnail)
            }
            LIST -> {  // 그룹
                Log.d("로그", "PlaylistRecyclerAdapter - bind playlist ${obj.title}")
                (holder as ListHeaderViewHolder).listTitle.text = obj.title
            }
            else -> { // 그룹 내 노래
                Log.d("로그", "PlaylistRecyclerAdapter - bind playlist song ${obj.title}")
                (holder as ListChildViewHolder).songTitle.text = obj.title
                holder.songArtist.text = obj.artist
                Glide.with(holder.itemView).load(obj.cover).into(holder.songThumbnail)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type!!
    }

    inner class SingleMusicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val songTitle: TextView = itemView.findViewById(R.id.playlist_title)
        val songThumbnail: ImageView = itemView.findViewById(R.id.playlist_thumbnail)
        val songArtist: TextView = itemView.findViewById(R.id.playlist_artist)
    }

    inner class ListHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val listTitle : TextView = itemView.findViewById(R.id.playlist_title)
    }

    inner class ListChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val songTitle: TextView = itemView.findViewById(R.id.playlist_title)
        val songThumbnail: ImageView = itemView.findViewById(R.id.playlist_thumbnail)
        val songArtist: TextView = itemView.findViewById(R.id.playlist_artist)
    }

    class Item (
        val type : Int? = null,
        val title : String? = null,
        val musicIdx : Int? = null,
        val albumIdx : Int? = null,
        val cover : String? = null,
        val artist : String? = null
    )
}