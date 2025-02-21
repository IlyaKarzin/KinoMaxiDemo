package ru.maxi.kinomaxi.demo.movieDetails.ui

import androidx.recyclerview.widget.DiffUtil
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage

class MovieImagesDiffCallback : DiffUtil.ItemCallback<MovieImage>() {

    override fun areItemsTheSame(oldItem: MovieImage, newItem: MovieImage): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: MovieImage, newItem: MovieImage): Boolean {
        return oldItem == newItem
    }
}