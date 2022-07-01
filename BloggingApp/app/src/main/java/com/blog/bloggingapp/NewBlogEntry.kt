package com.blog.bloggingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

class NewBlogEntry : AppCompatActivity() {

        var blog=Blog()

        lateinit var name:EditText
        lateinit var place:EditText
        lateinit var shortDesc:EditText
        lateinit var longDesc:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_blog_entry)

        name=findViewById(R.id.et_pic_name)
        place=findViewById(R.id.et_place)
        shortDesc=findViewById(R.id.et_short_desc)
        longDesc=findViewById(R.id.et_long_desc)

        findViewById<Button>(R.id.btn_add_new_blog).setOnClickListener {
            if(name.text.isEmpty()||place.text.isEmpty()||shortDesc.text.isEmpty()||
                    longDesc.text.isEmpty()){
                showToast("No field should be empty.")
            }else{
                blog.blogId=BlogEntries.blogDbModel.blogsList!!.size
                blog.place=place.text.toString()
                blog.shortDesc=shortDesc.text.toString()
                blog.longDesc=longDesc.text.toString()
                blog.by=MainActivity.user!!.name
                blog.rating=0
                BlogEntries.blogDbModel.blogsList?.add(blog)
                addNewBlog()
            }
        }

        findViewById<Button>(R.id.btn_choose).setOnClickListener {
            startActivityForResult(Intent(this@NewBlogEntry,ChoosePic::class.java),927)
        }

    }

    private fun addNewBlog() {
        val f = File(this@NewBlogEntry.filesDir, "text")
        if(!f.exists()){
            f.mkdir()
        }
        val file=File(f,"blog-db.txt")
        try {
            var streamWriter: OutputStreamWriter = OutputStreamWriter(file.outputStream())
            streamWriter.write(BlogEntries.blogDbModel.toString());
            streamWriter.close();

            showToast("Successfully added")

            finish()

        }
        catch ( e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }

    private fun showToast(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==927&&data!=null){
            var id=data.getIntExtra("int",-1)
            var n=data.getStringExtra("name")
            name.setText(n)
            blog.picId=id

        }
    }
}