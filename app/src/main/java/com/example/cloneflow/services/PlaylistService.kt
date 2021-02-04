package com.example.cloneflow.services

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

class PlaylistService {
    interface ShowPlaylistService{
        @GET("api/playlist")
        fun getPlaylistData(
            @Header("X-ACCESS-TOKEN") token : String
        ) : Call<PlaylistResponse>
    }
}

data class PlaylistResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : List<PlayList>? = null
)

data class PlayList(
    @SerializedName("order") var order : Int? = null,
    @SerializedName("isGroup") var isGroup : Int? = null,
    @SerializedName("groupName") var groupName : String? = null,
    @SerializedName("songs") var songs : List<PLSongs>? = null
)

data  class PLSongs(
    @SerializedName("musicIdx") var musicIdx : Int? = null,
    @SerializedName("cover") var cover : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("artist") var artist : String? = null
)