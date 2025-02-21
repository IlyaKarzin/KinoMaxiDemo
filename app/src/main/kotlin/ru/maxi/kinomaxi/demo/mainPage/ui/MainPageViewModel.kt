package ru.maxi.kinomaxi.demo.mainPage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ru.maxi.kinomaxi.demo.movieList.domain.GetMoviesListUseCase
import ru.maxi.kinomaxi.demo.movieList.model.MoviesListType
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getMoviesList: GetMoviesListUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainPageState>(MainPageState.Loading)

    /** Cостояние главной страницы */
    val viewState = _viewState.asStateFlow().onStart {
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainPageState.Loading
    )


    fun refreshData() {
        _viewState.value = MainPageState.Loading
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val (topRatedMovies, popularMovies, upcomingMovies) = coroutineScope {
                    listOf(
                        async { getMoviesList(MoviesListType.TOP_RATED_MOVIES) },
                        async { getMoviesList(MoviesListType.POPULAR_MOVIES) },
                        async { getMoviesList(MoviesListType.UPCOMING_MOVIES) },
                    ).awaitAll()
                }

                if (topRatedMovies.isEmpty() && popularMovies.isEmpty() && upcomingMovies.isEmpty()) {
                    _viewState.value = MainPageState.Error
                } else {
                    val viewData = MainPageData(
                        topRatedMoviesList = topRatedMovies,
                        topPopularMoviesList = popularMovies,
                        topUpcomingMoviesList = upcomingMovies,
                    )
                    _viewState.value = MainPageState.Success(viewData)
                }

            } catch (e: Exception) {
                _viewState.value = MainPageState.Error
            }

        }

    }
}