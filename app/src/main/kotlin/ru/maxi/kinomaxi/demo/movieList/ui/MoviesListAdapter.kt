package ru.maxi.kinomaxi.demo.movieList.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxi.kinomaxi.demo.databinding.ItemMovieBinding
import ru.maxi.kinomaxi.demo.movieList.model.Movie

/**
 * [RecyclerView.Adapter] для списка фильмов
 */
class MoviesListAdapter(
    private val onMovieClick: (movieId: Long) -> Unit,
    private val isFavoritesList: Boolean = false,
) : ListAdapter<Movie, MovieViewHolder>(MoviesDiffCallback()) {

    fun setItems(item: List<Movie>) {
        submitList(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(viewBinding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onViewAttachedToWindow(holder: MovieViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (!isFavoritesList) return

        with(ItemMovieBinding.bind(holder.itemView)) {
            root.updateLayoutParams {
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            moviePoster.updateLayoutParams {
                width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            }
        }
    }
}
