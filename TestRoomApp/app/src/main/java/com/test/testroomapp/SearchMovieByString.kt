package com.test.testroomapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.test.testroomapp.databinding.ActivitySearchMovieByStringBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class SearchMovieByString : AppCompatActivity() {
    lateinit var binding:ActivitySearchMovieByStringBinding

    val apiKey="577cd78d";

    var movieDetails=ArrayList<MovieRowModel>()

    companion object {
        enum class Keys(val value: String) {
            TITLE("Title"),
            YEAR("Year"),
            IMDBID("imdbID"),
            TYPE("Type"),
            POSTER("Poster"),
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchMovieByStringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Search Movie by String"
        binding.tvMovies.movementMethod = ScrollingMovementMethod()
        if(savedInstanceState!=null){
            binding.tvMovies.text=savedInstanceState.getString("tv")
            binding.etTitleString.setText(savedInstanceState.getString("et"))
            if(savedInstanceState.getBoolean("tv_state")){
                binding.tvMovies.visibility=View.VISIBLE
                binding.pBar.visibility=View.GONE
            }else{
                binding.tvMovies.visibility=View.GONE
                binding.pBar.visibility=View.VISIBLE
            }
            movieDetails= savedInstanceState.getSerializable("obj") as ArrayList<MovieRowModel>
        }

        binding.btnSearch.setOnClickListener {
            binding.tvMovies.visibility=View.GONE
            binding.pBar.visibility= View.VISIBLE
            binding.tvMovies.text=""
            movieDetails= ArrayList()
            var title=binding.etTitleString.text.toString().trim()
            if(title.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    searchMovie(title)
                }
            }
        }
    }

    private suspend fun searchMovie(title: String) {

        val url = URL("https://www.omdbapi.com/?s=$title&apikey=$apiKey")

        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            inputStream.bufferedReader().use {
                var result=""
                it.lines().forEach { line ->
                    result += line
                }
                var obj= JSONObject(result)
                getMovieDetails(obj)
                withContext(Dispatchers.Main){
                    var str=""
                    for(i in movieDetails.indices){
                        str+="\n\n$i\n${movieDetails[i].getPrintableString()}"
                    }
                    binding.tvMovies.text=str
                    binding.tvMovies.visibility=View.VISIBLE
                    binding.pBar.visibility=View.GONE
                }
            }
        }



    }

    private fun getMovieDetails(array: JSONObject) {
        val movies: JSONArray = array.getJSONArray("Search")
        for(i in 0 until movies.length()){
            val movie=MovieRowModel()
            val movieOBJ=movies.getJSONObject(i)
            movie.title=movieOBJ.getString(Keys.TITLE.value)
            movie.year=movieOBJ.getString(Keys.YEAR.value)
            movie.imdbID=movieOBJ.getString(Keys.IMDBID.value)
            movie.type=movieOBJ.getString(Keys.TYPE.value)
            movie.poster=movieOBJ.getString(Keys.POSTER.value)
            movieDetails.add(movie)
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("et",binding.etTitleString.text.toString())
        outState.putString("tv",binding.tvMovies.text.toString())
        outState.putBoolean("tv_state",binding.tvMovies.isVisible)
        outState.putSerializable("obj",movieDetails)

        super.onSaveInstanceState(outState)
    }

}