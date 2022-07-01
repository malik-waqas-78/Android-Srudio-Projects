package com.example.whatsappdatarecovery.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.whatsappdatarecovery.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class MainPdfActivity extends AppCompatActivity {
    private static final String TAG ="malik" ;
    PDFView pdfView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_pdf);
        pdfView=findViewById(R.id.pdf_view);
        Intent i=getIntent();
        String path =i.getStringExtra("image");
        uri = Uri.parse(path);
        Log.d(TAG, "onCreate_pdf: "+uri);
        pdfView.fromUri(uri).load();
        new ReciecvePdf().execute(String.valueOf(uri));
    }
    class ReciecvePdf extends AsyncTask<String, Void, InputStream>
    {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream;
            try {
                Intent i=getIntent();
                String path =i.getStringExtra("image");
                uri = Uri.parse(path);
                inputStream = new FileInputStream(String.valueOf(uri));
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            pdfView.fromStream(inputStream).load();
        }
    }
}
