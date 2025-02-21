package ru.maxi.kinomaxi.demo.mainPage.ui

/**
 * Возможные состояния главной страницы
 */
sealed class MainPageState {

    /**
     * Происходит загрузка данных
     */
    data object Loading : MainPageState()

    /**
     * Произошла ошибка при загрузке данных
     */
    data object Error : MainPageState()

    /**
     * Данные загружены
     *
     * @param data данные экрана
     */
    data class Success(
        val data: MainPageData,
    ) : MainPageState()
}
