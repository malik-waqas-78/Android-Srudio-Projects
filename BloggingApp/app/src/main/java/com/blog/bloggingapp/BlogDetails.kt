package com.blog.bloggingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.blog.bloggingapp.R

class BlogDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen6)

        if(intent!=null){
            var blog:Blog=intent.getSerializableExtra("item") as Blog

            findViewById<ImageView>(R.id.iv2).setImageResource(blog.picId)
            findViewById<TextView>(R.id.tv_place).text=blog.place
            findViewById<TextView>(R.id.tv_sdesc).text=blog.shortDesc
            findViewById<TextView>(R.id.tv_ldesc).text=blog.longDesc
            findViewById<TextView>(R.id.tv_by).text="by ${blog.by}"
            findViewById<RatingBar>(R.id.rb).rating= blog.rating.toFloat()

        }
    }
}