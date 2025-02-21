package ru.maxi.kinomaxi.demo.movieList.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.maxi.kinomaxi.demo.movieList.data.MoviesListApiService

@Module
@InstallIn(SingletonComponent::class)
class MovieListModule {

    @Provides
    fun provideMoviesListApiService(retrofit: Retrofit): MoviesListApiService {
        return retrofit.create(MoviesListApiService::class.java)
    }

}