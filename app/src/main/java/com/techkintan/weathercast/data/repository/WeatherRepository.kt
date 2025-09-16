package com.techkintan.weathercast.data.repository

import com.techkintan.weathercast.data.local.dao.ForecastDao
import com.techkintan.weathercast.data.local.entity.toUi
import com.techkintan.weathercast.data.remote.WeatherApi
import com.techkintan.weathercast.data.remote.toEntities
import com.techkintan.weathercast.ui.model.DailyForecast
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getCached(city: String): List<DailyForecast>
    suspend fun getForecast(city: String):  List<DailyForecast>
}

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val apiKey: String,
    private val dao: ForecastDao,
) : WeatherRepository {
    override suspend fun getCached(city: String): List<DailyForecast> =
        dao.getByCityName(city).map { it.toUi() }


    override suspend fun getForecast(city: String): List<DailyForecast> {
        val cached = dao.getByCityName(city) // keep for fallback
        return try {
            val response = api.getWeatherForecast(city = city, cnt = 24, apiKey = apiKey)
            val entities = response.toEntities()
            dao.replaceCity(city, entities)
            entities.map { it.toUi() }
        } catch (e: Exception) {
            if (cached.isNotEmpty()) cached.map { it.toUi() } else throw e
        }
    }
}