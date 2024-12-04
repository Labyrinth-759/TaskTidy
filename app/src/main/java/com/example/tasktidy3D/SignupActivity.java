package com.example.tasktidy3D;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText edtEmail,edtPassword, edtConfirmPass;
    private Button signinBtn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtEmail = findViewById(R.id.EmailAddress);
        edtPassword = findViewById(R.id.Password);
        edtConfirmPass = findViewById(R.id.ConfirmPassword);
        signinBtn  = findViewById(R.id.Sign_in);

        dbHelper = new DatabaseHelper(this);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
}
    private void RegisterUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            showMessage("All Fields are Required!");
        } else if (!password.equals(confirmPass)) {
            edtConfirmPass.setError("Passwords Do not Match!");
            edtConfirmPass.requestFocus();
        } else {
            // Add user to the database
            boolean isUserAdded = dbHelper.AddUser(email, password);
            if (isUserAdded) {
                Intent gotoLogIn = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(gotoLogIn);
                finish();
            } else {
                showMessage("Failed to register user!");
            }
        }
    }


    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}