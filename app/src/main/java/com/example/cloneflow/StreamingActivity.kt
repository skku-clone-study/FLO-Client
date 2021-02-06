package com.example.cloneflow

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.cloneflow.infoactivities.InfoArtistActivity
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
        var isPlaying : Boolean = false
        var isFirst : Boolean = true
        var pos : Int? = null
        var lyrics : String? = null
        var lastLyricPos : Int? = null
        lateinit var lyricsMap : Map<Int, String>
        lateinit var mediaPlayer : MediaPlayer
        lateinit var seekbar : SeekBar
        lateinit var handleBtn : ImageButton
        lateinit var currentLyricsTextView : TextView
        lateinit var nextLyricsTextView: TextView
    }

    private class StreamingThread : Thread() {
        fun milisecToString(miliseconds : Int) : String {
            val minutes : Int = (miliseconds / 1000 / 60)
            val seconds : Int = (miliseconds / 1000 % 60)
            val minutesString = String.format("%02d", minutes)
            val secondsString = String.format("%02d", seconds)
            return "$minutesString:$secondsString"
        }
        fun searchLyrics(curPos : Int) : Int {
            val timeStampKeys = lyricsMap.keys.toList()
            println(timeStampKeys)
            var curLyricPos : Int = -1
            for((i, item) in timeStampKeys.withIndex()) { // i는 인덱스, item은 시간
                if(curPos>=item && timeStampKeys[i+1]>=curPos) {
                    curLyricPos = i
                    break
                }
            }
            return curLyricPos
        }
        override fun run() {
            while(isPlaying) {
                seekbar.progress = mediaPlayer.currentPosition
                if(mediaPlayer.currentPosition%500==0){
                    val curLyrics = searchLyrics(mediaPlayer.currentPosition)
                    if(curLyrics != -1){
                        currentLyricsTextView.text = lyricsMap[lyricsMap.keys.toList()[curLyrics]]
                        if(curLyrics != lastLyricPos!!-3) {nextLyricsTextView.text = lyricsMap[lyricsMap.keys.toList()[curLyrics+1]]}
                    }
                }
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
                                findViewById<TextView>(R.id.streaming_artist).setOnClickListener{
                                    val artistInfoIntent = Intent(this@StreamingActivity, InfoArtistActivity::class.java)
                                    Log.d("로그", "StreamingActivity - lets see ${result.artistIdx}th artist activity")
                                    artistInfoIntent.putExtra("artistIdx", result.artistIdx!!.toInt())
                                    startActivity(artistInfoIntent)
                                }
                                findViewById<TextView>(R.id.streaming_artist).text = result.artist.toString()
                                //findViewById<TextView>(R.id.streaming_lyric).text = result.lyrics.toString()
                                Glide.with(this@StreamingActivity).load(result.cover.toString())
                                    .into(findViewById(R.id.streaming_albumcover))
                                lyrics = result.lyrics.toString()
                                lyricsMap = parseByRN(lyrics!!)
                                Log.d("로그", "lyricsmap $lyricsMap")
                                lastLyricPos = lyricsMap.size
                                currentLyricsTextView = findViewById(R.id.lyrics_now)
                                nextLyricsTextView = findViewById(R.id.lyrics_next)
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
                                        handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_pause))
                                        StreamingThread().start()
                                    }
                                })
                                handleBtn.setOnClickListener{
                                    Log.d("로그", "StreamingActivity - handlebtn clicked")
                                    if(isPlaying){ // pause를 누른 경우
                                        pauseStreaming()
                                    } else { // start를 누른 경우
                                        if(isFirst) { // 처음 트는 경우
                                            startStreaming(result.src!!.toString())
                                        } else { // 멈췄다가 다시 트는 경우
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
        // companion object는 초기화 해줘야 함
        isPlaying = false
        isFirst = true
        pos = null
    }

    private fun getToken(): String? {
        val pref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startStreaming(src : String){
        Log.d("로그", "StreamingActivity - startStreaming() called")
        //val URI = Uri.parse("https://r7---sn-3u-bh2ll.googlevideo.com/videoplayback?expire=1612389003&ei=KsYaYKLlO_KEs8IPyviK4AQ&ip=203.229.147.89&id=o-AP-tZk5MmPPvEX-y6Jrn6vdCs4tFRNYVtKbTjw8L9_RF&itag=140&source=youtube&requiressl=yes&hcs=yes&mh=v-&mm=31%2C26&mn=sn-3u-bh2ll%2Csn-i3belnlz&ms=au%2Conr&mv=m&mvi=7&pcm2cms=yes&pl=19&shardbypass=yes&initcwndbps=1177500&vprv=1&mime=audio%2Fmp4&ns=4qmMTtMjAvLileYQ9B2BCqQF&gir=yes&clen=3207289&dur=198.135&lmt=1611952280808383&mt=1612367100&fvip=3&keepalive=yes&c=WEB&txp=5531432&n=jXIVAMSq1TKDr26aX&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=hcs%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Cshardbypass%2Cinitcwndbps&lsig=AG3C_xAwRAIgPuWGTMynTk8zq5MuCRH16X3069OrptPV_YfrGdtiu14CIEeYoRDzHp9qtOZgnMrz_dvcwVSUWHekOZ82eVPNit8D&sig=AOq0QJ8wRgIhAPbBj1Qv9KEA4HxlDSGmCTu1v9aSdqu8SDFtzfc4D_8FAiEA8RPOzskjwx-huLOGj-AI14plsLEsI8LOxPrxV_hfDAc=&ratebypass=yes")
        val URI = Uri.parse(src)
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
        handleBtn.setImageDrawable(ContextCompat.getDrawable(this@StreamingActivity, R.drawable.ic_pause))
        StreamingThread().start()
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

    private fun stringToMilisec(miliStr : String) : Int {
        val minutes : Int = miliStr.slice(IntRange(0,1)).toInt()
        val seconds : Int = miliStr.slice(IntRange(3,4)).toInt()
        return (minutes * 60 + seconds) * 1000
    }
    private fun parseByRN(responseLyrics : String) : Map<Int, String> {
        val parsedList = responseLyrics.split("\r\n")
        val lyricsMap = mutableMapOf<Int, String>()
        for((i, _) in parsedList.withIndex()){
            if(i%2==0){
                lyricsMap[stringToMilisec(parsedList[i])] = parsedList[i+1]
            }
        }
        return lyricsMap
    }

    fun onPlaylistBtnClicked(v : View){
        val startPlayListIntent = Intent(this, PlaylistActivity::class.java)
        startActivity(startPlayListIntent)
    }
}