package com.test.testroomapp

import androidx.room.*


@Dao
open interface MovieWriterDao {
    @Query("SELECT * FROM movieWriter")
    fun getAll(): List<MovieWriter>

    @Query("SELECT movie_title FROM movieWriter WHERE writer_name LIKE :writerName")
    fun findMovieTitleByWriter(writerName: String): List<String>

    @Query("SELECT writer_name FROM movieWriter WHERE movie_title LIKE :movieTitle")
    fun findWriterByMovieTitle(movieTitle: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg directorName: MovieWriter)

    @Delete
    fun delete(directorName: MovieWriter)
}