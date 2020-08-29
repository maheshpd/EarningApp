package com.createsapp.earningapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText nameEdit, emailEdit, passwordEdt, confirmPasswordEdit;
    private ProgressBar progressBar;
    private TextView loginTv;
    private FirebaseAuth auth;
    private String deviceID;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

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


                queryAccountExistance(email, pass);

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
                            final FirebaseUser user = auth.getCurrentUser();
                            assert user != null;

                            //send email verfication link
                            auth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                updateUI(user, email);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    });
                        } else {
                            //Registration failed:

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void queryAccountExistance(final String email, final String pass) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = ref.orderByChild("deviceID").equalTo(deviceID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //device already registered
                    Toast.makeText(RegisterActivity.this,
                            "This device is already registered on another email, please login",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //device id not fount
                    createAccount(email, pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(FirebaseUser user, String email) {

        String refer = email.substring(0, email.lastIndexOf("@"));
        String referCode = refer.replace(".", "");

        //identify that this user already sign up

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", nameEdit.getText().toString());
        map.put("email", email);
        map.put("uid", user.getUid());
        map.put("image", "");
        map.put("coins", 0);
        map.put("referCode", referCode);
        map.put("spins", 2);
        map.put("deviceID", deviceID);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1); //to get yesterday date

        Date previousDate = calendar.getTime();

        String dateString = dateFormat.format(previousDate);

        FirebaseDatabase.getInstance().getReference().child("Daily Check")
                .child(user.getUid())
                .child("date")
                .setValue(dateString);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered, Please verify email", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                });

    }


}