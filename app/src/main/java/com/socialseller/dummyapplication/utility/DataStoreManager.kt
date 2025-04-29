package com.bypriyan.bustrackingsystem.utility


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.socialseller.clothcrew.utility.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context, private val gson: Gson) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    // Generic Save Boolean
    suspend fun putBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { it[dataStoreKey] = value }
    }

    // Generic Get Boolean
    fun getBoolean(key: String): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data.map { it[dataStoreKey] ?: false }
    }

    // Generic Save String
    suspend fun putString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { it[dataStoreKey] = value }
    }

    // Generic Get String
    fun getString(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return dataStore.data.map { it[dataStoreKey] }
    }

    // Remove a key
    suspend fun removeKey(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { it.remove(dataStoreKey) }
    }

    // Clear all
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
