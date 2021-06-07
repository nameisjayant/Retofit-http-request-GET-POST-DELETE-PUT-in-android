package com.codingwithjks.retrofithttprequest.data.Network

import com.codingwithjks.retrofithttprequest.data.Bus
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    companion object{
        const val BASE_URL = "https://dtc-api.herokuapp.com/"
    }

    @FormUrlEncoded
    @POST("bus/")
    suspend fun postBus(
        @Field("bus_no") busNo:String,
        @Field("towns") town:String
    ): Bus

    @GET("bus")
    suspend fun getAllBus(): List<Bus>

    @DELETE("bus/{bus_no}/")
    suspend fun delete(@Path("bus_no") bus_no:String):Response<Unit>
}