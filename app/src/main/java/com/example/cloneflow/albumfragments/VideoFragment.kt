package com.example.cloneflow.albumfragments

import android.content.Context
import android.content.Intent
import android.content.ReceiverCallNotAllowedException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.LoginActivity
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.InfoVideoRecyclerAdapter
import com.example.cloneflow.services.AlbumVideoInfoResponse
import com.example.cloneflow.services.InfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoFragment(val idx : Int) : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.AlbumVideoInfoService::class.java)
            val call = service.getAlbumVideoInfo(token = token, idx = idx, sort = 0)
            call.enqueue(object : Callback<AlbumVideoInfoResponse>{
                override fun onFailure(call: Call<AlbumVideoInfoResponse>, t: Throwable) {
                    Log.d("로그", "VideoFragment - onFailure() called")
                }
                override fun onResponse(
                    call: Call<AlbumVideoInfoResponse>,
                    response: Response<AlbumVideoInfoResponse>
                ) {
                    Log.d("로그", "VideoFragment - onResponse() called")
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "VideoFragment - onResponse() called")
                            Log.d("로그", "Response - $responseBody")
                            val result = responseBody.result!!
                            val albumVideos = result.videos
                            if(albumVideos!=null) {
                                val videoRecyclerView = view.findViewById<RecyclerView>(R.id.album_video_recyclerview)
                                videoRecyclerView.adapter = InfoVideoRecyclerAdapter(albumVideos)
                                videoRecyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        }
                        else -> {
                            Log.d("로그", "VideoFragment - onResponse() called")
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