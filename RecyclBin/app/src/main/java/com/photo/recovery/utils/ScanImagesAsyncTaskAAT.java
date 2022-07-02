/*
package com.codesk.recyclebin.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.util.ArrayList;

public class ScanImagesAsyncTask extends AsyncTask {
   private final String TAG = this.getClass().getName();
   private ArrayList alImageData;
   private Context context;
   private Handler handler;
   int i = 0;
   private ProgressDialog progressDialog;

   public ScanImagesAsyncTask(Context var1, Handler var2) {
      this.context = var1;
      this.handler = var2;
      this.alImageData = new ArrayList();
   }

   public void checkFileOfDirectory(File[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Integer[] var3 = new Integer[1];
         int var4 = this.i++;
         var3[0] = var4;
         this.publishProgress(var3);
         if (var1[var2].isDirectory()) {
            this.checkFileOfDirectory(this.getFileList(var1[var2].getPath()));
         } else {
            Options var5 = new Options();
            var5.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(var1[var2].getPath(), var5);
            if (var5.outWidth != -1 && var5.outHeight != -1 && !var1[var2].getPath().endsWith(".exo") && !var1[var2].getPath().endsWith(".mp3") && !var1[var2].getPath().endsWith(".mp4") && !var1[var2].getPath().endsWith(".pdf") && !var1[var2].getPath().endsWith(".apk") && !var1[var2].getPath().endsWith(".txt") && !var1[var2].getPath().endsWith(".doc") && !var1[var2].getPath().endsWith(".exi") && !var1[var2].getPath().endsWith(".dat") && !var1[var2].getPath().endsWith(".m4a") && !var1[var2].getPath().endsWith(".json") && !var1[var2].getPath().endsWith(".chck")) {
               this.alImageData.add(new ImageData(var1[var2].getPath(), false));
            }
         }
      }

   }

   protected ArrayList doInBackground(String... var1) {
      String var5;
      if (var1[0].equalsIgnoreCase("all")) {
         var5 = Environment.getExternalStorageDirectory().getAbsolutePath();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
         var2.append("/RestoredPhotos");
         var5 = var2.toString();
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("root = ");
      var6.append(var5);
      this.checkFileOfDirectory(this.getFileList(var5));
      return this.alImageData;
   }

   public File[] getFileList(String var1) {
      File var2 = new File(var1);
      return !var2.isDirectory() ? null : var2.listFiles();
   }

   protected void onPostExecute(ArrayList var1) {
      ProgressDialog var2 = this.progressDialog;
      if (var2 != null) {
         var2.cancel();
         this.progressDialog = null;
      }

      if (this.handler != null) {
         Message var3 = Message.obtain();
         var3.what = 1000;
         var3.obj = var1;
         this.handler.sendMessage(var3);
      }

      super.onPostExecute(var1);
   }

   @Override
   protected Object doInBackground(Object[] objects) {
      return null;
   }

   protected void onPreExecute() {
      super.onPreExecute();
   }

   protected void onProgressUpdate(Integer... var1) {
      if (this.handler != null) {
         Message var2 = Message.obtain();
         var2.what = 3000;
         var2.obj = var1[0];
         this.handler.sendMessage(var2);
      }

   }
}
*/
