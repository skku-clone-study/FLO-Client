package com.example.cloneflow.songfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.InfoVideoRecyclerAdapter
import com.example.cloneflow.services.InfoService
import com.example.cloneflow.services.SongDetailResult
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongInfoFragment(val idx : Int) : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_song_info, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(SongVideoFragment.BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.SongService::class.java)
            val call = service.getSongDetailInfo(token = token, idx = idx)
            call.enqueue(object : Callback<SongDetailResult>{
                override fun onFailure(call: Call<SongDetailResult>, t: Throwable) {
                    Log.d("로그", "SongInfoFragment - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<SongDetailResult>,
                    response: Response<SongDetailResult>
                ) {
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "SongInfoFragment - onResponse() called - $responseBody")
                            val responseResult = responseBody.result!!
                            view.findViewById<TextView>(R.id.song_title).text = responseResult.title
                            view.findViewById<TextView>(R.id.song_composor).text = responseResult.composer
                            view.findViewById<TextView>(R.id.song_lyricist).text = responseResult.lyricist
                            view.findViewById<TextView>(R.id.song_arrangement).text = responseResult.arrangement
                            view.findViewById<TextView>(R.id.song_lyrics).text = responseResult.lyrics
                        }
                        else -> {
                            Log.d("로그", "SongInfoFragment - onResponse() called")
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