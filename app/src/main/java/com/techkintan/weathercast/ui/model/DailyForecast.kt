package com.techkintan.weathercast.ui.model

data class DailyForecast(
    val date: String,
    val temp: String,
    val tempMin: String,
    val tempMax: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val condition: String
)