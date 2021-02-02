package com.example.cloneflow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cloneflow.services.StreamingResponse
import com.example.cloneflow.services.StreamingService
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StreamingActivity : AppCompatActivity() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streaming)
        overridePendingTransition(R.anim.fade_in, R.anim.none)
        val musicIndex = intent.getIntExtra("musicIdx", 0)
        if(musicIndex != 0){
            val token = getToken()
            if(token == null ){
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                val retrofit: Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
                val service = retrofit!!.create(StreamingService.StreamingService::class.java)
                val call = service.getStreaming(token = token, idx = musicIndex)
                call.enqueue(object : Callback<StreamingResponse>{
                    override fun onFailure(call: Call<StreamingResponse>, t: Throwable) {
                        Log.d("로그", "StreamingActivity - onFailure() called - $t")
                    }
                    override fun onResponse(
                        call: Call<StreamingResponse>,
                        response: Response<StreamingResponse>
                    ) {
                        Log.d("로그", "StreamingActivity - onResponse() called")
                        val responseBody = response.body()!!
                        when{
                            responseBody.isSuccess!! -> {
                                val result = responseBody.result!!
                                findViewById<TextView>(R.id.streaming_title).text = result.title.toString()
                                findViewById<TextView>(R.id.streaming_artist).text = result.artist.toString()
                                findViewById<TextView>(R.id.streaming_lyric).text = result.lyrics.toString()
                                Glide.with(this@StreamingActivity).load(result.src.toString())
                                    .into(findViewById(R.id.streaming_albumcover))
                            }
                            else -> {
                                Log.d("로그", "StreamingActivity - onResponse() called")
                                Log.d("로그", "However, Response is not successful")
                                Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                                val errorIntent =
                                    Intent(this@StreamingActivity, MainActivity::class.java)
                                errorIntent.putExtra("error", "1")
                                startActivity(errorIntent)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

}