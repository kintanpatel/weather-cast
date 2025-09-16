package com.techkintan.weathercast.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.techkintan.weathercast.data.local.entity.ForecastEntity

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast WHERE city = :cityName ORDER BY date ASC")
    suspend fun getByCityName(cityName: String): List<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ForecastEntity>)

    @Query("DELETE FROM forecast WHERE city = :city")
    suspend fun clearByCity(city: String)

    @Transaction
    suspend fun replaceCity(city: String, items: List<ForecastEntity>) {
        clearByCity(city)
        insertAll(items)
    }
}