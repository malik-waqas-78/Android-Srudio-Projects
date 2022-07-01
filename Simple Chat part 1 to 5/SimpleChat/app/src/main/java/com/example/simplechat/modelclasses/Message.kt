package com.example.simplechat.modelclasses

class Message(val txt:String="",val url:String="",val sentBy:String,val current:String){




    var viewType:Int= SENDER_TEXT

    companion object{
        const val SENDER_TEXT=0
        const val RECEIVING_TEXT=1
        const val SENDER_PIC=2
        const val RECEVIING_PIC=3
    }

    init {
        if (current==sentBy&&txt.isNotEmpty()&&url.isEmpty()) {
            viewType = SENDER_TEXT
        }else if (current==sentBy&&txt.isEmpty()&&url.isNotEmpty()) {
            viewType = SENDER_PIC
        }else if (current!=sentBy&&txt.isNotEmpty()&&url.isEmpty()) {
            viewType = RECEIVING_TEXT
        }else if (current!=sentBy&&txt.isEmpty()&&url.isNotEmpty()) {
            viewType = RECEVIING_PIC
        }else{
            viewType= SENDER_TEXT
        }
    }
}