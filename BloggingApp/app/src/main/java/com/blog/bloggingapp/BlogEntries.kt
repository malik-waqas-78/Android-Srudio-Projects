package com.blog.bloggingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blog.bloggingapp.R
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.NumberFormatException

class BlogEntries : AppCompatActivity() {

    companion object{
        var blogDbModel:BlogDbModel= BlogDbModel()
    }
    var isGuest=false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_entries)
        if(intent!=null){
            isGuest=intent.getBooleanExtra("isGuest",false)
            if(isGuest){
                findViewById<Button>(R.id.btn_add_new).isEnabled=false
                findViewById<Button>(R.id.btn_logout).isEnabled=false
            }
        }
        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            startActivity(Intent(this@BlogEntries,NewBlogEntry::class.java))
        }
        findViewById<Button>(R.id.btn_logout).setOnClickListener {
           finish()
        }


    }

    override fun onResume() {
        super.onResume()
        readFile()

        var rv=findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv.adapter=AdapterBlogEntries(this@BlogEntries, blogDbModel.blogsList!!)


    }

    private fun readFile(){


        var isFirst=true;
        var isSecond=false

        blogDbModel.blogsList= ArrayList()
        var reader: BufferedReader? = null;

        try {
            val f = File(this@BlogEntries.filesDir, "text")
            if(!f.exists()){
                f.mkdir()
            }
            val file=File(f,"blog-db.txt")
            reader = BufferedReader(
                InputStreamReader(file.inputStream(), "UTF-8")
            );

            // do reading, usually loop until end of file reading
            var mLine="";
            var index=0;
            while (reader!=null&&reader.readLine()?.apply { mLine=this }!=null) {
                if(isFirst){
                    blogDbModel.size=mLine.toInt()
                    isFirst=false
                    isSecond=true
                }else{
                    var blog=Blog()
                    if(isSecond){
                        index=mLine.toInt()
                        blog.blogId=index
                        isSecond=false
                        blog.place=reader.readLine()
                    }else{
                        blog.blogId=index
                        blog.place=mLine
                    }
                    blog.picId=reader.readLine().toInt()
                    blog.shortDesc=reader.readLine()
                    blog.by=reader.readLine()
                    blog.rating=reader.readLine().toInt()
                    var str=""
                    var line=""
                    while (true){
                        var line=reader.readLine()

                        if(line=="${index+1}"){
                            index=line.toInt()
                            break
                        }else if(line==null){
                            break
                        }else {
                            str+=line
                        }
                    }
                    blog.longDesc=str
                    blogDbModel.blogsList?.add(blog)
                }
            }
        } catch ( e : IOException) {

        }catch (e:NumberFormatException){

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch ( e : IOException) {
                    //log the exception
                }
            }
        }

    }
}