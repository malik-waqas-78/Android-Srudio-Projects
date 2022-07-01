package com.example.sqliteapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.sqliteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    private val faculties = arrayOf("Engineering","Bussiness", "Arts","Information Technology")
    private var myDBAdapter : MyDBAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, faculties)
        binding.spFaculty.adapter = arrayAdapter
        initializeDatabase()
        loadList()
        binding.btnAdd.setOnClickListener {
            myDBAdapter?.insertStudent(binding.etName.text.toString(), faculties[binding.spFaculty.selectedItemPosition].toString())
            loadList()
        }
        binding.btnDelEng.setOnClickListener {
            myDBAdapter?.deleteAllEngineers()
            Toast.makeText(this, "All Engineers deleted successfully. ", Toast.LENGTH_LONG).show()
            loadList()
        }
        binding.btnDelAll.setOnClickListener {
            myDBAdapter?.deleteStudents()
            Toast.makeText(this, "All records deleted successfully. ", Toast.LENGTH_LONG).show()
            loadList()
        }
    }

    private fun initializeDatabase() {
        myDBAdapter = MyDBAdapter(this)
        myDBAdapter!!.open()
    }

    private fun loadList(){
        val allStudents : ArrayList<String> = myDBAdapter?.selectAllStudents() as ArrayList<String>
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allStudents)
        binding.listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}