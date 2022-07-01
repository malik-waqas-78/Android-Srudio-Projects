package com.example.profilepicturedownloaderforinstagrame.activities;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilepicturedownloaderforinstagrame.R;
import com.example.profilepicturedownloaderforinstagrame.SuggestionsAdapter;
import com.example.profilepicturedownloaderforinstagrame.models.ModelClass;
import com.example.profilepicturedownloaderforinstagrame.response.IntagramProfileResponse;
import com.example.profilepicturedownloaderforinstagrame.utils.ApiUtils;
import com.example.profilepicturedownloaderforinstagrame.utils.ZoomstaUtil;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadProfileImageActivity extends AppCompatActivity {
    private String TAG = DownloadProfileImageActivity.class.getName();
    EditText username;
    Button search;
    ProgressBar progress_bar;
    Context context;
    Bitmap bitmap;
    String usernameText;
    TextView textViewUserName;
    Handler suggestionsFetchHandle;
    SuggestionsAdapter suggestionsAdapter;
    RecyclerView suggestion_recylerview;

    ModelClass modelClass;
    GridLayoutManager gridLayoutManager;
    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    Bitmap bitmap1;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_download_profile_image);
        suggestionsFetchHandle = new Handler();
        suggestion_recylerview=findViewById(R.id.suggestion_recylerview);
        username = findViewById(R.id.username);
        search = findViewById(R.id.search);
        progress_bar = findViewById(R.id.progress_bar);
        textViewUserName = findViewById(R.id.textViewUsername);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean[] searchUser = {true};
                final AsyncTask<?, ?, ?>[] prevSuggestionAsync = new AsyncTask<?, ?, ?>[1];
                final String[] COLUMNS = {
                        BaseColumns._ID,
                        Constants.EXTRAS_USERNAME,
                        Constants.EXTRAS_NAME,
                        Constants.EXTRAS_TYPE,
                        "pfp",
                        "verified"
                };
                String currentSearchQuery = username.getText().toString();
                Log.d(TAG, "currentSearchQuery: " + currentSearchQuery);
                final FetchListener<SuggestionModel[]> fetchListener = new FetchListener<SuggestionModel[]>() {
                    @Override
                    public void doBefore() {
                    }

                    @Override
                    public void onResult(final SuggestionModel[] result) {
                        final MatrixCursor cursor;
                        if (result == null) {
                        } else {
                            cursor = new MatrixCursor(COLUMNS, 0);
                            for (int i = 0; i < result.length; i++) {
                                final SuggestionModel suggestionModel = result[i];
                                if (suggestionModel != null) {
                                    final SuggestionType suggestionType = suggestionModel.getSuggestionType();
                                    final Object[] objects = {
                                            i,
                                            suggestionType == SuggestionType.TYPE_LOCATION ? suggestionModel.getName() : suggestionModel.getUsername(),
                                            suggestionType == SuggestionType.TYPE_LOCATION ? suggestionModel.getUsername() : suggestionModel.getName(),
                                            suggestionType,
                                            suggestionModel.getProfilePic(),
                                            suggestionModel.isVerified()};
                                    Log.d(TAG, "onResult: " + suggestionModel.getName());
                                    Log.d(TAG, "onResult: " + suggestionModel.getUsername());
                                    Log.d(TAG, "onResult: " + suggestionModel.getProfilePic());
                                    Log.d(TAG, "onResult: " + suggestionModel.isVerified());
                                    File imgFile = new  File(suggestionModel.getProfilePic());
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    Log.d(TAG, "myBitmap: "+imgFile);
                                    modelClass=new ModelClass();
                                    modelClass.setName(suggestionModel.getName());
                                    modelClass.setUsername(suggestionModel.getUsername());
                                    modelClass.setProfile_imge(suggestionModel.getProfilePic());
                                    modelClassArrayList.add(modelClass);
                                    suggestionsAdapter=new SuggestionsAdapter(DownloadProfileImageActivity.this,modelClassArrayList);
                                    gridLayoutManager=new GridLayoutManager(DownloadProfileImageActivity.this,1);
                                    suggestion_recylerview.setLayoutManager(gridLayoutManager);
                                    suggestion_recylerview.setAdapter(suggestionsAdapter);
                                    if (!searchUser[0]) cursor.addRow(objects);
                                    else {
                                        final boolean isCurrHash = suggestionType == SuggestionType.TYPE_HASHTAG;
                                        if (isCurrHash || !isCurrHash)
                                            cursor.addRow(objects);
                                    }
                                }
                            }
                        }
                    }
                };
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        /*cancelSuggestionsAsync();*/
                        searchUser[0] = currentSearchQuery.charAt(0) == '@';
                        prevSuggestionAsync[0] = new SuggestionsFetcher(fetchListener).executeOnExecutor(
                                AsyncTask.THREAD_POOL_EXECUTOR,
                                searchUser[0] ? currentSearchQuery.substring(1) : currentSearchQuery);
                    }

                    ;
               /* private void cancelSuggestionsAsync () {
                    if (prevSuggestionAsync != null)
                        try {
                            prevSuggestionAsync.cancel(true);
                        } catch (final Exception ignored) {
                        }
                }*/
                };
                suggestionsFetchHandle.removeCallbacks(runnable);
                suggestionsFetchHandle.postDelayed(runnable, 800);
            }
        });
    }

    /* private boolean setupSearchView() {

         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             private boolean searchUser;
             private boolean searchHash;
             private AsyncTask<?, ?, ?> prevSuggestionAsync;
             private final String[] COLUMNS = {
                     BaseColumns._ID,
                     Constants.EXTRAS_USERNAME,
                     Constants.EXTRAS_NAME,
                     Constants.EXTRAS_TYPE,
                     "pfp",
                     "verified"
             };
             private String currentSearchQuery;

             private final FetchListener<SuggestionModel[]> fetchListener = new FetchListener<SuggestionModel[]>() {
                 @Override
                 public void doBefore() {
                     suggestionAdapter.changeCursor(null);
                 }

                 @Override
                 public void onResult(final SuggestionModel[] result) {
                     final MatrixCursor cursor;
                     if (result == null) cursor = null;
                     else {
                         cursor = new MatrixCursor(COLUMNS, 0);
                         for (int i = 0; i < result.length; i++) {
                             final SuggestionModel suggestionModel = result[i];
                             if (suggestionModel != null) {
                                 final SuggestionType suggestionType = suggestionModel.getSuggestionType();
                                 final Object[] objects = {
                                         i,
                                         suggestionType == SuggestionType.TYPE_LOCATION ? suggestionModel.getName() : suggestionModel.getUsername(),
                                         suggestionType == SuggestionType.TYPE_LOCATION ? suggestionModel.getUsername() : suggestionModel.getName(),
                                         suggestionType,
                                         suggestionModel.getProfilePic(),
                                         suggestionModel.isVerified()};
                                 Log.d("92727", "onResult: "+suggestionModel.getName()+""+suggestionModel.getUsername());
                                 if (!searchHash && !searchUser) cursor.addRow(objects);
                                 else {
                                     final boolean isCurrHash = suggestionType == SuggestionType.TYPE_HASHTAG;
                                     if (searchHash && isCurrHash || !searchHash && !isCurrHash)
                                         cursor.addRow(objects);
                                 }
                             }
                         }
                     }
                     suggestionAdapter.changeCursor(cursor);
                 }
             };

             private final Runnable runnable = () -> {
                 cancelSuggestionsAsync();
                 searchUser = currentSearchQuery.charAt(0) == '@';
                 searchHash = currentSearchQuery.charAt(0) == '#';

                     prevSuggestionAsync = new SuggestionsFetcher(fetchListener).executeOnExecutor(
                             AsyncTask.THREAD_POOL_EXECUTOR,
                             searchUser || searchHash ? currentSearchQuery.substring(1)
                                     : currentSearchQuery);

             };

             private void cancelSuggestionsAsync() {
                 if (prevSuggestionAsync != null)
                     try {
                         prevSuggestionAsync.cancel(true);
                     } catch (final Exception ignored) {}
             }

         });
         return true;
     }*/
    class RequestInstagramAPI extends AsyncTask<Void, String, String> {
        String url = "";

        public RequestInstagramAPI(String url) {
            //this.url = url + "?__a=1";

        }

        @Override
        protected String doInBackground(Void... params) {
            IntagramProfileResponse intagramProfileResponse;
            this.url = ApiUtils.getUsersSugestions(username.getText().toString(), new IntagramProfileResponse().getGraphql().getUser().getId());

            HttpClient httpClient = new DefaultHttpClient();
            try {

                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpGet);
                //final JSONObject jObject = new JSONObject(EntityUtils.toString(response.getEntity()));

                HttpEntity httpEntity = response.getEntity();
                //Log.d(TAG, "doInBackground: " + jObject);
                return EntityUtils.toString(httpEntity);
            } catch (Exception e) {
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
////                    IntagramProfileResponse intagramProfileResponse = new Gson().fromJson(response, IntagramProfileResponse.class);
////                    if (intagramProfileResponse.getGraphql() != null && intagramProfileResponse.getGraphql().getUser() != null) {
////                        final String newLink = intagramProfileResponse.getGraphql().getUser().getProfilePicUrlHd();
////                        new RequestProfileInstagramAPI(newLink).execute();
////                        Log.d(TAG, "onPostlink: " + newLink);
//                    //}
//                    IntagramProfileResponse intagramProfileResponse = new Gson().fromJson(response, IntagramProfileResponse.class);
//                    if (intagramProfileResponse.getGraphql() != null && intagramProfileResponse.getGraphql().getUser() != null) {
//                        final String newLink = intagramProfileResponse.getGraphql().getUser().getProfilePicUrlHd();
//                        new RequestProfileInstagramAPI(newLink).execute();
//                        Log.d(TAG, "onPostlink: " + newLink);
//                    }
//                    //this.url=ApiUtils.getUsersSugestions(username.getText().toString(),intagramProfileResponse.getGraphql().getUser().getId());
//                    HttpClient httpClient = new DefaultHttpClient();
//                    try {
//
//                        HttpGet httpGet = new HttpGet(url);
//                        HttpResponse response1 = httpClient.execute(httpGet);
//                        HttpEntity httpEntity1 = response1.getEntity();
//                        Log.d(TAG, "doInBackground: "+EntityUtils.toString(httpEntity1));
//                        //EntityUtils.toString(httpEntity1);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    progress_bar.setVisibility(View.GONE);
//                    textViewUserName.setVisibility(View.VISIBLE);
//                    search.setVisibility(View.VISIBLE);
//                    username.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            } else {

//            }
                } catch (Exception e) {

                }
            }
            progress_bar.setVisibility(View.GONE);
            textViewUserName.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

        }

        private class RequestProfileInstagramAPI extends AsyncTask<Void, String, String> {
            String url = "";
            HttpURLConnection connection = null;

            public RequestProfileInstagramAPI(String url) {
                this.url = url;
                Log.d(TAG, "RequestProfileInstagramAPI: " + url);
            }

            @Override
            protected String doInBackground(Void... params) {
                connection = null;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.connect();
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            Intent intent = new Intent(DownloadProfileImageActivity.this, ViewProfileActivity.class);
                            intent.putExtra("username", usernameText);
                            Log.d("TAG", "run_username: " + usernameText);
                            Log.d("TAG", "run_bitmap: " + bitmap);
                            ZoomstaUtil.createImageFromBitmap(DownloadProfileImageActivity.this, bitmap);
                            DownloadProfileImageActivity.this.startActivity(intent);
                            progress_bar.setVisibility(View.GONE);
                            textViewUserName.setVisibility(View.VISIBLE);
                            search.setVisibility(View.VISIBLE);
                            username.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}