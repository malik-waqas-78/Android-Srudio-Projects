package com.test.testroomapp

import androidx.room.*

@Dao
open interface MovieGenreDao {
    @Query("SELECT * FROM movieGenre")
    fun getAll(): List<MovieGenre>

    @Query("SELECT movie_title FROM movieGenre WHERE genre_name LIKE :genreName")
    fun findMovieTitleByGenre(genreName: String): List<String>

    @Query("SELECT movie_title FROM movieGenre WHERE movie_title LIKE :movieTitle")
    fun findGenreByMovieTitle(movieTitle: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg genre: MovieGenre)

    @Delete
    fun delete(genre: MovieGenre)
}