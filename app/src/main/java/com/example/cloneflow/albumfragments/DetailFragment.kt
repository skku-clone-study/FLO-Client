package com.example.cloneflow.albumfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cloneflow.useractivities.LoginActivity
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.services.AlbumInfoInfoResponse
import com.example.cloneflow.services.InfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailFragment(val idx : Int) : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.AlbumInfoInfoService::class.java)
            val call = service.getAlbumInfoInfo(token = token, idx = idx)
            call.enqueue(object : Callback<AlbumInfoInfoResponse>{
                override fun onFailure(call: Call<AlbumInfoInfoResponse>, t: Throwable) {
                    Log.d("로그", "DetailFragment - onFailure() called")
                }
                override fun onResponse(
                    call: Call<AlbumInfoInfoResponse>,
                    response: Response<AlbumInfoInfoResponse>
                ) {
                    Log.d("로그", "DetailFragment - onResponse() called")
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "DetailFragment - onResponse() called")
                            Log.d("로그", "Response - $responseBody")
                            val result = responseBody.result!!
                            val albumName = result.albumName
                            val albumArtist = result.artist
                            val albumrc = result.releaseCompany
                            val albumag = result.agency
                            val albumDescription = result.description

                            view.findViewById<TextView>(R.id.info_album_detail_albumname).text = albumName
                            view.findViewById<TextView>(R.id.info_album_detail_artist).text = albumArtist
                            view.findViewById<TextView>(R.id.info_album_detail_releasecompany).text = albumrc
                            view.findViewById<TextView>(R.id.info_album_detail_agency).text = albumag
                            view.findViewById<TextView>(R.id.info_album_detail_textview).text = albumDescription
                        }
                        else -> {
                            Log.d("로그", "DetailFragment - onResponse() called")
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