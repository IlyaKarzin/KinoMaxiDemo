package ru.maxi.kinomaxi.demo.mainPage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.R
import ru.maxi.kinomaxi.demo.databinding.FragmentMainPageBinding
import ru.maxi.kinomaxi.demo.databinding.LayoutErrorViewBinding
import ru.maxi.kinomaxi.demo.databinding.LayoutMoviesListBinding
import ru.maxi.kinomaxi.demo.movieList.model.Movie
import ru.maxi.kinomaxi.demo.movieList.model.MoviesListType
import ru.maxi.kinomaxi.demo.movieList.ui.MoviesListAdapter
import ru.maxi.kinomaxi.demo.setSubtitle
import ru.maxi.kinomaxi.demo.setTitle

@AndroidEntryPoint
class MainPageFragment : Fragment() {

    private var _viewBinding: FragmentMainPageBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: MainPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewBinding = FragmentMainPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            favoritesButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainPageFragment_to_favoritesFragment)
            }
            errorView.setOnInflateListener { _, inflated ->
                with(LayoutErrorViewBinding.bind(inflated)) {
                    errorActionButton.setOnClickListener { viewModel.refreshData() }
                }
            }

            topRatedMoviesList.moviesListSlider.adapter = MoviesListAdapter(::onMovieClick)
            topPopularMoviesList.moviesListSlider.adapter = MoviesListAdapter(::onMovieClick)
            topUpcomingMoviesList.moviesListSlider.adapter = MoviesListAdapter(::onMovieClick)

            topRatedMoviesList.setMoviesListTitle(MoviesListType.TOP_RATED_MOVIES)
            topPopularMoviesList.setMoviesListTitle(MoviesListType.POPULAR_MOVIES)
            topUpcomingMoviesList.setMoviesListTitle(MoviesListType.UPCOMING_MOVIES)

            ViewCompat.setOnApplyWindowInsetsListener(contentView) { _, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                favoritesButton.updateLayoutParams<MarginLayoutParams> {
                    bottomMargin += insets.bottom
                }
                contentScrollView.updatePadding(bottom = insets.bottom)
                WindowInsetsCompat.CONSUMED
            }
        }

        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.viewState.collect { state ->
                        showNewState(state)
                    } }
                }
            }
        }
    }

    private fun onMovieClick(movieId: Long) {
        val action =
            MainPageFragmentDirections.actionMainPageFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }

    private fun showNewState(state: MainPageState) {
        when (state) {
            MainPageState.Loading -> with(viewBinding) {
                contentView.isVisible = false
                loaderView.show()
                errorView.isVisible = false
            }

            MainPageState.Error -> with(viewBinding) {
                contentView.isVisible = false
                loaderView.hide()
                errorView.isVisible = true
            }

            is MainPageState.Success -> with(viewBinding) {
                contentView.isVisible = true
                loaderView.hide()
                errorView.isVisible = false
                showData(state.data)
            }
        }
    }

    private fun FragmentMainPageBinding.showData(data: MainPageData) {
        setTitle(getString(R.string.app_name))
        setSubtitle(null)

        topRatedMoviesList.showMoviesList(data.topRatedMoviesList)
        topPopularMoviesList.showMoviesList(data.topPopularMoviesList)
        topUpcomingMoviesList.showMoviesList(data.topUpcomingMoviesList)
    }

    private fun LayoutMoviesListBinding.setMoviesListTitle(moviesListType: MoviesListType) {
        moviesListTitle.setText(moviesListType.titleResId)
    }

    private fun LayoutMoviesListBinding.showMoviesList(moviesList: List<Movie>) {
        (moviesListSlider.adapter as? MoviesListAdapter)?.setItems(moviesList)
    }
}

private val MoviesListType.titleResId: Int
    @StringRes get() = when (this) {
        MoviesListType.TOP_RATED_MOVIES -> R.string.top_rated_title
        MoviesListType.POPULAR_MOVIES -> R.string.top_popular_title
        MoviesListType.UPCOMING_MOVIES -> R.string.top_upcoming_title
    }
