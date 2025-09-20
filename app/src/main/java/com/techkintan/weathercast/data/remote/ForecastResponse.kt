package com.techkintan.weathercast.data.remote

import com.google.gson.annotations.SerializedName
import com.techkintan.weathercast.data.local.entity.CityEntity
import com.techkintan.weathercast.data.local.entity.ForecastEntity


data class ForecastResponse(
    val list: List<WeatherItem>,
    val city: City
)

data class City(
    val id: Long,
    val name: String,
    val country: String
)

data class WeatherItem(
    @SerializedName("dt_txt")
    val dtTxt: String,
    val dt: Long,
    val main: Main,
    val weather: List<WeatherDescription>
)

data class Main(
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,

    val pressure: Int,
    val humidity: Int
)

data class WeatherDescription(
    val description: String,
    val icon: String,
    val main: String
)


fun City.toEntity(): CityEntity = CityEntity(
    id = id,
    name = name,
    country = country
)

fun ForecastResponse.toEntities(): List<ForecastEntity> {
    return list.map { fc ->
        ForecastEntity(
            cityId = city.id,
            dt = fc.dt,                           // forecast timestamp
            date = fc.dtTxt,                      // human-readable datetime
            temp = fc.main.temp,
            tempMin = fc.main.tempMin,
            tempMax = fc.main.tempMax,
            feelsLike = fc.main.feelsLike,
            pressure = fc.main.pressure,
            humidity = fc.main.humidity,
            condition = fc.weather.firstOrNull()?.main ?: "—",
            updatedAt = System.currentTimeMillis()
        )
    }
}

/*fun ForecastResponse.toEntities(): List<ForecastEntity> {
    val fc = list.first()
    return list.map {
        ForecastEntity(
        cityId = city.id,
        dt = fc.dt,
        date = fc.dtTxt,
        temp = fc.main.temp,
        tempMin = fc.main.tempMin,
        tempMax = fc.main.tempMax,
        feelsLike = fc.main.feelsLike,
        pressure = fc.main.pressure,
        humidity = fc.main.humidity,
        condition = fc.weather.firstOrNull()?.main ?: "—",
        updatedAt = System.currentTimeMillis()
    )}
}*/
