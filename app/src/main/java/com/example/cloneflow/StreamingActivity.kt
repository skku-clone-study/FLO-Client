package com.example.cloneflow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.cloneflow.services.StreamingResponse
import com.example.cloneflow.services.StreamingService
import com.example.cloneflow.useractivities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

class StreamingActivity : AppCompatActivity() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
        var isPlaying : Boolean = false
        var isFirst : Boolean = true
        var pos : Int? = null
        lateinit var mediaPlayer : MediaPlayer
        lateinit var seekbar : SeekBar
        lateinit var handleBtn : ImageButton
    }

    private class StreamingThread : Thread() {
        override fun run() {
            while(isPlaying) {
                seekbar.progress = mediaPlayer.currentPosition
            }
        }
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
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
                                mediaPlayer = MediaPlayer()
                                seekbar = findViewById(R.id.streaming_seekbar)
                                handleBtn = findViewById(R.id.streaming_handle_btn)
                                seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                        if (seekBar!!.max == progress) {
                                            handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_play_button_big))
                                            isPlaying = false
                                            mediaPlayer.stop()
                                        }
                                    }
                                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                        isPlaying = false
                                        mediaPlayer.pause()
                                    }
                                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                        isPlaying = true
                                        val ttt : Int = seekBar!!.progress
                                        mediaPlayer.seekTo(ttt)
                                        mediaPlayer.start()
                                        StreamingThread().start()
                                    }
                                })
                                handleBtn.setOnClickListener{
                                    Log.d("로그", "StreamingActivity - handlebtn clicked")
                                    if(isPlaying){ // pause를 누른 경우
                                        Log.d("로그", "isplaying $isPlaying isFirst $isFirst lets pause streaming")
                                        pauseStreaming()
                                    } else { // start를 누른 경우
                                        if(isFirst) { // 처음 트는 경우
                                            Log.d("로그", "isplaying $isPlaying isFirst $isFirst lets start streaming")
                                            startStreaming()
                                        } else { // 멈췄다가 다시 트는 경우
                                            Log.d("로그", "isplaying $isPlaying isFirst $isFirst lets restart streaming")
                                            restartStreaming()
                                        }
                                    }
                                }
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

    override fun onPause() {
        super.onPause()
        isPlaying = false
        if(mediaPlayer!=null){
            mediaPlayer.release()
        }
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startStreaming(){
        Log.d("로그", "StreamingActivity - startStreaming() called")
        StreamingThread().start()

        handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_pause))
        val URI = Uri.parse("https://www.bensound.com/bensound-music/bensound-summer.mp3")
        val audioAttributes = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build()
        mediaPlayer.setAudioAttributes(audioAttributes)
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(this@StreamingActivity, URI)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            seekbar.max = mediaPlayer.duration
            mediaPlayer.start()
        }
        val a = mediaPlayer.duration
        seekbar.max = a
        isFirst = false
        isPlaying = true
        Log.d("로그", "isplaying $isPlaying isfirst $isFirst")
    }

    private fun restartStreaming(){
        Log.d("로그", "StreamingActivity - restartStreaming() called")
        isPlaying = true
        handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_pause))
        mediaPlayer.seekTo(pos!!)
        mediaPlayer.start()
        StreamingThread().start()
    }

    private fun pauseStreaming(){
        pos = mediaPlayer.currentPosition
        Log.d("로그", "StreamingActivity - pauseStreaming() called position $pos")
        mediaPlayer.pause()
        isPlaying = false
        handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_play_button_big))
    }

}