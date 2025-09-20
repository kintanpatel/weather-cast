package com.techkintan.weathercast.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techkintan.weathercast.data.local.dao.WeatherDao
import com.techkintan.weathercast.data.local.entity.CityEntity
import com.techkintan.weathercast.data.local.entity.ForecastEntity

@Database(entities = [CityEntity::class,ForecastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): WeatherDao
}