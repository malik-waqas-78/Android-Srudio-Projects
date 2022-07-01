package com.data.usage.manager.usefullclasses

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.R


class MyDialogueBoxes(var activity:Context,var context: Context, var inflater: LayoutInflater) {
    var dialogueClickListner = context as DialogueClickListner
    constructor(activity: Context,context: Context,inflater: LayoutInflater,dialogueClickListner: DialogueClickListner):this(activity,context, inflater){
        this.dialogueClickListner=dialogueClickListner
    }
//    fun runtimePermissionsWarning(title:String, msg:String) {
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
//            dialogueClickListner.positiveRunTimeButton()
//        }
//        negative.setOnClickListener {
//            dialog.dismiss()
//            dialogueClickListner.negativeRunTimeButton()
//        }
//        dialog.show()
//    }
    fun systemLevelPermissionsWarning(title:String, msg:String) {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
    dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("Allow")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText(title)
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText(msg)
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.positiveSyatemLevelButton()
        }
        negative.setOnClickListener {
           dialogueClickListner.negativeSyatemLevelButton()
            dialog.dismiss()
        }
    dialog.setOnCancelListener{
        dialogueClickListner.cancelListener()
    }
        dialog.show()
    }

    fun cantAskForPermissions() {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("I'm sure")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText("Turn Permission-On Manually")
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText("You have denied permissions so this feature is currently not available. " +
                "But you can turn the permission on manually by clicking \" I'M sure\" and then search for permissions > phone.\n" +
                "Are you sure you want to Allow permissions?")
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.turnPermissionsOn()
        }
        negative.setOnClickListener {
//            dialogueClickListner.negativeRunTimeButton()
            dialog.dismiss()
        }
        dialog.setOnCancelListener{
            dialogueClickListner.cancelListener()
        }
        dialog.show()
    }

    fun showWarning() {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("I'm sure")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText("Turning Permission off")
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText("This permission is necessary for geting data usage for Mobile Network." +
                "\n Do you still want to continue?")
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.turnPermissionsOn()
        }
        negative.setOnClickListener {

            dialog.dismiss()
            dialogueClickListner.dismissed();
        }
        dialog.setOnCancelListener{
            dialogueClickListner.cancelListener()
        }
        dialog.show()
    }

    fun ignoreBatteryOptimization() {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("Ignore")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText("Ignore Battery Optimization")
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText("This permission is necessary for Data Monitoring." +
                "\n Do you still want to continue?")
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.ignoreBatteryOptimization()
        }
        negative.setOnClickListener {
            dialog.dismiss()
            dialogueClickListner.ignoreDismissed()
        }
        dialog.setOnCancelListener{
            dialogueClickListner.cancelListener()
        }
        dialog.show()
    }
    fun speedTestErrorOccurred(title:String,msg:String){
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("Retry")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        negative.setText("Cancel")
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText(title)
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText(msg)
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.retrySpeedTest()
        }
        negative.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnCancelListener{
            dialog.dismiss()
        }
        dialog.show()
    }
    fun removeusagepermission(title: String, msg: String) {
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText(title)
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText(msg)
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.systemRemoved()
        }
        negative.setOnClickListener {
            dialogueClickListner.sysytemdismissed()
            dialog.dismiss()
        }
        dialog.setOnCancelListener{
            dialogueClickListner.cancelListener()
        }
        dialog.show()
    }
    fun dataPlanDelete(){
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("Yes/Sure")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        negative.setText("No")
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText("")
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText("Are you sure you want to delete current data Plan?")
        positive.setOnClickListener{
            dialog.dismiss()
            dialogueClickListner.deletePlan()
        }
        negative.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnCancelListener{
            dialog.dismiss()
        }
        dialog.show()
    }
    fun dataPlanSet(){
        val dialog = Dialog(activity)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialoguebox_layout)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_rateus)
        positive.setText("ok")
        val negative: Button = dialog.findViewById(R.id.btn_yes)
        negative.visibility= View.GONE
        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
        tv_title.setText("Data Plan Set")
        val tv_msg=dialog.findViewById<TextView>(R.id.tv_msg)
        tv_msg.setText("Your Data Plan has been set.")
        positive.setOnClickListener{
            dialog.dismiss()
        }
        negative.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnCancelListener{
            dialog.dismiss()
        }
        dialog.show()
    }

}