package com.blog.bloggingapp

data class User(var name:String,var password:String){
    override fun toString(): String {
        return "\n$name\n$password"
    }
}