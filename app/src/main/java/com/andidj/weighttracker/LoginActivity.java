package com.andidj.weighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

        EditText etUsername = findViewById(R.id.editTextText2);
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        Button btnLogin = findViewById(R.id.button);
        Button btnCreate = findViewById(R.id.button2);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.validateLogin(username, password)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid login.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreate.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password to create an account.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userExists(username)) {
                Toast.makeText(this, "Username already exists. Try logging in.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean created = db.createUser(username, password);
            if (created) {
                Toast.makeText(this, "Account created! You can log in now.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could not create account.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}