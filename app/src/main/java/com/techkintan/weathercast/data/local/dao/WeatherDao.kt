package com.techkintan.weathercast.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.techkintan.weathercast.data.local.entity.CityEntity
import com.techkintan.weathercast.data.local.entity.CityWithForecasts
import com.techkintan.weathercast.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecasts: List<ForecastEntity>)

    @Transaction
    @Query("DELETE FROM forecasts WHERE cityId = :cityId")
    suspend fun clearForecasts(cityId: Long)

    @Transaction
    @Query("SELECT * FROM cities WHERE id = :cityId")
     fun getCityWithForecasts(cityId: Long): Flow<CityWithForecasts?>

}