package com.example.appll;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    ImageView imageView;
    Button edtc,edsm,edvutc,eddlp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView=findViewById(R.id.imageView);
        edtc = findViewById(R.id.edtc);
        edsm = findViewById(R.id.edsm);
        edvutc = findViewById(R.id.edvutc);
        eddlp = findViewById(R.id.eddlp);

        edsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,SurpriseMeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,CreateTimeCapsule.class);
                startActivity(intent);
                finish();
            }
        });
        edvutc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SecondActivity.this,ViewTimeCapsule.class);
                startActivity(intent);
                finish();
            }
        });
        eddlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,DigitalLegacyPlanning.class);
                startActivity(intent);
            }
        });
    }
}