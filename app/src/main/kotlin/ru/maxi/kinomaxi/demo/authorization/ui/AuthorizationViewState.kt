package ru.maxi.kinomaxi.demo.authorization.ui

/**
 * Возможные состояния страницы авторизации
 */
sealed class AuthorizationViewState {

    /**
     * Происходит загрузка данных
     */
    data object Loading : AuthorizationViewState()

    /**
     * Произошла ошибка при загрузке данных
     * @param message текст ошибки
     */
    data class Error(val message: String) : AuthorizationViewState()

    /**
     * Данные загружены
     *
     * @param sessionId идентификатор сессии
     */
    data class Success(val sessionId: String) : AuthorizationViewState()
}