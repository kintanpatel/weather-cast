package com.techkintan.weathercast.helper

import com.techkintan.weathercast.ui.model.DailyForecast

val sampleData = listOf(
    DailyForecast(
        date = "2025-09-20 03:00:00",
        temp = "21.3°C", tempMin = "20.9°C", tempMax = "22.1°C",
        feelsLike = "21.0°C", pressure = "1012 hPa", humidity = "68%",
        condition = "Clouds"
    ),
    DailyForecast(
        date = "2025-09-20 06:00:00",
        temp = "23.1°C", tempMin = "22.0°C", tempMax = "24.2°C",
        feelsLike = "23.0°C", pressure = "1013 hPa", humidity = "65%",
        condition = "Clear"
    ),
    DailyForecast(
        date = "2025-09-20 09:00:00",
        temp = "25.7°C", tempMin = "24.5°C", tempMax = "27.0°C",
        feelsLike = "26.0°C", pressure = "1014 hPa", humidity = "60%",
        condition = "Clear"
    ),
    DailyForecast(
        date = "2025-09-21 03:00:00",
        temp = "20.4°C", tempMin = "19.9°C", tempMax = "21.0°C",
        feelsLike = "20.2°C", pressure = "1011 hPa", humidity = "72%",
        condition = "Rain"
    ),
    DailyForecast(
        date = "2025-09-21 06:00:00",
        temp = "22.0°C", tempMin = "21.0°C", tempMax = "23.2°C",
        feelsLike = "21.8°C", pressure = "1010 hPa", humidity = "75%",
        condition = "Rain"
    )
)