package com.test.testroomapp

import android.content.Context
import androidx.room.Room

class DataBaseManager {
    companion object{
        var db:AppDataBase?=null
        var movieDao:MovieDao?=null
        var movieWriterDao:MovieWriterDao?=null
        var movieDirectorsDao:MovieDirectorsDao?=null
        var movieActorsDAO:MovieActorsDAO?=null
        var movieGenreDao:MovieGenreDao?=null
        fun getMovieDB(context: Context):AppDataBase?{
            initMovieDB(context)
            return db
        }
        fun getMWD():MovieWriterDao?{
            if(movieWriterDao==null){
                movieWriterDao=db?.movieWriterDao()
            }
            return movieWriterDao
        }
        fun getMDD():MovieDirectorsDao?{
            if(movieDirectorsDao==null){
                movieDirectorsDao=db?.movieDirectorDao()
            }
            return movieDirectorsDao
        }
        fun getMD():MovieDao?{
            if(movieDao==null){
                movieDao=db?.movieDao()
            }
            return movieDao
        }
        fun getMAD():MovieActorsDAO?{
            if(movieActorsDAO==null){
                movieActorsDAO=db?.movieActorsDao()
            }
            return movieActorsDAO
        }
        fun getMGD():MovieGenreDao?{
            if(movieGenreDao==null){
                movieGenreDao=db?.movieGenreDao()
            }
            return movieGenreDao
        }
        fun initMovieDB(applicationContext: Context) {
            if(db==null) {
                db = Room.databaseBuilder(
                    applicationContext,
                    AppDataBase::class.java, "movies_database"
                ).allowMainThreadQueries()
                    .build()
            }
            getMAD()
            getMD()
            getMDD()
            getMGD()
            getMWD()
        }
    }
}