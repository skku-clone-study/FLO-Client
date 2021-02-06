package com.example.cloneflow.infoactivities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import com.example.cloneflow.artistfragments.AlbumFragment
import com.example.cloneflow.artistfragments.SongsFragment
import com.example.cloneflow.artistfragments.VideoFragment
import com.example.cloneflow.mainfragments.ChartFragment
import com.example.cloneflow.mainfragments.HomeFragment
import com.example.cloneflow.mainfragments.SearchFragment
import com.example.cloneflow.mainfragments.StorageFragment
import com.example.cloneflow.services.ArtistInfoResult
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

class InfoArtistActivity : AppCompatActivity() {

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
        overridePendingTransition(R.anim.fade_in, R.anim.none)
        setContentView(R.layout.activity_info_artist)

        val artistIdx = intent.getIntExtra("artistIdx", -1)
        if(artistIdx == -1){
            val errorIntent = Intent(this@InfoArtistActivity, MainActivity::class.java)
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
                val service = retrofit!!.create(InfoService.ArtistService::class.java)
                val call = service.getArtistInfo(token = token, idx = artistIdx)
                call.enqueue(object : Callback<ArtistInfoResult>{
                    override fun onFailure(call: Call<ArtistInfoResult>, t: Throwable) {
                        Log.d("로그", "InfoArtistActivity - onFailure() called - $t")
                    }
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onResponse(
                        call: Call<ArtistInfoResult>,
                        response: Response<ArtistInfoResult>
                    ) {
                        val responseBody = response.body()!!
                        if(responseBody.isSuccess!!){
                            val responseResult = responseBody.result!!
                            Log.d("로그", "InfoArtistActivity - onResponse() called")
                            Glide.with(this@InfoArtistActivity).load(responseResult.profileImg).into(findViewById<ImageView>(R.id.info_artist_profilepic))
                            findViewById<TextView>(R.id.info_artist_name).text = responseResult.artistName
                            findViewById<TextView>(R.id.info_artist_type).text = responseResult.artistType
                            findViewById<TextView>(R.id.info_artist_genre).text = responseResult.artistGenre

                            val fragmentManager =
                                (this@InfoArtistActivity as FragmentActivity).supportFragmentManager
                            val adapter = ViewPagerAdapter(fragmentManager)
                            adapter.addFragment(SongsFragment(artistIdx), "곡")
                            adapter.addFragment(AlbumFragment(artistIdx), "앨범")
                            adapter.addFragment(VideoFragment(artistIdx), "영상")
                            val viewPager = findViewById<ViewPager>(R.id.view_pager)
                            viewPager.adapter = adapter
                            val tabs = findViewById<TabLayout>(R.id.tabs)
                            tabs.setupWithViewPager(viewPager)
                            val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
                            appBarLayout.outlineProvider = null
                        }
                        else {
                            Log.d("로그", "InfoArtistActivity - onResponse() called")
                            Log.d("로그", "However, Response is not successful")
                            Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                            val errorIntent = Intent(this@InfoArtistActivity, MainActivity::class.java)
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
        if(isFinishing){
            overridePendingTransition(
                R.anim.none,
                R.anim.fade_out
            )
        }
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(InfoAlbumActivity.PREFERENCE, Context.MODE_PRIVATE)
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