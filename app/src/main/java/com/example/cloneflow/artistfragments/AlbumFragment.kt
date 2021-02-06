package com.example.cloneflow.artistfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.InfoArtistAlbumRecyclerAdapter
import com.example.cloneflow.services.ArtistAlbumResult
import com.example.cloneflow.services.InfoService
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlbumFragment(val idx : Int) : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist_albums, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.ArtistService::class.java)
            val call = service.getArtistAlbumInfo(token = token, idx = idx, sort = null, type = null)
            call.enqueue(object : Callback<ArtistAlbumResult>{
                override fun onFailure(call: Call<ArtistAlbumResult>, t: Throwable) {
                    Log.d("로그", "AlbumFragment - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<ArtistAlbumResult>,
                    response: Response<ArtistAlbumResult>
                ) {
                    val responseBody = response.body()!!
                    Log.d("로그", "AlbumFragment - onResponse() called")
                    when{
                        responseBody.isSuccess!! -> {
                            val recyclerView = view.findViewById<RecyclerView>(R.id.info_artist_album_recyclerview)
                            recyclerView.adapter = InfoArtistAlbumRecyclerAdapter(responseBody.result!!.albums!!)
                            recyclerView.layoutManager = GridLayoutManager(context, 2)
                        }
                        else -> {
                            Log.d("로그", "However, Response was not successful")
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