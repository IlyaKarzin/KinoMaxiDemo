package ru.maxi.kinomaxi.demo.movieDetails.di

import dagger.assisted.AssistedFactory
import ru.maxi.kinomaxi.demo.movieDetails.ui.MovieDetailsViewModel

@AssistedFactory
interface MovieDetailsViewModelFactory {
    fun create(movieId: Long): MovieDetailsViewModel
}