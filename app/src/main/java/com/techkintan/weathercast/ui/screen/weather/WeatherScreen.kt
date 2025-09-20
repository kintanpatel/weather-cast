package com.techkintan.weathercast.ui.screen.weather

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.techkintan.weathercast.R
import com.techkintan.weathercast.helper.sampleData
import com.techkintan.weathercast.helper.toDisplayDate
import com.techkintan.weathercast.ui.screen.weather.component.CityInputBar
import com.techkintan.weathercast.ui.screen.weather.component.ErrorState
import com.techkintan.weathercast.ui.screen.weather.component.ForecastGridCard
import com.techkintan.weathercast.ui.theme.WeatherCastTheme

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        uiState = uiState,
        onFetch = { city -> if (city.isNotBlank()) viewModel.fetchWeather(city) })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onFetch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isGrid by rememberSaveable { mutableStateOf(false) }

    var city by rememberSaveable { mutableStateOf("") }

    // Sync city state with DB result (uiState)
    LaunchedEffect(uiState) {
        if (uiState is WeatherUiState.Success) {
            city = uiState.city
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        }, actions = {
            IconButton(onClick = {
                onFetch(city)
            }) {
                Icon(
                    imageVector = Icons.Filled.Refresh, contentDescription = "Toggle View"
                )
            }
            IconButton(onClick = { isGrid = !isGrid }) {
                AnimatedContent(targetState = isGrid, transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }, label = "IconSwitch") { grid ->
                    Icon(
                        imageVector = if (grid) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                        contentDescription = "Toggle View"
                    )
                }
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CityInputBar(
                city = city,
                onCityChange = { city = it },
                onFetch = {
                    keyboardController?.hide()
                    onFetch(city)
                }
            )
            when (uiState) {
                is WeatherUiState.Error ->
                    ErrorState(uiState.message)

                is WeatherUiState.Idle -> Text(stringResource(R.string.enter_a_city_and_tap_fetch))
                is WeatherUiState.Loading -> Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is WeatherUiState.Success -> {
                    val grouped = uiState.items.groupBy { it.date.split(" ")[0].toDisplayDate() }
                    Crossfade(targetState = isGrid, label = "ViewToggle") { grid ->
                        if (grid) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                items(uiState.items) { day ->
                                    ForecastGridCard(day)
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                grouped.forEach { (date, forecastsForDay) ->
                                    item {
                                        Text(
                                            date,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    item {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            items(forecastsForDay) { forecast ->
                                                ForecastGridCard(forecast)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WeatherScreenPreview() {
    WeatherCastTheme {
        WeatherScreenContent(
            WeatherUiState.Success("London, GB", sampleData),
            onFetch = {}
        )
    }
}
