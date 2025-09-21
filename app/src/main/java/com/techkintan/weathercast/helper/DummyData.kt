package com.techkintan.weathercast.helper

import com.techkintan.weathercast.ui.model.DailyForecast

val sampleData = listOf(
    DailyForecast(
        date = "2025-09-20 03:00:00",
        temp = "21.3", tempMin = "20.9", tempMax = "22.1",
        feelsLike = "21.0", pressure = "1012 hPa", humidity = "68%",
        condition = "Clouds"
    ),
    DailyForecast(
        date = "2025-09-20 06:00:00",
        temp = "23.1", tempMin = "22.0", tempMax = "24.2",
        feelsLike = "23.0", pressure = "1013 hPa", humidity = "65%",
        condition = "Clear"
    ),
    DailyForecast(
        date = "2025-09-20 09:00:00",
        temp = "25.7", tempMin = "24.5", tempMax = "27.0",
        feelsLike = "26.0", pressure = "1014 hPa", humidity = "60%",
        condition = "Clear"
    ),
    DailyForecast(
        date = "2025-09-21 03:00:00",
        temp = "20.4", tempMin = "19.9", tempMax = "21.0",
        feelsLike = "20.2", pressure = "1011 hPa", humidity = "72%",
        condition = "Rain"
    ),
    DailyForecast(
        date = "2025-09-21 06:00:00",
        temp = "22.0", tempMin = "21.0", tempMax = "23.2",
        feelsLike = "21.8", pressure = "1010 hPa", humidity = "75%",
        condition = "Rain"
    )
)