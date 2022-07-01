package com.test.testroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["movie_title","writer_name"])
data class MovieWriter(
    @ColumnInfo(name="movie_title") val movieTitle:String,
    @ColumnInfo(name="writer_name") val writerName:String
)