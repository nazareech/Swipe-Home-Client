package com.nazar_protasov.swipehome.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import com.nazar_protasov.swipehome.network.CreateUserInfoResponses
import kotlinx.coroutines.flow.map

class SessionManager(private val dataStore: DataStore<Preferences>) {
    // Визначаємо ключі
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val PROFILE_KEY = stringPreferencesKey("user_profile")

    private val json = Json { ignoreUnknownKeys = true }

    // Робота з токеном
    val tokenFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    suspend fun saveToken(token: String){
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    // Робота з профілем

    val userProfileFlow: Flow<CreateUserInfoResponses?> = dataStore.data.map { prefs ->
        val profileJson = prefs[PROFILE_KEY]
        if (profileJson != null) {
            try {
                json.decodeFromString<CreateUserInfoResponses>(profileJson)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    suspend fun saveProfile(profile: CreateUserInfoResponses) {
        val profileJson = json.encodeToString(profile)
        dataStore.edit { prefs ->
            prefs[PROFILE_KEY] = profileJson
        }
    }

    // Вихід з акаунта
    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

}