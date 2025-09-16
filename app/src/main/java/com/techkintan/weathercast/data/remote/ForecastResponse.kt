package com.techkintan.weathercast.data.remote

import com.google.gson.annotations.SerializedName
import com.techkintan.weathercast.data.local.entity.ForecastEntity

data class ForecastResponse(
    val list: List<WeatherItem>,
    val city: City
)

data class City(
    val name: String,
    val country: String
)

data class WeatherItem(
    @SerializedName("dt_txt")
    val dtTxt: String,
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

fun ForecastResponse.toEntities(): List<ForecastEntity> {
    val cityName = city.name

    return list.groupBy { it.dtTxt.substring(0, 10) } // Group by date only
        .toSortedMap()
        .entries
        .take(3)
        .map { (day, items) ->
            val avgTemp = items.map { it.main.temp }.average()
            val feelsLikeAvg = items.map { it.main.feelsLike }.average()
            val tempMin = items.minOf { it.main.tempMin }
            val tempMax = items.maxOf { it.main.tempMax }
            val pressureAvg = items.map { it.main.pressure }.average()
            val humidityAvg = items.map { it.main.humidity }.average()

            val condition = items.mapNotNull { it.weather.firstOrNull()?.main }
                .groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "—"

            val icon = items.find { "12:00:00" in it.dtTxt }
                ?.weather?.firstOrNull()?.icon
                ?: items.firstOrNull()?.weather?.firstOrNull()?.icon
                ?: "01d"

            ForecastEntity(
                date = day,
                city = cityName,
                avgTemp = avgTemp,
                tempMin = tempMin,
                tempMax = tempMax,
                feelsLike = feelsLikeAvg,
                pressure = pressureAvg,
                humidity = humidityAvg,
                condition = condition,
                icon = icon,
                updatedAt = System.currentTimeMillis()
            )
        }
}
