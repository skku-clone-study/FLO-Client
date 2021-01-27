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
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class PhoneAuthActivity : AppCompatActivity() {

    companion object {
        var BaseUrL = "http://sms.heedong.dev/api/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_authorize_by_phone)

        val sendMessageBtn = findViewById<Button>(R.id.signin_auth_by_phone_send_message)
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.signin_abp_phonenumber)
        phoneNumberInput.addTextChangedListener(object:TextWatcher{
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun afterTextChanged(s: Editable?) {
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
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.signin_abp_phonenumber)
        val usernameInput = findViewById<TextInputEditText>(R.id.signin_abp_name)
        val phoneAuthFinBtn = findViewById<Button>(R.id.signin_abp_finished_btn)
        val phoneNumber = phoneNumberInput.text.toString()
        val username = usernameInput.text.toString()
        val retrofit : Retrofit = Retrofit.Builder().baseUrl(BaseUrL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(SMSService.SmsSendService::class.java)
        val sendJsonObject = JsonObject()
        sendJsonObject.addProperty("name", username)
        sendJsonObject.addProperty("tel", phoneNumber)
        val call = service.postSmsAuth(sendJsonObject)
        Log.d("로그", "PhoneAuthActivity - call was successful , $sendJsonObject")
        call.enqueue(object: Callback<SMSResponse> {
            override fun onFailure(call: Call<SMSResponse>?, t: Throwable) {
                Log.d("로그", "PhoneAuthActivity - However, onFailure() called ${t.message}")
            }
            override fun onResponse(call: Call<SMSResponse>?, response: Response<SMSResponse>) {
                Log.d("로그", "PhoneAuthActivity - onResponse() called")
                val smsResponse = response.body()
                val statusCode = smsResponse!!.code
                val isSuccess = smsResponse.isSuccess
                val statusMessage = smsResponse.message
                Log.d("로그", "PhoneAuthActivity - status code $statusCode | is success $isSuccess | status message $statusMessage")
                val authInput = findViewById<TextInputEditText>(R.id.signin_abp_auth_number)
                authInput.visibility = View.VISIBLE
                authInput.addTextChangedListener(object :TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
                        if(authInput.text.toString().length == 6){
                            val service2 = retrofit.create(SMSService.SmsCheckService::class.java)
                            val sendJsonObject2 = JsonObject()
                            sendJsonObject2.addProperty("tel", phoneNumber)
                            sendJsonObject2.addProperty("check", authInput.text.toString())
                            val call2 = service2.postSmsAuth(sendJsonObject2)
                            call2.enqueue(object : Callback<SMSResponse>{
                                override fun onFailure(call: Call<SMSResponse>, t: Throwable) {
                                    Log.d("로그", "PhoneAuthActivity - onFailure() called ${t.message}")
                                }
                                override fun onResponse(call: Call<SMSResponse>, response: Response<SMSResponse>) {
                                    val smsResponse2 = response.body()
                                    val isSuccess2 = smsResponse2?.isSuccess
                                    if(isSuccess2!!) {
                                        phoneAuthFinBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.flo_blue))
                                        phoneAuthFinBtn.isClickable = true
                                        authInput.error = null
                                    } else {
                                        phoneAuthFinBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray_400))
                                        phoneAuthFinBtn.isClickable = false
                                        authInput.error = "인증번호를 다시 입력해주세요"
                                    }
                                }
                            })
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