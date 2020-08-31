package com.createsapp.earningapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.createsapp.earningapp.R;
import com.createsapp.earningapp.model.ProfileModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class WatchActivity extends AppCompatActivity {

    DatabaseReference reference;
    private InterstitialAd interstitialAd;
    private com.facebook.ads.InterstitialAd mInterstitial;
    private RewardedVideoAd rewardedVideoAd;
    private Button watchBtn1, watchBtn2;
    private TextView coinsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        init();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        loadData();

        loadInterstitialAd();

        loadRewardedAds();

        clickListener();

    }

    private void clickListener() {
        watchBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardVideo(1);
            }
        });
    }

    private void showRewardVideo(final int i) {
        if (rewardedVideoAd.isAdLoaded()) {
            rewardedVideoAd.show();
            rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoCompleted() {

                    if (i == 1) {
                        watchBtn1.setVisibility(View.GONE);
                        watchBtn2.setVisibility(View.VISIBLE);
                    }

                    if (i == 2) {
                        onBackPressed();
                    }

                    updateDataFirebase();
                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }

                @Override
                public void onRewardedVideoClosed() {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }
            });
        }
    }


    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Watch and Earn");

        watchBtn1 = findViewById(R.id.warchBtn1);
        watchBtn2 = findViewById(R.id.warchBtn2);
        coinsTv = findViewById(R.id.coinsTv);

    }

    private void loadRewardedAds() {

        rewardedVideoAd = new RewardedVideoAd(this, getString(R.string.fb_rewarded_id));
        rewardedVideoAd.loadAd();
    }

    private void loadInterstitialAd() {

        //admob init
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //fb init
        mInterstitial = new com.facebook.ads.InterstitialAd(this, getString(R.string.fb_interstital_id));
        mInterstitial.loadAd();
    }

    @Override
    public void onBackPressed() {

        //fb
        if (mInterstitial.isAdLoaded()) {
            mInterstitial.show();

            mInterstitial.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    finish();
                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            return;
        }

        //admob
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();

            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });

            return;
        }

        finish();

    }

    private void loadData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model = snapshot.getValue(ProfileModel.class);
                coinsTv.setText(String.valueOf(model.getCoins()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WatchActivity.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateDataFirebase() {
        int currentCoins = Integer.parseInt(coinsTv.getText().toString());
        int updatedCoin = currentCoins + 5;

        HashMap<String, Object> map = new HashMap<>();
        map.put("coins", updatedCoin);

        reference.updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(WatchActivity.this, "Coins added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}