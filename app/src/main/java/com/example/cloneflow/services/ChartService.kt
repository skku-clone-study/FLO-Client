package com.example.cloneflow.services

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

class ChartService {
    interface ShowChartService{
        @GET("api/chart")
        fun getChartData(
            @Header("X-ACCESS-TOKEN") token : String
        ) : Call<ChartResponse>
    }
}

data class ChartResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : Result? = null
)

data class Result(
    @SerializedName("floChart") var floChart : Chart? = null,
    @SerializedName("increasingChart") var increasingChart : Chart? = null,
    @SerializedName("foreignChart") var foreignChart : Chart? = null,
    @SerializedName("videos") var videos : List<Videos>? = null
)

data class Chart(
    @SerializedName("updated") var updated : String? = null,
    @SerializedName("playlistIdx") var playlistIdx : Int? = null,
    @SerializedName("songs") var songs : List<Songs>? = null
)

data class Songs(
    @SerializedName("ranking") var ranking : Int? = null,
    @SerializedName("musicIdx") var musicIdx : Int? = null,
    @SerializedName("cover") var cover : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artist") var artist : String? = null
)

data class Videos(
    @SerializedName("videoIdx") var videoIdx : Int? = null,
    @SerializedName("thumbnail") var thumbnail : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("runningTime") var runningTime : String? = null
)
