package com.example.tasktidy3D;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail,edtPassword;
    private TextView txtLink;
    private Button btnLogin;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        edtEmail = findViewById(R.id.editTextTextEmailAddress);
        edtPassword = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtLink = findViewById(R.id.tvclickHere);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });

        txtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LogIn(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            showMessage("All Fields are Required!");
        }else{
            if(dbHelper.checkUser(email, password)){
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user_email", email);
                editor.apply();

                showMessage("Log-In Successful");
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }else{
                showMessage("Invalid Credentials!");
            }
        }
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

