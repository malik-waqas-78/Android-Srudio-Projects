package com.blog.bloggingapp

class BlogDbModel(_size:Int=0, var blogsList:ArrayList<Blog>?=null) {
    var size=_size
        get() {
            return if(blogsList!=null)
                blogsList!!.size
            else
                0
        }
    override fun toString(): String {
        var blogs:String=""
        for(blog in blogsList!!){
            blogs+=blog.toString()
        }

        return "$size$blogs"

    }
}