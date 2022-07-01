package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instalockerappsss.R;
import com.example.instalockerappsss.database.RealmHelper;
import com.example.instalockerappsss.modelclass.ModelClass;

import io.realm.Realm;

public class EmailSavedActvity extends AppCompatActivity {

    Button btn_save_email;
    EditText edit_email;
    ModelClass md = new ModelClass();
    Realm realm;
    RealmHelper realmHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_saved);
        Realm.init(EmailSavedActvity.this);
        realm=Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, this);
        btn_save_email = findViewById(R.id.btn_save_email);
        edit_email = findViewById(R.id.edit_email);
        btn_save_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(edit_email.getText().toString())){
                    String email = edit_email.getText().toString();
                    md.setEmail(email);
                    realmHelper.saveEmail(md);
                    Toast.makeText(EmailSavedActvity.this, "Mail Account  Saved Successfully", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(EmailSavedActvity.this,SettingActvity.class);
                    startActivity(i);
                }
                else {
                   edit_email.setError("Invalid Email Address");
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}