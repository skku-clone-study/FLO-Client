package com.example.cloneflow

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate

class ClauseSigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_clause)
        Log.d("로그", "ClauseSigninActivity - onCreate() called")
    }

    fun mOnClauseNextClick(v : View){
        if(v.findViewById<Button>(R.id.signin_clause_btn_next).isClickable){
            val phoneAuthStartIntent = Intent(this, PhoneAuthActivity::class.java)
            startActivity(phoneAuthStartIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRadioButtonClicked(view: View) {
        if (view is CheckBox) {
            val checked = view.isChecked
            Log.d("로그", "${view.getId()} $checked")
            when (view.getId()) {
                R.id.signin_radio_clause_all ->
                    if (checked) {
                        Log.d("로그", "MainActivity - all check")
                        findViewById<CheckBox>(R.id.signin_radio_clause1).isChecked = true
                        findViewById<CheckBox>(R.id.signin_radio_clause2).isChecked = true
                        findViewById<CheckBox>(R.id.signin_radio_clause3).isChecked = true
                        findViewById<CheckBox>(R.id.signin_radio_clause4).isChecked = true
                        findViewById<CheckBox>(R.id.signin_radio_clause5).isChecked = true
                        onAdvClauseChecked()
                    } else {
                        Log.d("로그", "MainActivity - all uncheck")
                        findViewById<CheckBox>(R.id.signin_radio_clause1).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause2).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause3).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause4).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause5).isChecked = false
                    }
                R.id.signin_radio_clause5 -> {
                    if(checked) {
                        onAdvClauseChecked()
                    }
                }
            }
            isAllRequiredRadioButtonClicked()
        }
    }

    private fun isAllRequiredRadioButtonClicked(){
        val btnNext = findViewById<Button>(R.id.signin_clause_btn_next)
        val cBox1 = findViewById<CheckBox>(R.id.signin_radio_clause1)
        val cBox2 = findViewById<CheckBox>(R.id.signin_radio_clause2)
        val cBox3 = findViewById<CheckBox>(R.id.signin_radio_clause3)
        btnNext.isClickable = cBox1.isChecked && cBox2.isChecked && cBox3.isChecked
        if(cBox1.isChecked && cBox2.isChecked && cBox3.isChecked) {
            btnNext.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            btnNext.isClickable = true
        } else {
            btnNext.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_400))
            btnNext.isClickable = false
        }
        val cBox4 = findViewById<CheckBox>(R.id.signin_radio_clause4)
        val cBox5 = findViewById<CheckBox>(R.id.signin_radio_clause5)
        val cBoxAll = findViewById<CheckBox>(R.id.signin_radio_clause_all)
        if(cBox1.isChecked && cBox2.isChecked && cBox3.isChecked && cBox4.isChecked && cBox5.isChecked) {
            if(cBoxAll.isChecked){} else{cBoxAll.isChecked = true}
        } else {
            if(cBoxAll.isChecked){ cBoxAll.isChecked = false}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onAdvClauseChecked(){
        val localDate : LocalDate = LocalDate.now()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("[FLO] ${localDate.year}년 ${localDate.monthValue}월 ${localDate.dayOfMonth}일\n광고수신동의 처리되었습니다.")
            .setPositiveButton("확인"){ _: DialogInterface, _: Int -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#4D2BFF"))
    }

}