package com.test.testroomapp

import androidx.room.*


@Dao
open interface MovieDirectorsDao {
    @Query("SELECT * FROM movieDirectors")
    fun getAll(): List<MovieDirectors>

    @Query("SELECT movie_title FROM movieDirectors WHERE director_name LIKE :directorName")
    fun findMovieTitleByDirector(directorName: String): List<String>

    @Query("SELECT director_name FROM movieDirectors WHERE movie_title LIKE :movieTitle")
    fun findDirectorByMovieTitle(movieTitle: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg directorName: MovieDirectors)

    @Delete
    fun delete(directorName: MovieDirectors)
}