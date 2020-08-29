package com.createsapp.earningapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createsapp.earningapp.R;
import com.createsapp.earningapp.spin.SpinItem;
import com.createsapp.earningapp.spin.WheelView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LuckySpin extends Fragment {

    List<SpinItem> spinItemList;
    private Button playBtn;
    private WheelView wheelView;
    private FirebaseUser user;

    public LuckySpin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lucky_spin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        spinList();

    }

    private void init(View view) {
        playBtn = view.findViewById(R.id.playBtn);
        wheelView = view.findViewById(R.id.wheelView);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        spinItemList = new ArrayList<>();

    }

    private void spinList() {
        SpinItem item1 = new SpinItem();
        item1.text = "0";
        item1.color = 0xffFFF30;
        spinItemList.add(item1);

        SpinItem item2 = new SpinItem();
        item1.text = "2";
        item1.color = 0xffFFF3E0;
        spinItemList.add(item2);

        SpinItem item3 = new SpinItem();
        item1.text = "3";
        item1.color = 0xffFFCC80;
        spinItemList.add(item3);

        SpinItem item4 = new SpinItem();
        item1.text = "0";
        item1.color = 0xffFFF3E0;
        spinItemList.add(item4);

    }

}