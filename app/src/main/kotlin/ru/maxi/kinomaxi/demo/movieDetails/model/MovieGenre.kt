package ru.maxi.kinomaxi.demo.movieDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Сущность жанра фильма
 */
@Serializable
data class MovieGenre(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
)
