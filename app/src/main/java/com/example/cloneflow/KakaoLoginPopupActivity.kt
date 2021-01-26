package com.example.cloneflow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window

class KakaoLoginPopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_kakao_popup)
        window.setWindowAnimations(R.style.CustomDialogAnimation) // not working
    }

    public fun mOnClose(v : View) {
        val kakaoLoginPopupCloseIntent = Intent()
        setResult(RESULT_OK, kakaoLoginPopupCloseIntent)
        finish()
    }
}