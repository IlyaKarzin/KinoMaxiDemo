package ru.maxi.kinomaxi.demo.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.maxi.kinomaxi.demo.movieList.model.Movie
import javax.inject.Inject

/**
 * Репозиторий для работы с избранными фильмами
 */
class FavoriteMoviesRepository @Inject constructor(
    private val movieDao: FavoriteMovieDao
) {

    /** Cписок избранных фильмов */
    val favoriteMoviesFlow: Flow<List<Movie>> = movieDao.getFavoriteMovies().map {
        it.map(FavoriteMoviesEntity::toMovie)
    }

    /**
     * Добавить фильм [movie] в список избранных фильмов
     */
    suspend fun addToFavorites(movie: Movie) {
        movieDao.insert(movie.toFavoriteMovieEntity(System.currentTimeMillis()))
    }

    /**
     * Удалить фильм с идентификатором [movieId] из списка избранных фильмов
     */
    suspend fun removeFromFavorites(movieId: Long) {
        movieDao.deleteById(movieId)
    }

    /**
     * Получить признак наличия фильма с идентификатором [movieId] в списке избранных фильмов
     */
    suspend fun isFavorite(movieId: Long): Boolean {
        return movieDao.isFavorite(movieId)
    }
}

private fun Movie.toFavoriteMovieEntity(addedDate: Long) = FavoriteMoviesEntity(id, posterPath!!, addedDate )

private fun FavoriteMoviesEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = "Placeholder Title",
        posterPath = this.posterPath
    )
}
