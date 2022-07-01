package com.test.testroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["movie_title","genre_name"])
data class MovieGenre (

    @ColumnInfo(name="movie_title") val movieTitle:String,
    @ColumnInfo(name="genre_name") val genreName:String
    )
