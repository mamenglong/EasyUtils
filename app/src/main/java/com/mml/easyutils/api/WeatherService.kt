package com.mml.easyutils.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherService {
    companion object{
        val BASE_URL="http://www.weather.com.cn/data/cityinfo/"
    }
    @GET("{cityid}.html")
    fun getWeatherData(@Path("cityid") cityid: Int): Call<Any>
//    @GET("api/bing_pic")
//    fun getBingPck(): Call<String>

}