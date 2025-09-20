package com.techkintan.weathercast.di

import android.content.Context
import androidx.room.Room
import com.techkintan.weathercast.data.local.dao.WeatherDao
import com.techkintan.weathercast.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weather.db"
        ).build()

    @Provides
    fun provideForecastDao(db: AppDatabase): WeatherDao = db.forecastDao()
}