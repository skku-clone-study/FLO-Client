package com.example.cloneflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("로그", "MainActivity - onCreate() called")
    }

    fun mOnSettingClick(v : View) {
        val settingsStartIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsStartIntent)
    }
}