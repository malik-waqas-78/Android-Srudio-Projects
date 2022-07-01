package com.test.testroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["movie_title","actor_name"])
data class MovieActors (

    @ColumnInfo(name="movie_title") val movieTitle:String,
    @ColumnInfo(name="actor_name") val actorName:String
    )