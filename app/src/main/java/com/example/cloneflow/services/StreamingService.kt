package com.example.cloneflow.services

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class StreamingService {

    interface StreamingService {
        @GET("/api/streaming/{idx}")
        fun getStreaming(
            @Header("X-ACCESS-TOKEN") token: String,
            @Path("idx") idx: Int
        ): Call<StreamingResponse>
    }
}

data class StreamingResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : StreamingResult? = null
)

data class StreamingResult(
    @SerializedName("musicIdx") var musicIdx : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artistIdx") var artistIdx : String? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("cover") var cover : String? = null,
    @SerializedName("videoThumbnail") var videoThumbnail : String? = null,
    @SerializedName("videoRunningTime") var videoRunningTime : String? = null,
    @SerializedName("src") var src : String? = null,
    @SerializedName("lyrics") var lyrics : String? = null,
    @SerializedName("isLiked") var isLiked : String? = null
)