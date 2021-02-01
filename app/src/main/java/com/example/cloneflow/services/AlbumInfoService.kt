package com.example.cloneflow.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

class AlbumInfoService {
}interface ShowChartService{
    @GET("api/music/")
    fun getChartData(
        @Header("X-ACCESS-TOKEN") token : String
    ) : Call<ChartResponse>
}