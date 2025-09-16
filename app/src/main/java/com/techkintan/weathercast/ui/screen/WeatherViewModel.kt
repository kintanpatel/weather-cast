package com.techkintan.weathercast.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techkintan.weathercast.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getForecast(city)
                Log.d("WeatherViewModel", "City: ${response.city.name}, ${response.city.country}")
                response.list.forEach {
                    Log.d(
                        "WeatherViewModel",
                        "${it.dt_txt} → ${it.main.temp}°C, ${it.weather.firstOrNull()?.description}"
                    )
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}", e)
            }
        }
    }
}