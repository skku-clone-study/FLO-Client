package com.example.cloneflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cloneflow.services.UserResponse
import com.example.cloneflow.services.UserService
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    val PREFERENCE = "com.example.cloneflow"

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

    fun mOnLoginClick(v : View){
        val emailInput = findViewById<TextInputEditText>(R.id.login_email).text.toString()
        val emailFormatInput = findViewById<TextInputEditText>(R.id.login_emil_format).text.toString()
        val passwordInput = findViewById<TextInputEditText>(R.id.login_pw)

        val email = "$emailInput@$emailFormatInput"
        val password = passwordInput.text.toString()

        val retrofit : Retrofit = Retrofit.Builder().baseUrl(LocalSigninInfoInputActivity.BaseUrL).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retrofit.create(UserService.SignInService::class.java)
        val sendJsonObject = JsonObject()
        sendJsonObject.addProperty("email", email)
        sendJsonObject.addProperty("pwd", password)
        Log.d("로그", "LoginActivity - $sendJsonObject")
        val call = service.postSignIn(sendJsonObject)
        call.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("로그", "LoginActivity - However, onFailure() called ${t.message}")
            }
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val responseBody = response.body()
                Log.d("로그", "LoginActivity - onResponse() called")
                when {
                    responseBody == null -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - response body is null ㅋㅋㅋ")
                    }
                    responseBody.isSuccess!! -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - isSuccess! code ${responseBody!!.code} | message ${responseBody.message} | jwt ${responseBody.jwt}")
                        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("jwt", responseBody.jwt)
                        editor.apply()
                        finish()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                    else -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - isNotSuccess! code ${responseBody!!.code} | message ${responseBody.message}")
                        when (response.code()) {
                            301 -> { Toast.makeText(this@LoginActivity, "이메일을 입력하거라", Toast.LENGTH_SHORT).show() }
                            302 -> { Toast.makeText(this@LoginActivity, "이메일 형식이 이상하다", Toast.LENGTH_SHORT).show() }
                            303 -> { Toast.makeText(this@LoginActivity, "비밀번호를 입력 안 했다", Toast.LENGTH_SHORT).show() }
                            400 -> { Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show() }
                            401 -> { Toast.makeText(this@LoginActivity, "중복된 이메일", Toast.LENGTH_SHORT).show() }
                            402 -> { Toast.makeText(this@LoginActivity, "탈퇴된 계정", Toast.LENGTH_SHORT).show() }
                            500 -> { Toast.makeText(this@LoginActivity, "DB Error : err메시지", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            }
        })

    }

}