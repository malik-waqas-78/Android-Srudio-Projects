package com.test.testroomapp

import androidx.room.*


@Dao
open interface MovieActorsDAO {
    @Query("SELECT * FROM movieActors")
    fun getAll(): List<MovieActors>

    @Query("SELECT * FROM movieActors WHERE actor_name Like :actorName")
    fun findMovieTitleByActor(actorName: String): List<MovieActors>

    @Query("SELECT * FROM movieActors WHERE movie_title LIKE :movieTitle")
    fun findActorByMovieTitle(movieTitle: String): List<MovieActors>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg moviesActor: MovieActors)

    @Delete
    fun delete(movieActors: MovieActors)
}