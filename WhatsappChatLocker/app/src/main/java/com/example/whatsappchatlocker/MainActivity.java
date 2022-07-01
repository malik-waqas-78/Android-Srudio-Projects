package com.example.whatsappchatlocker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.whatsappchatlocker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    LinearLayout linearLayout;
    Realm realm;
    RealmHelper realmHelper;
    ArrayList<Record> arrayList = new ArrayList<>();
    Toolbar toolbar;
    private Toast backtoast;
    Boolean DouleTap = false;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floating_action_button);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        setUpToolBar();
        Realm.init(MainActivity.this);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, this);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linear);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        arrayList = new ArrayList();
        arrayList = realmHelper.retriveData();
        recyclerAdapter = new RecyclerAdapter(this, arrayList);
        recyclerView.setAdapter(recyclerAdapter);
        SharedPreferences preferences = getSharedPreferences("Lock", 0);
        editor = preferences.edit();
        editor.putBoolean("whatsapp", true);
        editor.putBoolean("share", false);
        editor.apply();
        if (!isAccessibilitySettingsOn(MainActivity.this)) {
            requestAlertDialog();
        } else {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    editor.putBoolean("whatsapp", true);
                    editor.apply();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.yowhatsapp");
                    intent.setClassName("com.yowhatsapp", "com.yowhatsapp.HomeActivity");
                    startActivity(intent);
                }
            });
            Intent intent = getIntent();
            String Name = intent.getStringExtra("Name");
            if (Name != null) {
                arrayList = new ArrayList();
                arrayList = realmHelper.retriveData();
                recyclerAdapter = new RecyclerAdapter(this, arrayList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    protected void onRestart() {
        Intent intent = new Intent(MainActivity.this, EnterPassword.class);
        startActivity(intent);
        super.onRestart();
    }

    public void requestAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Accessibilty Permission Required.");
        builder.setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;

        final String service = mContext.getPackageName()
                + "/" + Service.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v("TAG", "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("TAG",
                    "Error finding setting, default accessibility to not found: "
                            + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (DouleTap) {
                editor.putBoolean("whatsapp", false);
                editor.putBoolean("share", false);
                editor.apply();
                backtoast.cancel();
                ExitActivity.exitApplication(this);
               // finishAffinity();
//                Intent setIntent = new Intent(Intent.ACTION_MAIN);
//                setIntent.addCategory(Intent.CATEGORY_HOME);
//                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(setIntent);
            } else {
                backtoast = Toast.makeText(this, "Tap again to Exit", Toast.LENGTH_SHORT);
                backtoast.show();

                DouleTap = true;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DouleTap = false;

                    }
                }, 1000);
            }
        }
    }

    @Override
    protected void onResume() {
        editor.putBoolean("whatsapp", true);
        editor.putBoolean("share", false);
        editor.apply();
        super.onResume();
    }

    public void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My WhatsChat Locker");
        actionBarDrawer = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.Open, R.string.close);
        actionBarDrawer.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawer);

        actionBarDrawer.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
        return actionBarDrawer.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home1) {
            Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.setting1) {
            Toast.makeText(MainActivity.this, "Setting Clicked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.about) {
            Toast.makeText(MainActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.share) {
            editor.putBoolean("whatsapp", false);
            editor.putBoolean("share", true);
            editor.apply();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id"+getPackageName();
            String shareSub = "Your subject here";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        if (id == R.id.rateus) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent);
        }
        if (id == R.id.c_password1) {
            Intent intent = new Intent(MainActivity.this, Old_Password.class);
            startActivity(intent);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        return false;
    }
}
