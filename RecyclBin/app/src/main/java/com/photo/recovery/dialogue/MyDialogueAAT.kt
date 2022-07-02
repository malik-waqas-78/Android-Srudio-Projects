package com.photo.recovery.dialogue


import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import com.photo.recovery.R

class MyDialogueAAT {
    companion object{
        fun showPrivacyDialog(activity: Activity) {
            if(!activity.isFinishing&&!activity.isDestroyed){
                val dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                dialog.setContentView(R.layout.dialog_privacy_aat)
                dialog.setCanceledOnTouchOutside(false)
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(true)
                dialog.setCancelable(true)
              if(!activity.isDestroyed&&!activity.isFinishing){
                  dialog.show()
              }
                val ok = dialog.findViewById<Button>(R.id.btn_okay)
                ok.setOnClickListener {
                    if(!activity.isDestroyed&&!activity.isFinishing) {
                        dialog.cancel()
                    }
                }
            }
        }
    }
}