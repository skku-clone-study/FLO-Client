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
        setContentView(R.layout.activity_login)
        Log.d("로그", "MainActivity - onCreate() called")
    }

    public fun mOnKakaoLoginPopupClick(v : View) {
        val kakaoLoginPopupStartIntent = Intent(this, KakaoLoginPopupActivity::class.java)
        startActivityForResult(kakaoLoginPopupStartIntent, 1)
    }

    public fun mOnLocalSigninClick(v : View){
        val localSigninStartIntent = Intent(this, LocalSigninActivity::class.java)
        startActivity(localSigninStartIntent)
    }

}