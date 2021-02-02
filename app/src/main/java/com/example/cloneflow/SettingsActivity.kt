package com.example.cloneflow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cloneflow.useractivities.LoginActivity

class SettingsActivity : AppCompatActivity() {
    val PREFERENCE = "com.example.cloneflow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun mOnLoginSigninClick(v : View) {
        val LoginStartIntent = Intent(this, LoginActivity::class.java)
        startActivity(LoginStartIntent)
        //overridePendingTransition(R.anim.popup_open, R.anim.popup_close)
    }

    fun mOnLogOutClicked(v : View) {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
    }
}