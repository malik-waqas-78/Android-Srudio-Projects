package com.ppt.walkie.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.ppt.walkie.R
import com.ppt.walkie.databinding.DbConnectioonRequestOkraBinding


class MyDialogueOKRA {
    companion object{
        fun connectRequest(activity: Activity, endpointName:String?, callback: ConnectionRequest){
            if(!activity.isFinishing&&!activity.isDestroyed){
                val dialog: Dialog = Dialog(activity)
                val binding= DbConnectioonRequestOkraBinding.inflate(LayoutInflater.from(dialog.context))
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )

                binding.tvMsg.text="Are you sure you want to connect to $endpointName"
                binding.tvTitle.text=activity.getString(R.string.connection_request)

                binding.btnConnect.setOnClickListener {
                    if(!activity.isFinishing&&!activity.isDestroyed){
                        callback.connectToEndPoint()
                        dialog.cancel()
                    }

                }
                binding.btnReject.setOnClickListener {
                    if(!activity.isFinishing&&!activity.isDestroyed){
                        callback.rejectConnection()
                        dialog.cancel()
                    }
                }

                if(!activity.isFinishing&&!activity.isDestroyed){
                    dialog.show()
                }

            }
        }
        fun requestTurnWifiOn(activity: Activity, callback: WifiStateChangeRequest){
            if(!activity.isFinishing&&!activity.isDestroyed){
                val dialog: Dialog = Dialog(activity)
                val binding= DbConnectioonRequestOkraBinding.inflate(LayoutInflater.from(dialog.context))
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                binding.tvMsg.text=activity.getString(R.string.turnon_wifi_msg)
                binding.tvTitle.text=activity.getString(R.string.turnon_wifi_title)
                binding.btnConnect.text=activity.getString(R.string.turn_on_wifi)
                binding.btnConnect.setOnClickListener {
                    if(!activity.isFinishing&&!activity.isDestroyed){
                        callback.requestApproved()
                        dialog.cancel()
                    }

                }
                binding.btnReject.text=activity.getString(R.string.cancel)
                binding.btnReject.setOnClickListener {
                    if(!activity.isFinishing&&!activity.isDestroyed){
                        callback.requestDenied()
                        dialog.cancel()
                    }
                }

                if(!activity.isFinishing&&!activity.isDestroyed){
                    dialog.show()
                }

            }
        }
        fun showExplanation(activity: Activity,permissionPermission: String, title: String, msg: String,callBacks: MyPermissionsOKRA.ExplanationCallBack) {
            if (!activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.db_permissions_rationals_okra)
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
        fun alertDisconnected(activity: Activity) {
            if (!activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                val binding=DbConnectioonRequestOkraBinding.inflate(LayoutInflater.from(activity))
                dialog.setContentView(binding.root)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                val btn_allow: Button = binding.btnConnect
                val btn_dontAllow: Button = binding.btnReject
                val tv_title: TextView = binding.tvTitle
                val tv_msg: TextView = binding.tvMsg
                val anim:LottieAnimationView=binding.animCalling

                tv_msg.text = activity.getString(R.string.alert_dicsonnected_msg)
                tv_title.text = activity.getString(R.string.alert_disconnected_title)

                btn_allow.text= activity.getString(R.string.ok)

                anim.visibility=View.GONE


                btn_allow.setOnClickListener {
                    dialog.dismiss()
                }
                btn_dontAllow.visibility= View.GONE
                btn_dontAllow.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        fun showRational(activity: Activity,permissionPermission: String, title: String, msg: String,callBacks: MyPermissionsOKRA.RationalCallback) {
            if (!activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.db_permissions_rationals_okra)
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
    interface ConnectionRequest{
        fun connectToEndPoint()
        fun rejectConnection()
    }
    interface WifiStateChangeRequest{
        fun requestApproved()
        fun requestDenied()
    }
}