package com.test.testroomapp


import java.io.Serializable

data class MovieRowModel(var title:String?=null,var year:String?="",var imdbID:String?="",
                         var type:String?="",
                         var poster:String?=""
):Serializable{


    fun getPrintableString(): String {
        return "${SearchMovieByString.Companion.Keys.TITLE.value} : $title\n" +
                "${SearchMovieByString.Companion.Keys.YEAR.value} : $year\n" +
                "${SearchMovieByString.Companion.Keys.IMDBID.value} : $imdbID\n" +
                "${SearchMovieByString.Companion.Keys.TYPE.value} : $type\n" +
                "${SearchMovieByString.Companion.Keys.POSTER.value} : $poster"

    }





}