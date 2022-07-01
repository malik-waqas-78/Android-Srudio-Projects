package com.test.testroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(

    @PrimaryKey val title: String,
    @ColumnInfo(name="year") val year: String?,
    @ColumnInfo(name="rated") val rated: String?,
    @ColumnInfo(name="released") val released: String?,
    @ColumnInfo(name="runtime") val runtime: String?,
    @ColumnInfo(name="plots") val plots: String?,

)