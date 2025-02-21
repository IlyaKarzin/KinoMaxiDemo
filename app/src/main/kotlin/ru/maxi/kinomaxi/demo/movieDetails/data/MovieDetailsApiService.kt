package ru.maxi.kinomaxi.demo.movieDetails.data

import retrofit2.http.GET
import retrofit2.http.Path
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieDetails

/**
 * Интерфейс взаимодействия с REST API
 */
interface MovieDetailsApiService {

    /**
     * Получить подробную информацию о фильме с идентификатором [movieId]
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Long): MovieDetails

    /**
     * Получить список изображений для фильма с идентификатором [movieId]
     */
    @GET("movie/{movie_id}/images?include_image_language=en,null")
    suspend fun getMovieImages(@Path("movie_id") movieId: Long): MovieImagesResponse

}
