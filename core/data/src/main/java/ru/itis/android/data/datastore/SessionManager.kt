package ru.itis.android.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.itis.android.network.auth.AuthTokenProvider
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reparo_preferences")

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) : AuthTokenProvider {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    val roleFlow: Flow<String?> = context.dataStore.data.map { it[USER_ROLE_KEY] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { it[USER_ROLE_KEY] = role }
    }

    suspend fun clearToken() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ROLE_KEY)
        }
    }

    override suspend fun token(): String? = tokenFlow.first()
}
