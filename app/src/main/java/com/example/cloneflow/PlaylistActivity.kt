package com.example.cloneflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.adapters.PlaylistRecyclerAdapter
import com.example.cloneflow.services.PlaylistResponse
import com.example.cloneflow.services.PlaylistService
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaylistActivity : AppCompatActivity() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val token = getToken()
        if(token != null) {
            val retrofit: Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()
            ).build()
            val service = retrofit!!.create(PlaylistService.ShowPlaylistService::class.java)
            val call = service.getPlaylistData(token = token)
            call.enqueue(object : Callback<PlaylistResponse>{
                override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                    Log.d("로그", "PlaylistActivity - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<PlaylistResponse>,
                    response: Response<PlaylistResponse>
                ) {
                    val responseBody = response.body()!!
                    when{
                        responseBody.isSuccess!! -> {
                            val playListRecyclerView = findViewById<RecyclerView>(R.id.playlist_recyclerview)
                            val playListData = ArrayList<PlaylistRecyclerAdapter.Item>()
                            Log.d("로그", "PlaylistActivity - onResponse() called ${responseBody.result!!}")
                            for(it in responseBody.result!!){
                                when (it.isGroup){
                                    0 -> {
                                        playListData.add(PlaylistRecyclerAdapter.Item(0, it.songs?.get(0)?.title, it.songs?.get(0)?.musicIdx, it.songs?.get(0)?.albumIdx, it.songs?.get(0)?.cover, it.songs?.get(0)?.artist))
                                    }
                                    1 -> {
                                        playListData.add(PlaylistRecyclerAdapter.Item(1, it.groupName, null, null, null, null))
                                        for(iit in it.songs!!){
                                            playListData.add(PlaylistRecyclerAdapter.Item(2, iit.title, iit.musicIdx, iit.albumIdx, iit.cover, iit.artist))
                                        }
                                    }
                                }
                            }
                            playListRecyclerView.adapter = PlaylistRecyclerAdapter(playListData)
                            playListRecyclerView.layoutManager = LinearLayoutManager(this@PlaylistActivity)
                        }
                        else -> {
                            Log.d("로그", "PlaylistActivity - onResponse() called")
                            Log.d("로그", "However, Response is not successful")
                            Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                            val errorIntent =
                                Intent(this@PlaylistActivity, MainActivity::class.java)
                            errorIntent.putExtra("error", "1")
                            startActivity(errorIntent)
                        }
                    }

                }
            })
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }
}

