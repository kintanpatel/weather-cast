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
    suspend fun updateLastCityId(cityId: Long) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.LAST_CITY_ID] = cityId
        }
    }

    val lastCityIdFlow: Flow<Long?> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.LAST_CITY_ID] }
}
