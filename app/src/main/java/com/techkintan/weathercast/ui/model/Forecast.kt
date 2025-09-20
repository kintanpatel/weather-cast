package com.techkintan.weathercast.ui.model

import com.techkintan.weathercast.data.remote.City

data class Forecast(
    val city: City,
    val list: List<DailyForecast>
)
