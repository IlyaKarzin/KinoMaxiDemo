package ru.maxi.kinomaxi.demo.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.maxi.kinomaxi.demo.favorites.data.FavoriteMoviesRepository
import ru.maxi.kinomaxi.demo.movieList.model.Movie
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
) : ViewModel() {

    /** Cписок избранных фильмов */
    val favoriteMoviesFlow: StateFlow<List<Movie>> =
        favoriteMoviesRepository.favoriteMoviesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = mutableListOf()
        )
}
