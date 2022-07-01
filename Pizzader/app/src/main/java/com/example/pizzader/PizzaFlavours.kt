package com.example.pizzader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzader.databinding.ActivityPizzaFlavoursBinding

class PizzaFlavours : AppCompatActivity() {

    lateinit var binding: ActivityPizzaFlavoursBinding

    companion object{
        val flavoursList=listOf("Cheese Pizza","Veggie Pizza","Pepperoni Pizza",
            "Meat Pizza",  "Margherita Pizza","BBQ Chicken Pizza",
            "Hawaiian Pizza","Buffalo Pizza","Supreme Pizza","The Works Pizza")
        val descriptionList=listOf(R.string.cheeze_pizza_description,R.string.veggie_pizza_description,R.string.pepperoni_pizza_description,
            R.string.meat_pizza_description,R.string.margherita_pizza_description,R.string.bbq_pizza_description,R.string.hawaiian_pizza_description,
            R.string.buffalo_pizza_description,R.string.supreme_pizza_description,R.string.works_pizza_description)

        val imageList=listOf(R.drawable.cheese_pizza,R.drawable.veggie_pizza,R.drawable.pipperoni_pizza,R.drawable.meat_pizza
            ,R.drawable.margarita_pizza,R.drawable.bbq_chicken_pizza,R.drawable.hawaiian_pizza,R.drawable.buffalo_pizza,
            R.drawable.the_supreme_pizza,R.drawable.the_works_pizza)

        val priceSizesList=listOf(listOf(5,7,10),listOf(7,10,15),listOf(12,15,20),listOf(15,25,30),listOf(20,30,35),
            listOf(30,35,40),listOf(35,45,50),listOf(40,50,45),listOf(50,55,60),listOf(55,60,70))
    }


    val pizzaList=ArrayList<PizzaModelClass>()
//    val pizzaNames=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPizzaFlavoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWelcomNote.text="Welcome ${intent.getStringExtra("name")}"

        populatePizzaList()

        val adapter=CustomList(this@PizzaFlavours,pizzaList)
        binding.rvPizzaFlavours.layoutManager= LinearLayoutManager(this@PizzaFlavours,LinearLayoutManager.VERTICAL,false)
        binding.rvPizzaFlavours.adapter=adapter

    }

    private fun populatePizzaList() {
        for(i in flavoursList){
            pizzaList.add(PizzaModelClass(i) )
        //pizzaNames.add(i)
        }
    }
}