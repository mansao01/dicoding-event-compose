package com.mansao.dicodingfundamentalsubmission.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class AppPreferences @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    private val isDarkMode = booleanPreferencesKey("is_dark_mode")
    private val isPushNotification = booleanPreferencesKey("push_notification")

    suspend fun saveIsDarkMode(newValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkMode] = newValue

        }
    }

    suspend fun savePushNotification(newValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[isPushNotification] = newValue

        }
    }

    fun getIsDarkMode(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeState = preferences[isDarkMode] ?: false
            themeState
        }

    fun getIsPushNotification(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val notificationState = preferences[isPushNotification] ?: false
            notificationState
        }
}