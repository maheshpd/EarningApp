package com.createsapp.earningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText nameEdit, emailEdit, passwordEdt, confirmPasswordEdit;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        cliclListener();

    }

    private void init() {
        registerBtn = findViewById(R.id.registerBtn);
        nameEdit = findViewById(R.id.nameET);
        emailEdit = findViewById(R.id.emailET);
        passwordEdt = findViewById(R.id.passwordET);
        confirmPasswordEdit = findViewById(R.id.confirmPass);
        progressBar = findViewById(R.id.registerBtn);

    }

    private void cliclListener() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String pass = passwordEdt.getText().toString();
                String confirmPass = confirmPasswordEdit.getText().toString();

                if (name.isEmpty()) {
                    nameEdit.setError("Required");
                    return;
                }

                if (email.isEmpty()) {
                    emailEdit.setError("Required");
                    return;
                }

                if (pass.isEmpty()) {
                    passwordEdt.setError("Required");
                    return;
                }

                if (confirmPass.isEmpty() || !pass.equals(confirmPass)) {
                    nameEdit.setError("Invalid Password");
                    return;
                }

                createAccount(email,pass);

            }
        });
    }

    private void createAccount(String email, String password) {

    }



}