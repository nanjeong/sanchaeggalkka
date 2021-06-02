package com.example.sanchaeggalkka

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("getVilageFcst?serviceKey=${BuildConfig.WEATHER_API_KEY}")
    fun getWeather(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") type: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Call<WeatherForecast>
}