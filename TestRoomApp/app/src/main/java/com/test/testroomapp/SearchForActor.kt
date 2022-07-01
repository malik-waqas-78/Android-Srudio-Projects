package com.test.testroomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.core.view.isVisible
import com.test.testroomapp.databinding.ActivitySearchForActorBinding

class SearchForActor : AppCompatActivity() {
    lateinit var binding:ActivitySearchForActorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchForActorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Search By Actor's Name"
        binding.tvMovies.movementMethod = ScrollingMovementMethod()

        if(savedInstanceState!=null){
            binding.tvMovies.text=savedInstanceState.getString("tv")
            binding.etActorName.setText(savedInstanceState.getString("et"))
        }

        binding.btnSearch.setOnClickListener {
            binding.tvMovies.text = ""
            var actorName=binding.etActorName.text.toString().trim()
            if(actorName.isNotEmpty()){
                var list=DataBaseManager.getMAD()?.findMovieTitleByActor("%$actorName%")
                var str=""
                if (list != null&& list.isNotEmpty()) {
                    for(movieActor in list){
                        str="$str${movieActor.actorName} : ${movieActor.movieTitle}\n"
                    }
                    binding.tvMovies.text=str
                }else{
                    binding.tvMovies.text="No Movie Found"
                }
            }else{
                binding.tvMovies.text="No Movie Found"
            }
        }



    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("et",binding.etActorName.text.toString())
        outState.putString("tv",binding.tvMovies.text.toString())


        super.onSaveInstanceState(outState)
    }
}