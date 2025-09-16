package com.techkintan.weathercast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techkintan.weathercast.ui.model.DailyForecast
import kotlin.math.round

@Entity(tableName = "forecast")
class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val city: String,
    val date: String,       // "yyyy-MM-dd"
    val avgTemp: Double,
    val condition: String,
    val icon: String,
    val updatedAt: Long     // epoch millis
)

fun ForecastEntity.toUi(): DailyForecast =
    DailyForecast(
        date = date,                         // raw "yyyy-MM-dd" (format later)
        temp = "${round(avgTemp * 10) / 10.0}°C",
        condition = condition,
        iconId = icon
    )


