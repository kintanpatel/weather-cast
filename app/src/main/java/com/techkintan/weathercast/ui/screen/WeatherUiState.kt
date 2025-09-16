package com.techkintan.weathercast.ui.screen

import com.techkintan.weathercast.ui.model.DailyForecast

sealed class WeatherUiState() {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val city: String, val items: List<DailyForecast>, val offline: Boolean) :
        WeatherUiState()

    data class Error(val message: String) : WeatherUiState()
}