package com.phone.clone.utills

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.phone.clone.R
import com.phone.clone.activity.HSActivityChooseDevice
import com.phone.clone.interfaces.HSDialogueClickListener


class HSMyDialogues(var activity: Activity) {
    var dialogueClickListner = activity as HSDialogueClickListener
    var decodingDialogue: Dialog? = null

    /* fun turnHotSpotOFF() {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.normal_dialogue_view)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_yes)
//        positive.setText("I'm sure")
        val negative: Button = dialog.findViewById(R.id.btn_no)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
        positive.setOnClickListener {
            dialog.dismiss()
            dialogueClickListner.positiveHotSpotTurnOFF()
        }
        negative.setOnClickListener {
            dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }*/
    fun decodingData() {
        decodingDialogue = Dialog(activity).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.db_decoding_data)
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        decodingDialogue?.run {
            show()
        }
    }

    fun stopDecodingDialogue() {
        decodingDialogue?.dismiss()
    }

    /* fun getNetworkPassword(SSID: String) {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.db_getpassword)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv_SSID = dialog.findViewById<TextView>(R.id.tv_db_ssid)
        tv_SSID.text = SSID

        val et_password: EditText = dialog.findViewById(R.id.et_password)

        val positive: TextView = dialog.findViewById(R.id.btn_db_connect)
//        positive.setText("I'm sure")
        val negative: TextView = dialog.findViewById(R.id.btn_cancel)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
        positive.setOnClickListener {
            if (et_password.text.isEmpty()) {
                et_password.error = "key can not be empty"

            } else {
                dialogueClickListner.gotPassword(SSID, et_password.text.toString())
                dialog.dismiss()
            }
        }
        negative.setOnClickListener {
//            dialogueClickListner.negativeRunTimeButton()
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            // dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }*/

    fun tuneWifiOFF(state: Boolean) {
        if (!activity.isFinishing) {
            val dialog = Dialog(activity)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.normal_dialogue_view)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_yes)
            positive.text = "reTry"
//        positive.setText("I'm sure")
            val negative: Button = dialog.findViewById(R.id.btn_no)
            negative.text = "Turn ON"

            val tv_message: TextView = dialog.findViewById(R.id.tv_message)
            if (state) {
                tv_message.text = "Plz turn ON WIFI...."
            } else {
                tv_message.text = "Plz turn OFF WIFI...."
            }

//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
            positive.setOnClickListener {
                dialog.dismiss()
                dialogueClickListner.reTryWifiTurnON(state)
            }
            negative.setOnClickListener {
                dialogueClickListner.WifiTurnON(state)
                dialog.dismiss()
            }
            dialog.setOnCancelListener {
                //dialogueClickListner.negativeHotSpotTurnOFF()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun transferCompleted() {
        if (!activity.isFinishing) {
            val dialog = Dialog(activity)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.db_transfer_completed)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)
//        positive.setText("I'm sure")


