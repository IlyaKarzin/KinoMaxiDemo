package ru.maxi.kinomaxi.demo.movieDetails.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Сущность детальной информации о фильме
 */
@Serializable
data class MovieDetails(
    @SerialName("id") val id: Long,
    @SerialName("imdb_id") val imdbId: String?,
    @SerialName("title") val title: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("overview") val overview: String?,
    @SerialName("tagline") val tagline: String?,
    @SerialName("genres") val genres: List<MovieGenre>,
    @SerialName("release_date") val releaseDate: LocalDate,
    @SerialName("runtime") val lengthMinutes: Int,
    @SerialName("vote_average") val rating: Float,
    val isFavorite: Boolean = false,
) {

    val posterImage: MovieImage?
        get() = posterPath?.let { MovieImage(posterPath) }
}
