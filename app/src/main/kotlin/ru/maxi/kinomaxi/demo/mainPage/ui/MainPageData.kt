package ru.maxi.kinomaxi.demo.mainPage.ui

import ru.maxi.kinomaxi.demo.movieList.model.Movie

/**
 * View-представление данных главной страницы
 *
 * @param topRatedMoviesList список фильмов с самым высоким рейтингом
 * @param topPopularMoviesList список текущих популярных фильмов
 * @param topUpcomingMoviesList список ещё не вышедших фильмов
 */
data class MainPageData(
    val topRatedMoviesList: List<Movie>,
    val topPopularMoviesList: List<Movie>,
    val topUpcomingMoviesList: List<Movie>,
)
