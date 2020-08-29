package com.createsapp.earningapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.createsapp.earningapp.Internet;
import com.createsapp.earningapp.R;
import com.createsapp.earningapp.fragment.FragmentReplacerActivity;
import com.createsapp.earningapp.model.ProfileModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


//mahesh223prasad@gmail.com.Glam
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference reference;
    private CardView dailyCheckCard, luckyCard, taskCard, redeemCard, watchCard, aboutCard, referCard;
    private CircleImageView profileImage;
    private TextView coinsTv, nameTv, emailTv;
    private FirebaseUser user;
    private Dialog dialog;

    Internet internet;

    //Admob
    AdView adView;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        internet = new Internet(MainActivity.this);

        checkInternetConnection();

        //initialize admob sdk
        MobileAds.initialize(this);

       /* AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner_id));*/

        adView = findViewById(R.id.banner_id);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //admob inters ads
        loadInterstitialAd();


        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();

        clickListener();

    }

    private void clickListener() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitalAd(1);
            }
        });

        referCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitalAd(2);
            }
        });

        dailyCheckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyCheck();
            }
        });

        redeemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RedeemActivity.class));
            }
        });

        luckyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragmentReplacerActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
            }
        });

        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragmentReplacerActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
            }
        });

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

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

    }

    private void getDataFromDatabase() {

        dialog.show();
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

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void checkInternetConnection() {
        if (internet.isConnected()) {
            new isInternetActive().execute();
        } else {
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void dailyCheck() {

        if (internet.isConnected()) {

            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText("Please wait");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();

            final Date currentDate = Calendar.getInstance().getTime();

            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

            final String date = dateFormat.format(currentDate);

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            reference.child("Daily Check").child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String dbDateString = snapshot.child("date").getValue(String.class);

                                try {
                                    assert dbDateString != null;
                                    Date dbDate = dateFormat.parse(dbDateString);

                                    String xDate = dateFormat.format(currentDate);
                                    Date date = dateFormat.parse(xDate);


                                    if (date.after(dbDate) && date.compareTo(dbDate) != 0) {
                                        //reward available

                                        reference.child("Users").child(user.getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ProfileModel model = snapshot.getValue(ProfileModel.class);

                                                        int currentCoins = model.getCoins();
                                                        int update = currentCoins + 10;
                                                        int spinC = model.getSpins();
                                                        int updatedSpins = spinC + 2;
                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("coins", update);
                                                        map.put("spin", updatedSpins);

                                                        reference.child("Users").child(user.getUid())
                                                                .updateChildren(map);

                                                        Date newDate = Calendar.getInstance().getTime();
                                                        String newDateString = dateFormat.format(newDate);

                                                        HashMap<String, String> dateMap = new HashMap<>();
                                                        dateMap.put("date", newDateString);

                                                        reference.child("Daily Check").child(user.getUid()).setValue(dateMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                                        sweetAlertDialog.setTitleText("Success");
                                                                        sweetAlertDialog.setContentText("Coins added to your account successfully");
                                                                        sweetAlertDialog.setConfirmText("Dismiss");
                                                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                sweetAlertDialog.dismissWithAnimation();
                                                                            }
                                                                        }).show();
                                                                    }
                                                                });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        sweetAlertDialog.setTitleText("Failed");
                                        sweetAlertDialog.setContentText("You have already rewarded, come back tomorrow");
                                        sweetAlertDialog.setConfirmText("Dismiss");
                                        sweetAlertDialog.show();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();

                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            } else {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog.setTitleText("System Busy");
                                sweetAlertDialog.setContentText("System is busy, please try again later!");
                                sweetAlertDialog.setConfirmText("Dismiss");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                                sweetAlertDialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            sweetAlertDialog.dismissWithAnimation();

                        }
                    });
        } else {
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void showInterstitalAd(final int i) {
//

        if (interstitialAd.isLoaded()) {
            interstitialAd.show();

            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (i == 1) { //that mean user click on profile button
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }

                    if (i == 2) {
                        startActivity(new Intent(MainActivity.this, InviteActivity.class));
                    }

                }
            });
        }

    }

    class isInternetActive extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            InputStream inputStream = null;
            String json = "";

            try {
                //Url contain small android icon to check internet access
                //replace with your own url or with icon url
                //For more icon go to: https://icons.iconarchive.com/
                String strURL = "https://icons.iconarchive.com/";
                URL url = new URL(strURL);

                URLConnection urlConnection = url.openConnection();

                urlConnection.setDoOutput(true);
                inputStream = urlConnection.getInputStream();
                json = "success";


            } catch (Exception e) {
                e.printStackTrace();
                json = "failed";
            }

            return json;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null) {
                if (s.equals("success")) {
                    Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No Internet access", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Validating internet", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }
    }

}