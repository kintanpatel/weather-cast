package com.techkintan.weathercast.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("cnt") cnt: Int = 24, // 3 days × 8 intervals
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse
}