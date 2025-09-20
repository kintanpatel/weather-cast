package com.techkintan.weathercast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techkintan.weathercast.ui.screen.weather.WeatherScreen
import com.techkintan.weathercast.ui.theme.WeatherCastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherCastTheme {
                WeatherScreen()
            }
        }
    }
}

