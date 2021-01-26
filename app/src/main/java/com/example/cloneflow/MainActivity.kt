package com.example.cloneflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    /* onRadioButtonClicked, isAllRequiredRadioButtonClicked 함수는 나중에 권한 페이지가 있는 액티비티로 옮기기!! */
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
                    } else {
                        Log.d("로그", "MainActivity - all uncheck")
                        findViewById<CheckBox>(R.id.signin_radio_clause1).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause2).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause3).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause4).isChecked = false
                        findViewById<CheckBox>(R.id.signin_radio_clause5).isChecked = false
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
        } else {
            btnNext.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_400))
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

    public fun mOnKakaoLoginPopupClick(v : View) {
        val kakaoLoginPopupStartIntent = Intent(this, KakaoLoginPopupActivity::class.java)
        startActivityForResult(kakaoLoginPopupStartIntent, 1)
    }

}