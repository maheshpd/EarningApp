package com.createsapp.earningapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createsapp.earningapp.R;
import com.createsapp.earningapp.model.AmazonModel;
import com.createsapp.earningapp.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class AmazonFragment extends Fragment {

    private RadioGroup radioGroup;
    private Button withdrawBtn;
    private TextView coinsTv;
    DatabaseReference reference;
    FirebaseUser user;

    public AmazonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amazon, container, false);
    }

    DatabaseReference amazonRef;

    private void init(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        withdrawBtn = view.findViewById(R.id.submitBtn);
        coinsTv = view.findViewById(R.id.coinsTv);
    }

    Query query;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        loadData();
        clickListener();
    }

    private void loadData() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model = snapshot.getValue(ProfileModel.class);
                coinsTv.setText(String.valueOf(model.getCoins()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }

    private void clickListener() {
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCoins = Integer.parseInt(coinsTv.getText().toString());

                int checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.amazon25:
                        AmaxonCard(25, currentCoins);
                        break;
                    case R.id.amazon50:
                        AmaxonCard(50, currentCoins);
                        break;
                }
            }
        });
    }

    private void AmaxonCard(int amazonCard, int currentCoins) {
        if (amazonCard == 25) {
            if (currentCoins >= 6000) { //minimum coins should be 6000
                sendGiftCard(1);

            }
        } else if (amazonCard == 50) {
            if (currentCoins >= 12000) {
                sendGiftCard(2);
            }
        }
    }

    private void sendGiftCard(int cardAmount) {
        amazonRef = FirebaseDatabase.getInstance().getReference().child("Gift Cards").child("Amazon");

        if (cardAmount == 1) {
            query = amazonRef.orderByChild("amazon").equalTo(25);

        } else if (cardAmount == 2) {
            query = amazonRef.orderByChild("amazon").equalTo(50);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Random random = new Random();

                int childCount = (int) snapshot.getChildrenCount();
                int rand = random.nextInt(childCount);
                Iterator iterator = snapshot.getChildren().iterator();

                for (int i = 0; i < rand; i++) {
                    iterator.next();
                }

                DataSnapshot childSnap = (DataSnapshot) iterator.next();

                AmazonModel model = childSnap.getValue(AmazonModel.class);

                String id = model.getId();

                String giftCode = model.getAmazonCode();

                printAmazonCode(id, giftCode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void printAmazonCode(String id, String amazonCode) {

    }


}