package ru.maxi.kinomaxi.demo.movieDetails.ui

import ru.maxi.kinomaxi.demo.movieDetails.model.MovieDetails
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage

/**
 * Возможные состояния экрана детальной информации о фильме
 */
sealed class MovieDetailsViewState {

    /**
     * Происходит загрузка данных
     */
    data object Loading : MovieDetailsViewState()

    /**
     * Произошла ошибка при загрузке данных
     */
    data object Error : MovieDetailsViewState()

    /**
     * Данные загружены
     *
     * @param movieDetails информация о фильме
     * @param movieImages список изображений фильма
     */
    data class Success(
        val movieDetails: MovieDetails,
        val movieImages: List<MovieImage>,
    ) : MovieDetailsViewState()

}
