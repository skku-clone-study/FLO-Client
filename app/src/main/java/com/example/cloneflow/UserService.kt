package com.example.cloneflow

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class UserService {
    interface SignUpService {
        @Headers("accept: application/json", "content-type: application/json")
        @POST("api/sign-up")
        fun postSignUp(
            @Body params: JsonObject
        ) : Call<SignUpResponse>
    }
}

data class SignUpResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("jwt") var jwt : String? = null
)