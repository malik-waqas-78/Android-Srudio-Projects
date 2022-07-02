package com.smartswitch.phoneclone.activities

import android.accounts.AccountManager
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentProviderOperation
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.adapters.CAPPAdapterContacts
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.modelclasses.CAPPContactsModel
import java.io.*
import java.lang.Exception

class CAPPActivityStoreContact : AppCompatActivity()  {


    var contactsList=ArrayList<CAPPContactsModel>()

    var rc_contacts:RecyclerView?=null

    var HSAdapter_Contacts:CAPPAdapterContacts?=null

    companion object {
        var cbSelectAll: CheckBox? = null
    }
    
    
    var btn_saveContacts: Button?=null
    override fun onCreate(
        savedInstanceState: Bundle?
    ){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_contacts_capp)

        findViewById<ImageView>(R.id.imagBack).setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

       /* admobBanner()*/

//        AATIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )


        onViewCreated()
    }

    var isContactsSaved=false

    fun onViewCreated() {
        //load data
        loadContactsList()
        //
    }


    inner class asyncTask : AsyncTask<Unit,Unit,Unit>(){
        var progressDialog:ProgressDialog?=null
        override fun onPreExecute() {
            super.onPreExecute()
           progressDialog = ProgressDialog(this@CAPPActivityStoreContact)
            progressDialog?.setTitle("Saving Contacts")
            progressDialog?.setMessage("Saving contacts to your directory, please wait")
            progressDialog?.show()
        }
        override fun doInBackground(vararg params: Unit?) {

            for(contact in contactsList){
                if(contact.isSelected){
                    addContact(contact.name,contact.phoneNumber)
                }
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            //return

            if(this@CAPPActivityStoreContact.isDestroyed||this@CAPPActivityStoreContact.isFinishing){
                return
            }
            isContactsSaved=true
            progressDialog?.cancel()
            contactsHasBeenSaved()

            //        Toast.makeText(context!!,"Contacts has been stored",Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContactsList() {
        LoadContactsFile().execute()
    }
    inner class LoadContactsFile() : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ConstraintLayout>(R.id.cl_progress).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.cl_noContacts).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.cl_contacstlist).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            var fileReader: BufferedReader? = null
            try {
                val root = Environment.getExternalStorageDirectory().absolutePath + "/${
                    applicationContext.resources?.getString(R.string.app_name)
                }/${CAPPMConstants.ContactsFileName}"

                fileReader = BufferedReader(FileReader(root))

                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                var line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        var contact = CAPPContactsModel(
                            tokens[CAPPContactsModel.NAME_INDEX],
                            tokens[CAPPContactsModel.PHONE_NUMBER_INDEX]
                        )
                        contact.isSelected = true
                        contactsList.add(contact)
                    }

                    line = fileReader.readLine()
                }
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (contactsList.size > 0) {
                findViewById<ConstraintLayout>(R.id.cl_progress).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.cl_noContacts).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.cl_contacstlist).visibility = View.VISIBLE

                cbSelectAll = findViewById(R.id.cb_selectallcontacts)
                cbSelectAll?.isChecked = true
                btn_saveContacts = findViewById(R.id.btn_yes_save)
                //manage RecycleView
                rc_contacts = findViewById(R.id.rc_apps)
                var linearLayoutManager = LinearLayoutManager(this@CAPPActivityStoreContact)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                rc_contacts?.layoutManager = linearLayoutManager
                //create HSAdapter
                HSAdapter_Contacts = CAPPAdapterContacts(this@CAPPActivityStoreContact, contactsList)
                //connect HSAdapter to recycleView
                rc_contacts?.adapter = HSAdapter_Contacts

                cbSelectAll?.setOnClickListener {
                    for (contact in contactsList) {
                        contact.isSelected = cbSelectAll!!.isChecked
                    }
                    HSAdapter_Contacts?.notifyDataSetChanged()
                }

                btn_saveContacts?.setOnClickListener {
                    contactsList = HSAdapter_Contacts?.HSContactsList!!
                    asyncTask().execute()
                }

            } else {
                findViewById<ConstraintLayout>(R.id.cl_progress).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.cl_noContacts).visibility = View.VISIBLE
                findViewById<ConstraintLayout>(R.id.cl_contacstlist).visibility = View.GONE
                //no contacts available
                findViewById<TextView>(R.id.tv_nocontacts).text = "No Contacts Received"
            }
        }


    }
    private fun addContact(name: String, mobile: String): Boolean {
        val contact = ArrayList<ContentProviderOperation>()
        contact.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, AccountManager.KEY_ACCOUNT_TYPE)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, AccountManager.KEY_ACCOUNT_NAME)
                .build()
        )

        // first and last names
        contact.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.RawContacts.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                    name
                )//enterName
                .build()
        )

        // Contact No Mobile
        contact.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)//enterNo
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )
        try {
            val results = this.contentResolver?.applyBatch(ContactsContract.AUTHORITY, contact)
            if (results!!.isNotEmpty()) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }
    fun contactsHasBeenSaved() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_transfer_completedon_receiving_end_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_error_ok)
//        positive.setText("I'm sure")


        val tv_title=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_title.setText("Contacts Have been stored Successfully.")
//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
        positive.setOnClickListener {
            dialog.dismiss()
            val intent=Intent()
            intent.putExtra(CAPPMConstants.isContactsSaved,isContactsSaved)
            setResult(CAPPMConstants.STORE_CONTACTS,intent)
            finish()
        }

        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        //set result telling whether contacs are saved or not
        backPressed()

       /* val intent=Intent()
        intent.putExtra(Constants.isContactsSaved,isContactsSaved)
        setResult(Constants.STORE_CONTACTS,intent)
        finish()*/
    }

    fun next_apps(view: View) {
       backPressed()
    }


    fun backPressed() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.db_contacts_back_prerssed_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_yes_save)
        val negative: Button = dialog.findViewById(R.id.btn_no)
//        positive.setText("I'm sure")

//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
        positive.setOnClickListener {
            dialog.dismiss()
        }
        negative.setOnClickListener{
            val intent=Intent()
            intent.putExtra(CAPPMConstants.isContactsSaved,isContactsSaved)
            setResult(CAPPMConstants.STORE_CONTACTS,intent)
            finish()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }

   /* fun loadFbBannerAdd() {

        val adView = AdView(
            this@StoreContacts,
            this@StoreContacts.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                if (com.facebook.ads.BuildConfig.DEBUG) {
                    *//* Toast.makeText(
                        this@HSSplashScreen,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()*//*
                }
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(this)?.build())
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
            }
        }


        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
        findViewById<RelativeLayout>(R.id.top_banner).addView(adView)
    }*/
/*
    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSActivityStoreContact)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSActivityStoreContact)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.top_banner) as RelativeLayout
        adViewLayout.addView(mAdView)

        mAdView.loadAd(adRequest)

        mAdView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                adViewLayout.visibility = View.INVISIBLE
            }


            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(HSMyConstants.TAG, "onAdOpened: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adViewLayout.visibility = View.VISIBLE
                Log.d(HSMyConstants.TAG, "onAdLoaded: ")
            }
        }

    }*/
}