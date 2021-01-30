package com.example.cloneflow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cloneflow.mainfragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {


    companion object {
        private val homeFragment = HomeFragment()
        private val chartFragment = ChartFragment()
        private val searchFragment = SearchFragment()
        private val storageFragment = StorageFragment()
        private val errorFragment = ErrorFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base)
        Log.d("로그", "MainActivity - onCreate() called")

        if (intent?.getStringExtra("error") != null && intent?.getStringExtra("error") == "1") {
            changeFramgent(errorFragment)
        } else {
            initNavigationBar()
        }
    }

    fun mOnSettingClick(v : View) {
        val settingsStartIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsStartIntent)
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
            selectedItemId = R.id.bottomNavigationHomeMenuId
        }
    }

    private fun changeFramgent(fragment: Fragment) {
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.base_frame_layout, fragment)
        fragmentTransaction.commit()
    }

}