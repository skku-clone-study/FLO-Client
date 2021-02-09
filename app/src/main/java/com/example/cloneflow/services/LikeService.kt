package com.example.cloneflow.services

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*


class LikeService {
    interface LikeOrUnlike{
        @Headers("accept: application/json", "content-type: application/json")
        @POST("/api/liked/{object}")
        fun postLiked(
            @Header("X-ACCESS-TOKEN") token: String,
            @Body params: JsonObject,
            @Path("object") obj : Int
        ) : Call<LikedResponse>
    }
}

data class LikedResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("result") var result : LikedResult? = null
)

data class LikedResult(
    @SerializedName("type") var type : String? = null,
    @SerializedName("idx") var idx : Int? = null
)