package com.example.tasktidy3D;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splash = new Thread(() -> {
            try {
                Thread.sleep(3000);
                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
                Toast.makeText(SplashActivity.this, "App Error"+e,
                        Toast.LENGTH_SHORT).show();
            }
        });
        splash.start();
    }
}