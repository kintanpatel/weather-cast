package com.techkintan.weathercast.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techkintan.weathercast.data.local.dao.ForecastDao
import com.techkintan.weathercast.data.local.entity.ForecastEntity

@Database(entities = [ForecastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}