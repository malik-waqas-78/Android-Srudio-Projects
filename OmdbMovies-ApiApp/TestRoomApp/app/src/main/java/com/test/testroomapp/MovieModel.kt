package com.test.testroomapp

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Room
import com.test.testroomapp.DataBaseManager.Companion.movieActorsDAO
import com.test.testroomapp.DataBaseManager.Companion.movieDirectorsDao
import com.test.testroomapp.DataBaseManager.Companion.movieGenreDao
import com.test.testroomapp.DataBaseManager.Companion.movieWriterDao

class MovieModel (

    var title: String?=null,

    var year: String?=null,

    var rated: String?=null,

    var released: String?=null,

    var runtime: String?=null,

    var genreList: ArrayList<String>?=null,

    var directorList: ArrayList<String>?=null,

    var writerList: ArrayList<String>?=null,

    var actorsList: ArrayList<String>?=null,

    var plots: String?=null,
):Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("genreList"),
        TODO("directorList"),
        TODO("writerList"),
        TODO("actorsList"),
        parcel.readString()
    ) {
    }

    fun insertMovie(){
            DataBaseManager.movieDao?.insertAll(Movie(title!!,year,rated,released,runtime,plots))
            insertMA()
            insertMD()
            insertMG()
            insertMW()
    }
    private fun insertMA(){
        for(actor in actorsList!!) {
            movieActorsDAO?.insertAll(MovieActors(movieTitle = title!!, actorName = actor))
        }
    }
    private fun insertMD(){
        for(actor in directorList!!) {
            movieDirectorsDao?.insertAll(MovieDirectors(movieTitle = title!!, directorName = actor))
        }
    }
    private fun insertMW(){
        for(actor in writerList!!) {
            movieWriterDao?.insertAll(MovieWriter(movieTitle = title!!, writerName = actor))
        }
    }
    private fun insertMG(){
        for(actor in genreList!!) {
            movieGenreDao?.insertAll(MovieGenre(movieTitle = title!!, genreName = actor))
        }
    }



    fun getPrintableString(): String? {
        return "${SearchForMovie.Companion.Keys.TITLE.value} : $title\n" +
                "${SearchForMovie.Companion.Keys.YEAR.value} : $year\n" +
                "${SearchForMovie.Companion.Keys.RATED.value} : $rated\n" +
                "${SearchForMovie.Companion.Keys.RELEASED.value} : $released\n" +
                "${SearchForMovie.Companion.Keys.RUNTIME.value} : $runtime\n" +
                "${SearchForMovie.Companion.Keys.GENRE.value} : ${getString(genreList)}\n"+
                "${SearchForMovie.Companion.Keys.DIRECTOR.value} : ${getString(directorList)}\n"+
                "${SearchForMovie.Companion.Keys.WRITER.value} : ${getString(writerList)}\n"+
                "${SearchForMovie.Companion.Keys.ACTORS.value} : ${getString(actorsList)}\n"+
                "${SearchForMovie.Companion.Keys.PLOT.value} : $plots\n"

    }
    private fun getString(arr:ArrayList<String>?):String{
        var str:String=""
        if (arr != null) {
            for(string in arr){
                str= "$str ${if (str.isNotEmpty()) "," else ""}$string"
            }
        }else{
            str="N/A"
        }
        return str
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(year)
        parcel.writeString(rated)
        parcel.writeString(released)
        parcel.writeString(runtime)
        parcel.writeString(plots)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieModel> {
        override fun createFromParcel(parcel: Parcel): MovieModel {
            return MovieModel(parcel)
        }

        override fun newArray(size: Int): Array<MovieModel?> {
            return arrayOfNulls(size)
        }
    }


}