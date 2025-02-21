package ru.maxi.kinomaxi.demo.movieDetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.favorites.data.FavoriteMoviesRepository
import ru.maxi.kinomaxi.demo.movieDetails.di.MovieDetailsViewModelFactory
import ru.maxi.kinomaxi.demo.movieDetails.domain.GetMovieDetailsUseCase
import ru.maxi.kinomaxi.demo.movieDetails.domain.GetMovieImagesUseCase
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieDetails
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage
import ru.maxi.kinomaxi.demo.movieList.model.Movie

@HiltViewModel(assistedFactory = MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Long,
    private val getMovieDetailsById: GetMovieDetailsUseCase,
    private val getMovieImagesById: GetMovieImagesUseCase,
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow<MovieDetailsViewState>(MovieDetailsViewState.Loading)

    /** Cостояние страницы информации о фильме */
    val viewState = _viewState.asStateFlow().onStart {
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieDetailsViewState.Loading
    )

    fun refreshData() {
        _viewState.value = MovieDetailsViewState.Loading
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val (movieDetails, movieImages) = coroutineScope {
                    listOf(
                        async { getMovieDetailsById(movieId) },
                        async { getMovieImagesById(movieId) },
                    ).awaitAll()
                }

                require(movieDetails is MovieDetails)
                movieImages as List<MovieImage>

                if (movieDetails != null && movieImages.isNotEmpty()) _viewState.value =
                    MovieDetailsViewState.Success(
                        movieDetails = movieDetails,
                        movieImages = movieImages,
                    )
            } catch (e: Exception) {
                _viewState.value = MovieDetailsViewState.Error
            }
        }
    }


    fun toggleFavorites() {
        val movieImages = (_viewState.value as MovieDetailsViewState.Success).movieImages
        val movieDetails = (_viewState.value as MovieDetailsViewState.Success).movieDetails.let {
            it.copy(isFavorite = !it.isFavorite)
        } ?: return
        viewState
        if (movieDetails.isFavorite) {
            viewModelScope.launch {
                try {
                    favoriteMoviesRepository.addToFavorites(movieDetails.toMovie())
                } catch (e: Exception) {
                    _viewState.value = MovieDetailsViewState.Error
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    favoriteMoviesRepository.removeFromFavorites(movieId)
                } catch (e: Exception) {
                    _viewState.value = MovieDetailsViewState.Error
                }
            }
        }
        _viewState.value = MovieDetailsViewState.Success(movieDetails, movieImages)
    }
}

private fun MovieDetails.toMovie() = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
)
