package com.techkintan.weathercast.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.techkintan.weathercast.R
import com.techkintan.weathercast.ui.model.DailyForecast
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
    uiState: WeatherUiState, onFetch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isGrid by rememberSaveable { mutableStateOf(false) }

    var city by rememberSaveable { mutableStateOf("London") }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "3-Day Forecast")
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        onFetch(city)
                    })
                )

                Button(onClick = {
                    keyboardController?.hide()
                    onFetch(city)
                }, enabled = city.isNotBlank()) { Text("Fetch") }
            }
            when (uiState) {
                is WeatherUiState.Error ->
                    ErrorState(uiState.message)

                is WeatherUiState.Idle -> Text("Enter a city and tap Fetch.")
                is WeatherUiState.Loading -> Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is WeatherUiState.Success -> {
                    if (uiState.offline) {
                        Text(
                            "Showing cached data (offline).",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text("City: ${uiState.city}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
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
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding =
                                    PaddingValues(vertical = 8.dp)
                            ) {
                                items(uiState.items) { day ->
                                    ForecastRow(day)
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bad),
            contentDescription = "Error",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp).aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ForecastGridCard(day: DailyForecast, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(1f)
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.06f)
                    .padding(10.dp),
                alignment = Alignment.BottomEnd
            )

            // Foreground weather details
            Column(
                modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            day.date,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            day.condition, style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Image(
                        imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                        contentDescription = day.condition,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    day.avgTemp, style = MaterialTheme.typography.titleMedium
                )


                Text(
                    "↓ ${day.tempMin}  ↑ ${day.tempMax}", style = MaterialTheme.typography.bodySmall
                )


                IconWithText(
                    iconRes = R.drawable.ic_humidity, label = day.humidity
                )
            }
        }
    }
}


@Composable
fun ForecastRow(day: DailyForecast) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box {
            // Faded Background Icon
            Image(
                imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.08f) // light fade
                    .padding(end = 16.dp),
                alignment = Alignment.CenterEnd
            )

            // Foreground Content
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                        contentDescription = day.condition,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(day.date, style = MaterialTheme.typography.titleMedium)
                        Text(day.condition, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text("${day.avgTemp}%", style = MaterialTheme.typography.titleMedium)

                }

                Spacer(Modifier.height(10.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_temp),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "↓ ${day.tempMin}  ↑ ${day.tempMax}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconWithText(iconRes = R.drawable.ic_humidity, label = day.humidity)
                }
            }
        }
    }
}

fun getWeatherIcon(condition: String): Int {
    return when (condition.lowercase()) {
        "clear" -> R.drawable.ic_clear_day
        "clouds" -> R.drawable.ic_scattered_clouds
        "rain" -> R.drawable.ic_rain
        "drizzle" -> R.drawable.ic_shower_rain
        "thunderstorm" -> R.drawable.ic_thunderstorm
        "snow" -> R.drawable.ic_snow
        "mist", "fog", "haze", "smoke", "dust", "sand", "ash", "squall", "tornado" -> R.drawable.ic_mist
        else -> R.drawable.ic_unknown
    }
}


@Composable
fun IconWithText(iconRes: Int, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

/*@Preview(showBackground = true)
@Composable
private fun ForecastRowPreview() {
    ForecastRow(DailyForecast("2025-09-16", "27.3°C", "Clouds", "03d"))
}*/

@Preview(
    showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WeatherScreenPreview() {
    val sample = listOf(
        DailyForecast(
            date = "2025-09-16",
            avgTemp = "27.3°C",
            tempMin = "25.9°C",
            tempMax = "29.2°C",
            feelsLike = "28.0°C",
            pressure = "1012 hPa",
            humidity = "68%",
            condition = "Clouds",
            iconId = "03d"
        ), DailyForecast(
            date = "2025-09-17",
            avgTemp = "25.8°C",
            tempMin = "24.5°C",
            tempMax = "27.1°C",
            feelsLike = "26.2°C",
            pressure = "1011 hPa",
            humidity = "72%",
            condition = "Rain",
            iconId = "10d"
        ), DailyForecast(
            date = "2025-09-18",
            avgTemp = "29.1°C",
            tempMin = "27.8°C",
            tempMax = "30.5°C",
            feelsLike = "30.0°C",
            pressure = "1013 hPa",
            humidity = "65%",
            condition = "Clear",
            iconId = "01d"
        )
    )
    WeatherCastTheme {
        WeatherScreenContent(
            WeatherUiState.Success("London, GB", sample, offline = false), onFetch = {})
    }
}
