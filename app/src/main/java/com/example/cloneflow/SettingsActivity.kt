package com.example.cloneflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun mOnLoginSigninClick(v : View) {
        val LoginStartIntent = Intent(this, LoginActivity::class.java)
        startActivity(LoginStartIntent)
        //overridePendingTransition(R.anim.popup_open, R.anim.popup_close)
    }
}