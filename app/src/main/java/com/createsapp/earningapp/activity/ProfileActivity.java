package com.createsapp.earningapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.createsapp.earningapp.R;
import com.createsapp.earningapp.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Button updateBtn;
    private CircleImageView profileImage;
    private TextView nameTv, emailTv, shareTv, redeemHistoryTv, coinsTv, logoutTv;
    private ImageButton imageEditBtn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        loadDataFromDatabase();
        clickListener();

    }

    private void init() {
        profileImage = findViewById(R.id.profileImage);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        shareTv = findViewById(R.id.shareTv);
        redeemHistoryTv = findViewById(R.id.redeemHistoryTv);
        logoutTv = findViewById(R.id.logoutTv);
        coinsTv = findViewById(R.id.coinsTv);
        imageEditBtn = findViewById(R.id.editImage);
        updateBtn = findViewById(R.id.updateBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void loadDataFromDatabase() {

        reference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfileModel model = snapshot.getValue(ProfileModel.class);
                        nameTv.setText(model.getName());
                        emailTv.setText(model.getEmail());
                        coinsTv.setText(String.valueOf(model.getCoins()));

                        Glide.with(getApplicationContext())
                                .load(model.getImage())
                                .timeout(6000)
                                .placeholder(R.drawable.profile)
                                .into(profileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void clickListener() {
        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "Check out the best earning app. Download" + getString(R.string.app_name) +
                        " from Play Store\n" +
                        "https://play.google.com/store/apps/details?id=" +
                        getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        imageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}