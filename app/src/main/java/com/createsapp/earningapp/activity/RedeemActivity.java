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

public class RedeemActivity extends AppCompatActivity {

    private ImageView amazonImage;
    private CardView amazonCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        init();
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


}