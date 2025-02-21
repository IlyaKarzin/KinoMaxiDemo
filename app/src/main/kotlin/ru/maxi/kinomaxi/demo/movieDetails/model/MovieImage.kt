package ru.maxi.kinomaxi.demo.movieDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.maxi.kinomaxi.demo.AppConfig

/**
 * Сущность изображения фильма
 */
@Serializable
data class MovieImage(
    @SerialName("file_path") val path: String,
) {
    val previewUrl: String
        get() = "${AppConfig.IMAGE_BASE_URL}${AppConfig.BACKDROP_PREVIEW_SIZE}$path"
}
