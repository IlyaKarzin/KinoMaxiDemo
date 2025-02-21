package ru.maxi.kinomaxi.demo.favorites.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
class FavoriteMoviesEntity(
    @PrimaryKey val id: Long,
    val posterPath: String,
    val addedDate: Long
)