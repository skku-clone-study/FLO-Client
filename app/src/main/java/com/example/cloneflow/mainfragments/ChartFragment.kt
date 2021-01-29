package com.example.cloneflow.mainfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chahinem.pageindicator.PageIndicator
import com.example.cloneflow.ChartRecyclerAdapter
import com.example.cloneflow.R
import com.example.cloneflow.services.Chart
import com.example.cloneflow.services.ChartResponse
import com.example.cloneflow.services.ChartService
import com.example.cloneflow.services.Videos
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import me.relex.circleindicator.CircleIndicator2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

        } else {
            val retrofit : Retrofit? = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
                GsonConverterFactory.create()).build()
            val service = retrofit!!.create(ChartService.ShowChartService::class.java)
            val call = service.getChartData(token = token)
            call.enqueue(object : Callback<ChartResponse>{
                override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                    Log.d("로그", "ChartFragment - onFailure() called - $t")
                }
                override fun onResponse(
                    call: Call<ChartResponse>,
                    response: Response<ChartResponse>
                ) {
                    Log.d("로그", "ChartFragment - onResponse() called")
                    Log.d("로그", "Response - ${response.body()}")
                    val responseBody = response.body()
                    val responseBodyResult = responseBody!!.result
                    val floChart = responseBodyResult!!.floChart
                    val increasingChart = responseBodyResult.increasingChart
                    val foreignChart = responseBodyResult.foreignChart
                    makeMusicChart(floChart!!, R.id.chart_card_flo)
                    val video = responseBodyResult.videos
                }
            })
        }
        return view
    }

    private fun getToken(): String? {
        val pref = this.activity?.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        return pref?.getString("jwt", null)
    }

    private fun makeMusicChart(chart : Chart, id: Int) {
        val recyclerView = view!!.findViewById<RecyclerView>(R.id.chart_recyclerview)
        recyclerView.adapter = ChartRecyclerAdapter(chart.songs!!)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 5, GridLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        /*
        val indicator: CircleIndicator2 = view!!.findViewById(R.id.recyclerViewIndicator)
        indicator.attachToRecyclerView(recyclerView, snapHelper)
         */
        /*
        val pageIndicator = view!!.findViewById<PageIndicator>(R.id.pageIndicator)
        pageIndicator.attachTo(recyclerView)
         */
        /*
        val recyclerview_pager_indicator = view!!.findViewById<IndefinitePagerIndicator>(R.id.recyclerview_pager_indicator)
        recyclerview_pager_indicator.attachToRecyclerView(recyclerView)
         */
    }

    private fun makeVideoChart(video : Videos) {

    }

}