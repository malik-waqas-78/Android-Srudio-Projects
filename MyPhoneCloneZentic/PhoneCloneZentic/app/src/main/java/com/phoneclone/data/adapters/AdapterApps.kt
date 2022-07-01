package com.phoneclone.data.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.modelclasses.AppsModel
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException

class AdapterApps(var context: Context, var appSrcDir: ArrayList<AppsModel>) :
    RecyclerView.Adapter<AdapterApps.ApplicationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_applications, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        var packageManager = context.getPackageManager() as PackageManager
        var pathToApk = appSrcDir[position].srcDir
        var packageInfo = packageManager.getPackageArchiveInfo(pathToApk, 0);
        var label: String? = "MyApplication"
        var icon: Drawable? = null
        // those two lines do the magic:
        try {
            if (packageInfo != null) {
                packageInfo.applicationInfo.sourceDir = pathToApk;
                packageInfo.applicationInfo.publicSourceDir = pathToApk;
                label = packageManager.getApplicationLabel(packageInfo.applicationInfo) as String
                icon = packageManager.getApplicationIcon(packageInfo.applicationInfo)
            }
        } catch (exception: Exception) {
            //exception occurred
            appSrcDir.removeAt(position)
            notifyItemChanged(position)
        }
        holder.tv_appName.text = label
        Glide.with(context).load(icon).into(holder.iv_appIcon)
            .onLoadStarted(context.getDrawable(R.drawable.ic_apk))
        // holder.iv_appIcon.setImageDrawable(icon)
        //set btn click
        if (appSrcDir[position].installed) {
            holder.anim.visibility = View.VISIBLE
            holder.btn_appaction.visibility = View.GONE
        }
        holder.btn_appaction.setOnClickListener {
            //just request install application
            installThisApp(label!!)
        }
    }

    override fun getItemCount(): Int {
        return appSrcDir.size
    }

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_appName = itemView.findViewById<TextView>(R.id.tv_appname)
        val iv_appIcon = itemView.findViewById<ImageView>(R.id.iv_appicon)
        val btn_appaction = itemView.findViewById<Button>(R.id.btn_appaction)
        val anim = itemView.findViewById<LottieAnimationView>(R.id.animationView)
    }

    fun installThisApp(label: String) {
        val rootpath =
            Environment.getExternalStorageDirectory().absolutePath + "/" + context.resources?.getString(
                R.string.app_name
            )
        val file = File(rootpath, label + ".apk")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var contentUri: Uri? =null
                try{
                    contentUri=FileProvider.getUriForFile(
                        context,
                        "com.phoneclone.clonemyphone.data.transfer.unlimited.fileprovider",
                        file
                    )
                }catch (e: IllegalArgumentException){
                    Toast.makeText(context,"Installation Failed",Toast.LENGTH_SHORT).show()
                }
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            (context as Activity).startActivityForResult(install, MyConstants.INSTALL_APPS)
            // finish()
        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val uri1: Uri = Uri.fromFile(File(rootpath, label + ".apk"))
            install.setDataAndType(
                uri1,
                "\"application/vnd.android.package-archive\""
            )
            (context as Activity).startActivityForResult(install, MyConstants.INSTALL_APPS)
            // finish()
        }
    }
}