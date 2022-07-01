package com.test.testroomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.test.testroomapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    lateinit var binding:ActivityMainBinding
    var moviesList=ArrayList<MovieModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        DataBaseManager.initMovieDB(applicationContext)


        binding.btnAddMoviesToDb.setOnClickListener {
            initMoviesList()
        }

        binding.btnSearchMovie.setOnClickListener {
            startActivity(Intent(this,SearchForMovie::class.java))
        }

        binding.btnSearchActor.setOnClickListener {
            startActivity(Intent(this,SearchForActor::class.java))
        }

        binding.btnSearchByString.setOnClickListener {
            startActivity(Intent(this,SearchMovieByString::class.java))
        }

    }

    private fun initMoviesList() {
        moviesList.add(
            MovieModel(
                "The Shawshank Redemption","1994","R",
                "14 oct 1994","142 min", arrayListOf("Drama"),
                arrayListOf("Frank Darabont"), arrayListOf("Stephen King","Frank Darabont"),
                arrayListOf("Tim Robbins", "Morgan Freeman", "Bob Gunton"),"Two imprisoned men bond over a number of years, finding solace " +
                        "and eventual redemption through acts of common decency."
            ))
        moviesList.add(MovieModel("Batman: The Dark Knight Returns, Part 1","2012","PG-13",
            "25 Sep 2012","76 min", arrayListOf("Animation", "Action", "Crime", "Drama", "Thriller"),
            arrayListOf("Jay oliva"), arrayListOf("Bob Kane (character created by: Batman)", "Frank Miller","(comic book)", "Klaus Janson (comic book)", "Bob Goodman"),
            arrayListOf("Peter Weller", "Ariel Winter", "David Selby", "Wade Williams"),
            "Batman has not been seen for ten years. A new breed of criminal" +
                    " ravages Gotham City, forcing 55-year-old Bruce Wayne back into" +
                    " the cape and cowl. But, does he still have what it takes to " +
                    "fight crime in a new era?"))
        moviesList.add(MovieModel("The Lord of the Rings: The Return of the King",
            "2003","PG-13","17 Dec 2003","201 min",
            arrayListOf("Action", "Adventure", "Drama"),
            arrayListOf("Peter Jackson"), arrayListOf("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"),
            arrayListOf("Elijah Wood", "Viggo Mortensen", "Ian McKellen"),"Gandalf and " +
                    "Aragorn lead the World of Men against Sauron's army to draw his" +
                    " gaze from Frodo and Sam as they approach Mount Doom with the One" +
                    " Ring."))
        moviesList.add(MovieModel("Inception","2010","PG-13","16 Jul 2010",
            "148 min", arrayListOf("Action", "Adventure", "Sci-Fi"), arrayListOf("Christopher Nolan"),
            arrayListOf("Christopher Nolan"), arrayListOf("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
            "A thief who steals corporate secrets through the use of dream-sharing" +
                    " technology is given the inverse task of planting an idea into the" +
                    " mind of a C.E.O., but his tragic past may doom the project and his" +
                    " team to disaster."))
        moviesList.add(MovieModel("The Matrix","1999","R","31 Mar 1999",
            "136 min", arrayListOf("Action", "Sci-Fi"), arrayListOf("Lana Wachowski", "Lilly Wachowski"),
            arrayListOf("Lilly Wachowski", "Lana Wachowski"), arrayListOf("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
            "When a beautiful stranger leads computer hacker Neo to a forbidding" +
                    " underworld, he discovers the shocking truth--the life he knows" +
                    " is the elaborate deception of an evil cyber-intelligence."))

        for(movie in moviesList){
            movie.insertMovie()
        }
        showMsg("Total movies in DB : "+DataBaseManager.getMD()?.getAll()?.size.toString())
    }

    private fun showMsg(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}