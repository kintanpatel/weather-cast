package com.techkintan.weathercast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techkintan.weathercast.ui.model.DailyForecast
import kotlin.math.roundToInt

@Entity(tableName = "forecast")
class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val city: String,
    val date: String,
    val avgTemp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val feelsLike: Double,
    val pressure: Double,
    val humidity: Double,
    val condition: String,
    val icon: String,
    val updatedAt: Long
)

fun ForecastEntity.toUi(): DailyForecast =
    DailyForecast(
        date = date, // raw "yyyy-MM-dd" (format in UI if needed)
        avgTemp = "${(avgTemp * 10).roundToInt() / 10.0}°C",
        tempMin = "${(tempMin * 10).roundToInt() / 10.0}°C",
        tempMax = "${(tempMax * 10).roundToInt() / 10.0}°C",
        feelsLike = "${(feelsLike * 10).roundToInt() / 10.0}°C",
        pressure = "${(pressure * 10).roundToInt() / 10.0} hPa",
        humidity = "${(humidity * 10).roundToInt() / 10.0}%",
        condition = condition,
        iconId = icon
    )


