package com.techkintan.weathercast.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techkintan.weathercast.data.remote.toThreeDayUI
import com.techkintan.weathercast.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun fetchWeather(city: String) =
        viewModelScope.launch {
            try {
                _uiState.value = WeatherUiState.Loading
                val catchData = repository.getCached(city)
                if (catchData.isNotEmpty()) {
                    _uiState.value = WeatherUiState.Success(city, catchData, true)
                }

                val liveData = repository.getForecast(city)
                if (liveData.isEmpty()) {
                    _uiState.value = WeatherUiState.Error("No data.")
                } else {
                    _uiState.value = WeatherUiState.Success(city, liveData, false)
                }

            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}", e)
                _uiState.value = WeatherUiState.Error(e.localizedMessage ?: "Something went wrong")
            }

        }
}