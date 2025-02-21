package ru.maxi.kinomaxi.demo.movieDetails.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage

/**
 * Ответ на запрос списка изображений фильма
 */
@Serializable
data class MovieImagesResponse(
    @SerialName("backdrops") val backdrops: List<MovieImage>
)
