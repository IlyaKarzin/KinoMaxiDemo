package ru.maxi.kinomaxi.demo

import ru.maxi.kinomaxi.demo.data.UserPreferences

sealed class MainActivityViewState {

    /**
     * Пользователь неавторизирован
     */
    data object Unauthorized :  MainActivityViewState()

    /**
     * Пользователь авторизирован
     *
     * @param userPreferences данные об аккаунте
     */
    data class Authorized(val userPreferences: UserPreferences?) :  MainActivityViewState()

}