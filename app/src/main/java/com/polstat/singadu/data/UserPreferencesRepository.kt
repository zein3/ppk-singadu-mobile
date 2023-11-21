package com.polstat.singadu.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("user_token")
        val NAME = stringPreferencesKey("user_name")
        val EMAIL = stringPreferencesKey("user_email")
        val IS_ADMIN = booleanPreferencesKey("user_is_admin")
        val IS_SUPERVISOR = booleanPreferencesKey("user_is_supervisor")
        val IS_ENUMERATOR = booleanPreferencesKey("user_is_enumerator")
        const val TAG = "UserPreferencesRepo"
    }

    val user: Flow<UserState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences:", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserState(
                preferences[TOKEN] ?: "",
                preferences[NAME] ?: "",
                preferences[EMAIL] ?: "",
                preferences[IS_ADMIN] ?: false,
                preferences[IS_SUPERVISOR] ?: false,
                preferences[IS_ENUMERATOR] ?: false
            )
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun saveIsAdmin(isAdmin: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ADMIN] = isAdmin
        }
    }

    suspend fun saveIsSupervisor(isSupervisor: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_SUPERVISOR] = isSupervisor
        }
    }

    suspend fun saveIsEnumerator(isEnumerator: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ENUMERATOR] = isEnumerator
        }
    }
}

data class UserState(
    val token: String,
    val name: String,
    val email: String,
    val isAdmin: Boolean,
    val isSupervisor: Boolean,
    val isEnumerator: Boolean
)