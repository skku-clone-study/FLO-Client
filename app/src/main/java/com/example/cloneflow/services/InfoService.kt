package com.example.cloneflow.services

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

class InfoService {
    interface AlbumInfoService {
        @GET("api/album/{idx}")
        fun getAlbumInfo(
            @Header("X-ACCESS-TOKEN") token: String,
            @Path("idx") idx: Int
        ): Call<AlbumInfoResponse>
    }
    interface AlbumTrackInfoService {
        @GET("api/album/{idx}/track")
        fun getAlbumTrackInfo(
            @Header("X-ACCESS-TOKEN") token: String,
            @Path("idx") idx: Int
        ): Call<AlbumTrackInfoResponse>
    }
    interface AlbumInfoInfoService {
        @GET("api/album/{idx}/info")
        fun getAlbumInfoInfo(
            @Header("X-ACCESS-TOKEN") token: String,
            @Path("idx") idx: Int
        ): Call<AlbumInfoInfoResponse>
    }
    interface AlbumVideoInfoService {
        @GET("api/album/{idx}/video")
        fun getAlbumVideoInfo(
            @Header("X-ACCESS-TOKEN") token: String,
            @Path("idx") idx: Int,
            @Query("sort") sort : Int
        ): Call<AlbumVideoInfoResponse>
    }
}

data class AlbumInfoResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : AlbumResult? = null
)

data class AlbumResult(
    @SerializedName("albumIdx") var albumIdx : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artistIdx") var artistIdx : String? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("releasedAt") var releasedAt : String? = null,
    @SerializedName("albumType") var albumType : String? = null,
    @SerializedName("albumGenre") var albumGenre : String? = null,
    @SerializedName("cover") var cover :	String? = null,
    @SerializedName("isLiked") var isLiked : String? = null
)

data class AlbumTrackInfoResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : List<AlbumTrackResult>? = null
)

data class AlbumTrackResult(
    @SerializedName("musicIdx") var musicIdx : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artistIdx") var artistIdx : Int? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("isTitle") var isTitleOfAlbum : String? = null
)

data class AlbumInfoInfoResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : AlbumInfoResult? = null
)

data class AlbumInfoResult(
    @SerializedName("albumName") var albumName :	String? = null,
    @SerializedName("artist") var artist: String? = null,
    @SerializedName("releaseCompany") var releaseCompany : String? = null,
    @SerializedName("agency") var agency : String? = null,
    @SerializedName("description") var description : String? = null
)

data class AlbumVideoInfoResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : AlbumVideoResult? = null
)

data class AlbumVideoResult(
    @SerializedName("sort") var sort : String? = null,
    @SerializedName("videos") var videos : List<AlbumVideo>? = null
)

data class AlbumVideo(
    @SerializedName("videoIdx") var videoIdx : Int? = null,
    @SerializedName("thumbnail") var thumbnail : String? = null,
    @SerializedName("runningTime") var runningTime : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("releasedAt") var releasedAt : String? = null
)

data class MusicResult(
    @SerializedName("musicIdx") var musicIdx : Int? = null,
    @SerializedName("cover") var cover :	String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artistIdx") var artistIdx : String? = null,
    @SerializedName("artist") var artist : String? = null,
    @SerializedName("albumIdx") var albumIdx : Int? = null,
    @SerializedName("album") var album : String? = null,
    @SerializedName("isLiked") var isLiked : String? = null
)