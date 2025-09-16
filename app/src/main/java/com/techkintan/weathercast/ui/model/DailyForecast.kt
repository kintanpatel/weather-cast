package com.techkintan.weathercast.ui.model

data class DailyForecast(
    val date: String,      // "YYYY-MM-DD"
    val temp: String,      // "27.3°C"
    val condition: String, // "Clear"
    val iconId: String     // "01d"
)