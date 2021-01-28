package com.example.cloneflow

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocalSigninInfoInputActivity : AppCompatActivity() {

    lateinit var userphonenum : String
    lateinit var username : String
    var isPwCorrect : Boolean = false
    val PREFERENCE = "com.example.cloneflow"

    companion object {
        var BaseUrL = "https://www.heedong.dev/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_signin)
        val intent = intent
        username = intent.getStringExtra("signin_username")!!
        userphonenum = intent.getStringExtra("signin_userphonenum")!!
        val userInfoTextView = findViewById<TextView>(R.id.signin_signin_userinfo_text)
        val userInfoString = "이름 : $username / 휴대폰 번호 : $userphonenum"
        userInfoTextView.text = userInfoString
        addChangeListenerToTextInput()
    }

    fun showEmailFormatOptionDialog(v : View) {
        val userEmailFormat = findViewById<TextInputEditText>(R.id.signin_signin_user_email_format)
        val emailFormatOptionArray = arrayOf("naver.com", "hanmail.net", "daum.net", "gmail.com", "nate.com", "hotmail.com", "yahoo.co.kr", "paran.com", "lycos.co.kr", "empal.com", "cyworld.com", "dreamwiz.com")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("도메인")
        builder.setSingleChoiceItems(emailFormatOptionArray, 0) {dialogInterface, which ->
            val chosenEmailFormat = emailFormatOptionArray[which]
            userEmailFormat.setText(chosenEmailFormat)
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun addChangeListenerToTextInput() {
        val pw1Input = findViewById<EditText>(R.id.signin_signin_pw_1)
        val pw2Input = findViewById<EditText>(R.id.signin_signin_pw_2)
        val genderInput = findViewById<EditText>(R.id.signin_signin_gender)
        val inputFinBtn = findViewById<Button>(R.id.signin_signin_input_fin_btn)
        Log.d("로그", "LocalSigninInfoInputActivity - addChangeListenerToTextInput() called")
        pw2Input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val pw1 = pw1Input.text.toString()
                val pw2 = pw2Input.text.toString()
                Log.d("로그", "LocalSigninInfoInputActivity - $pw1 $pw2")
                if(pw1.length == pw2.length) {
                    if(pw1 == pw2) {
                        Log.d("로그", "LocalSigninInfoInputActivity - pw1 == pw2")
                        isPwCorrect = true
                    } else {
                        isPwCorrect = false
                        pw2Input.error = "비밀번호가 올바르지 않습니다."
                        Log.d("로그", "LocalSigninInfoInputActivity - 비밀번호가 올바르지 않습니다")
                        inputFinBtn.isClickable = false
                        inputFinBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_200))
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        genderInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.d("로그", "LocalSigninInfoInputActivity - ${genderInput.text}")
                if(isPwCorrect) {
                    Log.d("로그", "LocalSigninInfoInputActivity - all input field is filled")
                    inputFinBtn.isClickable = true
                    inputFinBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                } else {
                    inputFinBtn.isClickable = false
                    inputFinBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_200))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    fun onSigninBtnClicked(v : View) {
        Log.d("로그", "LocalSigninInfoInputActivity - onSigninBtnClicked() called")
        val emailInput = findViewById<EditText>(R.id.signin_signin_user_email)
        val emailFormatInput = findViewById<EditText>(R.id.signin_signin_user_email_format)
        val pw1Input = findViewById<EditText>(R.id.signin_signin_pw_1)
        val pw2Input = findViewById<EditText>(R.id.signin_signin_pw_2)
        val birthInput = findViewById<EditText>(R.id.signin_signin_birthdate)
        val genderInput = findViewById<EditText>(R.id.signin_signin_gender)

        val email = emailInput.text.toString() + "@" + emailFormatInput.text.toString()
        val pw1 = pw1Input.text.toString()
        val pw2 = pw2Input.text.toString()
        val birthday = birthInput.text.toString()
        val gender = if (genderInput.text.toString()[0].toInt()%2==0) "F" else "M"

        val retrofit : Retrofit = Retrofit.Builder().baseUrl(BaseUrL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(UserService.SignUpService::class.java)
        val sendJsonObject = JsonObject()
        sendJsonObject.addProperty("name", username)
        sendJsonObject.addProperty("tel", userphonenum)
        sendJsonObject.addProperty("email", email)
        sendJsonObject.addProperty("pwd", pw1)
        sendJsonObject.addProperty("pwdCheck", pw2)
        sendJsonObject.addProperty("birth", birthday)
        sendJsonObject.addProperty("gender", gender)
        Log.d("로그", "LocalSigninInfoInputActivity - $sendJsonObject")
        val call = service.postSignUp(sendJsonObject)
        call.enqueue(object: Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>?, t: Throwable) {
                Log.d("로그", "LocalSigninInfoInputActivity - However, onFailure() called ${t.message}")
            }
            override fun onResponse(call: Call<SignUpResponse>?, response: Response<SignUpResponse>) {
                val responseBody = response.body()
                Log.d("로그", "LocalSigninInfoInputActivity - response $response responseBody $responseBody")
                when {
                    responseBody == null -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - response body is null ㅋㅋㅋ")
                    }
                    responseBody.isSuccess!! -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - isSuccess! code ${responseBody!!.code} | message ${responseBody.message} | jwt ${responseBody.jwt}")
                        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("jwt", responseBody.jwt)
                        editor.putString("username", username)
                        editor.apply()
                        finish()
                        startActivity(Intent(this@LocalSigninInfoInputActivity, MainActivity::class.java))
                    }
                    else -> {
                        Log.d("로그", "LocalSigninInfoInputActivity - isNotSuccess! code ${responseBody!!.code} | message ${responseBody.message}")
                        when (response.code()) {
                            300 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "전화번호를 입력하거라", Toast.LENGTH_SHORT).show() }
                            301 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "이메일을 입력하거라", Toast.LENGTH_SHORT).show() }
                            302 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "이메일 형식이 이상하다", Toast.LENGTH_SHORT).show() }
                            303 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "비밀번호를 입력 안 했다", Toast.LENGTH_SHORT).show() }
                            304 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "비밀번호 형식 오류 (8~20자)", Toast.LENGTH_SHORT).show() }
                            305 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "비밀번호 확인 미입력", Toast.LENGTH_SHORT).show() }
                            306 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "비밀번호 확인 불일치", Toast.LENGTH_SHORT).show() }
                            307 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "생년월일 미입력", Toast.LENGTH_SHORT).show() }
                            308 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "생년월일 형식 오류", Toast.LENGTH_SHORT).show() }
                            309 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "성별 입력 오류 ", Toast.LENGTH_SHORT).show() }
                            310 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "성별 입력 형식 오류", Toast.LENGTH_SHORT).show() }
                            311 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "이용약관 입력값 형식 오류", Toast.LENGTH_SHORT).show() }
                            312 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "제3자 제공 입력값 형식 오류", Toast.LENGTH_SHORT).show() }
                            313 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "처리위탁동의 입력값 형식 오류", Toast.LENGTH_SHORT).show() }
                            314 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "광고 수신 입력값 형식 오류", Toast.LENGTH_SHORT).show() }
                            315 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "14세 이상 입력값 형식 오류", Toast.LENGTH_SHORT).show() }
                            401 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "중복된 이메일", Toast.LENGTH_SHORT).show() }
                            402 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "인증되지 않은 전화번호", Toast.LENGTH_SHORT).show() }
                            500 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "DB Error : err메시지", Toast.LENGTH_SHORT).show() }
                            501 -> { Toast.makeText(this@LocalSigninInfoInputActivity, "트랜잭션 에러", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            }
        })
    }

}