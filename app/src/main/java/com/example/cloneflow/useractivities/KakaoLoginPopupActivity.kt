package com.example.cloneflow.useractivities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.cloneflow.R

class KakaoLoginPopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_kakao_popup)
        window.setWindowAnimations(R.style.CustomDialogAnimation) // not working
    }

    fun mOnClose(v : View) {
        val kakaoLoginPopupCloseIntent = Intent()
        setResult(RESULT_OK, kakaoLoginPopupCloseIntent)
        finish()
    }
}