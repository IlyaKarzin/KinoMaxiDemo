package ru.maxi.kinomaxi.demo.movieDetails.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxi.kinomaxi.demo.databinding.ItemMovieImageBinding
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage

/**
 * Класс для отображения элемента списка изображений фильма
 */
class MovieImageViewHolder(
    binding: ItemMovieImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val imageView = binding.root

    /**
     * Установить данные [data] для отображения
     */
    fun bind(data: MovieImage) {
        Glide.with(imageView).load(data.previewUrl).into(imageView)
    }
}
