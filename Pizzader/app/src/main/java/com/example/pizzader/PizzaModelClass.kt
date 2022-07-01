package com.example.pizzader

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.example.pizzader.PizzaFlavours.Companion.descriptionList
import com.example.pizzader.PizzaFlavours.Companion.flavoursList
import com.example.pizzader.PizzaFlavours.Companion.imageList
import com.example.pizzader.PizzaFlavours.Companion.priceSizesList
import java.io.Serializable

class PizzaModelClass(val pizzaFlavour: String?) :Serializable {



    var pizzaDescription:Int? = null
    var pizzaImage: Int? = null
    var smallSizePrice:Double =0.0
    var mediumSizePrice:Double =0.0
    var largeSizePrice:Double =0.0

    var price=0.0

    init {
        val index=flavoursList.indexOf(pizzaFlavour)
        pizzaDescription=descriptionList.get(index)
        pizzaImage=imageList.get(index)
        val sizelist= priceSizesList.get(index)
        smallSizePrice=sizelist.get(0).toDouble()
        mediumSizePrice=sizelist.get(1).toDouble()
        largeSizePrice=sizelist.get(2).toDouble()
        price=smallSizePrice
    }





}