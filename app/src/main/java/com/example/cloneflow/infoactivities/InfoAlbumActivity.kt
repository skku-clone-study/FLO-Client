package com.example.cloneflow.infoactivities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.cloneflow.MainActivity
import com.example.cloneflow.R
import com.example.cloneflow.adapters.ViewPagerAdapter
import com.example.cloneflow.albumfragments.DetailFragment
import com.example.cloneflow.albumfragments.SongsFragment
import com.example.cloneflow.albumfragments.VideoFragment
import com.example.cloneflow.mainfragments.*
import com.example.cloneflow.services.AlbumInfoResponse
import com.example.cloneflow.services.InfoService
import com.example.cloneflow.useractivities.LoginActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoAlbumActivity : AppCompatActivity() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
        private val homeFragment = HomeFragment()
        private val chartFragment = ChartFragment()
        private val searchFragment = SearchFragment()
        private val storageFragment = StorageFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            R.anim.fade_in,
            R.anim.none
        )
        setContentView(R.layout.activity_info_album)
        val albumIndex = intent.getIntExtra("albumIdx", 0)
        if(albumIndex!=0){
            initNavigationBar()
            val token = getToken()
            if(token == null ){
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                val retrofit: Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
                val service = retrofit!!.create(InfoService.AlbumService::class.java)
                val call = service.getAlbumInfo(token = token, idx = albumIndex)
                call.enqueue(object : Callback<AlbumInfoResponse> {
                    override fun onFailure(call: Call<AlbumInfoResponse>, t: Throwable) {
                        Log.d("로그", "ChartFragment - onFailure() called - $t")
                        val errorIntent = Intent(this@InfoAlbumActivity, MainActivity::class.java)
                        errorIntent.putExtra("error", "1")
                        startActivity(errorIntent)
                    }
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onResponse(
                        call: Call<AlbumInfoResponse>,
                        response: Response<AlbumInfoResponse>
                    ) {
                        val responseBody = response.body()!!
                        when {
                            responseBody.isSuccess!! -> {
                                Log.d(
                                    "로그",
                                    "SearchFragment - onResponse() called - response body ${responseBody.result!!}"
                                )
                                val responseResult = responseBody.result!!
                                val albumName = responseResult.title.toString()
                                val albumArtist = responseResult.artist.toString()
                                val albumReleasedDate = responseResult.releasedAt.toString()
                                val albumType = responseResult.albumType.toString()
                                val albumGenre = responseResult.albumGenre.toString()
                                val albumCoverSrc = responseResult.cover.toString()
                                val albumIdx = responseResult.albumIdx!!.toInt()
                                val descStr = "$albumReleasedDate | $albumType | $albumGenre"

                                findViewById<TextView>(R.id.album_title).text = albumName
                                findViewById<TextView>(R.id.album_artist).text = albumArtist
                                findViewById<TextView>(R.id.album_artist).setOnClickListener{
                                    val artistIdx = responseResult.artistIdx!!.toInt()
                                    val artistInfoIntent = Intent(this@InfoAlbumActivity, InfoArtistActivity::class.java)
                                    artistInfoIntent.putExtra("artistIdx", artistIdx)
                                    startActivity(artistInfoIntent)
                                }
                                findViewById<TextView>(R.id.album_short_desc).text = descStr
                                Glide.with(this@InfoAlbumActivity).load(albumCoverSrc)
                                    .into(findViewById(R.id.album_thumbnail_img))

                                val fragmentManager =
                                    (this@InfoAlbumActivity as FragmentActivity).supportFragmentManager
                                val adapter = ViewPagerAdapter(fragmentManager)
                                adapter.addFragment(SongsFragment(albumIdx), "수록곡")
                                adapter.addFragment(DetailFragment(albumIdx), "상세정보")
                                adapter.addFragment(VideoFragment(albumIdx), "영상")
                                val viewPager = findViewById<ViewPager>(R.id.view_pager)
                                viewPager.adapter = adapter
                                val tabs = findViewById<TabLayout>(R.id.tabs)
                                tabs.setupWithViewPager(viewPager)
                                val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
                                appBarLayout.outlineProvider = null
                            }
                            else -> {
                                Log.d("로그", "SearchFragment - onResponse() called")
                                Log.d("로그", "However, Response is not successful")
                                Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                                val errorIntent =
                                    Intent(this@InfoAlbumActivity, MainActivity::class.java)
                                errorIntent.putExtra("error", "1")
                                startActivity(errorIntent)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
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
        finish()
        overridePendingTransition(
            R.anim.none,
            R.anim.fade_out
        )
    }
}