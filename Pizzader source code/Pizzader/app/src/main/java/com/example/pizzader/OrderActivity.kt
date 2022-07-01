package com.example.pizzader

import android.R
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pizzader.databinding.ActivityOrderBinding
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri


class OrderActivity : AppCompatActivity() {

    lateinit var binding:ActivityOrderBinding

    companion object{
        val cartPizzas=ArrayList<PizzaModelClass>()
        val orderPizzas=ArrayList<PizzaModelClass>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pizza=intent.getSerializableExtra("obj") as PizzaModelClass

        with(binding) {
            tvName.text=pizza.pizzaFlavour
            tvPrice.text="Total price of Carted Items\n"+getTotalCartPrice().toString()+" AED"
            tvDescription.text= pizza.pizzaDescription?.let { getString(it) }

            clBg.background= pizza.pizzaImage?.let { getDrawable(it) }

            rbSmall.text="Small (${pizza.smallSizePrice} AED)"
            rbMedium.text="Medium (${pizza.mediumSizePrice} AED)"
            rbLarge.text="Large (${pizza.largeSizePrice} AED)"

            if(cartPizzas.isEmpty()){
                btnOrder.isEnabled = false
                //Toast.makeText(this@OrderActivity,"Item Added to Cart",Toast.LENGTH_SHORT).show()
            }

            rbSmall.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    pizza.price=pizza.smallSizePrice
                }
            }

            rbMedium.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    pizza.price=pizza.mediumSizePrice
                }

            }

            rbLarge.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    pizza.price=pizza.largeSizePrice
                }

            }

            btnAddToCart.setOnClickListener {
                cartPizzas.add(pizza)
                tvPrice.text ="Total price of Carted Items\n"+getTotalCartPrice().toString()+" AED"
                btnAddToCart.isEnabled = false
                btnOrder.isEnabled=true
                Toast.makeText(this@OrderActivity,"Item Added to Cart",Toast.LENGTH_SHORT).show()
            }

            btnOrder.setOnClickListener {

                orderPizzas.addAll(cartPizzas)
                cartPizzas.clear()
                tvPrice.text="Total price of Carted Items\n"+getTotalCartPrice().toString()+" AED"

                AlertDialog.Builder(this@OrderActivity)
                    .setTitle("Order Placed")
                    .setMessage("You have placed an order of ${getTotalOrderPrice().toString()+" AED"}.") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        "Ok",
                        DialogInterface.OnClickListener { dialog, which ->
                           orderPizzas.clear()
                            finish()
                        })
                    .show()
            }

            ivCallUs.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+971508998898"))
                startActivity(intent)
            }

        }


    }

    private fun getTotalCartPrice(): Double {
        var totalPrice =0.0
        for(i in cartPizzas){
            totalPrice+=i.price
        }
        return totalPrice
    }
    private fun getTotalOrderPrice(): Double {
        var totalPrice =0.0
        for(i in orderPizzas){
            totalPrice+=i.price
        }
        return totalPrice
    }
}