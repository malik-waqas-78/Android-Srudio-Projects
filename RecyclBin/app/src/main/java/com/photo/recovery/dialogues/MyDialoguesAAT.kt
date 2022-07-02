package com.photo.recovery.dialogues

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
//import com.datarecovery.recyclebindatarecovery.ads.NativeAdHelperAAT
import com.photo.recovery.databinding.DbGeneralAatBinding
import com.photo.recovery.permissions.MyPermissionsAAT

class MyDialoguesAAT {
    companion object {
        fun showExplanation(
            activity: Activity,
            permissionPermission: String,
            title: String,
            msg: String,
            callBacks: MyPermissionsAAT.Companion.ExplanationCallBack
        ) {
            if (!activity.isFinishing && !activity.isDestroyed) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(activity))
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )

                val btn_allow: Button = binding.btnPositive
                val btn_dontAllow: Button = binding.btnNegative
                val tv_title: TextView = binding.tvTitle
                val tv_msg: TextView = binding.tvMsg

                btn_allow?.text = "Allow"
                btn_dontAllow?.text = "Deny"

                tv_msg.setText(msg)
                tv_title.setText(title)

                btn_allow.setOnClickListener {
                    if (!activity.isDestroyed && !activity.isFinishing) {
                        dialog.cancel()
                        callBacks.requestPermission()
                    }
                }
                btn_dontAllow.setOnClickListener {
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        callBacks.denyPermission()
                        dialog.cancel()
                    }
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()
                    if (!activity.isDestroyed && !activity.isFinishing) {
                        callBacks.denyPermission()
                        dialog.cancel()
                    }
                }
                if (!activity.isFinishing && !activity.isDestroyed) {
                    dialog.show()
                }
            }
        }

        fun showRational(
            activity: Activity,
            permissionPermission: String,
            title: String,
            msg: String,
            callBacks: MyPermissionsAAT.Companion.RationalCallback
        ) {
            if (!activity.isFinishing && !activity.isDestroyed) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(activity))
                dialog.setContentView(binding.root)

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val btn_allow: Button = binding.btnPositive
                val btn_dontAllow: Button = binding.btnNegative
                val tv_title: TextView = binding.tvTitle
                val tv_msg: TextView = binding.tvMsg

                btn_allow?.text = "Allow"
                btn_dontAllow?.text = "Deny"

                tv_msg.setText(msg)
                tv_title.setText(title)

                btn_allow.setOnClickListener {
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        dialog.cancel()
                        callBacks.openSettings()
                    }


                }
                btn_dontAllow.setOnClickListener {

                    if (!activity.isFinishing && !activity.isDestroyed) {
                        dialog.cancel()
                        callBacks.dismissed()
                    }
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()
                    callBacks.dismissed()
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        dialog.cancel()
                    }
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        dialog.show()
                    }
                }
            }
        }

        fun showPermissionsRequired(
            activity: Activity,
            msg: String,
            callBacks: MyPermissionsAAT.Companion.PermissionsDeniedCallBack
        ) {
            if (!activity.isFinishing && !activity.isDestroyed) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)
                val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(activity))
                dialog.setContentView(binding.root)

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val btn_allow: Button = binding.btnPositive
                val btn_dontAllow: Button = binding.btnNegative
                val tv_title: TextView = binding.tvTitle
                val tv_msg: TextView = binding.tvMsg

                btn_allow.text = "Retry"
                btn_dontAllow.text = "Cancel"

                tv_title.setText("Permissions Denied")
                tv_msg.setText(msg)

                btn_allow.setOnClickListener {
                    if (!activity.isDestroyed && !activity.isFinishing) {
                        dialog.cancel()
                        callBacks.retryPermissions()
                    }
                }
                btn_dontAllow.setOnClickListener {
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        callBacks.exitApp()
                        dialog.cancel()
                    }
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()
                    if (!activity.isDestroyed && !activity.isFinishing) {
                        callBacks.exitApp()
                        dialog.cancel()
                    }
                }
                if (!activity.isFinishing && !activity.isDestroyed) {
                    dialog.show()
                }
            }

        }

        fun showRecoveryCompleted(
            context: Activity,
            msg: String?,
        ) {
            val dialog = Dialog(context)
            dialog.setCancelable(false)
            val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            binding.tvMsg.text = "$msg File(s) Recovered Successfully"
            binding.tvTitle.text = "Recovered"
            binding.adFrame.visibility=View.VISIBLE
//            NativeAdHelperAAT.showAdmobNativeAd(context, binding.adFrame)
            binding.btnPositive.text="OK"

            binding.btnNegative.visibility = View.GONE
            binding.btnPositive.setOnClickListener {
                if (!context.isFinishing &&!context.isDestroyed)
                    dialog.cancel()
            }

            dialog.setOnCancelListener {
                if (!context.isFinishing && !context.isDestroyed)
                    dialog.cancel()
            }
            if (!context.isFinishing && !context.isDestroyed)
                dialog.show()
        }

        fun showDeleteWarning(activity: Activity, callBacks: DeletionCallBack) {
            if (!activity.isFinishing) {
                val dialog = Dialog(activity)
                dialog.setCancelable(false)

                val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(activity))
                dialog.setContentView(binding.root)

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )

                val btn_allow: Button = binding.btnPositive
                val btn_dontAllow: Button = binding.btnNegative
                val tv_title: TextView = binding.tvTitle
                val tv_msg: TextView = binding.tvMsg

                tv_msg.setText("Are you sure you want to delete selected items?")
                tv_title.setText("Warning")

                btn_allow.text = "Delete"
                btn_dontAllow.text = "cancel"
                binding.adFrame.visibility=View.VISIBLE
//                NativeAdHelperAAT.showAdmobNativeAd(activity, binding.adFrame)
                btn_allow.setOnClickListener {
                    if (!activity.isFinishing&& !activity.isDestroyed) {
                        dialog.cancel()
                        callBacks.deleteFiles()
                    }

                }
                btn_dontAllow.setOnClickListener {

                    if (!activity.isFinishing&& !activity.isDestroyed) {
                        callBacks.dismiss()
                        dialog.cancel()
                    }
                }

                dialog.setOnCancelListener {
                    //dialogueClickListner.negativeHotSpotTurnOFF()

                    if (!activity.isFinishing&& !activity.isDestroyed) {
                        callBacks.dismiss()
                        dialog.cancel()
                    }
                }
                if (!activity.isFinishing && !activity.isDestroyed) {
                    dialog.show()
                }
            }
        }

        interface DeletionCallBack {
            fun deleteFiles()
            fun dismiss()
        }

    }
}