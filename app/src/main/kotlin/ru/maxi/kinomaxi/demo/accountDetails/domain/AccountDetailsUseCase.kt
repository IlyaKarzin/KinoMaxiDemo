package ru.maxi.kinomaxi.demo.accountDetails.domain

import ru.maxi.kinomaxi.demo.accountDetails.data.AccountDetailsApiService
import ru.maxi.kinomaxi.demo.data.UserPreferences
import javax.inject.Inject

/**
 * Бизнес-сценарий получения информации об аккаунте
 */
class AccountDetailsUseCase @Inject constructor(
    private val apiService: AccountDetailsApiService
) {
    /**
     * Получить детальную информацию об аккаунте
     */
    suspend operator fun invoke(
        sessionId: String,
    ): UserPreferences {
        val accountDetails = apiService.getAccountDetails(sessionId)
        val avatarUrl = accountDetails.avatar?.gravatar?.avatarUrl
        val userPreferences = UserPreferences(
            sessionId = sessionId,
            avatarUrl = avatarUrl,
            username = accountDetails.username,
            name = null
        )
        return userPreferences
    }
}