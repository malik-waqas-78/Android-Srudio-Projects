package com.example.simplechat.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.adapters.ChatAdapter
import com.example.simplechat.databinding.ActivityChatBinding
import com.example.simplechat.modelclasses.ContactsModelClass
import com.example.simplechat.modelclasses.Message
import com.example.simplechat.utills.MyPermissions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    var chatID:String?=null

    lateinit var binding:ActivityChatBinding

    val msgList=ArrayList<Message>()

    var sTime:Long=-1L

    var adapter:ChatAdapter?=null
    lateinit var contactDetails:ContactsModelClass

    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db=Firebase.firestore
        contactDetails=intent.getSerializableExtra("details") as ContactsModelClass
        binding.tvChatName.text = contactDetails.name


        loadOldMessages()

         adapter= ChatAdapter(this@ChatActivity,msgList)
        binding.rvChats.layoutManager=LinearLayoutManager(this@ChatActivity,LinearLayoutManager.VERTICAL,true)
        binding.rvChats.adapter = adapter

        binding.ibSend.setOnClickListener {
            sTime=System.currentTimeMillis()
            addMessage(binding.etMsg.text.toString())
            binding.etMsg.setText("")

        }

        binding.ibPic.setOnClickListener {
            if(MyPermissions.haveStoragePermission(this@ChatActivity)){
                loadPictures()
            }
        }

    }

    private fun logChatActivity() {
        chatID=System.currentTimeMillis().toString()

        db.collection("users").document(LoginActivity.ID).set(hashMapOf(contactDetails.contactNo to chatID),
            SetOptions.merge()).
                addOnSuccessListener {
                    db.collection("users").document(contactDetails.contactNo).set(hashMapOf(LoginActivity.ID to chatID))
                        .addOnSuccessListener {
                            loadOldMessages()
                        }
                }

    }

    private fun addMessage(msg: String) {
        val msg=Message(txt = msg, sentBy = LoginActivity.ID, current = LoginActivity.ID)
        syncWithDatabase(msg)
    }

    private fun syncWithDatabase(msg: Message) {
        chatID?.let {
            db.collection(it).document(System.currentTimeMillis().toString()).set(
                hashMapOf(
                    "msg" to msg.txt,
                    "sender" to msg.sentBy
                )
            ).addOnFailureListener {
                Toast.makeText(this@ChatActivity,"Failed to send",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadOldMessages() {
        if(LoginActivity.ID.isEmpty()){
            finish()
        }
        db.collection("users").document(LoginActivity.ID).get().addOnCompleteListener { it: Task<DocumentSnapshot> ->
            if(it.result?.exists()==true){
                val doc=it.result
                chatID=doc?.get(contactDetails.contactNo).toString()
                Log.d("92727", "loadOldMessages: $chatID")
                if(chatID!="null"&&chatID!=null&&!chatID.isNullOrEmpty()){
                    Log.d("92727", "not null: $chatID")

                    db.collection(chatID!!).addSnapshotListener{ documents: QuerySnapshot?, e: FirebaseFirestoreException? ->
                        msgList.clear()
                        adapter?.notifyDataSetChanged()
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if (documents != null) {
                            for(doc in documents.documents){
                                msgList.add(0,Message(txt = doc.getString("msg")?:"?",sentBy=
                                doc.getString("sender")?:"",current=LoginActivity.ID))
                            }
                            val eTime=System.currentTimeMillis()
                            if(sTime!=-1L){
                                val timeDiff=eTime-sTime
                                Toast.makeText(this@ChatActivity,
                                    "${
                                        if(msgList[0].txt.length>5){
                                        
                                        msgList[0].txt.substring(0,5)
                                    }else{
                                        msgList[0].txt
                                    }
                                    }  $timeDiff mSec",Toast.LENGTH_SHORT)
                                    .show()
                            }
                            adapter?.notifyDataSetChanged()

                        }
                    }
                }else{
                    Log.d("92727", "caht is nulll or empty: $chatID")
                    logChatActivity()
                }
            }else{
                Log.d("92727", "no successful: $chatID")
                logChatActivity()
            }
        }



    }

    private fun loadPictures(){
        val intent = Intent(this@ChatActivity,GalleryActivity::class.java)
        startActivityForResult(intent,9277)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(permissions.isNotEmpty()&&grantResults.isNotEmpty()){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                loadPictures()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==9278){
            val path=data?.getStringExtra("path")
            if(!path.isNullOrEmpty()){
                sendPicture(path)
                adapter?.notifyItemInserted(0)
            }
        }
    }

    private fun sendPicture(path: String?) {
        if(path!=null){
            msgList.add(0,Message(url=path, sentBy = "1",current="1"))
        }
    }
}
