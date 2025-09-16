package com.techkintan.weathercast.data.repository

import com.techkintan.weathercast.data.local.dao.ForecastDao
import com.techkintan.weathercast.data.local.entity.toUi
import com.techkintan.weathercast.data.remote.WeatherApi
import com.techkintan.weathercast.data.remote.toEntities
import com.techkintan.weathercast.helper.Result
import com.techkintan.weathercast.helper.safeApiCall
import com.techkintan.weathercast.ui.model.DailyForecast
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getCached(city: String): Result<List<DailyForecast>>
    suspend fun getForecast(city: String): Result<List<DailyForecast>>
}

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val apiKey: String,
    private val dao: ForecastDao,
) : WeatherRepository {
    override suspend fun getCached(city: String): com.techkintan.weathercast.helper.Result<List<DailyForecast>> {
        return try {
            val data = dao.getByCityName(city).map { it.toUi() }
            Result.Success(data)

        } catch (e: Exception) {
            Result.Error("Failed to load cached data", e)
        }
    }

    /*override suspend fun getForecast(city: String): Result<List<DailyForecast>> {
        return try {
            val response = api.getWeatherForecast(city = city, cnt = 24, apiKey = apiKey)
            val entities = response.toEntities()
            dao.replaceCity(city, entities)
            Result.Success(entities.map { it.toUi() })
        } catch (e: Exception) {
            val fallback = dao.getByCityName(city)
            if (fallback.isNotEmpty()) {
                Result.Success(fallback.map { it.toUi() })
            } else {
                Result.Error("Network error: ${e.message}", e)
            }
        }
    }*/
    override suspend fun getForecast(city: String): Result<List<DailyForecast>> {
        val cached = dao.getByCityName(city)

        return safeApiCall {
            val response = api.getWeatherForecast(city = city, cnt = 24, apiKey = apiKey)
            val entities = response.toEntities()
            dao.replaceCity(city, entities)
            entities.map { it.toUi() }
        }.also { result ->
            // Optional fallback to cached
            if (result is Result.Error && cached.isNotEmpty()) {
                return Result.Success(cached.map { it.toUi() })
            }
        }
    }

}