package com.example.simplechat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.simplechat.R
import com.example.simplechat.databinding.ActivityMainBinding
import com.example.simplechat.modelclasses.ContactsModelClass
import com.example.simplechat.utills.MyPermissions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import android.app.ProgressDialog





class ContactsActivity : AppCompatActivity(),
    AdapterView.OnItemClickListener {


    @SuppressLint("InlinedApi")
    private val FROM_COLUMNS: Array<String> = arrayOf(
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        } else {
            ContactsContract.Contacts.DISPLAY_NAME
        }
    )

    @SuppressLint("InlinedApi")
    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        else
            ContactsContract.Contacts.DISPLAY_NAME
    )

    // The column index for the _ID column
    private val CONTACT_ID_INDEX: Int = 0

    // The column index for the CONTACT_KEY column
    private val CONTACT_KEY_INDEX: Int = 1

    private val CONTACT_DISPLAY_INDEX = 2


    lateinit var binding: ActivityMainBinding

    var contactsList: ArrayList<ContactsModelClass> = ArrayList()
    private var cursorAdapter: SimpleCursorAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       if(MyPermissions.havePermissions(this@ContactsActivity)){
           loadContactsList();
       }


        binding.btnLogout.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    finish()
                }

        }

        // Gets a CursorAdapter
        cursorAdapter = SimpleCursorAdapter(
            this@ContactsActivity,
            R.layout.contacts_list,
            null,
            FROM_COLUMNS, intArrayOf(R.id.tv_contact_name),
            0
        )
        // Sets the adapter for the ListView
        binding.listView.adapter = cursorAdapter
        binding.listView.onItemClickListener = this
    }

    private fun loadContactsList() {

        val dialog = ProgressDialog.show(
            this@ContactsActivity, "",
            "Loading Contacts. Please wait...", true
        )

        LoaderManager.getInstance(this).initLoader(
            0,
            null,
            object : LoaderCallbacks<Cursor> {
                override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                    return CursorLoader(
                        this@ContactsActivity,
                        ContactsContract.Contacts.CONTENT_URI,
                        PROJECTION,
                        null,
                        null,
                        null
                    )
                    //  return CursorLoader(this@MainActivity,ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
                }


                override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                    while (data?.moveToNext() == true) {
                        val name = data?.getString(CONTACT_DISPLAY_INDEX)
                        val contactId: String =
                            data.getString(CONTACT_ID_INDEX)
                        //
                        //  Get all phone numbers.
                        //
                        //
                        //  Get all phone numbers.
                        //
                        val phones: Cursor? = contentResolver.query(
                            Phone.CONTENT_URI, null,
                            Phone.CONTACT_ID + " = " + contactId, null, null
                        )
                        val number_index = phones?.getColumnIndex(Phone.NUMBER) ?: 0
                        while (phones?.moveToNext() == true) {
                            val number = phones.getString(number_index)
                            contactsList.add(ContactsModelClass(name, number))
                        }
                        phones?.close()
                    }
                    cursorAdapter?.swapCursor(data)
                    dialog?.dismiss()
                }

                override fun onLoaderReset(loader: Loader<Cursor>) {
                    cursorAdapter?.swapCursor(null)
                }

            }
        )

    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        startActivity(Intent(this@ContactsActivity,ChatActivity::class.java).apply{
            putExtra("details",contactsList[position])
            //putExtra("details",ContactsModelClass("Malik Waqas","786907"))

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(permissions.isNotEmpty()&&grantResults.isNotEmpty()){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                loadContactsList()
            }
        }
    }


}