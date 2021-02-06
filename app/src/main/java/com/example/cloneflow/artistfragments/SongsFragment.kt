package com.example.cloneflow.artistfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.InfoArtistTrackRecyclerAdapter
import com.example.cloneflow.services.ArtistTrackResult
import com.example.cloneflow.services.InfoService
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongsFragment(val idx : Int) : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist_songs, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.ArtistService::class.java)
            val call = service.getArtistTrackInfo(token = token, idx = idx, type = null, sort = null)
            call.enqueue(object : Callback<ArtistTrackResult> {
                override fun onFailure(call: Call<ArtistTrackResult>, t: Throwable) {
                    Log.d("로그", "SongsFragment - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<ArtistTrackResult>,
                    response: Response<ArtistTrackResult>
                ) {
                    Log.d("로그", "SongsFragment - onResponse() called")
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "SongFragment - onResponse() called")
                            Log.d("로그", "Response - $responseBody")
                            val albumTracks = responseBody.result!!.songs
                            if (albumTracks != null) {
                                val trackRecyclerView = view.findViewById<RecyclerView>(R.id.info_artist_song_recyclerview)
                                trackRecyclerView.adapter = InfoArtistTrackRecyclerAdapter(albumTracks)
                                trackRecyclerView.layoutManager = LinearLayoutManager(context)
                            } // else { 곡이 없습니다.. 창 띄우기 }
                        }
                        else -> {
                            Log.d("로그", "SongFragment - onResponse() called")
                            Log.d("로그", "However, Response is not successful")
                            Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                            val errorIntent = Intent(requireActivity(), MainActivity::class.java)
                            errorIntent.putExtra("error", "1")
                            startActivity(errorIntent)
                        }
                    }
                }
            })
        }
        return view
    }

    private fun getToken(): String? {
        val pref = this.activity?.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

}