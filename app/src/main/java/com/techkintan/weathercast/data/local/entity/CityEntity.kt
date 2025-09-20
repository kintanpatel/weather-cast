package com.techkintan.weathercast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techkintan.weathercast.data.remote.City

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val country: String
)

fun CityEntity.toUi(): City =
    City(
        id = id,
        name = name,
        country = country
    )
