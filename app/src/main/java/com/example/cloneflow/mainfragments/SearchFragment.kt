package com.example.cloneflow.mainfragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.cloneflow.LoginActivity
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.ViewPagerAdapter
import com.example.cloneflow.albumfragments.DetailFragment
import com.example.cloneflow.albumfragments.SongsFragment
import com.example.cloneflow.albumfragments.VideoFragment
import com.example.cloneflow.services.AlbumInfoResponse
import com.example.cloneflow.services.InfoService
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_search_album, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(InfoService.AlbumInfoService::class.java)
            val call = service.getAlbumInfo(token = token, idx = 1)
            call.enqueue(object : Callback<AlbumInfoResponse>{
                override fun onFailure(call: Call<AlbumInfoResponse>, t: Throwable) {
                    Log.d("로그", "ChartFragment - onFailure() called - $t")
                    val errorIntent = Intent(requireActivity(), MainActivity::class.java)
                    errorIntent.putExtra("error", "1")
                    startActivity(errorIntent)
                }
                override fun onResponse(call: Call<AlbumInfoResponse>, response: Response<AlbumInfoResponse>) {
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "SearchFragment - onResponse() called - response body ${responseBody.result!!}")
                            val responseResult = responseBody.result!!
                            val albumName = responseResult.title.toString()
                            val albumArtist = responseResult.artist.toString()
                            val albumReleasedDate = responseResult.releasedAt.toString()
                            val albumType = responseResult.albumType.toString()
                            val albumGenre = responseResult.albumGenre.toString()
                            val albumCoverSrc = responseResult.cover.toString()
                            val albumIdx = responseResult.albumIdx!!.toInt()
                            val descStr = "$albumReleasedDate | $albumType | $albumGenre"

                            view.findViewById<TextView>(R.id.album_title).text = albumName
                            view.findViewById<TextView>(R.id.album_artist).text = albumArtist
                            view.findViewById<TextView>(R.id.album_short_desc).text = descStr
                            Glide.with(view).load(albumCoverSrc).into(view.findViewById(R.id.album_thumbnail_img))

                            Log.d("로그", "SearchFragment - album cover src => $albumCoverSrc")
                            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
                            val adapter = ViewPagerAdapter(fragmentManager)
                            adapter.addFragment(SongsFragment(albumIdx), "수록곡")
                            adapter.addFragment(DetailFragment(albumIdx), "상세정보")
                            adapter.addFragment(VideoFragment(albumIdx), "영상")
                            val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
                            viewPager.adapter = adapter
                            val tabs = view!!.findViewById<TabLayout>(R.id.tabs)
                            tabs.setupWithViewPager(viewPager)
                            val appBarLayout = view.findViewById<AppBarLayout>(R.id.app_bar_layout)
                            appBarLayout.outlineProvider = null
                        }
                        else -> {
                            Log.d("로그", "SearchFragment - onResponse() called")
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