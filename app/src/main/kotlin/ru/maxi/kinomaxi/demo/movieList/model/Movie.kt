package ru.maxi.kinomaxi.demo.movieList.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.maxi.kinomaxi.demo.AppConfig

/**
 * Данные о фильме
 */
@Serializable
data class Movie(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("poster_path") val posterPath: String?,
) {

    val posterUrl: String?
        get() = posterPath?.let {
            "${AppConfig.IMAGE_BASE_URL}${AppConfig.POSTER_PREVIEW_SIZE}$it"
        }
}
