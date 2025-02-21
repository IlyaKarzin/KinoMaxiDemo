package ru.maxi.kinomaxi.demo.movieList.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxi.kinomaxi.demo.databinding.ItemMovieBinding
import ru.maxi.kinomaxi.demo.movieList.model.Movie

/**
 * [RecyclerView.ViewHolder] для отображения карточки фильма в списке фильмов
 */
class MovieViewHolder(
    viewBinding: ItemMovieBinding,
    private val onMovieClick: (movieId: Long) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private val rootView = viewBinding.root
    private val imageView = viewBinding.moviePoster

    /**
     * Установить данные [data] для отображения
     */
    fun bind(data: Movie) {
        Glide.with(imageView).load(data.posterUrl).into(imageView)
        rootView.setOnClickListener { onMovieClick(data.id) }
    }
}
