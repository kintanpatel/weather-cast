package com.techkintan.weathercast.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveCity(city: String) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.LAST_CITY] = city
        }
    }

    val lastCityFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.LAST_CITY] }
}
