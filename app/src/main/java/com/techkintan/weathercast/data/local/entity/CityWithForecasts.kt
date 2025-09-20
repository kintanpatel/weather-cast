package com.techkintan.weathercast.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CityWithForecasts(
    @Embedded val city: CityEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cityId"
    )
    val forecasts: List<ForecastEntity>
)
