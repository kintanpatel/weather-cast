package com.techkintan.weathercast.data.remote

import com.techkintan.weathercast.ui.model.DailyForecast

data class ForecastResponse(
    val list: List<WeatherItem>,
    val city: City
)

data class City(
    val name: String,
    val country: String
)

data class WeatherItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<WeatherDescription>
)

data class Main(
    val temp: Double
)

data class WeatherDescription(
    val description: String,
    val icon: String,
    val main: String
)


fun ForecastResponse.toThreeDayUI(): List<DailyForecast> =
    list.groupBy { it.dt_txt.substring(0, 10) }   // "YYYY-MM-DD"
        .toSortedMap()
        .entries
        .take(3)
        .map { (date, items) ->
            val avg = items.map { it.main.temp }.average()
            val condition = items.mapNotNull { it.weather.firstOrNull()?.main }
                .groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "—"
            val icon = items.firstOrNull()?.weather?.firstOrNull()?.icon ?: "01d"
            DailyForecast(
                date = date,
                temp = "${kotlin.math.round(avg * 10) / 10.0}°C",
                condition = condition,
                iconId = icon
            )
        }