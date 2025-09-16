package com.techkintan.weathercast.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techkintan.weathercast.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.techkintan.weathercast.helper.Result

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun fetchWeather(city: String) =
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            when (val cachedResult = repository.getCached(city)) {
                is Result.Success -> if (cachedResult.data.isNotEmpty()) {
                    _uiState.value = WeatherUiState.Success(city, cachedResult.data, offline = true)
                }

                is Result.Error -> {
                    Log.w("WeatherVM", "Cached fetch failed: ${cachedResult.message}")
                }

                else -> Unit
            }
            // Fetch Live
            when (val liveResult = repository.getForecast(city)) {
                is Result.Success -> {
                    if (liveResult.data.isEmpty()) {
                        _uiState.value = WeatherUiState.Error("No data.")
                    } else {
                        _uiState.value =
                            WeatherUiState.Success(city, liveResult.data, offline = false)
                    }
                }

                is Result.Error -> {
                    _uiState.value = WeatherUiState.Error(liveResult.message)
                }

                else -> Unit
            }

        }
}