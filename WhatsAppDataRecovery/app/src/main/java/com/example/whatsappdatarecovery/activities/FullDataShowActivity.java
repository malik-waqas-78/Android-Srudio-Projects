package com.example.whatsappdatarecovery.activities;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.modelclass.ModelClass;

public class FullDataShowActivity extends AppCompatActivity {
    Realm realm;
    TextView title, details, date;
    int position;
    ImageButton btn_delete;
    ModelClass modelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_data_show);
        title = findViewById(R.id.t1);
        details = findViewById(R.id.t2);
        date = findViewById(R.id.t3);
        btn_delete=findViewById(R.id.btn_delete1);
        realm = Realm.getDefaultInstance();
        Intent i = getIntent();
        position = i.getIntExtra("numposition", 1);
        modelClass = realm.where(ModelClass.class).equalTo("id", position).findFirst();
        title.setText(modelClass.getName());
        details.setText(modelClass.getDetails());
        date.setText(modelClass.getDate());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        modelClass.deleteFromRealm();
                        Intent i=new Intent(FullDataShowActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
    }
}