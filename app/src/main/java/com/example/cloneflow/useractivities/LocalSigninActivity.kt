package com.example.cloneflow.useractivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cloneflow.R

class LocalSigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        Log.d("로그", "LocalSigninActivity - onCreate() called")
    }

    fun mOnEmailSigninClick(v : View){
        val emailSigninStartIntent = Intent(this, ClauseSigninActivity::class.java)
        startActivity(emailSigninStartIntent)
    }

    fun mOnKakaoLoginPopupClick(v : View) {
        val kakaoLoginPopupStartIntent = Intent(this, KakaoLoginPopupActivity::class.java)
        startActivityForResult(kakaoLoginPopupStartIntent, 1)
    }

}