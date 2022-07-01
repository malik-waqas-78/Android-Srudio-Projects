package com.blog.bloggingapp

class UserDbModel(_size:Int=0,var usersList:ArrayList<User>?=null) {
    var size=_size
    get() {
       return if(usersList!=null)
            usersList!!.size
        else
            0
    }
    override fun toString(): String {
        var users:String=""
        for(user in usersList!!){
            users+=user.toString()
        }
        return "$size"+users
    }

}