package com.techkintan.weathercast.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.techkintan.weathercast.ui.model.DailyForecast
import kotlin.math.roundToInt

@Entity(
    tableName = "forecasts",
    foreignKeys = [ForeignKey(
        CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cityId")]
)
class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val forecastId: Int = 0,
    val cityId: Long,
    val dt: Long,
    val date: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val condition: String,
    val updatedAt: Long
)

fun ForecastEntity.toUi(): DailyForecast =
    DailyForecast(
        date = date, // raw "yyyy-MM-dd" (format in UI if needed)
        temp = "$temp",
        tempMin = "$tempMin",
        tempMax = "$tempMax",
        feelsLike = "${(feelsLike * 10).roundToInt() / 10.0}",
        pressure = "$pressure hPa",
        humidity = "$humidity%",
        condition = condition
    )


