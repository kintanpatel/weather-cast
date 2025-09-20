package com.techkintan.weathercast.ui.model

data class CityForecastDomain(
    val cityName: String,
    val forecasts: List<DailyForecast>
)