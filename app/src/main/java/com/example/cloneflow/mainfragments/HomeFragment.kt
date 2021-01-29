package com.example.cloneflow.mainfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cloneflow.R

class HomeFragment : Fragment() {

    val PREFERENCE = "com.example.cloneflow"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val jwt = getToken()
        val text3 = view.findViewById<TextView>(R.id.text3)
        if(jwt == "") {
            text3.text = "로그인 되지 않음"
        } else {
            text3.text = "로그인 된 상태"
        }
        return view
    }
    private fun getToken(): String? {
        val pref = this.activity?.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", "")
    }
}