package com.techkintan.weathercast.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techkintan.weathercast.data.local.preferences.CityPreferenceManager
import com.techkintan.weathercast.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.techkintan.weathercast.helper.Result
import com.techkintan.weathercast.ui.screen.WeatherUiState.*
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val cityPrefs: CityPreferenceManager

) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(Idle)
    val uiState = _uiState.asStateFlow()

    private val _lastCity = MutableStateFlow("")
    val lastCity = _lastCity.asStateFlow()

    init {
        viewModelScope.launch {
            cityPrefs.lastCityFlow.collectLatest { city ->
                city?.let {
                    _lastCity.value = it
                    fetchCachedOnly(it)
                } ?: run {
                    _uiState.value = WeatherUiState.Idle
                }
            }
        }
    }


    private suspend fun fetchCachedOnly(city: String) {
        when (val cachedResult = repository.getCached(city)) {
            is Result.Success -> if (cachedResult.data.isNotEmpty()) {
                _uiState.value = WeatherUiState.Success(city, cachedResult.data, offline = true)
            }

            is Result.Error -> {
                Log.w("WeatherVM", "Cached fetch failed: ${cachedResult.message}")
            }

            else -> Unit
        }
    }

    fun fetchWeather(city: String) =
        viewModelScope.launch {
            _uiState.value = Loading
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
            when (val result = repository.getForecast(city)) {
                is Result.Success -> {
                    _uiState.value = Success(city, result.data, false)
                    cityPrefs.saveCity(city)
                }

                is Result.Error -> _uiState.value = Error(result.message)
                Result.Loading -> _uiState.value = Loading
            }
        }
}