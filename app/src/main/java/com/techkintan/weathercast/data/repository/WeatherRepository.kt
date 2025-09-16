package com.techkintan.weathercast.data.repository

import com.techkintan.weathercast.data.remote.ForecastResponse
import com.techkintan.weathercast.data.remote.WeatherApi
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getForecast(city: String): ForecastResponse
}

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi, private val apiKey: String
) : WeatherRepository {

    override suspend fun getForecast(city: String): ForecastResponse =
        api.getWeatherForecast(city = city, apiKey = apiKey)
}