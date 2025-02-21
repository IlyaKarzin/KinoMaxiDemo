package ru.maxi.kinomaxi.demo.movieList.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.maxi.kinomaxi.demo.movieList.model.Movie

/**
 * Ответ на запрос списка фильмов
 */
@Serializable
data class MoviesListResponse(
    @SerialName("results") val movies: List<Movie>,
)
