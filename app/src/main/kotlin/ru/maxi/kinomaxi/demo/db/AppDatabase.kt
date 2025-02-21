package ru.maxi.kinomaxi.demo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.maxi.kinomaxi.demo.favorites.data.FavoriteMovieDao
import ru.maxi.kinomaxi.demo.favorites.data.FavoriteMoviesEntity

@Database(entities = [FavoriteMoviesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}