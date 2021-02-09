package com.example.cloneflow.infoactivities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.ViewPagerAdapter
import com.example.cloneflow.mainfragments.ChartFragment
import com.example.cloneflow.mainfragments.HomeFragment
import com.example.cloneflow.mainfragments.SearchFragment
import com.example.cloneflow.mainfragments.StorageFragment
import com.example.cloneflow.services.InfoService
import com.example.cloneflow.services.LikeService
import com.example.cloneflow.services.LikedResponse
import com.example.cloneflow.services.SongInfoResult
import com.example.cloneflow.songfragments.SimilarSongFragment
import com.example.cloneflow.songfragments.SongInfoFragment
import com.example.cloneflow.songfragments.SongVideoFragment
import com.example.cloneflow.useractivities.LoginActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoSongActivity : AppCompatActivity() {

    companion object {
        var idx : Int? = null
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
        private val homeFragment = HomeFragment()
        private val chartFragment = ChartFragment()
        private val searchFragment = SearchFragment()
        private val storageFragment = StorageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.none)
        setContentView(R.layout.activity_info_song)

        val songIdx = intent.getIntExtra("musicIdx", -1)
        if(songIdx == -1) {
            val errorIntent = Intent(this@InfoSongActivity, MainActivity::class.java)
            errorIntent.putExtra("error", "1")
            startActivity(errorIntent)
        } else {
            val token = getToken()
            if(token == null ){
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                val retrofit: Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
                val service = retrofit!!.create(InfoService.SongService::class.java)
                val call = service.getSongInfo(token = token, idx = songIdx)
                call.enqueue(object : Callback<SongInfoResult> {
                    override fun onFailure(call: Call<SongInfoResult>, t: Throwable) {
                        Log.d("로그", "InfoSongActivity - onFailure() called - $t")
                    }
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onResponse(
                        call: Call<SongInfoResult>,
                        response: Response<SongInfoResult>
                    ) {
                        val responseBody = response.body()!!
                        if(responseBody.isSuccess!!) {
                            val responseResult = responseBody.result!!
                            Log.d("로그", "InfoSongActivity - onResponse() called - $responseResult")
                            Glide.with(this@InfoSongActivity).load(responseResult.cover).into(findViewById(R.id.song_thumbnail))
                            findViewById<TextView>(R.id.info_song_title).text = responseResult.title
                            findViewById<TextView>(R.id.info_song_artist).text = responseResult.artist
                            findViewById<TextView>(R.id.info_song_album).text = responseResult.album
                            idx = responseResult.musicIdx!!
                            if(responseResult.isLiked == "TRUE") {
                                findViewById<ImageView>(R.id.like_btn).setImageDrawable(
                                    ContextCompat.getDrawable(this@InfoSongActivity, R.drawable.ic_like_filled))
                            }
                            val fragmentManager =
                                (this@InfoSongActivity as FragmentActivity).supportFragmentManager
                            val adapter = ViewPagerAdapter(fragmentManager)
                            adapter.addFragment(SongInfoFragment(songIdx), "상세정보")
                            adapter.addFragment(SimilarSongFragment(), "유사곡")
                            adapter.addFragment(SongVideoFragment(songIdx), "영상")
                            val viewPager = findViewById<ViewPager>(R.id.view_pager)
                            viewPager.adapter = adapter
                            val tabs = findViewById<TabLayout>(R.id.tabs)
                            tabs.setupWithViewPager(viewPager)
                            val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
                            appBarLayout.outlineProvider = null
                        } else {
                            Log.d("로그", "InfoSongActivity - onResponse() called")
                            Log.d("로그", "However, Response is not successful")
                            Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                            val errorIntent = Intent(this@InfoSongActivity, MainActivity::class.java)
                            errorIntent.putExtra("error", "1")
                            startActivity(errorIntent)
                        }
                    }
                })
                initNavigationBar()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("로그", "InfoSongActivity - onBackPressed() called")
        if(isFinishing){
            overridePendingTransition(
                R.anim.none,
                R.anim.fade_out
            )
        }
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

    private fun initNavigationBar() {
        Log.d("로그", "MainActivity - initNavigationBar() called")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.base_bot_nav_id)
        bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.bottomNavigationHomeMenuId -> {
                        changeFramgent(homeFragment)
                    }
                    R.id.bottomNavigationChartMenuId -> {
                        changeFramgent(chartFragment)
                    }
                    R.id.bottomNavigationSearchMenuId -> {
                        changeFramgent(searchFragment)
                    }
                    R.id.bottomNavigationStorageMenuId -> {
                        changeFramgent(storageFragment)
                    }
                }
                true
            }
        }
    }

    private fun changeFramgent(fragment: Fragment) {
        Log.d("로그", "InfoAlbumActivity - changeFramgent(${fragment}) called")
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.base_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun onBackBtnPressed(v : View){
        Log.d("로그", "InfoSongActivity - onBackBtnPressed() called")
        finish()
        overridePendingTransition(
            R.anim.none,
            R.anim.fade_out
        )
    }

    fun onHeartBtnClicked(v : View) {
        val token = getToken()
        val heartBtn = findViewById<ImageButton>(R.id.like_btn)
        if(token != null) {
            val retrofit: Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()
            ).build()
            val service = retrofit!!.create(LikeService.LikeOrUnlike::class.java)
            val sendJsonObject = JsonObject()
            sendJsonObject.addProperty("idx", idx)
            val call = service.postLiked(token= token, params=sendJsonObject, obj=0)
            call.enqueue(object : Callback<LikedResponse>{
                override fun onFailure(call: Call<LikedResponse>, t: Throwable) {
                    Log.d("로그", "InfoSongActivity - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<LikedResponse>,
                    response: Response<LikedResponse>
                ) {
                    Log.d("로그", "InfoSongActivity - onResponse() called")
                    when{
                        response.isSuccessful -> {
                            Log.d("로그", "[${response.body()!!.code}] ${response.body()!!.result!!.type} ${response.body()!!.message}")
                            when(response.body()!!.code){
                                200 -> {
                                    heartBtn.setImageDrawable(
                                        ContextCompat.getDrawable(this@InfoSongActivity, R.drawable.ic_like_filled))
                                }
                                201 -> {
                                    heartBtn.setImageDrawable(
                                        ContextCompat.getDrawable(this@InfoSongActivity, R.drawable.ic_like))
                                }
                            }
                        }
                        else -> {
                            Log.d("로그", "However, response was not successful")
                        }
                    }
                }
            })
        }
    }

}