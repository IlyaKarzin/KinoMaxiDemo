package ru.maxi.kinomaxi.demo.movieList.domain

import ru.maxi.kinomaxi.demo.movieList.data.MoviesListApiService
import ru.maxi.kinomaxi.demo.movieList.model.Movie
import ru.maxi.kinomaxi.demo.movieList.model.MoviesListType
import javax.inject.Inject


/**
 * Бизнес-сценарий получения списка фильмов
 */
class GetMoviesListUseCase @Inject constructor(
    private val apiService: MoviesListApiService,
) {

    /**
     * Получить список фильмов с типом [listType]
     */
    suspend operator fun invoke(listType: MoviesListType): List<Movie> {
        val response = when (listType) {
            MoviesListType.TOP_RATED_MOVIES -> apiService.getTopRatedMovies()
            MoviesListType.POPULAR_MOVIES -> apiService.getPopularMovies()
            MoviesListType.UPCOMING_MOVIES -> apiService.getUpcomingMovies()
        }
        return response.movies


    }
}
