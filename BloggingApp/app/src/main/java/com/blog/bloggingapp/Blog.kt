package com.blog.bloggingapp

import java.io.Serializable

class Blog(var blogId:Int=0,var picId:Int=-1,var place:String="",var by:String="",var rating:Int=0,
           var shortDesc:String="",var longDesc:String="") :Serializable{
    override fun toString(): String {
        return "\n$blogId\n$place\n$picId\n$shortDesc\n$by\n$rating\n$longDesc"
    }
}