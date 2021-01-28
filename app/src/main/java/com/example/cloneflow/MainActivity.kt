package com.example.cloneflow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    val PREFERENCE = "com.example.cloneflow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("로그", "MainActivity - onCreate() called")
        val jwt = getToken()
        val text3 = findViewById<TextView>(R.id.text3)
        if(jwt == "") {
            text3.text = "로그인 되지 않음"
        } else {
            text3.text = "로그인 된 상태"
        }
    }

    fun mOnSettingClick(v : View) {
        val settingsStartIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsStartIntent)
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref.getString("jwt", "")
    }
}