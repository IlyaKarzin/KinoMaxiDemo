package ru.maxi.kinomaxi.demo.movieList.ui

import androidx.recyclerview.widget.DiffUtil
import ru.maxi.kinomaxi.demo.movieList.model.Movie

class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}