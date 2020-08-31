package com.createsapp.earningapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.createsapp.earningapp.R;
import com.createsapp.earningapp.fragment.FragmentReplacerActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class RedeemActivity extends AppCompatActivity {

    private ImageView amazonImage;
    private CardView amazonCard;

    private InterstitialAd interstitialAd;
    private com.facebook.ads.InterstitialAd mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        init();

        loadInterstitialAd();

        loadImages();
        clickListener();

    }

    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        amazonImage = findViewById(R.id.amazonImage);
        amazonCard = findViewById(R.id.amazonGiftCard);
    }

    private void loadImages() {
        String amazonGiftImageURL = "https://toppng.com/uploads/preview/amazon-gift-card-11549868480mv0semfsfp.png"; //image url

        Glide.with(RedeemActivity.this)
                .load(amazonGiftImageURL)
                .into(amazonImage);

    }

    private void clickListener() {

        amazonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemActivity.this, FragmentReplacerActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
            }
        });

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

        //
        finish();

    }

}