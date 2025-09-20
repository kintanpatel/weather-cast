package com.techkintan.weathercast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techkintan.weathercast.data.repository.WeatherRepository
import com.techkintan.weathercast.helper.Result
import com.techkintan.weathercast.ui.screen.WeatherUiState.Error
import com.techkintan.weathercast.ui.screen.WeatherUiState.Idle
import com.techkintan.weathercast.ui.screen.WeatherUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeForecast().collect { domain ->
                _uiState.value = if (domain != null) {
                    WeatherUiState.Success(domain.cityName, domain.forecasts)
                } else {
                    WeatherUiState.Idle
                }
            }
        }
    }

    fun fetchWeather(city: String) =
        viewModelScope.launch {
            _uiState.value = Loading
            val result = repository.refreshForecast(city)
            //Success Will Emit Automatically Via Database Observer
            if (result is Result.Error) {
                _uiState.value = Error(result.message)
            }
        }
}