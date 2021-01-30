package com.example.cloneflow.mainfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneflow.*
import com.example.cloneflow.services.Chart
import com.example.cloneflow.services.ChartResponse
import com.example.cloneflow.services.ChartService
import com.example.cloneflow.services.Videos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator


class ChartFragment : Fragment() {

    companion object {
        var BaseUrl = "https://www.heedong.dev/"
        val PREFERENCE = "com.example.cloneflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        val token = getToken()
        if(token == null ){
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(ChartService.ShowChartService::class.java)
            val call = service.getChartData(token = token)
            call.enqueue(object : Callback<ChartResponse>{
                override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                    Log.d("로그", "ChartFragment - onFailure() called - $t")
                    val errorIntent = Intent(requireActivity(), MainActivity::class.java)
                    errorIntent.putExtra("error", "1")
                    startActivity(errorIntent)
                }
                override fun onResponse(
                    call: Call<ChartResponse>,
                    response: Response<ChartResponse>
                ) {
                    val responseBody = response.body()!!
                    when {
                        responseBody.isSuccess!! -> {
                            Log.d("로그", "ChartFragment - onResponse() called")
                            Log.d("로그", "Response - $responseBody")
                            val responseBodyResult = responseBody.result
                            val floChart = responseBodyResult!!.floChart!!
                            val increasingChart = responseBodyResult.increasingChart!!
                            val foreignChart = responseBodyResult.foreignChart!!
                            val charts : List<Chart> = listOf(floChart, increasingChart, foreignChart)
                            makeMusicChart(charts)
                            val video = responseBodyResult.videos
                            makeVideoChart(responseBodyResult.videos!!)
                        }
                        else -> {
                            Log.d("로그", "ChartFragment - onResponse() called")
                            Log.d("로그", "However, Response is not successful")
                            Log.d("로그", "[${responseBody.code}] ${responseBody.message}")
                            val errorIntent = Intent(requireActivity(), MainActivity::class.java)
                            errorIntent.putExtra("error", "1")
                            startActivity(errorIntent)
                        }
                    }
                }
            })
        }
        return view
    }

    private fun getToken(): String? {
        val pref = this.activity?.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

    private fun makeMusicChart(charts : List<Chart>) {
        val recyclerView = view!!.findViewById<RecyclerView>(R.id.chart_recyclerview)
        recyclerView.adapter = ChartListRecyclerAdapter(charts)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
    }

    private fun makeVideoChart(videos : List<Videos>) {
        val videoReclerView = view!!.findViewById<RecyclerView>(R.id.video_recyclerview)
        videoReclerView.adapter = VideoRecyclerAdapter(videos)
        videoReclerView.layoutManager = GridLayoutManager(this.requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(videoReclerView)
    }

}