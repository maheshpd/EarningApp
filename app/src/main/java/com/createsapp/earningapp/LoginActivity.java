package com.createsapp.earningapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView signUpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void init() {
        emailEdit = findViewById(R.id.emailET);
        passwordEdit = findViewById(R.id.passwordET);
        progressBar = findViewById(R.id.progressBar);
        signUpTv = findViewById(R.id.signup_tv);
        loginBtn = findViewById(R.id.loginBtn);
    }

}