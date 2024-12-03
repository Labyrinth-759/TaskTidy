package com.example.tasktidy3D;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        EditText emailField = findViewById(R.id.editTextTextEmailAddress2);
        EditText passwordField = findViewById(R.id.editTextTextPassword2);
        EditText confirmPasswordField = findViewById(R.id.editTextTextPassword3);
        Button signupButton = findViewById(R.id.button2);

        signupButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                if (isUserExists(email)) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert user into the database
                    boolean isInserted = dbHelper.insertUser(email, password);
                    if (isInserted) {
                        Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close SignupActivity and go back to LoginActivity
                    } else {
                        Toast.makeText(this, "Registration Failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isUserExists(String email) {
        // Query the database to check if the email already exists
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM Users WHERE email=?", new String[]{email});
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }
}
