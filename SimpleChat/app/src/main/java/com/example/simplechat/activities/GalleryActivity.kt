package com.example.simplechat.activities

import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.adapters.ImageAdapter
import com.example.simplechat.databinding.ActivityGalleryBinding
import com.example.simplechat.modelclasses.Image

class GalleryActivity : AppCompatActivity() {

    lateinit var binding:ActivityGalleryBinding

    var imageList=ArrayList<Image>()
    lateinit var adapter:ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        adapter=ImageAdapter(this@GalleryActivity,imageList,object:ImageAdapter.ItemClicked{
            override fun itemClick(image: Image) {
                val intent= Intent().apply {
                    putExtra("path",image.path)
                }
                setResult(9278,intent)
                finish()
            }

        })
        binding.rv.layoutManager= LinearLayoutManager(this@GalleryActivity,LinearLayoutManager.VERTICAL,false)

        binding.rv.adapter=adapter

        LoadImages().execute()

    }

    inner class LoadImages():AsyncTask<Void,Void,Void>(){
        var dialog:ProgressDialog?=null
        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog.show(
                this@GalleryActivity, "",
                "Loading Contacts. Please wait...", true
            )
        }
        override fun doInBackground(vararg params: Void?): Void? {
            val cursor: Cursor?
            //var absolutePathOfImage: String? = null
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                "_data",
            )
            cursor = contentResolver.query(
                uri, projection, null,
                null, null
            )
            var columnIndexId = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var clmindex_data = cursor!!.getColumnIndexOrThrow("_data")

            if (cursor != null && cursor.moveToFirst()) {
                do {


                   val contentUri =
                        ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId)).toString()
                    val path = cursor.getString(clmindex_data)
                    val image=Image(path,contentUri)
                    imageList.add(image)
                } while (cursor.moveToNext())

            }
            cursor.close()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            adapter.notifyDataSetChanged()
            dialog?.dismiss()
        }

    }
}