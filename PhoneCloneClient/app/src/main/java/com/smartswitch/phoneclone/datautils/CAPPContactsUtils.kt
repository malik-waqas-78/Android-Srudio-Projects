package com.smartswitch.phoneclone.datautils

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.ContactsContract
import com.smartswitch.phoneclone.interfaces.CAPPUtilsCallBacks
import com.smartswitch.phoneclone.interfaces.CAPPMyInterFace
import com.smartswitch.phoneclone.modelclasses.CAPPContactsModel
import java.lang.RuntimeException

class CAPPContactsUtils(var context: Context?, var HSMyInterFace: CAPPMyInterFace?) {

   companion object{
       var contactsList = ArrayList<CAPPContactsModel>()
   }
    var HSUtilsCallBacks:CAPPUtilsCallBacks?=null
    init {
       if(context!=null){
           HSUtilsCallBacks = context as CAPPUtilsCallBacks
       }
   }


    var totalSize = 0;


    fun getListSize(): Int {
        return contactsList.size
    }


    fun loadData() {
        AsyncTaskToLoadData().execute()
    }

    inner class AsyncTaskToLoadData : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            if(contactsList.isEmpty()){
                getContactList()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            HSUtilsCallBacks?.doneLoadingContacts()
        }
    }


    private fun getContactList() {
        contactsList.clear()
        val cr = context?.contentResolver
        val cur: Cursor? = cr?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        try{
            if (cur!=null&&(cur.count ?: 0) > 0) {
                while (cur.moveToNext()) {
                    val id: String = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    val name: String = cur.getString(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                        )
                    )
                    if (cur.getInt(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                            )
                        ) > 0
                    ) {
                        val pCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        // var count = 0;
                        while (pCur!!.moveToNext()) {
                            //  Log.d("92727", "getContactList: ${pCur.getColumnName(count++)}")
                            val phoneNo: String = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            //Log.i("92727", "Name: $name")
                            //  Log.i("92727", "Phone Number: $phoneNo")
                            contactsList.add(CAPPContactsModel(name, phoneNo))

                        }
                        pCur.close()
                    }

                }
            }
        }catch (e:RuntimeException){
            //do nothing
        }
        cur?.close()
        //handlersActionNotifier.doneLoadingContacts()
    }


}



