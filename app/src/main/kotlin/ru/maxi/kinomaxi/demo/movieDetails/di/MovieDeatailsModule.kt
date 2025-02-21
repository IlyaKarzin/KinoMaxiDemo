package ru.maxi.kinomaxi.demo.movieDetails.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.maxi.kinomaxi.demo.movieDetails.data.MovieDetailsApiService

@Module
@InstallIn(SingletonComponent::class)
class MovieDeatailsModule {

    @Provides
    fun provideMovieDetailsApiService(retrofit: Retrofit): MovieDetailsApiService {
        return retrofit.create(MovieDetailsApiService::class.java)
    }

}