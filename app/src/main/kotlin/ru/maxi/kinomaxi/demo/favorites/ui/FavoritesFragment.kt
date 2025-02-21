package ru.maxi.kinomaxi.demo.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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
import ru.maxi.kinomaxi.demo.databinding.FragmentFavoritesBinding
import ru.maxi.kinomaxi.demo.movieList.model.Movie
import ru.maxi.kinomaxi.demo.movieList.ui.MoviesListAdapter
import ru.maxi.kinomaxi.demo.setSubtitle
import ru.maxi.kinomaxi.demo.setTitle

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _viewBinding: FragmentFavoritesBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(getString(R.string.favorite_movies_list_title))
        setSubtitle(null)

        with(viewBinding) {
            moviesListView.adapter = MoviesListAdapter(::onMovieClick, isFavoritesList = true)

            ViewCompat.setOnApplyWindowInsetsListener(moviesListView) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updatePadding(bottom = view.paddingBottom + insets.bottom)
                WindowInsetsCompat.CONSUMED
            }
        }

        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch { viewModel.favoriteMoviesFlow.collect(::showFavorites) }
                }
            }
        }
    }

    private fun onMovieClick(movieId: Long) {
        val action =
            FavoritesFragmentDirections.actionFavoritesFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }

    private fun showFavorites(favoriteMovies: List<Movie>) {
        with(viewBinding) {
            moviesListView.isVisible = favoriteMovies.isNotEmpty()
            emptyDataView.isVisible = favoriteMovies.isEmpty()
            (moviesListView.adapter as? MoviesListAdapter)?.setItems(favoriteMovies)
        }
    }
}
