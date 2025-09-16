package com.techkintan.weathercast.di

import com.techkintan.weathercast.data.repository.WeatherRepository
import com.techkintan.weathercast.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindWeatherRepository(
        repository: WeatherRepositoryImpl
    ): WeatherRepository
}