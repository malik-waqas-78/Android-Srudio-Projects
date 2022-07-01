package com.example.quize3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Detail_activity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);

        TextView tvTemp=findViewById(R.id.tv_temp);
        ImageView iv=findViewById(R.id.iv_weather);
        Intent intent=getIntent();
        if(intent!=null){
            String city=intent.getStringExtra("city");
            if(city.equalsIgnoreCase("cairo")){
            tvTemp.setText("Temperature of Cairo today is 20 C");
            iv.setImageDrawable(
                    getDrawable(R.drawable.cloud)
            );
            }else if(city.equalsIgnoreCase("london")){
                tvTemp.setText("Temperature of London today is 10 C");
                iv.setImageDrawable(
                        getDrawable(R.drawable.rain)
                );

            }else if(city.equalsIgnoreCase("abu dhabi")){
                tvTemp.setText("Temperature of Abu Dhabi today is 25 C");
                iv.setImageDrawable(
                        getDrawable(R.drawable.sunny)
                );

            }

            iv.animate().rotationX(360).setDuration(500);

        }

    }
}
