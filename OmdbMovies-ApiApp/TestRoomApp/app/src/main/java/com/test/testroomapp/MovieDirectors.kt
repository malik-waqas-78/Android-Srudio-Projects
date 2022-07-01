package com.test.testroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["movie_title","director_name"])
data class MovieDirectors (
    @ColumnInfo(name="movie_title") val movieTitle:String,
    @ColumnInfo(name="director_name") val directorName:String
)