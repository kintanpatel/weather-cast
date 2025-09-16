package com.techkintan.weathercast.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.techkintan.weathercast.ui.model.DailyForecast

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        uiState = uiState,
        onFetch = { city -> if (city.isNotBlank()) viewModel.fetchWeather(city) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onFetch: (String) -> Unit
) {
    var city by rememberSaveable { mutableStateOf("London") }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "3-Day Forecast")
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
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onFetch(city) })
                )
                Button(onClick = { onFetch(city) }, enabled = city.isNotBlank()) { Text("Fetch") }
            }
            when (uiState) {
                is WeatherUiState.Error -> Text(
                    "Error: ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )

                is WeatherUiState.Idle -> Text("Enter a city and tap Fetch.")
                is WeatherUiState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is WeatherUiState.Success -> {
                    Text("City: ${uiState.city}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(uiState.items) { day ->
                            ForecastRow(day)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ForecastRow(
    day: DailyForecast
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(day.date, style = MaterialTheme.typography.titleMedium)
                Text(day.condition, style = MaterialTheme.typography.bodyMedium)
                Text(day.temp, style = MaterialTheme.typography.bodyLarge)
            }
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${day.iconId}@2x.png",
                contentDescription = day.condition,
                modifier = Modifier.size(56.dp)
            )
        }
    }

}

/*@Preview(showBackground = true)
@Composable
private fun ForecastRowPreview() {
    ForecastRow(DailyForecast("2025-09-16", "27.3°C", "Clouds", "03d"))
}*/

@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview() {
    val sample = listOf(
        DailyForecast("2025-09-16", "27.3°C", "Clouds", "03d"),
        DailyForecast("2025-09-17", "25.8°C", "Rain", "10d"),
        DailyForecast("2025-09-18", "29.1°C", "Clear", "01d")
    )
    WeatherScreenContent(WeatherUiState.Success("London, GB", sample), onFetch = {})
}
