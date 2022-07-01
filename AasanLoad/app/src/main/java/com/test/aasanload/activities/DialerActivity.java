package com.test.aasanload.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.test.aasanload.R;
import com.test.aasanload.adapter.MyAdapter;
import com.test.aasanload.constants.MyConstants;
import com.test.aasanload.interfaces.MyAdapterCallBacks;
import com.test.aasanload.modelclasses.CodeModelClass;
import com.test.aasanload.permissions.MyPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: DialerActivity.kt */
public final class DialerActivity extends AppCompatActivity implements MyAdapterCallBacks {
    private ArrayList<CodeModelClass> codesArray = new ArrayList();
    private MyAdapter myAdapter;
    private String networkEndCode;
    private String networkMiddleCode;
    private String networkStartCode;
    private RecyclerView recyclerView;

    public DialerActivity() {
        String str = "";
        this.networkStartCode = str;
        this.networkEndCode = "#";
        this.networkMiddleCode = str;
    }

    public final RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public final void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public final MyAdapter getMyAdapter() {
        return this.myAdapter;
    }

    public final void setMyAdapter(MyAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    public final ArrayList<CodeModelClass> getCodesArray() {
        return this.codesArray;
    }

    public final void setCodesArray(ArrayList<CodeModelClass> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
        this.codesArray = arrayList;
    }

    public final String getNetworkStartCode() {
        return this.networkStartCode;
    }

    public final void setNetworkStartCode(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.networkStartCode = str;
    }

    public final String getNetworkEndCode() {
        return this.networkEndCode;
    }

    public final void setNetworkEndCode(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.networkEndCode = str;
    }

    public final String getNetworkMiddleCode() {
        return this.networkMiddleCode;
    }

    public final void setNetworkMiddleCode(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.networkMiddleCode = str;
    }

    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(R.layout.activity_dilar);
        this.recyclerView = (RecyclerView) findViewById(R.id.rc_code);
        RecyclerView recyclerView = this.recyclerView;
        if (recyclerView != null) {
            recyclerView.setLayoutManager((LayoutManager) new LinearLayoutManager(getApplicationContext()));
            MyAdapter myAdapter = new MyAdapter((Context) this, this.codesArray);
            this.myAdapter = myAdapter;
            recyclerView.setAdapter((Adapter) myAdapter);
        }
        ArrayList stringArrayListExtra = getIntent().getStringArrayListExtra(MyConstants.Companion.getCODE_ARRAY());
        loadCodes(stringArrayListExtra);
        String stringExtra = getIntent().getStringExtra(MyConstants.Companion.getNETWORK_KEY());
        setupNetworkCodes(stringExtra);
        Bitmap bitmap = MyConstants.Companion.getBITMAP();

        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((ConstraintLayout) findViewById(R.id.btn_dial)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(DialerActivity.this, Manifest.permission.CALL_PHONE)==
                        PackageManager.PERMISSION_GRANTED){
                    proceedToCall();
                }else{
                    ActivityCompat.requestPermissions(DialerActivity.this,new String[]{Manifest.permission.CALL_PHONE},2255);
                }

            }
        });
    }



    public final Uri getLocalBitmapUri(ImageView imageView) {
        Intrinsics.checkNotNullParameter(imageView, "imageView");
        Bitmap bitmap = (Bitmap) null;
        if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
            return null;
        }
        Drawable drawable = imageView.getDrawable();
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.BitmapDrawable");
        Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
        Uri uri = (Uri) null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.close();
            uri = Uri.fromFile(file);
            if (VERSION.SDK_INT < 24) {
                return uri;
            }
            Context applicationContext = getApplicationContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getPackageName());
            stringBuilder.append(".fileprovider");
            return FileProvider.getUriForFile(applicationContext, stringBuilder.toString(), file);
        } catch (IOException e) {
            e.printStackTrace();
            return uri;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2255){
            if(ContextCompat.checkSelfPermission(DialerActivity.this,Manifest.permission.CALL_PHONE)==
            PackageManager.PERMISSION_GRANTED){
                proceedToCall();
            }
        }
    }

    public final void proceedToCall() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onCreate: ");
        stringBuilder.append(this.networkStartCode);
        stringBuilder.append(this.networkMiddleCode);
        stringBuilder.append(this.networkEndCode);
        String stringBuilder2 = stringBuilder.toString();
        String str = MyConstants.TAG;
        Log.d(str, stringBuilder2);
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append('*');
        stringBuilder3.append(Integer.parseInt(this.networkStartCode));
        stringBuilder3.append('*');
        stringBuilder3.append(Long.parseLong(this.networkMiddleCode));
        stringBuilder3.append('#');
        Intent intent = new Intent("android.intent.action.CALL", Uri.fromParts("tel", stringBuilder3.toString(), null));
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("data: ");
        stringBuilder3.append(intent.getData());
        Log.d(str, stringBuilder3.toString());
        startActivityForResult(intent, MyConstants.Companion.getDIALER_ACTIVITY_REQUEST_CODE());
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == MyConstants.Companion.getPERMISSION_SETTINGS()) {
            if(ContextCompat.checkSelfPermission(DialerActivity.this,Manifest.permission.CALL_PHONE)==
                    PackageManager.PERMISSION_GRANTED){
                proceedToCall();
            }
        } else if (i == MyConstants.Companion.getDIALER_ACTIVITY_REQUEST_CODE()) {
            View findViewById = findViewById(R.id.btn_dial);
            ((ConstraintLayout) findViewById).setVisibility(View.GONE);
            Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById<ConstraintLayout>(R.id.btn_invite)");
            ((ConstraintLayout) findViewById).setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        finish();
    }

    private final void setupNetworkCodes(String str) {
        if (str != null) {
            String str2 = "123";
            String str3 = "#";
            switch (str.hashCode()) {
                case -1429352729:
                    if (str.equals("telenor")) {
                        this.networkStartCode = "555";
                        this.networkEndCode = str3;
                        break;
                    }
                    break;
                case 3254967:
                    if (str.equals("jazz")) {
                        this.networkStartCode = str2;
                        this.networkEndCode = str3;
                        break;
                    }
                    break;
                case 3744686:
                    if (str.equals("zong")) {
                        this.networkStartCode = "101";
                        this.networkEndCode = str3;
                        break;
                    }
                    break;
                case 111200821:
                    if (str.equals("ufone")) {
                        this.networkStartCode = str2;
                        this.networkEndCode = str3;
                        break;
                    }
                    break;
            }
        }
        this.networkMiddleCode = ((CodeModelClass) this.codesArray.get(0)).getCode();
    }

    private final void loadCodes(ArrayList<String> arrayList) {
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                CodeModelClass codeModelClass = new CodeModelClass();
                Intrinsics.checkNotNullExpressionValue(str, "i");
                codeModelClass.setCode(str);
                codeModelClass.setSelected(false);
                this.codesArray.add(codeModelClass);
            }
        }
        if (!this.codesArray.isEmpty()) {
            ((CodeModelClass) this.codesArray.get(0)).setSelected(true);
        }
        MyAdapter myAdapter = this.myAdapter;
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    public void copyItem(CodeModelClass codeModelClass) {
        Intrinsics.checkNotNullParameter(codeModelClass, "item");
        Object systemService = getSystemService(Context.CLIPBOARD_SERVICE);
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.content.ClipboardManager");
        ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText(String.valueOf(16842753), codeModelClass.getCode()));
        Toast.makeText((Context) this, "Copied to Clipboard", Toast.LENGTH_LONG).show();
    }

    public void itemSelected(CodeModelClass codeModelClass) {
        Intrinsics.checkNotNullParameter(codeModelClass, "item");
        Iterator it = this.codesArray.iterator();
        while (it.hasNext()) {
            CodeModelClass codeModelClass2 = (CodeModelClass) it.next();
            codeModelClass2.setSelected(Intrinsics.areEqual(codeModelClass2, codeModelClass));
        }
        MyAdapter myAdapter = this.myAdapter;
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
        this.networkMiddleCode = codeModelClass.getCode();
    }

}
