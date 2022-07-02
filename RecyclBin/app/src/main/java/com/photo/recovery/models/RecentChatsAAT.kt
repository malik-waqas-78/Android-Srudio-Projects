package com.photo.recovery.models

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.photo.recovery.R
import com.photo.recovery.constants.MyConstantsAAT

class RecentChatsAAT(var context: Context, var senderName: String, var date: String, var chatType:String) {

    companion object{
        var wcCount=0
        var mcCount=0
        var gmcCount=0
    }

    val WhatsAppIcon = R.drawable.ic_whatsapp
    val MessengerIcon = R.drawable.ic_fbmes
    val MailIcon = R.drawable.ic_messages

    val WhatsAppColor = R.color.white
    val MessengerColor = R.color.white
    val MailColor = R.color.white

    var icon: Drawable? = null
    var color:Int?=null
    init {
        icon= when(chatType){
            MyConstantsAAT.TYPE_MESSENGER->{
                mcCount++
                color= ContextCompat.getColor(context,MessengerColor)
                ContextCompat.getDrawable(context,MessengerIcon)
            }
            MyConstantsAAT.TYPE_MAIL->{
                gmcCount++
                color= ContextCompat.getColor(context,MailColor)
                ContextCompat.getDrawable(context,MailIcon)
            }
            MyConstantsAAT.TYPE_WHATSAPP->{
                wcCount++
                color= ContextCompat.getColor(context,WhatsAppColor)
                ContextCompat.getDrawable(context,WhatsAppIcon)
            }
            else->{
                color= ContextCompat.getColor(context,MessengerColor)
                ContextCompat.getDrawable(context,MessengerIcon)
            }
        }
    }
}