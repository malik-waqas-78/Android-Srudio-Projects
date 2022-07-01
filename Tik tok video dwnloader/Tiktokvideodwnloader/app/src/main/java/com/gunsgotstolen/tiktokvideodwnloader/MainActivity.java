package com.gunsgotstolen.tiktokvideodwnloader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="waqas" ;
    EditText url;
    Button dowlaod;
    private ProgressDialog progressvalue;
    Handler handle;
    private DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url=findViewById(R.id.url);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dowlaod=findViewById(R.id.downlaod);
        dowlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  URL=url.getText().toString();
                saveVideo(URL);
                TextView tv=findViewById(R.id.tv);
                tv.setText("YOur video has been downloaded.\ncheck root/waqas/ to find the downloaded video.");
            }
        });



    }

    public void saveVideo(final String str) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String playURL = "";

                    final Document document = Jsoup.connect(str)
                            .userAgent("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                            .get();

                    for (Element element : document.select("script")) {
                        final String data = element.data();
                        if (data.contains("videoData")) {
                            final String substring = data.substring(data.lastIndexOf("urls"));
                            final String substring2 = substring.substring(substring.indexOf("[") + 1);
                            final String substring3 = substring2.substring(0, substring2.indexOf("]"));
                            playURL = substring3.substring(1, substring3.length() - 1);
                        }
                    }

                    MainActivity.this.download(playURL, "waqas.mp4");
                } catch (final Exception e) {
                    Log.e("tiktok", "", e);
                }
            }
        }).start();


    }
      void download(String url,String fileName){
          new Handler(Looper.getMainLooper()).post(new Runnable() {
              @Override
              public void run() {
                  Toast.makeText(MainActivity.this.getApplicationContext(), "Downlaod started", Toast.LENGTH_SHORT).show();
              }
          });

          final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(withoutWatermark(url)));
          request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
          request.setAllowedOverRoaming(true);
          request.setTitle(getString(R.string.app_name));
          request.setDescription("Downloading");
          request.setVisibleInDownloadsUi(true);
          request.allowScanningByMediaScanner();
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
          request.setDestinationInExternalFilesDir(this, "/TikFetch", ("tiktok__" + System.currentTimeMillis()) + "." + "mp4");

          downloadManager.enqueue(request);
          /*PRDownloader.initialize(getApplicationContext());
          //String url="https://v16m.tiktokcdn.com/30c5208a0b7f5e057cb9251927887bfa/5f1f13cd/video/tos/useast2a/tos-useast2a-ve-0068c001/7336467256a94b389c0bf49000eeba53/?a=1233&br=2292&bt=1146&cr=0&cs=0&dr=0&ds=3&er=&l=202007271149460101150040740316D405&lr=tiktok_m&mime_type=video_mp4&qs=0&rc=ampyNGhzbWZ1djMzZzczM0ApOWlnZDQzNDw1Nzw6N2Y8NGdwc3NvcW1mbjZfLS0yMTZzczFiYWAxXjQ2MjRjMTQxXjY6Yw%3D%3D&vl=&vr";
          String dirPath= Environment.getExternalStorageDirectory()+"/waqas/";
          //String fileName="waqas.mp4";
          int downloadId = PRDownloader.download(url, dirPath, fileName)
                  .build()
                  .setOnProgressListener(new OnProgressListener() {
                      @Override
                      public void onProgress(Progress progress) {
                          //tv.setText(progress.toString());
                          int p=(int)((100*progress.currentBytes)/progress.totalBytes);

                          handle.sendMessage(handle.obtainMessage());
                         // progressvalue.setProgress(p);
                          Log.d(TAG, "onProgress: "+(100*progress.currentBytes)/progress.totalBytes+"   "+p);

                      }
                  })
                  .start(new OnDownloadListener() {
                      @Override
                      public void onDownloadComplete() {
                          Log.d(TAG, "onDownloadComplete: ");
                      }

                      @Override
                      public void onError(Error error) {
                          Log.e(TAG, "onError: "+error.getConnectionException());
                      }
                  });
*/

      }

    public String withoutWatermark(final String url) {
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                final StringBuilder stringBuffer = new StringBuilder();

                String readLine;
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(readLine);

                    if (stringBuffer.toString().contains("vid:")) {
                        try {
                            if (stringBuffer.substring(stringBuffer.indexOf("vid:")).substring(0, 4).equals("vid:")) {
                                final String substring = stringBuffer.substring(stringBuffer.indexOf("vid:"));
                                final String trim = substring.substring(4, substring.indexOf("%"))
                                        .replaceAll("[^A-Za-z0-9]", "").trim();
                                return "https://api.tiktokv.com/aweme/v1/playwm/?video_id=" + trim;
                            }
                        } catch (final Exception e) {
                            Log.e("tiktok", "", e);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            Log.e("tiktok", "", e);
        }

        return "";
    }



}