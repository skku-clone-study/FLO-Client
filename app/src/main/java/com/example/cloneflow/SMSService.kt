package com.example.cloneflow

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class SMSService {
    interface SmsSendService {
        @Headers("accept: application/json", "content-type: application/json")
        @POST("sms-auth/")
        fun postSmsAuth(
            @Body params: JsonObject
        ) : Call<SMSResponse>
    }
    interface SmsCheckService {
        @Headers("accept: application/json", "content-type: application/json")
        @POST("sms-auth/check/")
        fun postSmsAuth(
            @Body params: JsonObject
        ) : Call<SMSResponse>
    }
}

data class SMSResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean? = null,
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null
)