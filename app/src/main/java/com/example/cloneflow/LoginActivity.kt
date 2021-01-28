package com.example.cloneflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun mOnKakaoLoginPopupClick(v : View) {
        val kakaoLoginPopupStartIntent = Intent(this, KakaoLoginPopupActivity::class.java)
        startActivityForResult(kakaoLoginPopupStartIntent, 1)
    }

    fun mOnLocalSigninClick(v : View){
        val localSigninStartIntent = Intent(this, LocalSigninActivity::class.java)
        startActivity(localSigninStartIntent)
    }

}