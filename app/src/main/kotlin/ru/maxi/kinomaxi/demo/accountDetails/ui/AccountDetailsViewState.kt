package ru.maxi.kinomaxi.demo.accountDetails.ui

import ru.maxi.kinomaxi.demo.data.UserPreferences

/**
 * Возможные состояния экрана детальной информации об аккаунте
 */
sealed class AccountDetailsViewState {

    /**
    * Загрузка страницы информации об аккаунте
    */
    data object Loading :  AccountDetailsViewState()

    /**
     * Произошла ошибка при загрузке данных аккаунта
     *
      * @param message сообщение об ошибке
     */
    data class Error(val message: String) : AccountDetailsViewState()

    /**
    * Пользователь авторизирован
    *
    * @param userPreferences данные об аккаунте
    */
    data class Authorized(val userPreferences: UserPreferences) : AccountDetailsViewState()


}