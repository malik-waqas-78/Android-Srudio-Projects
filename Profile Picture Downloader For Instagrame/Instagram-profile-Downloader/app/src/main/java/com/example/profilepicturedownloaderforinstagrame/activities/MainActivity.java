package com.example.profilepicturedownloaderforinstagrame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilepicturedownloaderforinstagrame.AppPrefrences;
import com.example.profilepicturedownloaderforinstagrame.Dialog.AuthenticationDialog;
import com.example.profilepicturedownloaderforinstagrame.R;
import com.example.profilepicturedownloaderforinstagrame.interfaces.AuthenticationListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AuthenticationListener {
    String token = null;
    Button btn_login_instagram_account;
    AuthenticationDialog authenticationDialog = null;
    AppPrefrences appPrefrences = null;
    View info = null;
    Button btn_searchingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login_instagram_account = findViewById(R.id.login_instagram_account);
        btn_searchingActivity=findViewById(R.id.searchingActivity);
        btn_searchingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,DownloadProfileImageActivity.class);
                startActivity(i);
            }
        });
        appPrefrences = new AppPrefrences(this);
        token = appPrefrences.getString(AppPrefrences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }
    }

    public void login() {
        btn_login_instagram_account.setText("LOGOUT");
        info.setVisibility(View.VISIBLE);
        ImageView pic = findViewById(R.id.pic);
        Picasso.with(this).load(appPrefrences.getString(AppPrefrences.PROFILE_PIC)).into(pic);
        TextView id = findViewById(R.id.id);
        id.setText(appPrefrences.getString(AppPrefrences.USER_ID));
        TextView name = findViewById(R.id.name);
        name.setText(appPrefrences.getString(AppPrefrences.USER_NAME));
    }

    public void logout() {
        btn_login_instagram_account.setText("LOGIN INSTAGRAM");
        token = null;
        info.setVisibility(View.VISIBLE);
        appPrefrences.clear();
    }

    @Override
    public void onTockenRecieved(String auth_tocken) {
        if (auth_tocken == null)
            return;
        appPrefrences.putString(AppPrefrences.TOKEN, auth_tocken);
        token = auth_tocken;
        getUserInfoByAccessToken(token);
    }

    public void onClick(View view) {
        if (token != null) {
            logout();
        } else {
          /*  authenticationDialog = new AuthenticationDialog(MainActivity.this, this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();*/
          Intent i=new Intent(MainActivity.this,AuthenticationActivity.class);
          startActivity(i);
            /*Toast.makeText(this, "Show", Toast.LENGTH_SHORT).show();*/
        }
    }
    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramApi().execute();
    }

    public class RequestInstagramApi extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        appPrefrences.putString(AppPrefrences.USER_ID, jsonData.getString("id"));
                        appPrefrences.putString(AppPrefrences.USER_NAME, jsonData.getString("username"));
                        appPrefrences.putString(AppPrefrences.PROFILE_PIC, jsonData.getString("profile_picture"));
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}