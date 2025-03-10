package com.busschedule.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class TokenManager @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    fun getFCMToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[FCM_TOKEN_KEY]
        }
    }

    suspend fun saveAccessToken(token: String){
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
        Log.d("daeyoung", "AccessToken: $token")
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    suspend fun saveFCMToken(token: String) {
        dataStore.edit { prefs ->
            prefs[FCM_TOKEN_KEY] = token
        }
        Log.d("daeyoung", "FCMToken: $token")
    }

    suspend fun isExistAccessToken(): Boolean {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY] != null
        }.first()
    }

    suspend fun isExistFCMToken(): Boolean {
        return dataStore.data.map { prefs ->
            prefs[FCM_TOKEN_KEY] != null
        }.first()
    }

    suspend fun deleteAccessToken(){
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }
    suspend fun deleteRefreshToken(){
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }
    suspend fun deleteFCMToken(){
        dataStore.edit { prefs ->
            prefs.remove(FCM_TOKEN_KEY)
        }
    }
}