//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Turn Permission-On Manually")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("You have denied permissions permanently so this feature of applications is not available. " +
//                "But you can turn the permissions on manually from the Settings > Apps > Check Data Usage > permissions > phone." +
//                "Are you sure you want to Allow permissions?")
            positive.setOnClickListener {
                dialog.dismiss()
                dialogueClickListner.transferFinished()
            }
            if (!activity.isFinishing) {
                dialog.show()
            }
        }
    }

    companion object {
        fun updateToNewVersion(
            context: Activity,
            title: String,
            msg: String,
            clickListener: HSActivityChooseDevice.OpenSettingsForPermissions
        ) {
            val dialog = Dialog(context)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.update_dialogue)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btn_allow: Button = dialog.findViewById(R.id.btn_allow)
            val btn_dontAllow: Button = dialog.findViewById(R.id.btn_dont_allow)
            val tv_title: TextView = dialog.findViewById(R.id.db_tv_title)
            tv_title.setText(title)
            val tv_msg: TextView = dialog.findViewById(R.id.tv_msg)
            tv_msg.setText(msg)


            btn_allow.setOnClickListener {
                dialog.dismiss()
                clickListener.allowCameraPermissions(context)
            }
            btn_dontAllow.setOnClickListener {
                dialog.cancel()
            }
            btn_dontAllow.setOnClickListener {
                dialog.cancel()
            }

            dialog.setOnCancelListener {
                //dialogueClickListner.negativeHotSpotTurnOFF()
                dialog.dismiss()
            }
            dialog.show()
        }


    fun showRational(
        activity: Activity,
        permissionPermission: String,
        title: String,
        msg: String,
        callBacks: HSMyPermissions.ExplanationCallBack
    ) {
        if (!activity.isFinishing) {
            val dialog = Dialog(activity)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.db_permissions_rationals)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btn_allow: Button = dialog.findViewById(R.id.btn_allow)
            val btn_dontAllow: Button = dialog.findViewById(R.id.btn_dont_allow)
            val tv_title: TextView = dialog.findViewById(R.id.tv_title)
            val tv_msg: TextView = dialog.findViewById(R.id.tv_msg)

            tv_msg.setText(msg)
            tv_title.setText(title)

            btn_allow.setOnClickListener {
                dialog.dismiss()
                callBacks.requestPermission()
            }
            btn_dontAllow.setOnClickListener {
                callBacks.denyPermission()
                dialog.dismiss()
            }

            dialog.setOnCancelListener {
                //dialogueClickListner.negativeHotSpotTurnOFF()
                callBacks.denyPermission()
                dialog.dismiss()
            }
            dialog.show()
        }
    }
        fun showPermissionsRequired(activity: Activity, msg: String,callBacks:HSMyPermissions.PermissionsDeniedCallBack) {
            if (!activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.db_permissions_rationals)
                dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val btn_allow: Button = dialog.findViewById(R.id.btn_allow)
                val btn_dontAllow: Button = dialog.findViewById(R.id.btn_dont_allow)
                val tv_title: TextView = dialog.findViewById(R.id.tv_title)
                val tv_msg: TextView = dialog.findViewById(R.id.tv_msg)

                btn_allow.text="Retry"
                btn_dontAllow.text="Cancel"

                tv_title.setText("Permissions Denied")
                tv_msg.setText(msg)

                btn_allow.setOnClickListener {
                    dialog.dismiss()
                    callBacks.retryPermissions()
                }
                btn_dontAllow.setOnClickListener {
                    callBacks.exitApp()
                    dialog.dismiss()
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()
                    callBacks.exitApp()
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    fun showRational(
        activity: Activity,
        permissionPermission: String,
        title: String,
        msg: String,
        callBacks: HSMyPermissions.RationalCallback
    ) {
        if (!activity.isFinishing) {
            val dialog = Dialog(activity)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.db_permissions_rationals)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btn_allow: Button = dialog.findViewById(R.id.btn_allow)
            val btn_dontAllow: Button = dialog.findViewById(R.id.btn_dont_allow)
            val tv_title: TextView = dialog.findViewById(R.id.tv_title)
            val tv_msg: TextView = dialog.findViewById(R.id.tv_msg)

            tv_msg.setText(msg)
            tv_title.setText(title)

            btn_allow.setOnClickListener {
                dialog.dismiss()
                callBacks.openSettings()
            }
            btn_dontAllow.setOnClickListener {
                callBacks.dismissed()
                dialog.dismiss()
            }

            dialog.setOnCancelListener {
                //dialogueClickListner.negativeHotSpotTurnOFF()
                callBacks.dismissed()
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}
}

/* fun showPermissionDialogueForR(title: String, msg: String) {
     if (!activity.isFinishing) {
         val dialog = Dialog(activity)
         dialog.setCancelable(false)
         dialog.setContentView(R.layout.db_permissions_rationals)
         dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         val btn_allow: Button = dialog.findViewById(R.id.btn_allow)
         val btn_dontAllow: Button = dialog.findViewById(R.id.btn_dont_allow)
         val tv_title: TextView = dialog.findViewById(R.id.tv_title)
         val tv_msg: TextView = dialog.findViewById(R.id.tv_msg)

         tv_msg.setText(msg)
         tv_title.setText(title)

         btn_allow.setOnClickListener {
             dialog.dismiss()
             dialogueClickListner.androidRStoragePermission()
         }
         btn_dontAllow.setOnClickListener {
             dialog.cancel()
         }
         btn_dontAllow.setOnClickListener {
             dialog.cancel()
         }

         dialog.setOnCancelListener {
             //dialogueClickListner.negativeHotSpotTurnOFF()
             dialog.dismiss()
         }
         dialog.show()
     }
 }*/


/*fun isOldDeviceAnAndroid10() {
    val dialog = Dialog(activity)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.db_isandroid10)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val positive: Button = dialog.findViewById(R.id.btn_yes_androidten)
    val negative:Button= dialog.findViewById(R.id.btn_no_android10)
    positive.setOnClickListener {
        dialog.dismiss()
        dialogueClickListner.isAndroid10()
    }

    negative.setOnClickListener {
        dialog.dismiss()
        dialogueClickListner.isNotAndroid10()
    }
    dialog.setOnCancelListener {
        //dialogueClickListner.negativeHotSpotTurnOFF()
        dialog.dismiss()
    }
    dialog.show()
}*/

//    fun showWarning() {
//        val dialog = Dialog(activity)
//        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.dialoguebox_layout)
//        val positive: Button = dialog.findViewById(R.id.btn_rateus)
//        positive.setText("I'm sure")
//        val negative: Button = dialog.findViewById(R.id.btn_yes)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Turning Permission off")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("This permission is necessary for geting data usage for Mobile Network." +
//                "\n Do you still want to continue?")
//        positive.setOnClickListener{
//            dialog.dismiss()
//            dialogueClickListner.turnPermissionsOn()
//        }
//        negative.setOnClickListener {
//
//            dialog.dismiss()
//            dialogueClickListner.dismissed();
//        }
//        dialog.setOnCancelListener{
//            dialogueClickListner.cancelListener()
//        }
//        dialog.show()
//    }

//    fun ignoreBatteryOptimization() {
//        val dialog = Dialog(activity)
//        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.dialoguebox_layout)
//        val positive: Button = dialog.findViewById(R.id.btn_rateus)
//        positive.setText("Ignore")
//        val negative: Button = dialog.findViewById(R.id.btn_yes)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText("Ignore Battery Optimization")
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText("This permission is necessary for Data Monitoring." +
//                "\n Do you still want to continue?")
//        positive.setOnClickListener{
//            dialog.dismiss()
//            dialogueClickListner.ignoreBatteryOptimization()
//        }
//        negative.setOnClickListener {
//            dialog.dismiss()
//            dialogueClickListner.ignoreDismissed()
//        }
//        dialog.setOnCancelListener{
//            dialogueClickListner.cancelListener()
//        }
//        dialog.show()
//    }
//
//    fun removeusagepermission(title: String, msg: String) {
//        val dialog = Dialog(activity)
//        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.dialoguebox_layout)
//        val positive: Button = dialog.findViewById(R.id.btn_rateus)
//        val negative: Button = dialog.findViewById(R.id.btn_yes)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        tv_title.setText(title)
//        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
//        tv_msg.setText(msg)
//        positive.setOnClickListener{
//            dialog.dismiss()
//            dialogueClickListner.systemRemoved()
//        }
//        negative.setOnClickListener {
//            dialogueClickListner.sysytemdismissed()
//            dialog.dismiss()
//        }
//        dialog.setOnCancelListener{
//            dialogueClickListner.cancelListener()
//        }
//        dialog.show()
//    }
