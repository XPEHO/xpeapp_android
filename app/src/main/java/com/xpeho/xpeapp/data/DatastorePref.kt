package com.xpeho.xpeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xpeho.xpeapp.di.TokenProvider
import com.xpeho.xpeapp.domain.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DatastorePref(
    private val context: Context,
    val tokenProvider: TokenProvider? = null
) {

    companion object {
        private const val PREF_NAME = "XPE_PREF"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
        val CONNECT = stringPreferencesKey("isConnectedLeastOneTime")
        val AUTH_DATA = stringPreferencesKey("authData")
        val WAS_CONNECTED_LAST_TIME = stringPreferencesKey("wasConnectedLastTime")
        val LAST_EMAIL = stringPreferencesKey("lastEmail")
        val IDEA_STATUSES_SNAPSHOT = stringPreferencesKey("ideaStatusesSnapshot")
        val IDEA_BANNER_MESSAGE = stringPreferencesKey("ideaBannerMessage")
        val IDEA_BANNER_TARGET_ID = stringPreferencesKey("ideaBannerTargetId")
        val IDEA_BANNER_EXPIRATION_MILLIS = longPreferencesKey("ideaBannerExpirationMillis")
    }

    val isConnectedLeastOneTime: Flow<Boolean> = context.dataStore.data
        .map { preference ->
            preference[CONNECT]?.toBoolean() ?: false
        }

    suspend fun setIsConnectedLeastOneTime(isConnected: Boolean) {
        context.dataStore.edit { preference ->
            preference[CONNECT] = isConnected.toString()
        }
    }

    suspend fun setUserId(userId: String) {
        context.dataStore.edit { preference ->
            preference[stringPreferencesKey("userId")] = userId
        }
    }

    val userId: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[stringPreferencesKey("userId")] ?: ""
        }

    // AuthData datastore

    suspend fun setAuthData(authData: AuthData) {
        val json = Gson().toJson(authData)
        context.dataStore.edit { preference ->
            preference[AUTH_DATA] = json
        }
    }

    suspend fun getAuthData(): AuthData? {
        val json = context.dataStore.data.map { preferences ->
            preferences[AUTH_DATA]
        }.first()
        val result = json?.let { Gson().fromJson(it, AuthData::class.java) }
        tokenProvider?.set("Bearer ${result?.token?.token}")
        return result
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { preference ->
            preference.remove(AUTH_DATA)
        }
    }

    // wasConnectedLastTime datastore

    suspend fun setWasConnectedLastTime(wasConnected: Boolean) {
        context.dataStore.edit { preference ->
            preference[WAS_CONNECTED_LAST_TIME] = wasConnected.toString()
        }
    }

    suspend fun getWasConnectedLastTime(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[WAS_CONNECTED_LAST_TIME]?.toBoolean() ?: false
        }.first()
    }

    // Last email persistence

    suspend fun setLastEmail(email: String) {
        context.dataStore.edit { preference ->
            preference[LAST_EMAIL] = email
        }
    }

    suspend fun getLastEmail(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_EMAIL]
        }.first()
    }

    // Idea statuses snapshot

    suspend fun setIdeaStatusesSnapshot(statusesById: Map<String, String>) {
        val json = Gson().toJson(statusesById)
        context.dataStore.edit { preference ->
            preference[IDEA_STATUSES_SNAPSHOT] = json
        }
    }

    suspend fun getIdeaStatusesSnapshot(): Map<String, String> {
        val json = context.dataStore.data.map { preferences ->
            preferences[IDEA_STATUSES_SNAPSHOT]
        }.first() ?: return emptyMap()

        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(json, mapType) ?: emptyMap()
    }

    // Idea status banner

    suspend fun setIdeaStatusBanner(
        message: String,
        targetIdeaId: String,
        expirationMillis: Long,
    ) {
        context.dataStore.edit { preference ->
            preference[IDEA_BANNER_MESSAGE] = message
            preference[IDEA_BANNER_TARGET_ID] = targetIdeaId
            preference[IDEA_BANNER_EXPIRATION_MILLIS] = expirationMillis
        }
    }

    suspend fun getIdeaStatusBanner(): IdeaStatusBanner? {
        val preferences = context.dataStore.data.first()
        val message = preferences[IDEA_BANNER_MESSAGE]
        val targetId = preferences[IDEA_BANNER_TARGET_ID]
        val expiration = preferences[IDEA_BANNER_EXPIRATION_MILLIS]

        if (message.isNullOrBlank() || targetId.isNullOrBlank() || expiration == null) {
            return null
        }

        if (System.currentTimeMillis() > expiration) {
            clearIdeaStatusBanner()
            return null
        }

        return IdeaStatusBanner(
            message = message,
            targetIdeaId = targetId,
            expirationMillis = expiration,
        )
    }

    suspend fun clearIdeaStatusBanner() {
        context.dataStore.edit { preference ->
            preference.remove(IDEA_BANNER_MESSAGE)
            preference.remove(IDEA_BANNER_TARGET_ID)
            preference.remove(IDEA_BANNER_EXPIRATION_MILLIS)
        }
    }
}

data class IdeaStatusBanner(
    val message: String,
    val targetIdeaId: String,
    val expirationMillis: Long,
)

