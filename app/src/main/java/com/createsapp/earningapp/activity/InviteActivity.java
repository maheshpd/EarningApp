package com.createsapp.earningapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.createsapp.earningapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InviteActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String opposite;

    private Toolbar toolbar;
    private TextView referCodeTv;
    private Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        referCodeTv = findViewById(R.id.referCodeTv);
        shareBtn = findViewById(R.id.shareBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}