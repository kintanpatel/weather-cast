package com.techkintan.weathercast.data.repository

import com.techkintan.weathercast.data.local.dao.WeatherDao
import com.techkintan.weathercast.data.local.entity.toUi
import com.techkintan.weathercast.data.local.preferences.CityPreferenceManager
import com.techkintan.weathercast.data.remote.WeatherApi
import com.techkintan.weathercast.data.remote.toEntities
import com.techkintan.weathercast.data.remote.toEntity
import com.techkintan.weathercast.helper.Result
import com.techkintan.weathercast.helper.safeApiCall
import com.techkintan.weathercast.ui.model.CityForecastDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface WeatherRepository {
    fun observeForecast(): Flow<CityForecastDomain?>
    suspend fun refreshForecast(cityName: String): Result<Unit>
}

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val apiKey: String,
    private val dao: WeatherDao,
    private val cityPrefs: CityPreferenceManager

) : WeatherRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeForecast(): Flow<CityForecastDomain?> {
        return cityPrefs.lastCityIdFlow.flatMapLatest { cityId ->
            if (cityId != null) {
                dao.getCityWithForecasts(cityId)
                    .map { cityWithForecasts ->
                        cityWithForecasts?.let {
                            CityForecastDomain(
                                cityName = it.city.name,
                                forecasts = it.forecasts
                                    .sortedBy { f -> f.dt }
                                    .map { f -> f.toUi() }
                            )
                        }
                    }
            } else {
                flowOf(null)
            }
        }
    }


    override suspend fun refreshForecast(cityName: String): Result<Unit> {
        return safeApiCall {
            val response = api.getWeatherForecast(city = cityName, cnt = 24, apiKey = apiKey)

            // save city in DB
            dao.insertCity(response.city.toEntity())

            // replace forecasts
            dao.clearForecasts(response.city.id)
            dao.insertForecast(response.toEntities())

            // save cityId for offline use
            cityPrefs.updateLastCityId(response.city.id)
        }
    }

}