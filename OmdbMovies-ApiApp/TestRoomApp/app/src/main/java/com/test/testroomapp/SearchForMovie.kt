package com.test.testroomapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.test.testroomapp.databinding.ActivitySearchForMovieBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

public class SearchForMovie : AppCompatActivity() {

    lateinit var binding:ActivitySearchForMovieBinding

    val apiKey="33553b0f";


    var movieDetails:MovieModel?=null

    companion object {
        enum class Keys(val value: String) {
            TITLE("Title"),
            YEAR("Year"),
            RATED("Rated"),
            RELEASED("Released"),
            RUNTIME("Runtime"),
            GENRE("Genre"),
            DIRECTOR("Director"),
            WRITER("Writer"),
            ACTORS("Actors"),
            PLOT("Plot")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchForMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Search Movies"
        binding.tvDetails.movementMethod = ScrollingMovementMethod()
        if(savedInstanceState!=null){
            binding.tvDetails.text=savedInstanceState.getString("tv")
            binding.etMovieTitle.setText(savedInstanceState.getString("et"))
            if(savedInstanceState.getBoolean("tv_state")){
                binding.tvDetails.visibility=View.VISIBLE
                binding.pBar.visibility=View.GONE
            }else{
                binding.tvDetails.visibility=View.GONE
                binding.pBar.visibility=View.VISIBLE
            }
            movieDetails=savedInstanceState.getParcelable("obj")
        }

        binding.btnRetrieve.setOnClickListener {
            binding.pBar.visibility= View.VISIBLE
            binding.tvDetails.visibility=View.GONE
            var title=binding.etMovieTitle.text.toString().trim()
            if(title.isNotEmpty()){
                CoroutineScope(IO).launch {
                    searchMovie(title)
                }

            }

        }
        binding.btnSave.setOnClickListener {
            if(movieDetails!=null){

                movieDetails?.insertMovie()
                Toast.makeText(this,"Total movies in DB : "+DataBaseManager.getMD()?.getAll()?.size,Toast.LENGTH_SHORT).show()
            }
        }

    }

    private suspend fun searchMovie(title: String) {

        val url = URL("https://www.omdbapi.com/?t=$title&apikey=$apiKey")

        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            inputStream.bufferedReader().use {
                var result=""
                it.lines().forEach { line ->
                   result=result+line
                }
                var obj=JSONObject(result)
                movieDetails=getMovieDetails(obj)
                withContext(Main){
                    var str:String?=movieDetails?.getPrintableString()
                    binding.tvDetails.text=str
                    binding.pBar.visibility= View.GONE
                    binding.tvDetails.visibility=View.VISIBLE
                }
            }
        }



    }

    private fun getMovieDetails(obj: JSONObject): MovieModel {
        val movieDetails=MovieModel()
        movieDetails.title= obj.get(Keys.TITLE.value) as String
        movieDetails.year= obj.get(Keys.YEAR.value) as String
        movieDetails.rated= obj.get(Keys.RATED.value) as String
        movieDetails.plots= obj.get(Keys.PLOT.value) as String
        movieDetails.released= obj.get(Keys.RELEASED.value) as String
        movieDetails.runtime= obj.get(Keys.RUNTIME.value) as String

        movieDetails.actorsList= ArrayList(getArrayList(obj.get(Keys.ACTORS.value).toString()))
        movieDetails.directorList= ArrayList(getArrayList(obj.get(Keys.DIRECTOR.value).toString()))
        movieDetails.genreList= ArrayList(getArrayList(obj.get(Keys.GENRE.value).toString()))
        movieDetails.writerList= ArrayList(getArrayList(obj.get(Keys.WRITER.value).toString()))
        return movieDetails

    }

    private fun  getArrayList(tempStr:String): ArrayList<String>? {
        var tempArr=tempStr.split(",")
        var strArray=ArrayList<String>()
        tempArr.forEach {
            strArray.add(it.trim())
        }
        return strArray
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("et",binding.etMovieTitle.text.toString())
        outState.putString("tv",binding.tvDetails.text.toString())
        outState.putBoolean("tv_state",binding.tvDetails.isVisible)
        outState.putParcelable("obj",movieDetails)


        super.onSaveInstanceState(outState)
    }

}