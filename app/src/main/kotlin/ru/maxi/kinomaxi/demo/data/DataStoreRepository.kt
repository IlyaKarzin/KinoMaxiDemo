package ru.maxi.kinomaxi.demo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.maxi.kinomaxi.demo.accountDetails.data.AccountDetailsApiService
import ru.maxi.kinomaxi.demo.accountDetails.domain.AccountDetailsUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы с данными аккаунта
 */
@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val accountDetailsApiService: AccountDetailsApiService,
    private val accountDetailsUseCase: AccountDetailsUseCase
) {

    /**
     * Информация об аккаунте
     * */
    val userPreferencesFlow: Flow<UserPreferences?> = getUserPreference()


    companion object {
        private val SESSION_ID_KEY = PreferencesKeys.SESSION_ID
        private val USERNAME_KEY = PreferencesKeys.USERNAME
        private val AVATAR_URL_KEY = PreferencesKeys.AVATAR_URL
    }

    /**
     * Сохранить данные пользователя
     * */
    suspend fun saveUserPreference(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[SESSION_ID_KEY] = userPreferences.sessionId ?: ""
            preferences[USERNAME_KEY] = userPreferences.username ?: ""
            preferences[AVATAR_URL_KEY] = userPreferences.avatarUrl ?: ""
        }
    }

    private fun getUserPreference(): Flow<UserPreferences?> {
        return dataStore.data
            .map { preferences ->
                val sessionId = preferences[SESSION_ID_KEY] ?: ""
                val username = preferences[USERNAME_KEY] ?: ""
                val avatarUrl = preferences[AVATAR_URL_KEY] ?: ""

                UserPreferences(sessionId, avatarUrl, username, null)
            }
    }

    /**
     * Проверить идентификатор сессии
     * */
    suspend fun checkSession() {
        val sessionId = dataStore.data.map { it[SESSION_ID_KEY] }.firstOrNull()
        if (sessionId.isNullOrEmpty()) {
            clearUserPreferences()
        } else {
            try {
                val accountDetails = accountDetailsUseCase.invoke(sessionId)
                saveUserPreference(accountDetails)
            } catch (e: Exception){
                val username = dataStore.data.map { it[USERNAME_KEY] }.firstOrNull()
                val newSessionId = dataStore.data.map { it[SESSION_ID_KEY] }.firstOrNull()
                val accountDetails = UserPreferences(
                    sessionId = newSessionId,
                    username = username.toString(),
                    avatarUrl = null,
                    name = null
                )
                saveUserPreference(accountDetails)
            }
        }
    }

    /**
     * Выйти из аккаунта и удалить сессию
     * */
    suspend fun clearUserPreferences() {
        val sessionId = getUserPreference()
            .map { userPreferences -> userPreferences?.sessionId }
            .firstOrNull()
        dataStore.edit { preferences ->
            preferences.clear()
        }
        accountDetailsApiService.deleteSession(sessionId!!)
    }
}