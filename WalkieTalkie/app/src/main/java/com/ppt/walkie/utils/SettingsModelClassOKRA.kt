package com.ppt.walkie.utils

import android.graphics.drawable.Drawable
import android.view.View

class SettingsModelClassOKRA(var iconDrawable:Drawable?, var title:String, var canReceiveCallInBackground:Boolean, var description:String="") {

    var descriptionVisibility= View.GONE
    var switchVisibility=View.INVISIBLE



    init {

        if(description.isNotEmpty()){
            descriptionVisibility=View.VISIBLE
            switchVisibility=View.VISIBLE
        }

    }
}