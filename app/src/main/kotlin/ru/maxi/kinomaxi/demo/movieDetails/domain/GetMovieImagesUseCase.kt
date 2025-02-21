package ru.maxi.kinomaxi.demo.movieDetails.domain

import ru.maxi.kinomaxi.demo.movieDetails.data.MovieDetailsApiService
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage
import javax.inject.Inject

/**
 * Бизнес-сценарий получения списка изображений фильма
 */
class GetMovieImagesUseCase @Inject constructor(
    private val apiService: MovieDetailsApiService,
) {

    /**
     * Получить список изображений фильма по идентификатору [movieId]
     */
    suspend operator fun invoke(
        movieId: Long,
    ): List<MovieImage> {
        val response = apiService.getMovieImages(movieId)
        return response.backdrops

    }
}
