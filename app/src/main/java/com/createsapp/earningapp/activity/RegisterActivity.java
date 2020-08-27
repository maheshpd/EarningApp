package com.createsapp.earningapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.createsapp.earningapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText nameEdit, emailEdit, passwordEdt, confirmPasswordEdit;
    private ProgressBar progressBar;
    private TextView loginTv;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        init();

        cliclListener();

    }

    private void init() {
        registerBtn = findViewById(R.id.registerBtn);
        nameEdit = findViewById(R.id.nameET);
        emailEdit = findViewById(R.id.emailET);
        passwordEdt = findViewById(R.id.passwordET);
        confirmPasswordEdit = findViewById(R.id.confirmPass);
        progressBar = findViewById(R.id.progressBar);
        loginTv = findViewById(R.id.login_tv);


    }

    private void cliclListener() {

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


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
                    confirmPasswordEdit.setError("Invalid Password");
                    return;
                }

                createAccount(email, pass);

            }
        });
    }

    private void createAccount(final String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Registration success:
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            updateUI(user, email);
                        } else {
                            //Registration failed:

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

       /* auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Registration success:
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            updateUI(user, email);
                        } else {
                            //Registration failed:

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    private void updateUI(FirebaseUser user, String email) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", nameEdit.getText().toString());
        map.put("email", email);
        map.put("uid", user.getUid());
        map.put("image", "");
        map.put("coins", 0);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Welcome here", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                });

    }


}