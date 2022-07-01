package com.test.testroomapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class,MovieActors::class,MovieDirectors::class,MovieWriter::class,MovieGenre::class], version = 1)
open abstract class AppDataBase :RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieActorsDao(): MovieActorsDAO
    abstract fun movieWriterDao(): MovieWriterDao
    abstract fun movieDirectorDao(): MovieDirectorsDao
    abstract fun movieGenreDao(): MovieGenreDao
}