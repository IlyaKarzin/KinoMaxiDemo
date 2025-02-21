package ru.maxi.kinomaxi.demo.movieDetails.domain

import ru.maxi.kinomaxi.demo.favorites.data.FavoriteMoviesRepository
import ru.maxi.kinomaxi.demo.movieDetails.data.MovieDetailsApiService
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieDetails
import javax.inject.Inject

/**
 * Бизнес-сценарий получения детальной информации о фильме
 */
class GetMovieDetailsUseCase @Inject constructor(
    private val apiService: MovieDetailsApiService,
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
) {

    /**
     * Получить детальную информацию о фильме по идентификатору [movieId]
     */
    suspend operator fun invoke(
        movieId: Long,
    ): MovieDetails {
        val response = apiService.getMovieDetails(movieId)
        val isFavorite = favoriteMoviesRepository.isFavorite(movieId)
        val details = response.copy(isFavorite = isFavorite)
        return details
    }
}
