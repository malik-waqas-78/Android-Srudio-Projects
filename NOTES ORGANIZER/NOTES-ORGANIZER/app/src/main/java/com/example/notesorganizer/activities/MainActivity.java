package com.example.notesorganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.notesorganizer.modelclass.ModelClass;
import com.example.notesorganizer.helperclass.MyHelperClass;
import com.example.notesorganizer.R;
import com.example.notesorganizer.adapter.ShowDatabaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import io.realm.Realm;

import static android.view.inputmethod.EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    ImageView head_image, btn_okay;
    TextView textView_create_important_things;
    RecyclerView recyclerView;
    ShowDatabaseAdapter showDatabaseAdapter;
    /*Toolbar toolbar;*/
    MyHelperClass myHelperClass;
    ArrayList<ModelClass> itemslist = new ArrayList<>();
    ArrayList<ModelClass> toReverse = new ArrayList<>();
    ArrayList<ModelClass>  copied=new ArrayList<>();
    Realm realm;
    EditText editText_searchview;
    ImageView search_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.new_recyclerview);
        editText_searchview = findViewById(R.id.edit_search);
        head_image = findViewById(R.id.head_main_image);
        search_images = findViewById(R.id.searc_images);
     /*   toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);*/
        search_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_searchview.setVisibility(View.VISIBLE);
            }
        });
        editText_searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                showDatabaseAdapter.notifyDataSetChanged();
            }
        });
        btn_okay = findViewById(R.id.image_btn_okay);
        textView_create_important_things = findViewById(R.id.text_creat_important_things);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        myHelperClass = new MyHelperClass(realm);
        myHelperClass.SelectFromDB();
        toReverse = myHelperClass.justRefresh();
        getreverselist();

        showDatabaseAdapter = new ShowDatabaseAdapter(this, itemslist);
        recyclerView.setAdapter(showDatabaseAdapter);
        filter("");
        //filter("");
        showDatabaseAdapter.notifyDataSetChanged();
//        if (itemslist.isEmpty()) {
//            recyclerView.setVisibility(View.INVISIBLE);
//            head_image.setVisibility(View.VISIBLE);
//            textView_create_important_things.setVisibility(View.VISIBLE);
//        } else {
            recyclerView.setVisibility(View.VISIBLE);
            head_image.setVisibility(View.INVISIBLE);
            textView_create_important_things.setVisibility(View.INVISIBLE);
//        }
        /*recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                recyclerView.getChildAt()
                return false;
            }
        });*/
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNewDataActivity.class);
                startActivity(i);
            }
        });
    }

    private void getreverselist() {
        for (int i = toReverse.size() - 1; i >= 0; i--) {
            copied.add(toReverse.get(i));
        }
    }

    @Override
    public void onBackPressed() {
        Confirm_Dialogue();
    }

    private void Confirm_Dialogue() {
        final Dialog dialog = new Dialog(MainActivity.this);
        /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        /*dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;*/
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.confirm_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button btn_yes, btn_no, btn_rates;
        btn_yes = dialog.findViewById(R.id.yes_btn);
        btn_no = dialog.findViewById(R.id.No_btn);
        btn_rates = dialog.findViewById(R.id.Rate_btn);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
        dialog.show();
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_items, menu);
        MenuItem searchitem = menu.findItem(R.id.text_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchitem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showDatabaseAdapter.getFilter().filter(s);
                return false;
            }
        });
*//*
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showDatabaseAdapter.getFilter().filter(s);
                return false;
            }
        });
*//*
        return true;
    }*/

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(getApplicationContext().getPackageName())));
        }
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
    private void filter(String text) {
        itemslist.clear();
        showDatabaseAdapter.notifyDataSetChanged();
        Log.d("TAG", "Text  "+text);
        for (ModelClass item : copied) {
            Log.d("TAG", "title  "+item.getTitle());
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) ) {
                Log.d("TAG", "\nfilteritem: " + item);
                itemslist.add(item);
            }else{
                itemslist.remove(item);
            }
        }
       /* if (modelClassArrayList1.isEmpty()) {
            showDatabaseAdapter.filterlist(itemslist);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }*/

    }
}