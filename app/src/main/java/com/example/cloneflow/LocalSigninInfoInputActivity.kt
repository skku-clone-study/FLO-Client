package com.example.cloneflow

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText

class LocalSigninInfoInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_signin)
        val intent = intent
        val username = intent.getStringExtra("signin_username")
        val userphonenum = intent.getStringExtra("signin_userphonenum")
        val userInfoTextView = findViewById<TextView>(R.id.signin_signin_userinfo_text)
        val userInfoString = "이름 : $username / 휴대폰 번호 : $userphonenum"
        userInfoTextView.text = userInfoString
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
}