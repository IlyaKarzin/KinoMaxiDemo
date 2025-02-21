package ru.maxi.kinomaxi.demo.data

/**
 * Данные об акканте
 */
data class UserPreferences(
    /**
     * Индентификатор сессии
     * */
    val sessionId: String?,
    /**
     * Ссылка на аватар
     * */
    val avatarUrl: String?,
    /**
     * Логин пользователя
     * */
    val username: String?,
    /**
     * Имя пользователя
     * */
    val name: String?
)