package com.example.cloneflow

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LocalSigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        Log.d("로그", "LocalSigninActivity - onCreate() called")
    }

    public fun mOnEmailSigninClick(v : View){
        val emailSigninStartIntent = Intent(this, ClauseSigninActivity::class.java)
        startActivity(emailSigninStartIntent)
    }

    public fun mOnKakaoLoginPopupClick(v : View) {
        val kakaoLoginPopupStartIntent = Intent(this, KakaoLoginPopupActivity::class.java)
        startActivityForResult(kakaoLoginPopupStartIntent, 1)
    }

}