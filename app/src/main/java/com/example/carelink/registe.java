package com.example.carelink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registe extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button registerButton, signin;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);

        db = new DBHelper(this);

        emailField = findViewById(R.id.editTextTextPersonName5);
        passwordField = findViewById(R.id.editTextTextPersonName52);
        signin = findViewById(R.id.button16);
        registerButton = findViewById(R.id.text);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registe.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        // Register button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (db.registerUser(email, password)) {
                    Toast.makeText(registe.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(registe.this, login.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(registe.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}