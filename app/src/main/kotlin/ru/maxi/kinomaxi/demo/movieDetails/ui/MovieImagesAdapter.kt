package ru.maxi.kinomaxi.demo.movieDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.maxi.kinomaxi.demo.databinding.ItemMovieImageBinding
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage

/**
 * Адаптер для списка изображений фильма
 */
class MovieImagesAdapter :
    ListAdapter<MovieImage, MovieImageViewHolder>(MovieImagesDiffCallback()) {
    fun setItems(item: List<MovieImage>) {
        submitList(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieImageBinding.inflate(layoutInflater, parent, false)
        return MovieImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieImageViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}
