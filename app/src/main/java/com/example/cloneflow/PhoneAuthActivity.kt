package com.example.cloneflow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class PhoneAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_authorize_by_phone)
        Log.d("로그", "PhoneAuthActivity - onCreate() called")
        val sendMessageBtn = findViewById<Button>(R.id.signin_auth_by_phone_send_message)
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.signin_abp_phonenumber)
        phoneNumberInput.addTextChangedListener(object:TextWatcher{
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun afterTextChanged(s: Editable?) {
                Log.d("로그", "PhoneAuthActivity - afterTextChanged() called, string ${phoneNumberInput.text.toString()}")
                if(phoneNumberInput.text.toString().length >= 9) {
                    sendMessageBtn.isClickable = true
                    sendMessageBtn.background = getDrawable(R.drawable.corner_rounded_flo_blue_btn)
                } else {
                    sendMessageBtn.isClickable = false
                    sendMessageBtn.background = getDrawable(R.drawable.corner_rounded_grayy_400_btn)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    public fun mOnSendMessageClicked(v : View) {
        // Do Send Message
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.signin_abp_phonenumber)
        val phoneAuthFinBtn = findViewById<Button>(R.id.signin_abp_finished_btn)
        val authInput = findViewById<TextInputEditText>(R.id.signin_abp_auth_number)
        authInput.visibility = View.VISIBLE
        authInput.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(authInput.text.toString().length == 6){
                    // 요청 전송하고 상태 코드 받아옴
                    // api/sms-auth/check/ 쿼리는 name tell check
                    if(authInput.text.toString() == "123456") { // 여기 조건 수정
                        phoneAuthFinBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.flo_blue))
                        phoneAuthFinBtn.isClickable = true
                        authInput.error = null
                    } else {
                        phoneAuthFinBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray_400))
                        phoneAuthFinBtn.isClickable = false
                        authInput.error = "인증번호를 다시 입력해주세요"
                    }
                } else {
                    phoneAuthFinBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray_400))
                    phoneAuthFinBtn.isClickable = false
                    authInput.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun mOnPhoneAuthFinishBtnClicked(v : View) {
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.signin_abp_phonenumber)
        val userNameInput = findViewById<TextInputEditText>(R.id.signin_abp_name)
        val localSigninInfoInputStartIntent = Intent(this, LocalSigninInfoInputActivity::class.java)
        localSigninInfoInputStartIntent.putExtra("signin_username", userNameInput.text.toString())
        localSigninInfoInputStartIntent.putExtra("signin_userphonenum", phoneNumberInput.text.toString())
        startActivity(localSigninInfoInputStartIntent)
    }

}