package com.createsapp.earningapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.createsapp.earningapp.R;
import com.createsapp.earningapp.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InviteActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String opposite;

    private Toolbar toolbar;
    private TextView referCodeTv;
    DatabaseReference reference;
    private Button shareBtn, redeemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        init();


        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        loadData();
        redeemAvailablility();
        clickListener();
    }

    private void redeemAvailablility() {
        reference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChild("redeemed")) {

                            boolean isAvailable = snapshot.child("redeemed").getValue(Boolean.class);

                            if (isAvailable) {
                                redeemBtn.setVisibility(View.GONE);
                                redeemBtn.setEnabled(false);
                            } else {
                                redeemBtn.setEnabled(true);
                                redeemBtn.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referCodeTv = findViewById(R.id.referCodeTv);
        shareBtn = findViewById(R.id.shareBtn);
        redeemBtn = findViewById(R.id.redeemBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        reference.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String referCode = snapshot.child("referCode").getValue(String.class);
                        referCodeTv.setText(referCode);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InviteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void clickListener() {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referCode = referCodeTv.getText().toString();

                String shareBody = "Hey, i'm using the best earning app. Join using my invite code to instantly get 100"
                        + "coins. My invite code is  " + referCode + "\n" +
                        "Download from Play Store\n" +
                        "https://play.google.com/store/apps/details?id=" +
                        getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(intent);
            }
        });

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(InviteActivity.this);
                editText.setHint("abc12");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                editText.setLayoutParams(layoutParams);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InviteActivity.this);
                alertDialog.setTitle("Redeem Code");

                alertDialog.setView(editText);

                alertDialog.setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputCode = editText.getText().toString();
                        if (TextUtils.isEmpty(inputCode)) {
                            Toast.makeText(InviteActivity.this, "Input valid code", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (inputCode.equals(referCodeTv.getText().toString())) {
                            Toast.makeText(InviteActivity.this, "You can not input your own code", Toast.LENGTH_SHORT).show();
                        }
                        redeemQuery(inputCode, dialogInterface);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                alertDialog.show();
            }
        });

    }

    private void redeemQuery(String inputCode, final DialogInterface dialogInterface) {
        Query query = reference.orderByChild("referCode").equalTo(inputCode);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    opposite = dataSnapshot.getKey();

                    reference
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ProfileModel model = snapshot.child(opposite).getValue(ProfileModel.class);
                                    ProfileModel myModel = snapshot.child(opposite).getValue(ProfileModel.class);

                                    int coins = model.getCoins();
                                    int updateCoins = coins + 100;

                                    int myCoins = myModel.getCoins();
                                    int myUpdate = myCoins + 100;

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("coins", updateCoins);

                                    HashMap<String, Object> myMap = new HashMap<>();
                                    myMap.put("coins", myUpdate);
                                    myMap.put("redeemed", true);

                                    reference.child(opposite).updateChildren(map);
                                    reference.child(user.getUid()).updateChildren(myMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(InviteActivity.this, "Congrates", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InviteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}