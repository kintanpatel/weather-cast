package com.techkintan.weathercast.data.remote

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
    val icon: String
)
