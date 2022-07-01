package com.example.instalockerappsss.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalockerappsss.R;
import com.example.instalockerappsss.activities.MainActivity;
import com.example.instalockerappsss.adapter.LockerAdapterClass;
import com.example.instalockerappsss.database.RealmHelper;
import com.example.instalockerappsss.modelclass.ModelClass;
import com.example.instalockerappsss.service.AccessabiltyServiceClass;

import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import io.realm.Realm;

public class HomeFragment extends Fragment {
    ImageButton btn_insta_locker;
    public String TAG = "MainActivity";
    RecyclerView recyclerview_locker;
    GridLayoutManager gridLayoutManager;
    LockerAdapterClass lockerAdapterClass;
  public static   ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    RealmHelper realmHelper;
    ImageButton item_delete_btn;
    Realm realm;
    TextView insta_text, delete_text, no_contacts_added;
    boolean activateTxt = true;
    public static ArrayList<ModelClass> selectionArraylist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, getContext());
        btn_insta_locker = view.findViewById(R.id.lock_insta_profile_btn);
        recyclerview_locker = view.findViewById(R.id.recyclerview_locker);
        insta_text = view.findViewById(R.id.insta_text);
        delete_text = view.findViewById(R.id.delete_text);
        no_contacts_added = view.findViewById(R.id.no_contacts_added);
        item_delete_btn = view.findViewById(R.id.item_delete_btn);
        modelClassArrayList = realmHelper.retriveData();
        if (modelClassArrayList.size() == 0) {
            item_delete_btn.setVisibility(View.GONE);
            recyclerview_locker.setVisibility(View.GONE);
            btn_insta_locker.setVisibility(View.VISIBLE);
            no_contacts_added.setVisibility(View.VISIBLE);
        } else {
            item_delete_btn.setVisibility(View.VISIBLE);
            recyclerview_locker.setVisibility(View.VISIBLE);
            btn_insta_locker.setVisibility(View.VISIBLE);
            no_contacts_added.setVisibility(View.GONE);
        }
        item_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateTxt = true;
                lockerAdapterClass = new LockerAdapterClass(getContext(), modelClassArrayList,activateTxt);
                gridLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerview_locker.setLayoutManager(gridLayoutManager);
                recyclerview_locker.setAdapter(lockerAdapterClass);
                delete_text.setVisibility(View.VISIBLE);
                item_delete_btn.setVisibility(View.GONE);
              /*  modelClassArrayList = lockerAdapterClass.getListItems();
                for (int i = 0; i < modelClassArrayList.size(); i++) {
                    ModelClass modelClass = modelClassArrayList.get(i);
                    realmHelper.update_Visibility(modelClass.getName(), true);
                }
                modelClassArrayList = realmHelper.retriveData();
                lockerAdapterClass.notifyDataSetChanged();
                delete_text.setVisibility(View.VISIBLE);
                item_delete_btn.setVisibility(View.GONE);*/
            }
        });
        delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateTxt = false;
                lockerAdapterClass = new LockerAdapterClass(getContext(), modelClassArrayList,activateTxt);
                gridLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerview_locker.setLayoutManager(gridLayoutManager);
                recyclerview_locker.setAdapter(lockerAdapterClass);

                privacy_Dialog();
               /* ArrayList<String> selectedModelClass = new ArrayList<>();
                lockerAdapterClass.getSelectedList();
                for (String s : selectedModelClass) {
                    realmHelper.deleteData(s);
                }
                lockerAdapterClass.setSelected();
                modelClassArrayList.clear();
                lockerAdapterClass.notifyDataSetChanged();
                modelClassArrayList = realmHelper.retriveData();
                lockerAdapterClass.notifyDataSetChanged();*/
            }
        });
        lockerAdapterClass = new LockerAdapterClass(getContext(), modelClassArrayList);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerview_locker.setLayoutManager(gridLayoutManager);
        recyclerview_locker.setAdapter(lockerAdapterClass);
        if (modelClassArrayList.size() == 0) {

        }
        btn_insta_locker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAccessibilitySettingsOn(getContext())) {
                    try {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Lock", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("lock", true);
                        editor.apply();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setClassName("com.instagram.android", "com.instagram.mainactivity.MainActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch
                    (Exception e) {
                        Toast.makeText(getContext(), "Instagram Not Installed in this device", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                    Toast.makeText(getContext(), "Service not Enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = getActivity().getIntent();
        String Name = intent.getStringExtra("Name");
        String Date = intent.getStringExtra("Date");
        boolean resume = intent.getBooleanExtra("resume", false);
        boolean appLock = intent.getBooleanExtra("appLock", false);
        SharedPreferences preferences = getContext().getSharedPreferences("Lock", 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (resume) {
            editor.putBoolean("resume", true);
        } else {
            editor.putBoolean("resume", false);
        }
        Log.d(TAG, "onCreate: " + Name);
        if (Name != null && Date != null) {
            Log.d(TAG, "Data Is = " + "Name : " + Name + "Date : " + Date);
            modelClassArrayList = realmHelper.retriveData();
            lockerAdapterClass = new LockerAdapterClass(getContext(), modelClassArrayList,activateTxt);
            recyclerview_locker.setAdapter(lockerAdapterClass);
            lockerAdapterClass.notifyDataSetChanged();
        }
        return view;
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName()
                + "/" + AccessabiltyServiceClass.class.getCanonicalName();
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
    public static void MakeSelection(View v, int adapterPosition) {
        if (((CheckBox) v).isChecked()) {
            selectionArraylist.add(modelClassArrayList.get(adapterPosition));
            Log.d("TAG111", "MakeSelection: Selection Success");
        } else {
            selectionArraylist.remove(modelClassArrayList.get(adapterPosition));
        }
    }
    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_data_confirm_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button ok = dialog.findViewById(R.id.delete_yes);
        final Button no = dialog.findViewById(R.id.delete_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_text.setVisibility(View.GONE);
                item_delete_btn.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockerAdapterClass.RemoveItem(selectionArraylist);
                for(int i=0; i<selectionArraylist.size(); i++) {
                    try {
                        realmHelper.deleteData(selectionArraylist.get(i).getName());
                        Log.d(TAG, "onClick: "+selectionArraylist.get(i).getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                delete_text.setVisibility(View.GONE);
                item_delete_btn.setVisibility(View.VISIBLE);
                selectionArraylist.clear();
                if (modelClassArrayList.size() == 0) {
                    item_delete_btn.setVisibility(View.GONE);
                    recyclerview_locker.setVisibility(View.GONE);
                    btn_insta_locker.setVisibility(View.VISIBLE);
                    no_contacts_added.setVisibility(View.VISIBLE);
                } else {
                    item_delete_btn.setVisibility(View.VISIBLE);
                    recyclerview_locker.setVisibility(View.VISIBLE);
                    btn_insta_locker.setVisibility(View.VISIBLE);
                    no_contacts_added.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}