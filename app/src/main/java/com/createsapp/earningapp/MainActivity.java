package com.createsapp.earningapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.createsapp.earningapp.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


//mahesh223prasad@gmail.com.Glam
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference reference;
    private CardView dailyCheckCard, luckyCard, taskCard, redeemCard, watchCard, aboutCard, referCard;
    private CircleImageView profileImage;
    private TextView coinsTv, nameTv, emailTv;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();

    }

    private void init() {
        dailyCheckCard = findViewById(R.id.dailyCheckCard);
        luckyCard = findViewById(R.id.luckySpinCard);
        taskCard = findViewById(R.id.taskCheckCard);
        redeemCard = findViewById(R.id.redeemCard);
        watchCard = findViewById(R.id.watchCard);
        aboutCard = findViewById(R.id.aboutCard);
        referCard = findViewById(R.id.referCard);
        coinsTv = findViewById(R.id.coinsTv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        profileImage = findViewById(R.id.profileImage);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getDataFromDatabase() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}