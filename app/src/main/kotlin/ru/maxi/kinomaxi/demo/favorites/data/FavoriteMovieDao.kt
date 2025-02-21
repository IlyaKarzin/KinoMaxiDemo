package ru.maxi.kinomaxi.demo.favorites.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMoviesEntity)

    @Delete
    suspend fun delete(movie: FavoriteMoviesEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteById(movieId: Long)

    @Query("SELECT * FROM favorite_movies ORDER BY addedDate")
    fun getFavoriteMovies(): Flow<List<FavoriteMoviesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Long): Boolean
}