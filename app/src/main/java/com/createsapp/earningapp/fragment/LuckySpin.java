package com.createsapp.earningapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createsapp.earningapp.R;
import com.createsapp.earningapp.model.ProfileModel;
import com.createsapp.earningapp.spin.SpinItem;
import com.createsapp.earningapp.spin.WheelView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.RewardedVideoAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LuckySpin extends Fragment {

    List<SpinItem> spinItemList = new ArrayList<>();
    DatabaseReference reference;
    private Button playBtn;
    private WheelView wheelView;
    private FirebaseUser user;
    int currentSpin;

    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;

    public LuckySpin() {
        // Required empty public constructor
    }

    private TextView coinsTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lucky_spin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadAd();

        init(view);
        loadData();
        spinList();

        clickListener();

    }

    private void init(View view) {
        playBtn = view.findViewById(R.id.playBtn);
        wheelView = view.findViewById(R.id.wheelView);
        coinsTv = view.findViewById(R.id.coinsTv);


        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    private void spinList() {
        SpinItem item1 = new SpinItem();
        item1.text = "0"; // You can change according to your need
        item1.color = 0xffFFF30; //Change background color
        spinItemList.add(item1);

        SpinItem item2 = new SpinItem();
        item2.text = "2";
        item2.color = 0xffFFE0B2;
        spinItemList.add(item2);

        SpinItem item3 = new SpinItem();
        item3.text = "3";
        item3.color = 0xffFFCC80;
        spinItemList.add(item3);

        SpinItem item4 = new SpinItem();
        item4.text = "2";
        item4.color = 0xffFFF3E0;
        spinItemList.add(item4);

        SpinItem item5 = new SpinItem();
        item5.text = "6";
        item5.color = 0xffFFE0B2;
        spinItemList.add(item5);

        SpinItem item6 = new SpinItem();
        item6.text = "8";
        item6.color = 0xffFFCC80;
        spinItemList.add(item6);

        SpinItem item7 = new SpinItem();
        item7.text = "10";
        item7.color = 0xffFFF3E0;
        spinItemList.add(item7);

        SpinItem item8 = new SpinItem();
        item8.text = "7";
        item8.color = 0xffFFE0B2;
        spinItemList.add(item8);

        SpinItem item9 = new SpinItem();
        item9.text = "9";
        item9.color = 0xffFFCC80;
        spinItemList.add(item9);

        SpinItem item10 = new SpinItem();
        item10.text = "5";
        item10.color = 0xffFFF3E0;
        spinItemList.add(item10);

        SpinItem item11 = new SpinItem();
        item11.text = "11";
        item11.color = 0xffFFE0B2;
        spinItemList.add(item11);

        SpinItem item12 = new SpinItem();
        item12.text = "20";
        item12.color = 0xffFFCC80;
        spinItemList.add(item12);

        wheelView.setData(spinItemList);
        wheelView.setRound(getRandCircleRound());

        wheelView.LuckyRoundItemSelectedListener(new WheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                playBtn.setEnabled(true);
                playBtn.setAlpha(1f);

                //wheel stop rotating:: here to show ad
                showAd();

                String value = spinItemList.get(index - 1).text;
                updateDataFirebase(Integer.parseInt(value));


            }
        });
    }

    private void clickListener() {
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex();
                if (currentSpin >= 1 && currentSpin < 3) {
                    wheelView.startWheelWithTargetIndex(index);
                    Toast.makeText(getContext(), "Watch video to get more spins", Toast.LENGTH_SHORT).show();
                }

                if (currentSpin < 1) {
                    playBtn.setEnabled(false);
                    playBtn.setAlpha(.6f);
                    Toast.makeText(getContext(), "Watch video to get more spins", Toast.LENGTH_SHORT).show();
                } else {
                    playBtn.setEnabled(false);
                    playBtn.setAlpha(.6f);
                    wheelView.startWheelWithTargetIndex(index);
                }
            }
        });
    }

    private int getRandomIndex() {
        int[] index = new int[]{1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6, 7, 7, 9, 9, 10, 11, 12};
        int random = new Random().nextInt(index.length);
        return index[random];
    }

    private int getRandCircleRound() {
        Random random = new Random();
        return random.nextInt(10) + 15;
    }

    private void loadData() {
        reference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfileModel model = snapshot.getValue(ProfileModel.class);
                        coinsTv.setText(String.valueOf(model.getCoins()));

                        currentSpin = model.getSpins();

                        String currentSpins = "Spin The wheel" + currentSpin;

                        playBtn.setText(currentSpins);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                });
    }

    private void updateDataFirebase(int reward) {

        int currentCoins = Integer.parseInt(coinsTv.getText().toString());
        int updateCoins = currentCoins + reward;

        int updatedSpins = currentSpin - 1;

        HashMap<String, Object> map = new HashMap<>();
        map.put("coins", updateCoins);
        map.put("spins", updatedSpins);

        reference.child(user.getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Coins added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void loadAd() {

        if (getContext() == null)
            return;

        rewardedVideoAd = new RewardedVideoAd(getContext(), getString(R.string.fb_rewarded_id));
        rewardedVideoAd.loadAd();

        interstitialAd = new InterstitialAd(getContext(), getString(R.string.fb_interstital_id));
        interstitialAd.loadAd();
    }

    private void showAd() {

        if (rewardedVideoAd.isAdLoaded() && rewardedVideoAd.isAdInvalidated()) {
            rewardedVideoAd.show();

            //Reward video ad is loaded no need to show interstitial ad
            return;
        }

        if (interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        }
    }

}