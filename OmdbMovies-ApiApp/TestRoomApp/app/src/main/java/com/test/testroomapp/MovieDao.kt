package com.test.testroomapp

import androidx.room.*


@Dao
open interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE title IN (:titles)")
    fun loadAllByTitle(titles: ArrayList<String>): List<Movie>

    @Query("SELECT * FROM movie WHERE title LIKE :title")
    fun findByTitle(title: String): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg movies: Movie)

    @Delete
    fun delete(movie: Movie)
}

